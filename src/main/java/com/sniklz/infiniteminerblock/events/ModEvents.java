package com.sniklz.infiniteminerblock.events;

import com.sniklz.infiniteminerblock.Infiniteminerblock;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Infiniteminerblock.MODID)
public class ModEvents {

    @SubscribeEvent
    public void onPlayerInteract(BlockEvent event) {
        System.out.println("TESTED");
    }

}
