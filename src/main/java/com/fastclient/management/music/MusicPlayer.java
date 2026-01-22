package com.fastclient.management.music;

import java.io.File;
import java.io.FileInputStream;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

import com.fastclient.animation.SimpleAnimation;
import com.fastclient.libraries.flac.FLACDecoder;
import com.fastclient.libraries.flac.frame.Frame;
import com.fastclient.libraries.flac.metadata.StreamInfo;
import com.fastclient.libraries.flac.util.ByteData;

public class MusicPlayer implements Runnable {

	public static final int SPECTRUM_BANDS = 100;
	public static float[] VISUALIZER = new float[SPECTRUM_BANDS];
	public static SimpleAnimation[] ANIMATIONS = new SimpleAnimation[SPECTRUM_BANDS];

	static {
		for (int i = 0; i < SPECTRUM_BANDS; i++) {
			VISUALIZER[i] = 0.0F;
			ANIMATIONS[i] = new SimpleAnimation();
		}
	}

	private static final int FFT_SIZE = 1024;
	private float[] fftBuffer = new float[FFT_SIZE];
	private float[] magnitudes = new float[SPECTRUM_BANDS];

	private Runnable runnable;

	private FLACDecoder decoder;
	private StreamInfo streamInfo;

	private AudioFormat audioFormat;
	private DataLine.Info info;
	private SourceDataLine sourceDataLine;

	private Music currentMusic;
	private boolean playing;
	private float volume;

	private float lastCurrentTime;
	private float mp3EndTime;

	public MusicPlayer(Runnable runnable) {
		this.runnable = runnable;
		this.playing = false;
		this.volume = 0.5F;
	}

	@Override
	public void run() {
		if (currentMusic != null && playing) {
			String fileName = currentMusic.getAudio().getName().toLowerCase();
			
			if (fileName.endsWith(".flac")) {
				playFlac();
			} else if (fileName.endsWith(".mp3")) {
				playMp3();
			}
		}
	}

