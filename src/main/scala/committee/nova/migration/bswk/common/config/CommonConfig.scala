package committee.nova.migration.bswk.common.config

import committee.nova.migration.bswk.common.config.CommonConfig._
import committee.nova.migration.bswk.common.enchantment.init.EnchantmentInit
import cpw.mods.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.common.config.Configuration
import org.apache.logging.log4j.Logger

object CommonConfig {
  var fallDmg: Boolean = _
  var enchantmentBasaltWalkerId: Int = _
  private var config: Configuration = _
  private var logger: Logger = _

  def getLogger: Logger = logger
}

class CommonConfig(event: FMLPreInitializationEvent) {
  val enchantmentMin = 64
  val enchantmentMax = 255
  logger = event.getModLog
  config = new Configuration(event.getSuggestedConfigurationFile)
  config.load()
  load()

  def load(): Unit = {
    // TODO:
    enchantmentBasaltWalkerId = getEnchantmentId(EnchantmentInit.basaltWalker, 140)
    config.save()
  }

  def getEnchantmentId(name: String, default: Int): Int = config.getInt(name, Configuration.CATEGORY_GENERAL, default, enchantmentMin, enchantmentMax, "Id of the enchantment " + name)
}
