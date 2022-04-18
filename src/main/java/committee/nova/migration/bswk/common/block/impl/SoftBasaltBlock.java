package committee.nova.migration.bswk.common.block.impl;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.HitResult;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Random;

public class SoftBasaltBlock extends Block {
    public static final IntegerProperty MELT = IntegerProperty.create("melt", 0, 3);

    public SoftBasaltBlock() {
        super(Properties.of(Material.STONE).sound(SoundType.STONE).strength(.8F).noDrops().randomTicks());
        registerDefaultState(getStateDefinition().any().setValue(MELT, 0));
    }

    @Override
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext ctx) {
        return getStateDefinition().any().setValue(MELT, 0);
    }

    @Override
    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> definition) {
        definition.add(MELT);
    }

    @Override
    public ItemStack getCloneItemStack(BlockState state, HitResult target, BlockGetter world, BlockPos pos, Player player) {
        return new ItemStack(Blocks.AIR);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void randomTick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        this.tick(state, world, pos, random);
    }

    @ParametersAreNonnullByDefault
    @Override
    public void tick(BlockState state, ServerLevel world, BlockPos pos, Random random) {
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        final Direction[] directions = Direction.values();
        for (final Direction direction : directions) {
            blockPos.setWithOffset(pos, direction);
            final BlockState blockstate = world.getBlockState(blockPos);
            if (blockstate.is(this) && !this.tryRemove(blockstate, world, blockPos)) {
                world.scheduleTick(blockPos, this, Mth.nextInt(random, 40, 80));
            }
        }
        world.scheduleTick(pos, this, Mth.nextInt(random, 40, 80));
    }

    @ParametersAreNonnullByDefault
    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos1, Block block, BlockPos pos2, boolean bool) {
        if (block.defaultBlockState().is(this) && this.fewerNeighboursThan(level, pos1)) {
            this.tryRemove(state, level, pos1);
        }
        super.neighborChanged(state, level, pos1, block, pos2, bool);
    }

    private boolean fewerNeighboursThan(BlockGetter level, BlockPos pos) {
        int i = 0;
        final BlockPos.MutableBlockPos pos0 = new BlockPos.MutableBlockPos();
        final Direction[] directions = Direction.values();
        for (Direction direction : directions) {
            pos0.setWithOffset(pos, direction);
            if (level.getBlockState(pos0).is(this)) {
                ++i;
                if (i >= 2) return false;
            }
        }
        return true;
    }

    public boolean tryRemove(BlockState state, Level world, BlockPos pos) {
        final int melt = state.getValue(MELT);
        final boolean shouldRemove = melt >= 3;
        if (!shouldRemove) {
            world.setBlockAndUpdate(pos, this.defaultBlockState().setValue(MELT, melt + 1));
            return false;
        }
        if (world.dimensionType().ultraWarm()) {
            world.setBlockAndUpdate(pos, Blocks.LAVA.defaultBlockState());
            world.neighborChanged(pos, Blocks.LAVA, pos);
            return true;
        }
        world.removeBlock(pos, false);
        world.neighborChanged(pos, Blocks.LAVA, pos);
        return true;
    }

}
