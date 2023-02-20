package com.sniklz.infiniteminerblock.item;

import com.sniklz.infiniteminerblock.block.entity.InfiniteOreMinerEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.*;
import java.util.Map;

public class InfiniteBlockItem extends BlockItem {
    public InfiniteBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    protected boolean canPlace(BlockPlaceContext pContext, BlockState pState) {
        boolean b = super.canPlace(pContext, pState);

        Level level = pContext.getLevel();

        Map<BlockPos, BlockEntity> blockEntities = level.getChunkAt(pContext.getClickedPos()).getBlockEntities();

        Player player = pContext.getPlayer();


        for (BlockEntity blockEntity : blockEntities.values()) {
            if (blockEntity instanceof InfiniteOreMinerEntity) {
                if(level.isClientSide)
                 player.sendMessage(new TextComponent("You can place only one block per chunk").withStyle(ChatFormatting.RED), player.getUUID());
                return false;
            }
        }
        return true && b;
    }
}
