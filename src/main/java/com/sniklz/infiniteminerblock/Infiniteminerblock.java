package com.sniklz.infiniteminerblock;

import com.mojang.logging.LogUtils;
import com.sniklz.infiniteminerblock.block.BlockRegister;
import com.sniklz.infiniteminerblock.block.entity.BlockEntityRegister;
import com.sniklz.infiniteminerblock.block.entity.InfiniteOreMinerEntity;
import com.sniklz.infiniteminerblock.item.ItemsRegister;
import com.sniklz.infiniteminerblock.networking.ModMessages;
import com.sniklz.infiniteminerblock.screen.InfiniteOreMinerScreen;
import com.sniklz.infiniteminerblock.screen.ModMenuTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.util.Map;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(Infiniteminerblock.MODID)
public class Infiniteminerblock {
    public static final String MODID = "infiniteminerblock";


    // Directly reference a slf4j logger
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

    @SubscribeEvent
    public void OnBlockPlace(BlockEvent.EntityPlaceEvent event) {
       /* if(event.getEntity() instanceof Player) {
            int counter = 0;
            Player player = (Player) event.getEntity();
            Level level = player.getLevel();
            Map<BlockPos, BlockEntity> blockEntities = level.getChunkAt(event.getPos()).getBlockEntities();
            for (BlockEntity blockEntity:blockEntities.values()) {
                if(blockEntity instanceof InfiniteOreMinerEntity) {
                    counter ++;
                    if(event.getPlacedBlock().is(BlockRegister.INFINITE_ORE_MINER.get()) && counter > 1) {
                        player.sendMessage(new TextComponent("In one chunk can be placed only one block").withStyle(ChatFormatting.RED), player.getUUID());
                        event.setCanceled(true);
                    }
                }
            }*/
            System.out.println("test");

        //}
    }

}
