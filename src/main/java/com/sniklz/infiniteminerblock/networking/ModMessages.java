package com.sniklz.infiniteminerblock.networking;

import com.sniklz.infiniteminerblock.Infiniteminerblock;
import com.sniklz.infiniteminerblock.networking.packet.GiveOreDataS2CPacket;
import com.sniklz.infiniteminerblock.networking.packet.RequestDataFromServerC2SPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModMessages {
    private static SimpleChannel INSTANCE;

    private static int packetId= 0;
    private static int id() {
        return packetId++;
    }

    public static void register() {
        SimpleChannel net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(Infiniteminerblock.MODID, "messages"))
                .networkProtocolVersion(()-> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        INSTANCE = net;

        net.messageBuilder(RequestDataFromServerC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(RequestDataFromServerC2SPacket::new)
                .encoder(RequestDataFromServerC2SPacket::toBytes)
                .consumer(RequestDataFromServerC2SPacket::handle)
                .add();

        net.messageBuilder(GiveOreDataS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(GiveOreDataS2CPacket::new)
                .encoder(GiveOreDataS2CPacket::toBytes)
                .consumer(GiveOreDataS2CPacket::handle)
                .add();

/*        net.messageBuilder(GetRecipecListC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(GetRecipecListC2SPacket::new)
                .encoder(GetRecipecListC2SPacket::toBytes)
                .consumer(GetRecipecListC2SPacket::handle)
                .add();

        net.messageBuilder(GetRecipeListS2CPacket.class, id(), NetworkDirection.PLAY_TO_CLIENT)
                .decoder(GetRecipeListS2CPacket::new)
                .encoder(GetRecipeListS2CPacket::toBytes)
                .consumer(GetRecipeListS2CPacket::handle)
                .add();*/
    }

    public static <MSG> void sendToServer(MSG message) {
        INSTANCE.sendToServer(message);
    }

    public static <MSG> void sendToPlayer(MSG message, ServerPlayer serverPlayer) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), message);
    }

    public static <MSG> void sendToClients(MSG message) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), message);
    }
}
