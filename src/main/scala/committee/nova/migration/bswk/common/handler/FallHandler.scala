package committee.nova.migration.bswk.common.handler

import committee.nova.migration.bswk.common.block.init.BlockInit
import committee.nova.migration.bswk.common.config.CommonConfig
import cpw.mods.fml.common.eventhandler.SubscribeEvent
import net.minecraft.block.material.Material
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraftforge.event.entity.living.LivingFallEvent

import scala.math.round

class FallHandler {
  @SubscribeEvent
  def onLivingFall(event: LivingFallEvent): Unit = {
    if (event.isCanceled) return
    val entity = event.entityLiving
    if (entity == null) return
    if (EnchantmentHelper.getEnchantmentLevel(CommonConfig.enchantmentBasaltWalkerId, entity.getEquipmentInSlot(1)) <= 0) return
    val world = entity.worldObj
    if (world.isRemote) return
    val pos = Array(round(entity.posX).intValue(), entity.posY.intValue() - 1, round(entity.posZ).intValue())
    val blocks = Array(
      world.getBlock(pos(0), pos(1), pos(2)), world.getBlock(pos(0) - 1, pos(1), pos(2)),
      world.getBlock(pos(0), pos(1), pos(2) - 1), world.getBlock(pos(0) - 1, pos(1), pos(2) - 1)
    )
    for (block <- blocks if block.getMaterial == Material.lava || block == BlockInit.softBasalt) event.setCanceled(true)
  }
}
