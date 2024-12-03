package me.smithingui.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.RecipePropertySet;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.SmithingScreenHandler;
import net.minecraft.screen.slot.ForgingSlotsManager;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import java.util.Objects;

@Mixin(SmithingScreenHandler.class)
public abstract class SmithingScreenHandlerMixin extends ForgingScreenHandler {
    public SmithingScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory,
                                      ScreenHandlerContext context, ForgingSlotsManager forgingSlotsManager) {
        super(type, syncId, playerInventory, context, forgingSlotsManager);
    }

    @ModifyArg(method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;Lnet/minecraft/world/World;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/ForgingScreenHandler;<init>(Lnet/minecraft/screen/ScreenHandlerType;ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;Lnet/minecraft/screen/slot/ForgingSlotsManager;)V"),
            index = 4)
    private static ForgingSlotsManager createForgingSlotsManager(ForgingSlotsManager slotManager, @Local(argsOnly = true) World world) {
        RecipeManager recipeMgr = world.getRecipeManager();
        RecipePropertySet templatePropSet = recipeMgr.getPropertySet(RecipePropertySet.SMITHING_TEMPLATE);
        RecipePropertySet armorPiecePropSet = recipeMgr.getPropertySet(RecipePropertySet.SMITHING_BASE);
        RecipePropertySet materialPropSet = recipeMgr.getPropertySet(RecipePropertySet.SMITHING_ADDITION);
        Objects.requireNonNull(armorPiecePropSet);
        Objects.requireNonNull(templatePropSet);
        Objects.requireNonNull(materialPropSet);

        // Move slots to fit gui
        return ForgingSlotsManager.builder()
                .input(0, 64, 35, templatePropSet::canUse)
                .input(1, 38, 45, armorPiecePropSet::canUse)
                .input(2, 18, 25, materialPropSet::canUse)
                .output(3, 142, 35).build();
    }
}
