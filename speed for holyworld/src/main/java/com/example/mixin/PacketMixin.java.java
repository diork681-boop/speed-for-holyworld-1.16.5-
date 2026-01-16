package com.example.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class PacketMixin {
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private int packetTicks = 0;

    @Inject(method = "sendMovementPackets", at = @At("HEAD"), cancellable = true)
    private void onSendMovementPackets(CallbackInfo ci) {
        if (mc.player == null) return;

        // Отменяем стандартные пакеты движения (заменяем своими)
        ci.cancel();

        // Отправляем фейковые пакеты с предсказанием движения
        double predictedX = mc.player.getX() + (mc.player.getVelocity().x * 1.05);
        double predictedZ = mc.player.getZ() + (mc.player.getVelocity().z * 1.05);

        mc.player.networkHandler.sendPacket(
            new PlayerMoveC2SPacket.PositionAndOnGround(
                predictedX,
                mc.player.getY(),
                predictedZ,
                mc.player.isOnGround()
            )
        );

        packetTicks++;
    }
}