package committee.nova.migration.bswk.mixin;

import committee.nova.migration.bswk.BasaltWalker;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class MixinLivingEntity extends Entity {

    public MixinLivingEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    @Shadow
    public abstract ItemStack getEquippedStack(EquipmentSlot var1);

    @Inject(method = "computeFallDamage", at = @At("HEAD"), cancellable = true)
    public void onComputeFallDamage(float fallDistance, float damageMultiplier, CallbackInfoReturnable<Integer> cir) {
        final BlockPos ePos = new BlockPos(Math.round(this.getX()), Math.round(this.getY()), Math.round(this.getZ())).down();
        final BlockPos[] posList = new BlockPos[]{ePos, ePos.west(), ePos.north(), ePos.west().north()};
        for (final BlockPos pos : posList) {
            final Block block = world.getBlockState(pos).getBlock();
            if (block.is(BasaltWalker.block) || block.is(Blocks.LAVA)) {
                cir.setReturnValue(0);
                return;
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    public void onTick(CallbackInfo ci) {
        final int radius = EnchantmentHelper.getLevel(BasaltWalker.enchantment, this.getEquippedStack(EquipmentSlot.FEET)) + 2;
        if (radius <= 2) return;
        final World level = this.world;
        final BlockPos thisPos = this.getBlockPos();
        if (!level.getBlockState(thisPos.down()).getFluidState().isIn(FluidTags.LAVA)) return;
        final Block basalt = BasaltWalker.block;
        final BlockState soft = basalt.getDefaultState();
        for (final BlockPos blockpos : BlockPos.iterate(thisPos.add(-radius, -1.0D, -radius), thisPos.add(radius, -1.0D, radius))) {
            if (!blockpos.isWithinDistance(this.getPos(), radius)) continue;
            final BlockState state0 = level.getBlockState(blockpos.up());
            if (!state0.isAir()) continue;
            final BlockState state1 = level.getBlockState(blockpos);
            if (state1.getMaterial() != Material.LAVA) continue;
            final boolean full = state1.getFluidState().isIn(FluidTags.LAVA) && state1.getFluidState().isStill();
            if (!full) continue;
            level.setBlockState(blockpos, soft);
            level.getBlockTickScheduler().schedule(blockpos, basalt, MathHelper.nextInt(level.getRandom(), 40, 80));
        }
    }
}
