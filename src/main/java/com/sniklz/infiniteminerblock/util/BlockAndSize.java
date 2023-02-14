package com.sniklz.infiniteminerblock.util;

import net.minecraft.world.level.block.Block;

public class BlockAndSize {
    private Block chunkBlock;
    private int blockSize;

    public BlockAndSize(Block chunkBlock, int blockSize) {
        this.chunkBlock = chunkBlock;
        this.blockSize = blockSize;
    }

    public BlockAndSize() {}

    public Block getChunkBlock() {
        return chunkBlock;
    }

    public void setChunkBlock(Block chunkBlock) {
        this.chunkBlock = chunkBlock;
    }

    public int getBlockSize() {
        return blockSize;
    }

    public void setBlockSize(int blockSize) {
        this.blockSize = blockSize;
    }
}