package com.sniklz.infiniteminerblock;

import com.mojang.logging.LogUtils;
import com.sniklz.infiniteminerblock.block.BlockRegister;
import com.sniklz.infiniteminerblock.block.entity.BlockEntityRegister;
import com.sniklz.infiniteminerblock.item.ItemsRegister;
import com.sniklz.infiniteminerblock.networking.ModMessages;
import com.sniklz.infiniteminerblock.screen.InfiniteOreMinerScreen;
import com.sniklz.infiniteminerblock.screen.ModMenuTypes;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(Infiniteminerblock.MODID)
public class Infiniteminerblock {
    public static final String MODID = "infiniteminerblock";

    public static final Logger LOGGER = LogUtils.getLogger();

    public Infiniteminerblock() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ItemsRegister.register(bus);
        BlockRegister.register(bus);
        BlockEntityRegister.register(bus);
        ModMenuTypes.register(bus);

        bus.addListener(this::setup);
        bus.addListener(this::clientSetup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void clientSetup(final FMLClientSetupEvent event) {

        MenuScreens.register(ModMenuTypes.INFINITE_ORE_MINER_MENU.get(), InfiniteOreMinerScreen::new);
    }

    private void setup(final FMLCommonSetupEvent event) {
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());

        ModMessages.register();
    }

    public static final CreativeModeTab TAB = new CreativeModeTab(MODID) {
        @Override
        public ItemStack makeIcon() {
            return Blocks.IRON_BLOCK.asItem().getDefaultInstance();
        }
    };

}
