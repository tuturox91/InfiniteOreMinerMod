package com.sniklz.infiniteminerblock.block.entity;

import com.sniklz.infiniteminerblock.Infiniteminerblock;
import com.sniklz.infiniteminerblock.block.BlockRegister;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockEntityRegister {
    public static final DeferredRegister<BlockEntityType<?>>BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, Infiniteminerblock.MODID);

    public static final RegistryObject<BlockEntityType<InfiniteOreMinerEntity>> INFINITE_ORE_MINER_ENTITY =
            BLOCK_ENTITIES.register("infinite_ore_miner_entity", () -> BlockEntityType.Builder.of(InfiniteOreMinerEntity::new,
                    BlockRegister.INFINITE_ORE_MINER.get()).build(null));

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }

}
