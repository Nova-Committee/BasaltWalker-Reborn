package committee.nova.migration.bswk.common.handler

import committee.nova.migration.bswk.common.block.init.BlockInit
import committee.nova.migration.bswk.common.config.CommonConfig
import net.minecraft.block.material.Material
import net.minecraft.enchantment.EnchantmentHelper
import net.minecraft.util.BlockPos
import net.minecraftforge.event.entity.living.LivingFallEvent
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent

class FallHandler {
  @SubscribeEvent
  def onLivingFall(event: LivingFallEvent): Unit = {
    if (event.isCanceled) return
    val entity = event.entityLiving
    if (entity == null) return
    if (EnchantmentHelper.getEnchantmentLevel(CommonConfig.enchantmentBasaltWalkerId, entity.getEquipmentInSlot(1)) <= 0) return
    val world = entity.worldObj
    if (world.isRemote) return
    val pos = new BlockPos(entity).down()
    val blocks = Array(
      world.getBlockState(pos).getBlock,
      world.getBlockState(pos.west()).getBlock,
      world.getBlockState(pos.north()).getBlock,
      world.getBlockState(pos.west().north()).getBlock
    )
    for (block <- blocks if block.getMaterial == Material.lava || block == BlockInit.softBasalt) event.setCanceled(true)
  }
}
