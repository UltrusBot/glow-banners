package me.ultrusmods.glowingbanners.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeveledCauldronBlock;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CauldronBehavior.class)
public interface CauldronBehaviorMixin {
    @Inject(method = "method_32214", at = @At("HEAD"), cancellable = true, remap = false)
    private static void addGlowingCleanBehavior(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, ItemStack stack, CallbackInfoReturnable<ActionResult> cir) {
        NbtCompound nbtCompound = BlockItem.getBlockEntityNbtFromStack(stack);
        if (nbtCompound != null && nbtCompound.getBoolean("isGlowing")) {
            if (!world.isClient) {
                ItemStack itemStack = stack.copy();
                itemStack.setCount(1);
                nbtCompound.putBoolean("isGlowing", false);
                itemStack.setSubNbt("BlockEntityTag", nbtCompound);
                if (!player.getAbilities().creativeMode) {
                    stack.decrement(1);
                }

                if (stack.isEmpty()) {
                    player.setStackInHand(hand, itemStack);
                } else if (player.getInventory().insertStack(itemStack)) {
                    player.playerScreenHandler.syncState();
                } else {
                    player.dropItem(itemStack, false);
                }

                LeveledCauldronBlock.decrementFluidLevel(state, world, pos);
            }
            cir.setReturnValue(ActionResult.success(world.isClient));
        }
    }
}
