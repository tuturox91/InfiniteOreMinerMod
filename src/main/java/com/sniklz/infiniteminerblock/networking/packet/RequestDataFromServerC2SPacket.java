package com.sniklz.infiniteminerblock.networking.packet;

import com.sniklz.infiniteminerblock.block.entity.InfiniteOreMinerEntity;
import com.sniklz.infiniteminerblock.networking.ModMessages;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class RequestDataFromServerC2SPacket {

    private int posX;
    private int posY;
    private int posZ;


    public RequestDataFromServerC2SPacket(BlockPos pos) {
        this.posX = pos.getX();
        this.posY = pos.getY();
        this.posZ = pos.getZ();
    }

    public RequestDataFromServerC2SPacket(FriendlyByteBuf buf) {
        this.posX = buf.readInt();
        this.posY = buf.readInt();
        this.posZ = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(posX);
        buf.writeInt(posY);
        buf.writeInt(posZ);
    }


    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            ServerLevel level = player.getLevel();

            BlockPos blockPos = new BlockPos(posX, posY, posZ);
            InfiniteOreMinerEntity infiniteOreMinerEntity = (InfiniteOreMinerEntity) level.getBlockEntity(blockPos);
            int oreSize = infiniteOreMinerEntity.getOreSize();
            ModMessages.sendToPlayer(new GiveOreDataS2CPacket(oreSize, blockPos), player);
        });
        return true;
    }

}
