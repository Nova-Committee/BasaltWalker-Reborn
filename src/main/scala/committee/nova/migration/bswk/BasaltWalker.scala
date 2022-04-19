package committee.nova.migration.bswk

import committee.nova.migration.bswk.BasaltWalker.proxy
import committee.nova.migration.bswk.common.proxy.CommonProxy
import net.minecraftforge.fml.common.Mod.EventHandler
import net.minecraftforge.fml.common.event.{FMLInitializationEvent, FMLPreInitializationEvent}
import net.minecraftforge.fml.common.{Mod, SidedProxy}

object BasaltWalker {
  final val MODID = "bswk"
  @SidedProxy(serverSide = "committee.nova.migration.bswk.common.proxy.CommonProxy")
  val proxy: CommonProxy = new CommonProxy
  @Mod.Instance(BasaltWalker.MODID)
  var instance: BasaltWalker = _
}

@Mod(modid = BasaltWalker.MODID, useMetadata = true)
class BasaltWalker {
  BasaltWalker.instance = this

  @EventHandler
  def preInit(event: FMLPreInitializationEvent): Unit = proxy.preInit(event)

  @EventHandler
  def init(event: FMLInitializationEvent): Unit = proxy.init(event)
}