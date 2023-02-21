package com.sniklz.infiniteminerblock.block;

import com.sniklz.infiniteminerblock.Infiniteminerblock;
import com.sniklz.infiniteminerblock.block.custom.InfiniteOreMiner;
import com.sniklz.infiniteminerblock.item.InfiniteBlockItem;
import com.sniklz.infiniteminerblock.item.ItemsRegister;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class BlockRegister {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Infiniteminerblock.MODID);

    public static <T extends Block> RegistryObject<T> registryBlock(String name, Supplier<T> block, CreativeModeTab tab) {
        RegistryObject<T> toReturn = BLOCKS.register(name, block);
        registryBlockItem(name, toReturn, tab);
        return toReturn;
    }

    public static <T extends Block> RegistryObject<Item> registryBlockItem (String name, RegistryObject<T> block, CreativeModeTab tab) {
        return ItemsRegister.ITEMS.register(name, () -> new InfiniteBlockItem(block.get(), new Item.Properties().tab(Infiniteminerblock.TAB)));
    }

    public static void register(IEventBus bus) {
        BLOCKS.register(bus);
    }


    public static RegistryObject<Block> INFINITE_ORE_MINER = registryBlock("infinite_ore_miner",
            () -> new InfiniteOreMiner(BlockBehaviour.Properties.of(Material.STONE)
                    .strength(5f).requiresCorrectToolForDrops()), Infiniteminerblock.TAB);

}
