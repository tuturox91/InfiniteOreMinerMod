package com.sniklz.infiniteminerblock.screen;

import com.sniklz.infiniteminerblock.Infiniteminerblock;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.network.IContainerFactory;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(ForgeRegistries.CONTAINERS, Infiniteminerblock.MODID);

    private static <T extends AbstractContainerMenu>RegistryObject<MenuType<T>>
        registerMenuType(IContainerFactory<T> factory, String name) {

        return MENUS.register(name, () -> IForgeMenuType.create(factory));
    }


    public static void register(IEventBus bus) {
        MENUS.register(bus);
    }

    public static final RegistryObject<MenuType<InfiniteOreMinerMenu>> INFINITE_ORE_MINER_MENU = registerMenuType(InfiniteOreMinerMenu::new, "infinite_ore_miner_menu");

}
