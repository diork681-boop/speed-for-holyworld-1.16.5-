package com.example.utils;

import net.minecraft.client.MinecraftClient;

public class AntiCheatUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();

    // Адаптивный Timer (подстраивается под пинг)
    public static float getAdaptiveTimer() {
        float baseTimer = 1.05f; // Базовый Timer
        float maxTimer = 1.1f;   // Максимальный Timer

        // Если пинг высокий, уменьшаем Timer
        if (mc.getNetworkHandler() != null && mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid()) != null) {
            int ping = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid()).getLatency();
            if (ping > 100) {
                return Math.max(baseTimer - 0.02f, 1.0f);
            }
        }

        return Math.min(baseTimer + (float) (Math.random() * 0.02), maxTimer);
    }
}
