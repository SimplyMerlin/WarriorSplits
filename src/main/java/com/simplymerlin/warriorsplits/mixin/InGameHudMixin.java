package com.simplymerlin.warriorsplits.mixin;

import com.simplymerlin.warriorsplits.Split;
import com.simplymerlin.warriorsplits.Timer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

import static com.simplymerlin.warriorsplits.utils.durationToString;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    Timer timer = Timer.getInstance();

    @Inject(at = @At("TAIL"), method = "render")
    public void renderSplits(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();

        TextRenderer renderer = minecraftClient.textRenderer;

        renderer.drawWithShadow(matrices, "WarriorSplits " + durationToString(timer.getTime()), 10, 10, 0xFFFFFF);

        var splits = timer.getSplits();
        int i = 0;

        for (Split split : splits) {
            int y = 19 + i * 9;
            renderer.drawWithShadow(matrices, split.getName(), 10, y, 0xFFFFFF);
            String relative = "";
            if (split.getPersonalBestTime() != null && i <= timer.getCurrentSplit()) {
                long time = split.getRelativeTime(timer.getStartTime()).toSeconds() - split.getPersonalBestTime().toSeconds();
                if (time > -5 || split.hasEnded()) {
                    relative = String.valueOf(time);
                }
            }
            int width1 = renderer.getWidth(relative);
            renderer.drawWithShadow(matrices, relative, 192 - width1, y, 0xFFFFFF);
            Duration time = null;
            if (split.getPersonalBestTime() != null) {
                time = split.getPersonalBestTime();
            }
            if (split.getRelativeTime() != null) {
                time = split.getRelativeTime();
            }
            String bestTime = time != null ? durationToString(time) : "-";
            int width = renderer.getWidth(bestTime);
            renderer.drawWithShadow(matrices, bestTime, 256 - width, y, 0xFFFFFF);
            ++i;
        }

    }

    @Inject(at = @At("TAIL"), method = "setTitle")
    public void startTimer(Text text, CallbackInfo ci) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (text.getString().equals("Go!")) {
            timer.startTimer();
            minecraftClient.inGameHud.getChatHud().addMessage(Text.of("started! :)"));
        }
        if (text.getString().equals("Run Complete!")) {
            timer.split();
        }
    }

    @Inject(at = @At("TAIL"), method = "setSubtitle")
    public void split(Text text, CallbackInfo ci) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (text.getString().startsWith("+1")) {
            timer.split();
            minecraftClient.inGameHud.getChatHud().addMessage(Text.of("split"));
        }
    }

}
