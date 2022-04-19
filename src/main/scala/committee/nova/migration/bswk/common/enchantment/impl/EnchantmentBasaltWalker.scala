package committee.nova.migration.bswk.common.enchantment.impl

import committee.nova.migration.bswk.BasaltWalker
import net.minecraft.enchantment.{Enchantment, EnumEnchantmentType}
import net.minecraft.util.ResourceLocation

class EnchantmentBasaltWalker(id: Int) extends Enchantment(id, new ResourceLocation(BasaltWalker.MODID, "basalt_walker"), 5, EnumEnchantmentType.ARMOR_FEET) {
  setName(BasaltWalker.MODID + ".basalt_walker")

  override def getMaxLevel: Int = 2
}
