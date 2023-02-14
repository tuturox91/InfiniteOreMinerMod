package com.sniklz.infiniteminerblock.item.custom;

import com.sniklz.infiniteminerblock.block.entity.InfiniteOreMinerEntity;
import com.sniklz.infiniteminerblock.util.BlockAndSize;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InfiniteOreMinerBlockItem extends BlockItem {

    public InfiniteOreMinerBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltip, TooltipFlag pFlag) {
        if (pStack.hasTag()) {
            pTooltip.add(new TextComponent("Im hover block:" + pStack.getTag().getString("block_name")));
        }
        super.appendHoverText(pStack, pLevel, pTooltip, pFlag);
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pPos, Level pLevel, @Nullable Player pPlayer, ItemStack pStack, BlockState pState) {

        MinecraftServer minecraftserver = pLevel.getServer();
        if (minecraftserver == null) {
            return false;
        } else {
            InfiniteOreMinerEntity entity = (InfiniteOreMinerEntity) pLevel.getBlockEntity(pPos);

            if(pStack.hasTag()) {
                int this_chunk_hash = pStack.getTag().getInt("this_chunk_hash");
                String block_name = pStack.getTag().getString("block_name");
                int deposit_size = pStack.getTag().getInt("deposit_size");


                entity.setThisChunkHash(this_chunk_hash);
                Map<Integer, BlockAndSize> blockAndSizeMap = new HashMap<>();
                Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block_name));
                blockAndSizeMap.put(this_chunk_hash, new BlockAndSize(block, deposit_size));
                entity.setChunkBlockAndSize(blockAndSizeMap);
            } else {
                CompoundTag compoundtag = getBlockEntityData(pStack);
                if (compoundtag != null) {
                    BlockEntity blockentity = pLevel.getBlockEntity(pPos);
                    if (blockentity != null) {
                        if (!pLevel.isClientSide && blockentity.onlyOpCanSetNbt() && (pPlayer == null || !pPlayer.canUseGameMasterBlocks())) {
                            return false;
                        }

                        CompoundTag compoundtag1 = blockentity.saveWithoutMetadata();
                        CompoundTag compoundtag2 = compoundtag1.copy();
                        compoundtag1.merge(compoundtag);
                        if (!compoundtag1.equals(compoundtag2)) {
                            blockentity.load(compoundtag1);
                            blockentity.setChanged();
                            return true;
                        }
                    }
                }
            }
            return false;
        }
    }
}
