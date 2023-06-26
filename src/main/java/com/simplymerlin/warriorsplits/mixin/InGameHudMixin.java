package com.simplymerlin.warriorsplits.mixin;

import com.simplymerlin.warriorsplits.segment.Segment;
import com.simplymerlin.warriorsplits.Timer;
import com.simplymerlin.warriorsplits.WarriorLiterals;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.scoreboard.ScoreboardPlayerScore;
import net.minecraft.scoreboard.Team;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.text.DecimalFormat;
import java.time.Duration;
import java.time.Instant;

import static com.simplymerlin.warriorsplits.util.Utils.durationToString;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    Timer timer = Timer.instance();
    DecimalFormat df = new DecimalFormat("#0.0");

    @Inject(at = @At("TAIL"), method = "render")
    public void renderSplits(MatrixStack matrices, float tickDelta, CallbackInfo ci) {
        Instant start = Instant.now();
        MinecraftClient minecraftClient = MinecraftClient.getInstance();

        TextRenderer renderer = minecraftClient.textRenderer;
        int i = 0;
        // this is a lot of code being ran on one frame, but this entire function takes approximately 0.000040 seconds!!!!
        if (timer.course() != null) {
            var segments = timer.segments();
            for (Segment segment : segments) {
                int y = 10 + i * 9;
                boolean hasHappenedOrCurrent = i <= timer.currentSplit();
                boolean isCurrent = i == timer.currentSplit();
                renderer.drawWithShadow(matrices, segment.name(), 10, y, isCurrent ? WarriorLiterals.ACTIVE_COLOR : WarriorLiterals.INACTIVE_COLOR);
                String relative = "";
                boolean positive = false;
                if (segment.comparisonRelativeTime() != null && hasHappenedOrCurrent) {
                    double time = (double) (segment.relativeTime(timer.startTime()).toMillis() - segment.comparisonRelativeTime().toMillis()) / 1000;
                    positive = time > 0;
                    if (time > -5 || segment.ended()) {
                        relative = (positive ? "+" : "") + df.format(time);
                    }
                }
                int width1 = renderer.getWidth(relative);
                renderer.drawWithShadow(matrices, relative, 192 - width1, y, positive ? WarriorLiterals.POSITIVE_COLOR : WarriorLiterals.NEGATIVE_COLOR);
                Duration time = null;
                if (segment.comparisonRelativeTime() != null) {
                    time = segment.comparisonRelativeTime();
                }
                if (segment.relativeTime() != null) {
                    time = segment.relativeTime();
                }
                String bestTime = time != null ? durationToString(time) : "-";
                int width = renderer.getWidth(bestTime);
                renderer.drawWithShadow(matrices, bestTime, 256 - width, y, isCurrent ? WarriorLiterals.ACTIVE_COLOR : WarriorLiterals.INACTIVE_COLOR);
                ++i;
            }
        }
        int y = (i + 1) * 9 + 10;


        renderer.drawWithShadow(matrices, "WarriorSplits", 10, y, WarriorLiterals.LOGO_COLOR);
        String duration = durationToString(timer.time());
        renderer.drawWithShadow(matrices, duration, 256 - renderer.getWidth(duration), y, timer.started() ? WarriorLiterals.MAIN_CLOCK_COLOR : WarriorLiterals.INACTIVE_COLOR);
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

    // TODO: find method that doesnt run every frame LOL
    @Inject(at = @At("TAIL"), method = "renderScoreboardSidebar")
    public void setCourse(MatrixStack matrixStack, ScoreboardObjective scoreboardObjective, CallbackInfo ci) {
        if (
                // hey noxcrew dev reading this code, when ingame the displayname has a space v so please don't remove that!!!
                scoreboardObjective.getDisplayName().getString().equals("MCCI: PARKOUR WARRIOR ")
        ) {
            for (ScoreboardPlayerScore score : scoreboardObjective.getScoreboard().getAllPlayerScores(scoreboardObjective)) {
                Team team = scoreboardObjective.getScoreboard().getPlayerTeam(score.getPlayerName());
                if (team == null)
                    continue;
                Text balls = team.getPrefix();
                // COURSE: Course #
                if (balls.getString().contains("COURSE:")) {
                    timer.course(balls.getString().substring(17));
                }
            }
        } else {
            timer.course(null);
        }
    }

}
