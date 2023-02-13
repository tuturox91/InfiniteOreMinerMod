package com.sniklz.infiniteminerblock.util;

import com.sniklz.infiniteminerblock.Infiniteminerblock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static class Blocks {
        public static final TagKey<Block> INFINITE_ORE_MINER_BLOCKS =
                tag("infinite_ore_miner_blocks");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(Infiniteminerblock.MODID, name));
        }

        private static TagKey<Block> forgeTag(String name) {
            return BlockTags.create(new ResourceLocation("data/forge", name));
        }
    }

    public static class Items {

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(Infiniteminerblock.MODID, name));
        }

        private static TagKey<Item> forgeTag(String name) {
            return ItemTags.create(new ResourceLocation("data/forge", name));
        }
    }
}
