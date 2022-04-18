package committee.nova.migration.bswk.common.enchantment.impl;

import committee.nova.migration.bswk.common.config.CommonConfig;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class BasaltWalkerEnchantment extends Enchantment {
    public BasaltWalkerEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET});
    }

    @Override
    public int getMaxLevel() {
        return 2;
    }

    @Override
    public boolean isTreasureOnly() {
        return CommonConfig.isTreasure.get();

    }
}
