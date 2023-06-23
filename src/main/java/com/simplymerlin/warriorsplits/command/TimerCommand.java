package com.simplymerlin.warriorsplits.command;

import com.mojang.brigadier.CommandDispatcher;
import com.simplymerlin.warriorsplits.Timer;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.*;

public final class TimerCommand {

    static Timer timer = Timer.getInstance();

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("timer")
                .then(literal("start").executes(
                    ctx -> {
                        timer.startTimer();
                        ctx.getSource().sendFeedback(Text.of("Timer started!"));
                        return 1;
                    }))
                .then(literal("split").executes(
                        ctx -> {
                            timer.split();
                            ctx.getSource().sendFeedback(Text.of("Split!"));
                            return 1;
                        }))
                .then(literal("reset").executes(
                        ctx -> {
                            timer.resetTimer();
                            ctx.getSource().sendFeedback(Text.of("Timer reset."));
                            return 1;
                        }))
        );
    }

}