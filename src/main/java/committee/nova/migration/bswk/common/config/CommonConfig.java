package committee.nova.migration.bswk.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {
    public static final ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec.BooleanValue isTreasure = builder.comment("Basalt Walker Configuration", "Should the basalt walker enchantment be a treasure enchantment?")
            .define("treasure", true);
    public static final ForgeConfigSpec.BooleanValue preventFallDamage = builder.comment("If true, player in boots with a basalt walker enchantment won't injure when falling on a soft basalt block")
            .define("no_fall_damage", true);
    public static final ForgeConfigSpec config = builder.build();
}
