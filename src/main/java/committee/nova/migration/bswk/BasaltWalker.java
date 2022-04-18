package committee.nova.migration.bswk;

import committee.nova.migration.bswk.common.block.init.BlockInit;
import committee.nova.migration.bswk.common.config.CommonConfig;
import committee.nova.migration.bswk.common.enchantment.init.EnchantmentInit;
import committee.nova.migration.bswk.common.util.RegistryHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.fml.common.Mod;

import static java.lang.Math.round;
import static net.minecraftforge.event.ForgeEventFactory.onBlockPlace;

@Mod(BasaltWalker.MODID)
public class BasaltWalker {
    public static final String MODID = "bswk";

    public BasaltWalker() {
        RegistryHandler.register();
        MinecraftForge.EVENT_BUS.addListener(this::onPlayerTick);
        MinecraftForge.EVENT_BUS.addListener(this::onLivingFallEvent);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public void onPlayerTick(final TickEvent.PlayerTickEvent event) {
        if (event.isCanceled()) return;
        if (event.phase != TickEvent.Phase.END) return;
        final Player player = event.player;
        final int radius = EnchantmentHelper.getItemEnchantmentLevel(EnchantmentInit.basaltWalker.get(), player.getItemBySlot(EquipmentSlot.FEET)) + 2;
        if (radius <= 2) return;
        final Level level = player.level;
        final BlockPos playerPos = player.blockPosition();
        if (!level.getBlockState(playerPos.below()).getFluidState().is(FluidTags.LAVA)) return;
        final Block basalt = BlockInit.softBasalt.get();
        final BlockState soft = basalt.defaultBlockState();
        for (final BlockPos blockpos : BlockPos.betweenClosed(playerPos.offset(-radius, -1.0D, -radius), playerPos.offset(radius, -1.0D, radius))) {
            if (!blockpos.closerToCenterThan(player.position(), radius)) continue;
            final BlockState state0 = level.getBlockState(blockpos.above());
            if (!state0.isAir()) continue;
            final BlockState state1 = level.getBlockState(blockpos);
            if (state1.getMaterial() != Material.LAVA) continue;
            final boolean full = state1.getFluidState().is(FluidTags.LAVA) && state1.getFluidState().isSource();
            if (!full || !level.isUnobstructed(soft, blockpos, CollisionContext.empty()) || onBlockPlace(player, BlockSnapshot.create(level.dimension(), level, blockpos), Direction.UP))
                continue;
            level.setBlockAndUpdate(blockpos, soft);
            level.scheduleTick(blockpos, basalt, Mth.nextInt(level.getRandom(), 40, 80));
        }
    }

    public void onLivingFallEvent(LivingFallEvent event) {
        if (event.isCanceled()) return;
        if (!CommonConfig.preventFallDamage.get()) return;
        final LivingEntity entity = event.getEntityLiving();
        if (entity == null) return;
        final Level world = entity.level;
        if (world.isClientSide) return;
        final BlockPos pos = new BlockPos(round((float) entity.getX()), entity.getY(), round((float) entity.getZ())).below();
        final BlockState[] blocks = new BlockState[]{
                world.getBlockState(pos),
                world.getBlockState(pos.west()),
                world.getBlockState(pos.north()),
                world.getBlockState(pos.west().north())
        };
        for (final BlockState block : blocks) {
            if (block.is(BlockInit.softBasalt.get()) || block.is(Blocks.LAVA)) {
                event.setCanceled(true);
                return;
            }
        }
    }
}
