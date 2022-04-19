package committee.nova.migration.bswk.common.config

import committee.nova.migration.bswk.common.config.CommonConfig._
import committee.nova.migration.bswk.common.enchantment.init.EnchantmentInit
import net.minecraftforge.common.config.Configuration
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import org.apache.logging.log4j.Logger

object CommonConfig {
  var preventFallDmg: Boolean = _
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
    preventFallDmg = config.getBoolean("prevent_fall_damage", Configuration.CATEGORY_GENERAL, true, "If true, player in boots with a basalt walker enchantment won't injure when falling on a soft basalt block")
    enchantmentBasaltWalkerId = getEnchantmentId(EnchantmentInit.basaltWalker, 140)
    config.save()
  }

  def getEnchantmentId(name: String, default: Int): Int = config.getInt(name, Configuration.CATEGORY_GENERAL, default, enchantmentMin, enchantmentMax, "Id of the enchantment " + name)
}
