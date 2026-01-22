package com.fastclient.mixin.mixins.minecraft.network;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.fastclient.event.EventBus;
import com.fastclient.event.client.ReceivePacketEvent;
import com.fastclient.event.client.SendPacketEvent;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.OffThreadException;
import net.minecraft.network.listener.PacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.BundleS2CPacket;

@Mixin(ClientConnection.class)
public abstract class MixinClientConnection {

	@Shadow
	protected static <T extends PacketListener> void handlePacket(Packet<T> packet, PacketListener listener) {
	}

	@Inject(method = "send(Lnet/minecraft/network/packet/Packet;)V", at = @At("HEAD"), cancellable = true)
	private void onSendPacket(Packet<?> packet, CallbackInfo ci) {

		SendPacketEvent event = new SendPacketEvent(packet);

		EventBus.getInstance().post(event);

		if (event.isCancelled()) {
			ci.cancel();
		}
	}

	@Inject(method = "handlePacket", at = @At("HEAD"), cancellable = true, require = 1)
	private static void onRecievePacket(Packet<?> packet, PacketListener listener, CallbackInfo ci) {

		if (packet instanceof BundleS2CPacket bundleS2CPacket) {
			ci.cancel();

			for (Packet<?> packetInBundle : bundleS2CPacket.getPackets()) {
				try {
					handlePacket(packetInBundle, listener);
				} catch (OffThreadException ignored) {
				}
			}
			return;
		}

		ReceivePacketEvent event = new ReceivePacketEvent(packet);
		EventBus.getInstance().post(event);

		if (event.isCancelled()) {
			ci.cancel();
		}
	}
}
