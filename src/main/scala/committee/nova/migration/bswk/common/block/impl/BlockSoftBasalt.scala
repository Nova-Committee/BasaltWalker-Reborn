package committee.nova.migration.bswk.common.block.impl

import committee.nova.migration.bswk.BasaltWalker
import committee.nova.migration.bswk.common.util.Utilities
import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.client.renderer.texture.IIconRegister
import net.minecraft.init.Blocks
import net.minecraft.item.{Item, ItemStack}
import net.minecraft.util.{EnumFacing, IIcon, MovingObjectPosition}
import net.minecraft.world.World

import java.util.Random

class BlockSoftBasalt extends Block(Material.rock) {
  setStepSound(Block.soundTypeStone)
  setHardness(.8F)
  setBlockName(BasaltWalker.MODID + ".soft_basalt")
  setTickRandomly(true)

  val icons: Array[IIcon] = Array(null, null, null, null, null, null, null, null)

  override def registerBlockIcons(reg: IIconRegister): Unit = for (i <- 0 until 8) icons(i) = reg.registerIcon(BasaltWalker.MODID + ":soft_basalt_" + String.valueOf(i))

  override def getIcon(side: Int, meta: Int): IIcon = icons(meta * 2 + (if (side > 1) 1 else 0))

  override def getItemDropped(i: Int, r: Random, j: Int): Item = null

  override def getPickBlock(target: MovingObjectPosition, world: World, x: Int, y: Int, z: Int): ItemStack = null

  override def tickRate(world: World): Int = 1

  override def updateTick(world: World, x: Int, y: Int, z: Int, r: Random): Unit = {
    super.updateTick(world, x, y, z, r)
    for (facing <- Utilities.facingHorizontal()) {
      val pos = Array(x + facing.getFrontOffsetX, y, z + facing.getFrontOffsetZ)
      val target = world.getBlock(pos(0), pos(1), pos(2))
      if (target == this && !tryRemove(world, pos(0), pos(1), pos(2))) world.scheduleBlockUpdate(pos(0), pos(1), pos(2), target, 40 + r.nextInt(41))
    }
    world.scheduleBlockUpdate(x, y, z, this, 40 + r.nextInt(41))
  }

  def tryRemove(world: World, x: Int, y: Int, z: Int): Boolean = {
    val melt = world.getBlockMetadata(x, y, z)
    val shouldRemove = melt >= 3
    if (!shouldRemove) {
      world.setBlockMetadataWithNotify(x, y, z, melt + 1, 2)
      world.notifyBlocksOfNeighborChange(x, y, z, this)
      return false
    }
    if (world.provider.isHellWorld) {
      world.setBlock(x, y, z, Blocks.lava, 0, 2)
      world.notifyBlocksOfNeighborChange(x, y, z, Blocks.lava)
      return true
    }
    world.setBlockToAir(x, y, z)
    world.notifyBlocksOfNeighborChange(x, y, z, Blocks.lava)
    true
  }

  override def onNeighborBlockChange(world: World, x: Int, y: Int, z: Int, block: Block): Unit = {
    if (this.fewerNeighboursThan(world, x, y, z)) this.tryRemove(world, x, y, z)
    super.onNeighborBlockChange(world, x, y, z, block)
  }

  private def fewerNeighboursThan(world: World, x: Int, y: Int, z: Int): Boolean = {
    var i = 0
    val facings = EnumFacing.values
    for (facing <- facings) {
      val pos = Array(x + facing.getFrontOffsetX, y + facing.getFrontOffsetY, z + facing.getFrontOffsetZ)
      if (world.getBlock(pos(0), pos(1), pos(2)) == this) {
        i += 1
        if (i >= 2) return false
      }
    }
    true
  }

  override def onBlockAdded(world: World, x: Int, y: Int, z: Int): Unit = {
    world.scheduleBlockUpdate(x, y, z, this, 40 + world.rand.nextInt(41))
  }
}
