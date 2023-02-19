package com.sniklz.infiniteminerblock.block.custom;

import com.sniklz.infiniteminerblock.block.entity.BlockEntityRegister;
import com.sniklz.infiniteminerblock.block.entity.InfiniteOreMinerEntity;
import com.sniklz.infiniteminerblock.networking.ModMessages;
import com.sniklz.infiniteminerblock.networking.packet.GiveOreDataS2CPacket;
import com.sniklz.infiniteminerblock.saveData.SaveLoadMineChunk;
import com.sniklz.infiniteminerblock.util.BlockAndSize;
import com.sniklz.infiniteminerblock.util.ModTags;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
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

import java.util.Map;
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

    @Override
    public boolean onDestroyedByPlayer(BlockState state, Level level, BlockPos pos, Player player, boolean willHarvest, FluidState fluid) {
        if (!level.isClientSide) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity instanceof InfiniteOreMinerEntity) {
                ((InfiniteOreMinerEntity) blockEntity).drops();
            }
        }

        return super.onDestroyedByPlayer(state, level, pos, player, willHarvest, fluid);

    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos,
                                 Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {

        if (!pLevel.isClientSide) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof InfiniteOreMinerEntity) {
                NetworkHooks.openGui(((ServerPlayer) pPlayer), (InfiniteOreMinerEntity) blockEntity, pPos);
                return InteractionResult.SUCCESS;
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
            SaveLoadMineChunk saveLoadMineChunk = SaveLoadMineChunk.get(pLevel);
            ChunkPos chunkPos = pLevel.getChunkAt(pPos).getPos();
            if (saveLoadMineChunk.FindBlockAndSizeByChunkPos(chunkPos) == null) {
                ITag<Block> itag = ForgeRegistries.BLOCKS.tags().getTag(ModTags.Blocks.INFINITE_ORE_MINER_BLOCKS);
                Optional<Block> block = itag.getRandomElement(new Random());
                double modifier =  1+((int)Math.sqrt(chunkPos.x * chunkPos.x+ chunkPos.z * chunkPos.z)/63)*0.1;
                //int ore_size = (int) (1000 + (Math.random() * (2000 + chunkPos.x + chunkPos.z)));
                double random = 800 + (Math.random() * 2000);
                int ore_size = (int) (random * modifier);
                //ModMessages.sendToClients(new GiveOreDataS2CPacket(ore_size));
                BlockAndSize blockAndSize = new BlockAndSize(block.get(), ore_size);
                saveLoadMineChunk.putNewChunkInfoToMap(chunkPos, blockAndSize);

                entity.setMineableBlock(block.get());
                entity.setOreSize(ore_size);
                entity.setChanged();
                //this.entity.someWorks(pLevel, pPos);
            } else {
                BlockAndSize blockAndSize = saveLoadMineChunk.FindBlockAndSizeByChunkPos(chunkPos);
                entity.setMineableBlock(blockAndSize.getChunkBlock());
                entity.setOreSize(blockAndSize.getBlockSize());
                entity.setChanged();
                //System.out.println(entity.toString());
            }
            /*InfiniteOreMinerEntity entity = (InfiniteOreMinerEntity) pLevel.getBlockEntity(pPos);
            Map<BlockPos, BlockEntity> blockEntities = pLevel.getChunkAt(pPos).getBlockEntities();
            ITag<Block> itag = ForgeRegistries.BLOCKS.tags().getTag(ModTags.Blocks.INFINITE_ORE_MINER_BLOCKS);
            Block block = itag.getRandomElement(new Random()).get();
            entity.setRandomElement(block);*/

            //this.entity.someWorks(pLevel, pPos);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, BlockEntityRegister.INFINITE_ORE_MINER_ENTITY.get(), InfiniteOreMinerEntity::tick);

    }
}
