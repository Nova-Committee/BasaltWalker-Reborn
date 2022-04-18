package committee.nova.migration.bswk.common.enchantment.init;

import committee.nova.migration.bswk.common.enchantment.impl.BasaltWalkerEnchantment;
import committee.nova.migration.bswk.common.util.RegistryHandler;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.RegistryObject;

public class EnchantmentInit {
    public static final RegistryObject<Enchantment> basaltWalker = RegistryHandler.Enchantments.register("basalt_walker", BasaltWalkerEnchantment::new);

    public static void init() {
    }
}
