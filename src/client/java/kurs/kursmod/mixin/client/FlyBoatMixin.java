package kurs.kursmod.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class FlyBoatMixin {
    private static final Logger LOGGER = LoggerFactory.getLogger("Maekiz");
    private static final double VELOCITY_BUMP = 0.09;
    private static final double MAX_SPEED = 1.5;
    private static final double MAX_VELY = 1.3;
    private static final double SPEED = 1.1;

    @Inject(at = @At("HEAD"), method = "tick")
    private void init(CallbackInfo info) {
        var mc = MinecraftClient.getInstance();
        var player = mc.player;

        // sjekke etter spiller:
        if (player == null){
            return;
        }

        //sjekker etter båt

        var vehicle = player.getVehicle();

        if (vehicle == null){
            return;
        }

        // sjekk etter jumpkey/space
        Vec3d current = vehicle.getVelocity();
        var velX = current.x * SPEED;
        var velZ = current.z * SPEED;

        double horizontalLen = Math.sqrt(velX * velX + velZ * velZ);

        if (horizontalLen > MAX_SPEED){
            // skalerer ned
            double scale = MAX_SPEED / horizontalLen;

            velX = velX * scale;
            velZ = velZ * scale;
        }
        double velY = current.y;

        if (mc.options.jumpKey.isPressed()){
            velY =Math.min(current.y + VELOCITY_BUMP, MAX_VELY);
        }
        vehicle.setVelocity(velX, velY, velZ);

        // Får velocity




    }
}
