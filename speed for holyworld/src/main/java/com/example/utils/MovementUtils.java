package com.example.utils;

import net.minecraft.client.MinecraftClient;

public class MovementUtils {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static double lastSpeed = 0.0;

    // Динамическое ускорение (адаптируется под сервер)
    public static double getDynamicSpeed() {
        double baseSpeed = 0.15; // Базовая скорость
        double maxSpeed = 0.3;   // Максимальная скорость

        // Если игрок движется медленно, ускоряем
        if (mc.player.forwardSpeed < 0.1) {
            lastSpeed = Math.min(lastSpeed + 0.01, maxSpeed);
        } else {
            lastSpeed = Math.max(lastSpeed - 0.01, baseSpeed);
        }

        // Случайные отклонения (чтобы не детектилось как бот)
        lastSpeed += (Math.random() * 0.02 - 0.01);

        return lastSpeed;
    }
}