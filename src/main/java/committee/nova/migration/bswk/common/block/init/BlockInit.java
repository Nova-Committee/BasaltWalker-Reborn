package committee.nova.migration.bswk.common.block.init;

import committee.nova.migration.bswk.common.block.impl.SoftBasaltBlock;
import committee.nova.migration.bswk.common.util.RegistryHandler;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

public class BlockInit {
    public static final RegistryObject<Block> softBasalt = RegistryHandler.Blocks.register("soft_basalt", SoftBasaltBlock::new);

    public static void init() {
    }
}
