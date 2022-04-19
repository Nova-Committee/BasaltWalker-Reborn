package committee.nova.migration.bswk.common.util

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.util.EnumFacing

import scala.collection.mutable.ArrayBuffer

object Utilities {
  def betweenClosedDown(pos: Array[Int], radius: Int): ArrayBuffer[Array[Int]] = {
    val buffer = new ArrayBuffer[Array[Int]]()
    for (i <- -radius to radius) for (j <- -radius to radius) buffer.+=(Array(pos(0) + i, pos(1) - 1, pos(2) + j))
    buffer
  }

  def closerToCenterThan(blockPos: Array[Int], player: EntityPlayer, radius: Int): Boolean = (player.posX - blockPos(0)) * (player.posX - blockPos(0)) + (player.posZ - blockPos(2)) * (player.posZ - blockPos(2)) < radius * radius

  def facingHorizontal(): Array[EnumFacing] = {
    val c: Array[EnumFacing] = Array(null, null, null, null)
    for (i <- 2 to 5) c(i - 2) = EnumFacing.getFront(i)
    c
  }
}
