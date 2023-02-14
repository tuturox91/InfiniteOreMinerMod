package com.sniklz.infiniteminerblock.block.custom;

import com.sniklz.infiniteminerblock.block.entity.BlockEntityRegister;
import com.sniklz.infiniteminerblock.block.entity.InfiniteOreMinerEntity;
import com.sniklz.infiniteminerblock.util.BlockAndSize;
import com.sniklz.infiniteminerblock.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.Optional;
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

/*    @Override
    public void destroy(LevelAccessor pLevel, BlockPos pPos, BlockState pState) {
        BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
        if (blockEntity instanceof InfiniteOreMinerEntity) {
            ((InfiniteOreMinerEntity) blockEntity).drops();
        }
        super.destroy(pLevel, pPos, pState);
    }*/

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if(!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof InfiniteOreMinerEntity) {
                ((InfiniteOreMinerEntity) blockEntity).drops();
            }
        }
        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);
    }

    /* @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pIsMoving) {
        if(pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if(blockEntity instanceof InfiniteOreMinerEntity) {
                ((InfiniteOreMinerEntity) blockEntity).drops();
            }
        }
        super.onRemove(pState, pLevel, pPos, pNewState, pIsMoving);
    }*/

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        if (!pLevel.isClientSide) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof InfiniteOreMinerEntity) {
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
        if (!pLevel.isClientSide) {
            InfiniteOreMinerEntity entity = (InfiniteOreMinerEntity) pLevel.getBlockEntity(pPos);
            int hashedChunk = hashPos(pPos, pLevel);

            if (entity.getChunkBlockAndSize().get(hashedChunk) == null) {
                ITag<Block> itag = ForgeRegistries.BLOCKS.tags().getTag(ModTags.Blocks.INFINITE_ORE_MINER_BLOCKS);
                Optional<Block> randomElement = itag.getRandomElement(new Random());
                entity.putChunkBlockAndSize(hashedChunk, new BlockAndSize(randomElement.get(), (int) ((200 + Math.random()) * 600)));
                entity.setThisChunkHash(hashedChunk);
                entity.setChanged();
                //this.entity.someWorks(pLevel, pPos);
            } else {
                System.out.println(entity.toString());
            }
        }
    }

    private int hashPos(BlockPos pos, Level pLevel) {
        ChunkPos chunkPos = pLevel.getChunkAt(pos).getPos();
        return Objects.hash(chunkPos.x, chunkPos.z);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, BlockEntityRegister.INFINITE_ORE_MINER_ENTITY.get(), InfiniteOreMinerEntity::tick);

    }
}
