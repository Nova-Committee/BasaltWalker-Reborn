package committee.nova.migration.bswk.common.enchantment.init

import committee.nova.migration.bswk.common.config.CommonConfig._
import committee.nova.migration.bswk.common.enchantment.impl.EnchantmentBasaltWalker
import net.minecraft.enchantment.Enchantment

import java.text.MessageFormat
import scala.collection.mutable

object EnchantmentInit {
  val basaltWalker = "basalt_walker"
  val enchantmentMap = new mutable.HashMap[String, Enchantment]()

  def init(): Unit = {
    try {
      val enchantment = new EnchantmentBasaltWalker(enchantmentBasaltWalkerId)
      enchantmentMap.put(basaltWalker, enchantment)
      Enchantment.addToBookList(enchantment)
    } catch {
      case _: Exception => getLogger.error(MessageFormat.format("Duplicate or illegal enchantment id: {0}, registry of enchantment {1} will be skipped.", String.valueOf(enchantmentBasaltWalkerId), basaltWalker))
    }
  }
}
