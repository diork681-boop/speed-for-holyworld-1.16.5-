package com.example.mixin;

import com.example.utils.MovementUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class SpeedMixin {
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private int ticks = 0;
    private double lastX, lastZ;
    private boolean wasOnGround = false;

    @Inject(method = "tickMovement", at = @At("HEAD"))
    private void onTickMovement(CallbackInfo ci) {
        if (mc.player == null || mc.world == null) return;

        // Включаем только при спринте
        if (!mc.player.isSprinting()) return;

        // Динамическое ускорение (адаптируется под сервер)
        double speed = MovementUtils.getDynamicSpeed();

        // LowHop (низкий прыжок)
        if (mc.player.isOnGround() && mc.options.keyJump.isPressed()) {
            mc.player.jump();
            mc.player.setVelocity(
                mc.player.getVelocity().x * (1.0 + speed),
                0.35, // Низкий прыжок (HolyWorld не детектит)
                mc.player.getVelocity().z * (1.0 + speed)
            );
            wasOnGround = true;
        } else {
            wasOnGround = false;
        }

        // Packet Spoofing (фейковые пакеты движения)
        if (ticks % 2 == 0) {
            double predictedX = mc.player.getX() + (mc.player.getVelocity().x * 1.1);
            double predictedZ = mc.player.getZ() + (mc.player.getVelocity().z * 1.1);

            // NoGround Spoof (обход проверки прыжков)
            mc.player.networkHandler.sendPacket(
                new PlayerMoveC2SPacket.PositionAndOnGround(
                    predictedX,
                    mc.player.getY(),
                    predictedZ,
                    false // Всегда "в воздухе" (обход NoGround)
                )
            );
        }

        // Phase Bypass (прохождение сквозь блоки)
        if (mc.player.horizontalCollision) {
            mc.player.setVelocity(
                mc.player.getVelocity().x * 1.05,
                0.1, // Небольшой подъём
                mc.player.getVelocity().z * 1.05
            );
        }

        ticks++;
    }
}