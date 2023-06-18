package com.simplymerlin.warriorsplits;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class WarriorSplits implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        new Timer();
    }
}
