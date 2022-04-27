package committee.nova.migration.bswk.common.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class SoftBasaltBlock extends Block {
    public static final IntProperty MELT = IntProperty.of("melt", 0, 3);

    public SoftBasaltBlock() {
        super(FabricBlockSettings.of(Material.STONE, MapColor.BLACK).sounds(BlockSoundGroup.STONE).strength(.8F).dropsNothing().ticksRandomly());
        this.setDefaultState(getStateManager().getDefaultState().with(MELT, 0));
    }

    @Override
    public BlockState getPlacementState(@Nonnull ItemPlacementContext ctx) {
        return getStateManager().getDefaultState().with(MELT, 0);
    }

    @Override
    public void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(MELT);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(Blocks.AIR);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        this.scheduledTick(state, world, pos, random);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        BlockPos.Mutable blockPos = new BlockPos.Mutable();
        final Direction[] directions = Direction.values();
        for (final Direction direction : directions) {
            blockPos.set(pos, direction);
            final BlockState blockstate = world.getBlockState(blockPos);
            if (blockstate.isOf(this) && !this.tryRemove(blockstate, world, blockPos))
                world.getBlockTickScheduler().schedule(blockPos, this, MathHelper.nextInt(random, 40, 80));
        }
        world.getBlockTickScheduler().schedule(pos, this, MathHelper.nextInt(random, 40, 80));
    }

    @ParametersAreNonnullByDefault
    @Override
    public void neighborUpdate(BlockState state, World level, BlockPos pos1, Block block, BlockPos pos2, boolean bool) {
        if (!level.isClient && block.getDefaultState().isOf(this) && this.fewerNeighboursThan(level, pos1)) {
            this.tryRemove(state, (ServerWorld) level, pos1);
        }
        super.neighborUpdate(state, level, pos1, block, pos2, bool);
    }

    private boolean fewerNeighboursThan(World level, BlockPos pos) {
        int i = 0;
        final BlockPos.Mutable pos0 = new BlockPos.Mutable();
        final Direction[] directions = Direction.values();
        for (Direction direction : directions) {
            pos0.set(pos, direction);
            if (level.getBlockState(pos0).isOf(this)) {
                ++i;
                if (i >= 2) return false;
            }
        }
        return true;
    }

    public boolean tryRemove(BlockState state, ServerWorld world, BlockPos pos) {
        final int melt = state.get(MELT);
        final boolean shouldRemove = melt >= 3;
        if (!shouldRemove) {
            world.setBlockState(pos, this.getDefaultState().with(MELT, melt + 1));
            return false;
        }
        if (world.getDimension().isUltrawarm()) {
            world.setBlockState(pos, Blocks.LAVA.getDefaultState());
            world.updateNeighbors(pos, Blocks.LAVA);
            return true;
        }
        world.removeBlock(pos, false);
        world.updateNeighbors(pos, Blocks.LAVA);
        return true;
    }
}
