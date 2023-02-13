package com.sniklz.infiniteminerblock.item;

import com.sniklz.infiniteminerblock.Infiniteminerblock;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemsRegister {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Infiniteminerblock.MODID);

    public static void register(IEventBus bus) {
        ITEMS.register(bus);
    }
}
