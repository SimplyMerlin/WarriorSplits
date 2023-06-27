package com.simplymerlin.warriorsplits.command;

import com.mojang.brigadier.CommandDispatcher;
import com.simplymerlin.warriorsplits.Timer;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

import static com.mojang.brigadier.arguments.StringArgumentType.string;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.argument;
import static net.fabricmc.fabric.api.client.command.v2.ClientCommandManager.literal;

public final class TimerCommand {

    static Timer timer = Timer.instance();

    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(literal("timer")
                .then(literal("start").executes(
                    ctx -> {
                        timer.start();
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
                            timer.reset();
                            ctx.getSource().sendFeedback(Text.of("Timer reset."));
                            return 1;
                        }))
                .then(literal("setcourse")
                        .then(argument("course", string())
                        .executes(
                                ctx -> {
                                    timer.course(ctx.getArgument("course", String.class));
                                    return 1;
                                }
                        )))
        );
    }

}