package committee.nova.migration.bswk.common.util;

import committee.nova.migration.bswk.BasaltWalker;
import committee.nova.migration.bswk.common.block.init.BlockInit;
import committee.nova.migration.bswk.common.config.CommonConfig;
import committee.nova.migration.bswk.common.enchantment.init.EnchantmentInit;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryHandler {
    public static final DeferredRegister<Block> Blocks = DeferredRegister.create(ForgeRegistries.BLOCKS, BasaltWalker.MODID);
    public static final DeferredRegister<Enchantment> Enchantments = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, BasaltWalker.MODID);

    public static void register() {
        final IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BlockInit.init();
        EnchantmentInit.init();
        Blocks.register(eventBus);
        Enchantments.register(eventBus);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, CommonConfig.config);
    }
}