	private void playFlac() {
		try {
			decoder = new FLACDecoder(new FileInputStream(currentMusic.getAudio()));
			streamInfo = decoder.readStreamInfo();
			audioFormat = new AudioFormat(streamInfo.getSampleRate(),
					streamInfo.getBitsPerSample() == 24 ? 16 : streamInfo.getBitsPerSample(),
					streamInfo.getChannels(), (streamInfo.getBitsPerSample() <= 8) ? false : true, false);
			info = new DataLine.Info(SourceDataLine.class, audioFormat);

			sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
			sourceDataLine.open(audioFormat);
			setVolume(volume);
			sourceDataLine.start();

			Frame frame;
			ByteData byteData = new ByteData(FFT_SIZE * 4);

			while ((frame = decoder.readNextFrame()) != null) {

				while (!playing) {
					Thread.sleep(10);
				}

				ByteData pcm = decoder.decodeFrame(frame, byteData);
				updateSpectrum(pcm.getData());
				sourceDataLine.write(pcm.getData(), 0, pcm.getLen());
			}

			if ((int) getCurrentTime() >= (int) getEndTime()) {
				runnable.run();
			}

			sourceDataLine.drain();
			sourceDataLine.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void playMp3() {
		AudioInputStream audioInputStream = null;
		AudioInputStream decodedAudioInputStream = null;
		
		try {
			File audioFile = currentMusic.getAudio();
			audioInputStream = AudioSystem.getAudioInputStream(audioFile);
			AudioFormat baseFormat = audioInputStream.getFormat();
			
			// Try to get duration from properties
			javax.sound.sampled.AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(audioFile);
			if (fileFormat instanceof javax.sound.sampled.AudioFileFormat) {
				java.util.Map<String, Object> properties = fileFormat.properties();
				if (properties.containsKey("duration")) {
					mp3EndTime = ((Long) properties.get("duration")) / 1000000.0f;
				} else if (properties.containsKey("duration")) {
					mp3EndTime = ((Number) properties.get("duration")).floatValue() / 1000000.0f;
				}
			}
			
			// Fallback: Estimate from file size if duration missing
			if (mp3EndTime <= 0) {
				long fileSize = audioFile.length();
				int frameSize = baseFormat.getFrameSize();
				float frameRate = baseFormat.getFrameRate();
				if (frameSize > 0 && frameRate > 0) {
					mp3EndTime = (fileSize / (float)frameSize) / frameRate;
				} else {
					// Fallback for variable bitrate: assume 128kbps average
					// Size (bits) / 128000
					mp3EndTime = (fileSize * 8) / 128000.0f;
				}
			}
			
			// Decode MP3 to PCM
			AudioFormat decodedFormat = new AudioFormat(
					AudioFormat.Encoding.PCM_SIGNED,
					baseFormat.getSampleRate(),
					16,
					baseFormat.getChannels(),
					baseFormat.getChannels() * 2,
					baseFormat.getSampleRate(),
					false
			);
			
			decodedAudioInputStream = AudioSystem.getAudioInputStream(decodedFormat, audioInputStream);
			audioFormat = decodedFormat;
			
			info = new DataLine.Info(SourceDataLine.class, decodedFormat);
			sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
			sourceDataLine.open(decodedFormat);
			setVolume(volume);
			sourceDataLine.start();
			
			byte[] buffer = new byte[4096];
			int bytesRead;
			
			while ((bytesRead = decodedAudioInputStream.read(buffer, 0, buffer.length)) != -1) {
				while (!playing) {
					Thread.sleep(10);
				}
				
				updateSpectrum(buffer);
				sourceDataLine.write(buffer, 0, bytesRead);
			}
			
			if ((int) getCurrentTime() >= (int) getEndTime()) {
				runnable.run();
			}
			
			sourceDataLine.drain();
			sourceDataLine.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (decodedAudioInputStream != null) {
					decodedAudioInputStream.close();
				}
				if (audioInputStream != null) {
					audioInputStream.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void updateSpectrum(byte[] audioData) {

		for (int i = 0; i < Math.min(audioData.length / 2, FFT_SIZE); i++) {
			int index = i * 2;
			if (index + 1 < audioData.length) {
				short sample = (short) ((audioData[index + 1] << 8) | (audioData[index] & 0xFF));
				fftBuffer[i] = sample / 32768.0f;
			}
		}

		for (int i = 0; i < SPECTRUM_BANDS; i++) {
			float sum = 0;
			int startIdx = (i * FFT_SIZE) / SPECTRUM_BANDS;
			int endIdx = ((i + 1) * FFT_SIZE) / SPECTRUM_BANDS;

			for (int j = startIdx; j < endIdx; j++) {
				sum += Math.abs(fftBuffer[j]);
			}

			float average = sum / (endIdx - startIdx);
			magnitudes[i] = average * 60;
			VISUALIZER[i] = magnitudes[i] * (-2F);
		}
	}

	public void setCurrentMusic(Music currentMusic) {

		playing = false;

		if (sourceDataLine != null) {
			sourceDataLine.stop();
			sourceDataLine.drain();
			sourceDataLine.close();
		}

		this.currentMusic = currentMusic;
		this.streamInfo = null;
		this.mp3EndTime = 0;
		playing = true;
	}

	public boolean isPlaying() {
		return playing;
	}

	public void setPlaying(boolean playing) {
		this.playing = playing;
	}

	public float getCurrentTime() {

		if (sourceDataLine == null || audioFormat == null) {
			return 0;
		}

		if (!playing) {
			return lastCurrentTime;
		}

		lastCurrentTime = (float) (sourceDataLine.getMicrosecondPosition() / 1000000.0);
		return lastCurrentTime;
	}

	public float getEndTime() {
		// For FLAC files
		if (streamInfo != null) {
			long totalSamples = streamInfo.getTotalSamples();
			int sampleRate = streamInfo.getSampleRate();

			if (totalSamples > 0 && sampleRate > 0) {
				return (float) totalSamples / sampleRate;
			}
		}
		
		// For MP3 files
		if (mp3EndTime > 0) {
			return mp3EndTime;
		}

		return 0;
	}

	public float getVolume() {
		return volume;
	}

	public void setVolume(float volume) {
		if (volume >= 0.0f && volume <= 1.0f) {
			this.volume = volume;
			if (sourceDataLine != null) {
				FloatControl gainControl = (FloatControl) sourceDataLine.getControl(FloatControl.Type.MASTER_GAIN);
				float gain = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
				gainControl.setValue(gain);
			}
		}
	}
}