package kurs.kursmod.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Vec3d;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class FlyBoatMixin {
    private static final double VELOCITY_BUMP = 0.09;
    private static final double MAX_SPEED = 1.5;
    private static final double MAX_VELY = 1.3;
    private static final double SPEED = 1.1;

    // Setter inn i minecraftClient.class i tick-funksjonen (kjører hele tiden)
    @Inject(at = @At("HEAD"), method = "tick")
    private void repeatingEverySecond(CallbackInfo info) {
        MinecraftClient mc = MinecraftClient.getInstance();
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
        // Endrer horisontal fart
        Vec3d current = vehicle.getVelocity();
        double velX = current.x * SPEED;
        double velZ = current.z * SPEED;

        double horizontalLen = Math.sqrt(velX * velX + velZ * velZ);

        // Justerer ned farten hvis lengden til posisjonsvektoren er større en max speed
        if (horizontalLen > MAX_SPEED){

            double scale = MAX_SPEED / horizontalLen;

            velX = velX * scale;
            velZ = velZ * scale;
        }

        double velY = current.y;

        // sjekke om space/hoppeknappen trykkes
        if (mc.options.jumpKey.isPressed()){
            velY =Math.min(current.y + VELOCITY_BUMP, MAX_VELY);
        }

        // setter ny fart
        vehicle.setVelocity(velX, velY, velZ);

    }
}