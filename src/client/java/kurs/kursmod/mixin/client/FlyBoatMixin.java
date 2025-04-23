package kurs.kursmod.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class FlyBoatMixin {
    private double VELOCITY_BUMP = 0.1;
    private double SPEED = 1.1;

    @Inject(at = @At("HEAD"), method = "tick")
    private void init(CallbackInfo info) {
        var mc = MinecraftClient.getInstance();
        var player = mc.player;

        // sjekke etter spiller:
        if (player == null){
            return;
        }
        // Sjekke etter båt

        var vehicle = player.getVehicle();

        if (vehicle == null){
            return;
        }

        // få vektorer og sett ny vektor

        if (mc.options.jumpKey.isPressed()){
            Vec3d current = vehicle.getVelocity();
            var velX = current.x * SPEED;
            var velZ = current.z * SPEED;


            vehicle.setVelocity(velX, current.y + VELOCITY_BUMP, velZ);
        }

        // set vektor y
    }
}
