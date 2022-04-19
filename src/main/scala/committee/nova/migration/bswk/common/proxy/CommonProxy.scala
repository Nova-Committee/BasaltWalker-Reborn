package committee.nova.migration.bswk.common.proxy

import committee.nova.migration.bswk.common.block.init.BlockInit
import committee.nova.migration.bswk.common.config.CommonConfig
import committee.nova.migration.bswk.common.enchantment.init.EnchantmentInit
import committee.nova.migration.bswk.common.handler.{FallHandler, TickHandler}
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPreInitializationEvent}

class CommonProxy {
  def preInit(event: FMLPreInitializationEvent): Unit = {
    new CommonConfig(event)
    BlockInit.init()
    EnchantmentInit.init()
  }

  def init(event: FMLInitializationEvent): Unit = {
    FMLCommonHandler.instance().bus().register(new TickHandler)
    if (CommonConfig.preventFallDmg) MinecraftForge.EVENT_BUS.register(new FallHandler)
  }
}
