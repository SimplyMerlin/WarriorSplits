package com.simplymerlin.warriorsplits.mixin;

import com.simplymerlin.warriorsplits.Split;
import com.simplymerlin.warriorsplits.Timer;
import com.simplymerlin.warriorsplits.WarriorLiterals;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.DecimalFormat;
import java.time.Duration;

import static com.simplymerlin.warriorsplits.utils.durationToString;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    Timer timer = Timer.getInstance();
    DecimalFormat df = new DecimalFormat("#0.0");

    @Inject(at = @At("TAIL"), method = "render")
    public void renderSplits(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
            MinecraftClient minecraftClient = MinecraftClient.getInstance();

            TextRenderer renderer = minecraftClient.textRenderer;

            var splits = timer.getSplits();
            int i = 0;

            for (Split split : splits) {
                int y = 10 + i * 9;
                boolean hasHappenedOrCurrent = i <= timer.getCurrentSplit();
                boolean isCurrent = i == timer.getCurrentSplit();
                renderer.drawWithShadow(matrices, split.getName(), 10, y, isCurrent ? WarriorLiterals.ACTIVE_COLOR : WarriorLiterals.INACTIVE_COLOR);
                String relative = "";
                boolean positive = false;
                if (split.getPersonalBestTime() != null && hasHappenedOrCurrent) {
                    double time = (double) (split.getRelativeTime(timer.getStartTime()).toMillis() - split.getPersonalBestTime().toMillis()) / 1000;
                    positive = time > 0;
                    if (time > -5 || split.hasEnded()) {
                        relative = (positive ? "+" : "") + df.format(time);
                    }
                }
                int width1 = renderer.getWidth(relative);
                renderer.drawWithShadow(matrices, relative, 192 - width1, y, positive ? WarriorLiterals.POSITIVE_COLOR : WarriorLiterals.NEGATIVE_COLOR);
                Duration time = null;
                if (split.getPersonalBestTime() != null) {
                    time = split.getPersonalBestTime();
                }
                if (split.getRelativeTime() != null) {
                    time = split.getRelativeTime();
                }
                String bestTime = time != null ? durationToString(time) : "-";
                int width = renderer.getWidth(bestTime);
                renderer.drawWithShadow(matrices, bestTime, 256 - width, y, isCurrent ? WarriorLiterals.ACTIVE_COLOR : WarriorLiterals.INACTIVE_COLOR);
                ++i;
            }
            int y = (splits.size() + 1) * 9 + 10;

        renderer.drawWithShadow(matrices, "WarriorSplits", 10, y, WarriorLiterals.LOGO_COLOR);
        String duration = durationToString(timer.getTime());
        renderer.drawWithShadow(matrices, duration, 256 - renderer.getWidth(duration), y, timer.isStarted() ? WarriorLiterals.MAIN_CLOCK_COLOR : WarriorLiterals.INACTIVE_COLOR);

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
