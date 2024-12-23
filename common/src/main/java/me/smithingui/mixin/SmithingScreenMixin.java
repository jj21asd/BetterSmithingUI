package me.smithingui.mixin;

import me.smithingui.SmithingUI;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.SmithingScreen;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SmithingScreen.class)
public abstract class SmithingScreenMixin extends ForgingScreen<SmithingScreenHandler> {
    @Unique
    private static final Quaternionf STAND_ROT = new Quaternionf()
            .rotationXYZ(MathHelper.PI * 0.12f, 0, MathHelper.PI);

    @Shadow
    private ArmorStandEntity armorStand;

    public SmithingScreenMixin(SmithingScreenHandler handler, PlayerInventory playerInventory,
                               Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    // Prevent title position from being set
    @Redirect(method = "<init>", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/client/gui/screen/ingame/SmithingScreen;titleX:I"))
    private void assignTitleX(SmithingScreen instance, int value) { }

    @Redirect(method = "<init>", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/client/gui/screen/ingame/SmithingScreen;titleY:I"))
    private void assignTitleY(SmithingScreen instance, int value) { }

    @Inject(method = "hasInvalidRecipe", at = @At("HEAD"), cancellable = true)
    private void hasInvalidRecipe(CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(false); // Hide the invalid recipe arrow
    }

    @Inject(method = "drawBackground", at = @At(value = "INVOKE", shift = At.Shift.AFTER,
            target = "Lnet/minecraft/client/gui/screen/ingame/ForgingScreen;drawBackground(Lnet/minecraft/client/gui/DrawContext;FII)V"), cancellable = true)
    private void drawBackground(DrawContext drawContext, float f, int i, int j, CallbackInfo ci) {
        InventoryScreen.drawEntity(drawContext, x + 111, y + 67, 25, new Vector3f(),
                STAND_ROT, new Quaternionf(), armorStand);
        ci.cancel(); // Skip the rest of the function
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/ForgingScreen;<init>(Lnet/minecraft/screen/ForgingScreenHandler;Lnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/text/Text;Lnet/minecraft/util/Identifier;)V"),
            index = 3)
    private static Identifier getTexture(Identifier identifier) {
        return SmithingUI.asId("menu.png"); // Replace texture
    }

    @Redirect(method = "setup", at = @At(value = "FIELD", opcode = Opcodes.PUTFIELD,
            target = "Lnet/minecraft/entity/decoration/ArmorStandEntity;bodyYaw:F"))
    private void assignBodyYaw(ArmorStandEntity instance, float value) {
        instance.bodyYaw = 200; // Customize armor stand yaw
    }

    @Redirect(method = "render", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/screen/ingame/SmithingScreen;renderSlotTooltip(Lnet/minecraft/client/gui/DrawContext;II)V"))
    private void renderSlotTooltip(SmithingScreen instance, DrawContext optional, int itemStack, int itemStack2) {
        // Hide all tooltips
    }
}
