package com.sniklz.infiniteminerblock.networking.packet;

import ca.weblite.objc.Proxy;
import com.sniklz.infiniteminerblock.client.ClientData;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class GiveOreDataS2CPacket {
    public GiveOreDataS2CPacket() {
    }

    int oreSize;

    public GiveOreDataS2CPacket(int oreSize) {
        this.oreSize = oreSize;
    }

    public GiveOreDataS2CPacket(FriendlyByteBuf buf) {
        this.oreSize = buf.readInt();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeInt(oreSize);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            // HERE WE ARE ON THE CLIENT!
            ClientData.setOreSize(oreSize);
        });
        return true;
    }
}
