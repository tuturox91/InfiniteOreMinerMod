package com.sniklz.infiniteminerblock.block.custom;

import com.sniklz.infiniteminerblock.block.entity.BlockEntityRegister;
import com.sniklz.infiniteminerblock.block.entity.InfiniteOreMinerEntity;
import com.sniklz.infiniteminerblock.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Random;

public class InfiniteOreMiner extends BaseEntityBlock {

    //private InfiniteOreMinerEntity entity;

    public InfiniteOreMiner(Properties pProperties) {
        super(pProperties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new InfiniteOreMinerEntity(pPos, pState);
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if(pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if(blockEntity instanceof InfiniteOreMinerEntity) {
                ((InfiniteOreMinerEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        if(!pLevel.isClientSide) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if(blockEntity instanceof InfiniteOreMinerEntity) {
                NetworkHooks.openGui(((ServerPlayer) pPlayer), (InfiniteOreMinerEntity) blockEntity, pPos);
            } else {
                throw new IllegalStateException("Our container provider is missing");
            }
        }

        return super.use(pState, pLevel, pPos, pPlayer, pHand, pHit);
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
        if(!pLevel.isClientSide) {
            InfiniteOreMinerEntity entity = (InfiniteOreMinerEntity) pLevel.getBlockEntity(pPos);
            Map<BlockPos, BlockEntity> blockEntities = pLevel.getChunkAt(pPos).getBlockEntities();
            ITag<Block> itag = ForgeRegistries.BLOCKS.tags().getTag(ModTags.Blocks.INFINITE_ORE_MINER_BLOCKS);
            Block block = itag.getRandomElement(new Random()).get();
            entity.setRandomElement(block);
            //this.entity.someWorks(pLevel, pPos);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, BlockEntityRegister.INFINITE_ORE_MINER_ENTITY.get(), InfiniteOreMinerEntity::tick);

    }
}
