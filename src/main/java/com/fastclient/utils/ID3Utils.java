package com.fastclient.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class ID3Utils {

    public static class Metadata {
        public String title;
        public String artist;
        public byte[] imageData;
    }

    public static Metadata extractMetadata(File file) {
        Metadata meta = new Metadata();
        try (FileInputStream fis = new FileInputStream(file)) {
            byte[] header = new byte[10];
            if (fis.read(header) != 10) return null;

            if (header[0] != 'I' || header[1] != 'D' || header[2] != '3') {
                return null;
            }

            int version = header[3];
            int size = ((header[6] & 0x7F) << 21) | ((header[7] & 0x7F) << 14) | ((header[8] & 0x7F) << 7) | (header[9] & 0x7F);

            byte[] data = new byte[size];
            if (fis.read(data) != size) return null;

            int offset = 0;
            while (offset < size - 10) {
                String frameId = new String(data, offset, 4, StandardCharsets.US_ASCII);
                int frameSize;
                if (version >= 4) {
                    frameSize = ((data[offset + 4] & 0x7F) << 21) | ((data[offset + 5] & 0x7F) << 14) | ((data[offset + 6] & 0x7F) << 7) | (data[offset + 7] & 0x7F);
                } else {
                    frameSize = ((data[offset + 4] & 0xFF) << 24) | ((data[offset + 5] & 0xFF) << 16) | ((data[offset + 6] & 0xFF) << 8) | (data[offset + 7] & 0xFF);
                }
                
                if (frameSize <= 0) break;

                if (frameId.equals("APIC")) {
                    extractApic(data, offset, frameSize, meta);
                } else if (frameId.equals("TIT2")) {
                    meta.title = extractText(data, offset, frameSize);
                } else if (frameId.equals("TPE1")) {
                    meta.artist = extractText(data, offset, frameSize);
                }
                
                offset += 10 + frameSize;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return meta;
    }

    private static void extractApic(byte[] data, int offset, int frameSize, Metadata meta) {
        int contentOffset = offset + 10;
        int encoding = data[contentOffset];
        contentOffset++;
        
        while (contentOffset < offset + 10 + frameSize && data[contentOffset] != 0) {
            contentOffset++;
        }
        contentOffset++;
        
        contentOffset++;
        if (encoding == 0 || encoding == 3) {
            while (contentOffset < offset + 10 + frameSize && data[contentOffset] != 0) {
                contentOffset++;
            }
            contentOffset++;
        } else {
            while (contentOffset < offset + 10 + frameSize - 1 && (data[contentOffset] != 0 || data[contentOffset + 1] != 0)) {
                contentOffset += 2;
            }
            contentOffset += 2;
        }
        
        int imageSize = (offset + 10 + frameSize) - contentOffset;
        if (imageSize > 0) {
            byte[] imageData = new byte[imageSize];
            System.arraycopy(data, contentOffset, imageData, 0, imageSize);
            meta.imageData = imageData;
        }
    }

    private static String extractText(byte[] data, int offset, int frameSize) {
        if (frameSize <= 1) return null;
        int encoding = data[offset + 10];
        Charset charset;
        int skip = 1;
        switch (encoding) {
            case 1: charset = StandardCharsets.UTF_16; break;
            case 2: charset = StandardCharsets.UTF_16BE; break;
            case 3: charset = StandardCharsets.UTF_8; break;
            default: charset = StandardCharsets.ISO_8859_1; break;
        }
        if (encoding == 1 && frameSize > 3) {
            if ((data[offset + 11] & 0xFF) == 0xFE && (data[offset + 12] & 0xFF) == 0xFF) {
            }
        }
        return new String(data, offset + 11, frameSize - 1, charset).trim();
    }
}
