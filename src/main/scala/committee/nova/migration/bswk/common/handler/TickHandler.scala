package committee.nova.migration.bswk.common.handler

import committee.nova.migration.bswk.common.block.init.BlockInit
import committee.nova.migration.bswk.common.config.CommonConfig
import committee.nova.migration.bswk.common.util.Utilities
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import cpw.mods.fml.common.gameevent.TickEvent.{Phase, PlayerTickEvent}
import net.minecraft.block.material.Material
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraftforge.common.util.{BlockSnapshot, ForgeDirection}
import net.minecraftforge.event.ForgeEventFactory.onPlayerBlockPlace

import scala.util.control.Breaks

class TickHandler {
  @SubscribeEvent
  def onPlayerTick(event: PlayerTickEvent): Unit = {
    if (event.isCanceled) return
    if (event.phase != Phase.END) return
    val player = event.player
    val radius = EnchantmentHelper.getEnchantmentLevel(CommonConfig.enchantmentBasaltWalkerId, player.getEquipmentInSlot(1)) + 2
    if (radius <= 2) return
    val world = player.worldObj
    val playerPos = Array(Math.floor(player.posX).intValue(), Math.floor(player.posY).intValue(), Math.floor(player.posZ).intValue())
    val ground = world.getBlock(playerPos(0), playerPos(1) - 1, playerPos(2))
    val basalt = BlockInit.softBasalt
    if (ground.getMaterial != Material.lava && ground != basalt) return
    for (blockPos <- Utilities.betweenClosedDown(playerPos, radius)) {
      val loop = new Breaks
      loop.breakable {
        if (!Utilities.closerToCenterThan(blockPos, player, radius)) loop.break()
        if (world.getBlock(blockPos(0), blockPos(1) + 1, blockPos(2)).getMaterial != Material.air) loop.break()
        if (world.getBlock(blockPos(0), blockPos(1), blockPos(2)).getMaterial != Material.lava) loop.break()
        if (onPlayerBlockPlace(player, BlockSnapshot.getBlockSnapshot(world, blockPos(0), blockPos(1), blockPos(2), 2), ForgeDirection.UP).isCanceled) loop.break()
        world.setBlock(blockPos(0), blockPos(1), blockPos(2), basalt, 0, 2)
        world.scheduleBlockUpdate(blockPos(0), blockPos(1), blockPos(2), basalt, 40 + world.rand.nextInt(41))
      }
    }
  }
}
