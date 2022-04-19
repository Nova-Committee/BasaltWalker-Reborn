package committee.nova.migration.bswk.common.handler

import committee.nova.migration.bswk.common.block.init.BlockInit
import committee.nova.migration.bswk.common.config.CommonConfig
import committee.nova.migration.bswk.common.util.Utilities
import net.minecraft.block.material.Material
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.util.{BlockPos, EnumFacing}
import net.minecraftforge.common.util.BlockSnapshot
import net.minecraftforge.event.ForgeEventFactory.onPlayerBlockPlace
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.TickEvent.{Phase, PlayerTickEvent}

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
    val playerPos = new BlockPos(player)
    val ground = world.getBlockState(playerPos.down()).getBlock
    val basalt = BlockInit.softBasalt
    if (ground.getMaterial != Material.lava && ground != basalt) return
    for (blockPos <- Utilities.betweenClosedDown(playerPos, radius)) {
      val loop = new Breaks
      loop.breakable {
        if (!Utilities.closerToCenterThan(blockPos, player, radius)) loop.break()
        if (world.getBlockState(blockPos.up()).getBlock.getMaterial != Material.air) loop.break()
        if (world.getBlockState(blockPos).getBlock.getMaterial != Material.lava) loop.break()
        if (onPlayerBlockPlace(player, BlockSnapshot.getBlockSnapshot(world, blockPos, 2), EnumFacing.UP).isCanceled) loop.break()
        world.setBlockState(blockPos, basalt.getDefaultState, 2)
        world.scheduleUpdate(blockPos, basalt, 40 + world.rand.nextInt(41))
      }
    }
  }
}
