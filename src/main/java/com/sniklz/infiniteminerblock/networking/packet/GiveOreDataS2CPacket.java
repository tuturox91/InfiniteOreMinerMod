package com.sniklz.infiniteminerblock.networking.packet;

import ca.weblite.objc.Proxy;
import com.sniklz.infiniteminerblock.block.entity.InfiniteOreMinerEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
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
            ClientLevel level = Minecraft.getInstance().level;
            if(level.getBlockEntity(blockPos) instanceof InfiniteOreMinerEntity entity) {
                entity.setOreSize(oreSize);
            }
        });
        return true;
    }
}
