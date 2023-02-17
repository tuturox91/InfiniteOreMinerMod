package com.sniklz.infiniteminerblock.networking.packet;

import ca.weblite.objc.Proxy;
import com.sniklz.infiniteminerblock.block.entity.InfiniteOreMinerEntity;
import com.sniklz.infiniteminerblock.client.ClientData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GiveOreDataS2CPacket {
    public GiveOreDataS2CPacket() {
    }

    int oreSize;
    BlockPos blockPos;

    public GiveOreDataS2CPacket(int oreSize, BlockPos blockPos) {
        this.oreSize = oreSize;
        this.blockPos = blockPos;
    }

    public GiveOreDataS2CPacket(FriendlyByteBuf buf) {
        this.oreSize = buf.readInt();
        this.blockPos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(oreSize);
        buf.writeBlockPos(blockPos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            //Player player = context.getSender();
            //Level level = player.getLevel();

            // HERE WE ARE ON THE CLIENT!
            //if(level.getBlockEntity(blockPos) instanceof InfiniteOreMinerEntity entity) {
                //entity.setOreSize(oreSize);
           // }
            ClientLevel level = Minecraft.getInstance().level;
            if(level.getBlockEntity(blockPos) instanceof InfiniteOreMinerEntity entity) {
                entity.setOreSize(oreSize);
            }
            //ClientData.setOreSize(oreSize);
        });
        return true;
    }
}
