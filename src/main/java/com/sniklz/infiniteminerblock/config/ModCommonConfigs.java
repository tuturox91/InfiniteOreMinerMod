package com.sniklz.infiniteminerblock.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static ForgeConfigSpec.ConfigValue<Double> ORE_CHUNK_NEEDED_CHANCE;
    public static ForgeConfigSpec.ConfigValue<Integer> MIN_ORE_VEIN_SIZE;
    public static ForgeConfigSpec.ConfigValue<Integer> MAX_ORE_VEIN_SIZE;
    public static ForgeConfigSpec.ConfigValue<Integer> CHUNK_COUNT_TO_UP_MODIFIER;
    public static ForgeConfigSpec.ConfigValue<Integer> TICK_COUNT_TO_PRODUCE_ORE;

    public static ForgeConfigSpec.ConfigValue<Integer> ENERGY_CAPACITY;
    public static ForgeConfigSpec.ConfigValue<Integer> ENERGY_TRANSFER_SIZE;
    public static ForgeConfigSpec.ConfigValue<Integer> ENERGY_REQ_PER_TICK;


    static {
        BUILDER.push("Configs for Ore Miner Mod");

        ORE_CHUNK_NEEDED_CHANCE = BUILDER.comment("Chance that there will be ore in the chunk, where bigger number, is bigger chance.")
                .defineInRange("Chunk have ore chance", 4.5d, 0d, 9.99d);

        MIN_ORE_VEIN_SIZE = BUILDER.define("Minimal size of ore vein", 800);
        MAX_ORE_VEIN_SIZE = BUILDER.define("Maximum size of ore vien", 2000);

        CHUNK_COUNT_TO_UP_MODIFIER = BUILDER.comment("Every 63 chunks the modifier is increased by 0.1: "
                + "the formula for the ore size: randomOreNumber * 1.0, where 1.0 is the modifier and randomedOreNumber "
                + "is a random number between the minimum ore size and the maximum ore size.").define("Modifier increased every", 63);

        TICK_COUNT_TO_PRODUCE_ORE = BUILDER.comment("One second is 20 ticks. Default value is 60 = 3 second. ").define("Tick count to produce ore", 60);

        ENERGY_CAPACITY = BUILDER.define("Energy capacity", 60000);
        ENERGY_TRANSFER_SIZE = BUILDER.define("Energy transfer size", 256);
        ENERGY_REQ_PER_TICK = BUILDER.define("How many energy need per tick for miner work", 5);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}
