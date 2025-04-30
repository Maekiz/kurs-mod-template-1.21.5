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

// Klasse med hacken
public class FlyBoatMixin {
    private static final double VELOCITY_BUMP = 0.09; // variabel for fart oppover
    private static final double MAX_SPEED = 1.5; // variabel for maks fart
    private static final double MAX_VELY = 1.3; // begrensning på fart oppover
    private static final double SPEED = 1.1; // Horisontal fart fremover

    // Setter funksjonen inn i minecraftClient.class i tick-funksjonen (kjører hele tiden)
    @Inject(at = @At("HEAD"), method = "tick")
    private void repeatingEverySecond(CallbackInfo info) {
        MinecraftClient mc = MinecraftClient.getInstance(); // variabel for om du er i spillet
        var player = mc.player; // variebel for spilleren i en verden

        // Hvis det ikke er en spiller i en verden
        if (player == null){
            return; // gjør ingenting
        }
        //variabel for båt
        var vehicle = player.getVehicle();

        // hvis spilleren ikke sitter i en båt
        if (vehicle == null){
            return; // gjør ingenting
        }
        // Endrer horisontal farts-variabler
        Vec3d current = vehicle.getVelocity(); // Får farten til båten
        double velX = current.x * SPEED;
        double velZ = current.z * SPEED;

        double horizontalLen = Math.sqrt(velX * velX + velZ * velZ); // variabel for fart framover

        // Justerer ned farten hvis farten framover er større enn maks-farten
        if (horizontalLen > MAX_SPEED){

            double scale = MAX_SPEED / horizontalLen;

            velX = velX * scale;
            velZ = velZ * scale;
        }

        double velY = current.y; // variabel for oppover fart er farten den allerede har

        // sjekker om space/hoppeknappen trykkes
        if (mc.options.jumpKey.isPressed()){
            velY =Math.min(current.y + VELOCITY_BUMP, MAX_VELY); // endrer på oppvoverfart
        }

        // setter ny fart
        vehicle.setVelocity(velX, velY, velZ); // setter ny velocity

    }
}