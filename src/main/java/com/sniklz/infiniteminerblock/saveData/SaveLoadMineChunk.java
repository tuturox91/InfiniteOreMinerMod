package com.sniklz.infiniteminerblock.saveData;

import com.sniklz.infiniteminerblock.util.BlockAndSize;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class SaveLoadMineChunk extends SavedData  {

    private final Map<ChunkPos, BlockAndSize> mineMap = new HashMap<>();

    public static SaveLoadMineChunk get(Level level) {
        if(level.isClientSide) {
            return null;
        }
        DimensionDataStorage storage = ((ServerLevel) level).getDataStorage();
        return storage.computeIfAbsent(SaveLoadMineChunk::new, SaveLoadMineChunk::new, "minerChunks");
    }

    public BlockAndSize FindBlockAndSizeByChunkPos(ChunkPos chunkPos) {
        BlockAndSize blockAndSize = mineMap.get(chunkPos);
        if(blockAndSize != null) {
            return blockAndSize;
        } else {
            return null;
        }
    }

    public void putNewChunkInfoToMap(ChunkPos chunkPos, BlockAndSize blockAndSize) {
        mineMap.put(chunkPos, blockAndSize);
        CompoundTag compoundTag = new CompoundTag();
        save(compoundTag);
        this.setDirty();
    }

    public void updateOreSize(ChunkPos chunkPos, int oreSize) {
        mineMap.get(chunkPos).setBlockSize(oreSize);
        CompoundTag compoundTag = new CompoundTag();
        save(compoundTag);
        this.setDirty();
    }

    public SaveLoadMineChunk() {

    }

    public SaveLoadMineChunk(CompoundTag tag) {
        ListTag list = tag.getList("mineMap", Tag.TAG_COMPOUND);
        for(Tag t: list) {
            CompoundTag chunkTag = (CompoundTag) t;
            String block_name = chunkTag.getString("block_name");
            int ore_size = chunkTag.getInt("ore_size");
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(block_name));
            BlockAndSize blockAndSize = new BlockAndSize(block, ore_size);
            ChunkPos chunkPos = new ChunkPos( chunkTag.getInt("x"), chunkTag.getInt("z"));
            mineMap.put(chunkPos, blockAndSize);
        }
    }

    @Override
    public CompoundTag save(CompoundTag pCompoundTag) {
        ListTag listTag = new ListTag();
        mineMap.forEach((chunkPos, blockAndSize) -> {
            CompoundTag chunkMap = new CompoundTag();
            chunkMap.putInt("x", chunkPos.x);
            chunkMap.putInt("z", chunkPos.z);
            chunkMap.putString("block_name",blockAndSize.getChunkBlock().getRegistryName().toString());
            chunkMap.putInt("ore_size", blockAndSize.getBlockSize());
            listTag.add(chunkMap);
        });
        pCompoundTag.put("mineMap", listTag);
        return pCompoundTag;
    }
}
