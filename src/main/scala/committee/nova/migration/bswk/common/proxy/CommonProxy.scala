package committee.nova.migration.bswk.common.proxy

import committee.nova.migration.bswk.common.block.init.BlockInit
import committee.nova.migration.bswk.common.config.CommonConfig
import committee.nova.migration.bswk.common.enchantment.init.EnchantmentInit
import committee.nova.migration.bswk.common.handler.{FallHandler, TickHandler}
import cpw.mods.fml.common.FMLCommonHandler
import cpw.mods.fml.common.event.{FMLInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.common.MinecraftForge

class CommonProxy {
  def preInit(event: FMLPreInitializationEvent): Unit = {
    new CommonConfig(event)
    BlockInit.init()
    EnchantmentInit.init()
  }

  def init(event: FMLInitializationEvent): Unit = {
    FMLCommonHandler.instance().bus().register(new TickHandler)
    MinecraftForge.EVENT_BUS.register(new FallHandler)
  }
}
