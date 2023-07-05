package com.simplymerlin.warriorsplits.mixin;

import com.simplymerlin.warriorsplits.Timer;
import com.simplymerlin.warriorsplits.WarriorLiterals;
import com.simplymerlin.warriorsplits.segment.ComparisonType;
import com.simplymerlin.warriorsplits.segment.Segment;
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

import static com.simplymerlin.warriorsplits.util.Utils.durationToString;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    Timer timer = Timer.instance();
    boolean hidden = true;
    DecimalFormat df = new DecimalFormat("#0.0");

    int yOffset = 10;
    int lineHeight = 9;
    int xSegmentName = 10;
    int xRelativeTime = 192;
    int xBestTime = 256;
    int activeColor = WarriorLiterals.ACTIVE_COLOR;
    int inactiveColor = WarriorLiterals.INACTIVE_COLOR;
    int positiveColor = WarriorLiterals.POSITIVE_COLOR;
    int negativeColor = WarriorLiterals.NEGATIVE_COLOR;

    @Inject(at = @At("TAIL"), method = "render")
    public void renderSplits(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        if (timer.course() == null) {
            return;
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();

        TextRenderer renderer = minecraftClient.textRenderer;
        int i = 0;
        // this is a lot of code being ran on one frame, but this entire function takes approximately 0.000040 seconds!!!!
        var segments = timer.segments();
        for (Segment segment : segments) {
            int y = yOffset + i * lineHeight;
            boolean hasHappenedOrCurrent = i <= timer.currentSplit();
            boolean isCurrent = i == timer.currentSplit();

            // Render segment name
            renderer.drawWithShadow(matrices, segment.name(), xSegmentName, y, isCurrent ? activeColor : inactiveColor);

            String relativeTime = "";
            boolean positive = false;

            if (segment.comparisonRelativeTime() != null && hasHappenedOrCurrent) {
                long relativeTimeMillis = segment.relativeTime(timer.startTime()).toMillis() - segment.comparisonRelativeTime().toMillis();
                double time = (double) relativeTimeMillis / 1000;
                positive = time > 0;

                if (time > -5 || segment.ended()) {
                    relativeTime = (positive ? "+" : "") + df.format(time);
                }
            }

            int relativeColor = positive ? positiveColor : negativeColor;
            if (
                    segment.length() != null &&
                    segment.comparison(ComparisonType.GOLD).length() != null &&
                    segment.length().compareTo(segment.comparison(ComparisonType.GOLD).length()) < 0) {
                relativeColor = 0xFFAA00;
            }

            int relativeTimeWidth = renderer.getWidth(relativeTime);
            renderer.drawWithShadow(matrices, relativeTime, xRelativeTime - relativeTimeWidth, y, relativeColor);

            Duration time = null;

            if (segment.comparisonRelativeTime() != null) {
                time = segment.comparisonRelativeTime();
            }

            // If there's already a relative time, use the segment's relative time
            if (segment.relativeTime() != null) {
                time = segment.relativeTime();
            }

            String bestTime = time != null ? durationToString(time) : "-";

            // Time width shouldn't change ever (unless someone takes 100 minutes on one segment?) so caching this would be a quick performance win.
            int bestTimeWidth = renderer.getWidth(bestTime);
            renderer.drawWithShadow(matrices, bestTime, xBestTime - bestTimeWidth, y, isCurrent ? activeColor : inactiveColor);

            i++;
        }
        int y = yOffset + (i + 1) * lineHeight;


        renderer.drawWithShadow(matrices, "WarriorSplits", 10, y, WarriorLiterals.LOGO_COLOR);
        String time = durationToString(timer.time());
        renderer.drawWithShadow(matrices, time, 256 - renderer.getWidth(time), y, timer.started() ? WarriorLiterals.MAIN_CLOCK_COLOR : WarriorLiterals.INACTIVE_COLOR);
    }

    @Inject(at = @At("TAIL"), method = "setTitle")
    public void startTimer(Text text, CallbackInfo ci) {
        if (text.getString().equals("Go!")) {
            timer.start();
        }
        if (text.getString().equals("Run Complete!")) {
            timer.split();
        }
    }

    @Inject(at = @At("TAIL"), method = "setSubtitle")
    public void split(Text text, CallbackInfo ci) {
        if (text.getString().startsWith("+1")) {
            timer.split();
        }
    }

}
