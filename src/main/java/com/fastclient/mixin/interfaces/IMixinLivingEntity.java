package com.fastclient.mixin.interfaces;

import net.minecraft.util.Hand;

public interface IMixinLivingEntity {
	void fakeSwingHand(Hand hand);
}
