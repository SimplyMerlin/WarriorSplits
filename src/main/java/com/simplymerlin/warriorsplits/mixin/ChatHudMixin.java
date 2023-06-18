package com.simplymerlin.warriorsplits.mixin;

import com.simplymerlin.warriorsplits.Timer;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatHud.class)
public class ChatHudMixin {

    Timer timer = Timer.getInstance();

    String RESTART_MESSAGE = "literal{[}[style={color=#FFFF00}, siblings=[literal{\uE075}[style={color=white,font=mcc:icon}], literal{] You restarted the course!}[style={}]]]";

    @Inject(at = @At("TAIL"), method = "addMessage(Lnet/minecraft/text/Text;)V")
    public void reset(Text text, CallbackInfo ci) {
        if(text.toString().equals(RESTART_MESSAGE)) {
            timer.resetTimer();
        }
    }

}
