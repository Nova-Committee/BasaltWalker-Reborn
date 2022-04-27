package committee.nova.migration.bswk;

import committee.nova.migration.bswk.common.block.SoftBasaltBlock;
import committee.nova.migration.bswk.common.enchantment.BasaltWalkerEnchantment;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class BasaltWalker implements ModInitializer {
    public static final String MODID = "bswk";
    public static BasaltWalkerEnchantment enchantment;
    public static SoftBasaltBlock block;

    public static Identifier prefixId(String id) {
        return new Identifier(MODID, id);
    }

    @Override
    public void onInitialize() {
        block = Registry.register(Registry.BLOCK, prefixId("soft_basalt"), new SoftBasaltBlock());
        enchantment = Registry.register(Registry.ENCHANTMENT, prefixId("basalt_walker"), new BasaltWalkerEnchantment());
    }
}
