package committee.nova.migration.bswk.common.enchantment.impl

import committee.nova.migration.bswk.BasaltWalker
import net.minecraft.enchantment.{Enchantment, EnumEnchantmentType}

class EnchantmentBasaltWalker(id: Int) extends Enchantment(id, 5, EnumEnchantmentType.armor_feet) {
  setName(BasaltWalker.MODID + ".basalt_walker")

  override def getMaxLevel: Int = 2
}
