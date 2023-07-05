package com.simplymerlin.warriorsplits.mixin;

import com.simplymerlin.warriorsplits.Timer;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.ScoreboardObjectiveUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.TeamS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPacketListenerMixin {

    Timer timer = Timer.instance();

    @Inject(method = "onScoreboardObjectiveUpdate", at = @At("TAIL"))
    public void onObjectiveChange(ScoreboardObjectiveUpdateS2CPacket scoreboardObjectiveUpdateS2CPacket, CallbackInfo ci) {
        if (!scoreboardObjectiveUpdateS2CPacket.getDisplayName().getString().equals("MCCI: PARKOUR WARRIOR ")) {
            timer.course(null);
        }
    }

    @Inject(method = "onTeam", at = @At("TAIL"))
    public void onTeamChange(TeamS2CPacket teamS2CPacket, CallbackInfo ci) {
        teamS2CPacket.getTeam().ifPresent(
                team -> {
                    String prefix = team.getPrefix().getString();
                    if (prefix.contains("COURSE:")) {
                        timer.monthlyCourse(prefix.substring(17));
                    }
                }
        );
    }

}
