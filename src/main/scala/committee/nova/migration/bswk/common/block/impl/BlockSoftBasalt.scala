package committee.nova.migration.bswk.common.block.impl

import committee.nova.migration.bswk.BasaltWalker
import committee.nova.migration.bswk.common.block.impl.BlockSoftBasalt.MELT
import committee.nova.migration.bswk.common.util.Utilities
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyInteger
import net.minecraft.block.state.{BlockState, IBlockState}
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.util.{BlockPos, EnumFacing, MovingObjectPosition}
import net.minecraft.world.{IBlockAccess, World}

import java.util
import java.util.Random

object BlockSoftBasalt {
  val MELT: PropertyInteger = PropertyInteger.create("melt", 0, 3)
}

class BlockSoftBasalt extends Block(Material.rock) {
  setStepSound(Block.soundTypeStone)
  setHardness(.8F)
  setUnlocalizedName(BasaltWalker.MODID + ".soft_basalt")
  setTickRandomly(true)
  setDefaultState(this.blockState.getBaseState.withProperty(MELT, 0))

  override def createBlockState(): BlockState = new BlockState(this, MELT)

  override def getMetaFromState(state: IBlockState): Int = state.getValue(MELT).asInstanceOf[Int]

  override def getStateFromMeta(meta: Int): IBlockState = this.getDefaultState.withProperty(MELT, meta)

  override def getDrops(world: IBlockAccess, pos: BlockPos, state: IBlockState, fortune: Int): util.List[ItemStack] = null

  override def getPickBlock(target: MovingObjectPosition, world: World, pos: BlockPos): ItemStack = null

  override def updateTick(world: World, blockPos: BlockPos, state: IBlockState, rand: Random): Unit = {
    super.updateTick(world, blockPos, state, rand)
    for (facing <- Utilities.facingHorizontal()) {
      val pos = blockPos.offset(facing)
      val target = world.getBlockState(pos).getBlock
      if (target == this && !tryRemove(world, pos)) world.scheduleUpdate(pos, target, 40 + rand.nextInt(41))
    }
    world.scheduleUpdate(blockPos, this, 40 + rand.nextInt(41))
  }

  override def onNeighborBlockChange(world: World, pos: BlockPos, state: IBlockState, neighborBlock: Block): Unit = {
    if (this.fewerNeighboursThan(world, pos)) this.tryRemove(world, pos)
    super.onNeighborBlockChange(world, pos, state, neighborBlock)
  }

  def tryRemove(world: World, pos: BlockPos): Boolean = {
    val melt = world.getBlockState(pos).getValue(MELT).asInstanceOf[Int]
    val shouldRemove = melt >= 3
    if (!shouldRemove) {
      world.setBlockState(pos, this.getDefaultState.withProperty(MELT, melt + 1), 2)
      world.notifyNeighborsOfStateChange(pos, this)
      return false
    }
    if (world.provider.getDimensionId == -1) {
      world.setBlockState(pos, Blocks.lava.getDefaultState, 2)
      world.notifyNeighborsOfStateChange(pos, Blocks.lava)
      return true
    }
    world.setBlockToAir(pos)
    world.notifyNeighborsOfStateChange(pos, Blocks.lava)
    true
  }

  private def fewerNeighboursThan(world: World, blockPos: BlockPos): Boolean = {
    var i = 0
    val facings = EnumFacing.values
    for (facing <- facings) {
      val pos = blockPos.offset(facing)
      if (world.getBlockState(pos).getBlock == this) {
        i += 1
        if (i >= 2) return false
      }
    }
    true
  }
}
