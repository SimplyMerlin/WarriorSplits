package com.simplymerlin.warriorsplits;

import com.simplymerlin.warriorsplits.command.TimerCommand;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;

@Environment(EnvType.CLIENT)
public class WarriorSplits implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        new Timer();

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> TimerCommand.register(dispatcher));
    }
}
