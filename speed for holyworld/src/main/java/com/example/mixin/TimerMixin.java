package com.example.mixin;

import com.example.utils.AntiCheatUtils;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class TimerMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null) return;

        // Адаптивный Timer (подстраивается под пинг)
        float timer = AntiCheatUtils.getAdaptiveTimer();
        mc.timer.tickLength = 50.0f / timer;
    }
}