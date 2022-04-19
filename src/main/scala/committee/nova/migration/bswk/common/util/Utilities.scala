package committee.nova.migration.bswk.common.util

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.{BlockPos, EnumFacing}

import scala.collection.mutable.ArrayBuffer

object Utilities {
  def betweenClosedDown(pos: BlockPos, radius: Int): ArrayBuffer[BlockPos] = {
    val buffer = new ArrayBuffer[BlockPos]()
    for (i <- -radius to radius) for (j <- -radius to radius) buffer.+=(pos.west(i).south(j).down())
    buffer
  }

  def closerToCenterThan(blockPos: BlockPos, player: EntityPlayer, radius: Int): Boolean = blockPos.distanceSqToCenter(player.posX, blockPos.getY, player.posZ) < radius * radius

  def facingHorizontal(): Array[EnumFacing] = {
    val c: Array[EnumFacing] = Array(null, null, null, null)
    for (i <- 2 to 5) c(i - 2) = EnumFacing.getFront(i)
    c
  }
}
