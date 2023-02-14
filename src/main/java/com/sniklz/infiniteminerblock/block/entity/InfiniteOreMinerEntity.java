package com.sniklz.infiniteminerblock.block.entity;

import com.sniklz.infiniteminerblock.block.BlockRegister;
import com.sniklz.infiniteminerblock.item.ItemsRegister;
import com.sniklz.infiniteminerblock.screen.InfiniteOreMinerMenu;
import com.sniklz.infiniteminerblock.util.BlockAndSize;
import com.sniklz.infiniteminerblock.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.tags.ITag;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class InfiniteOreMinerEntity extends BlockEntity implements MenuProvider {

    protected final ContainerData data = null;

    private final ItemStackHandler itemStackHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            if (slot == 0) {
                return false;
            } else {
                return false;
            }
        }
    };

    public void someWorks(Level level, BlockPos pos) {
        System.out.println(level.getChunkAt(pos).getPos());
        ITag<Block> itag = ForgeRegistries.BLOCKS.tags().getTag(ModTags.Blocks.INFINITE_ORE_MINER_BLOCKS);
        //this.randomElement = itag.getRandomElement(new Random());
    }

    private LazyOptional<IItemHandler> lazyItemHelper = LazyOptional.empty();

    public InfiniteOreMinerEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegister.INFINITE_ORE_MINER_ENTITY.get(), pPos, pBlockState);




        /*System.out.println(level.getChunkAt(this.getBlockPos()));
        Infiniteminerblock.LOGGER.info(level.getChunkAt(this.getBlockPos()).toString());*/
    }


    @Override
    public Component getDisplayName() {
        return new TextComponent("Infinite Miner");
    }


    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new InfiniteOreMinerMenu(pContainerId, pPlayerInventory, this, this.data);
    }


    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyItemHelper.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHelper = LazyOptional.of(() -> itemStackHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHelper.invalidate();
    }



    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.put("inventory", itemStackHandler.serializeNBT());
        int counter = 0;
        nbt.putInt("this_chunk_hash", thisChunkHash);
        nbt.putInt("map_size", chunkBlockAndSize.size());
        for (BlockAndSize blockAndSizeCollection:chunkBlockAndSize.values()) {
            nbt.putInt("chunk_hash_" + counter, thisChunkHash);
            nbt.putString("block_name_" + counter, blockAndSizeCollection.getChunkBlock().getRegistryName().toString());
            nbt.putInt("deposit_size_" + counter, blockAndSizeCollection.getBlockSize());
            counter++;
        }
    }



    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
        this.thisChunkHash =  nbt.getInt("this_chunk_hash");
        int mapSize = nbt.getInt("map_size");
        for(int i = 0; i<mapSize; i++) {
            int chunkHash = nbt.getInt("chunk_hash_" + i);
            String blockName = nbt.getString("block_name_" + i);
            Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(blockName));
            int depositSize = nbt.getInt("deposit_size_" + i);
            BlockAndSize blockAndSize  = new BlockAndSize(block, depositSize);
            this.chunkBlockAndSize.put(chunkHash, blockAndSize);
        }
    /*    String name = nbt.getString("randomElement");
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name));*/

    }

    private static boolean canInsertItemInOutputSlot(SimpleContainer inventory, ItemStack itemStack) {
        return inventory.getItem(0).getItem() == itemStack.getItem() || inventory.getItem(0).isEmpty();
    }

    private static boolean canInsertAmountIntOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(0).getMaxStackSize() > inventory.getItem(0).getCount();
    }

    private int timer = 0;


    public void drops() {
        SimpleContainer inventory = new SimpleContainer(2);
        inventory.setItem(0, itemStackHandler.getStackInSlot(0));

        ItemStack thisBlockItem = new ItemStack(BlockRegister.INFINITE_ORE_MINER.get().asItem());
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("this_chunk_hash", this.thisChunkHash);
        BlockAndSize blockAndSize = this.chunkBlockAndSize.get(thisChunkHash);
        nbt.putString("block_name", blockAndSize.getChunkBlock().getRegistryName().toString());
        nbt.putInt("deposit_size", blockAndSize.getBlockSize());
        thisBlockItem.setTag(nbt);

        inventory.setItem(1, thisBlockItem);

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    private Map<Integer, BlockAndSize> chunkBlockAndSize = new HashMap<>();
    private int thisChunkHash;

    public int getThisChunkHash() {
        return thisChunkHash;
    }

    public void setThisChunkHash(int thisChunkHash) {
        this.thisChunkHash = thisChunkHash;
    }

    public Map<Integer, BlockAndSize> getChunkBlockAndSize() {
        return chunkBlockAndSize;
    }

    public void putChunkBlockAndSize(int chunkHash, BlockAndSize blockAndSize) {
        this.chunkBlockAndSize.put(chunkHash, blockAndSize);
    }

    public void setChunkBlockAndSize(Map<Integer, BlockAndSize> chunkBlockAndSize) {
        this.chunkBlockAndSize = chunkBlockAndSize;
    }



    public static void tick(Level level, BlockPos blockPos, BlockState blockState, InfiniteOreMinerEntity pEntity) {
        if (level.isClientSide) {
            return;
        }

        pEntity.timer += 1;
        if (pEntity.timer >= 60) {

            if (pEntity.chunkBlockAndSize.get(pEntity.getThisChunkHash()) != null) {

                int slotsCount = pEntity.itemStackHandler.getSlots();
                SimpleContainer inventory = new SimpleContainer(slotsCount);
                inventory.setItem(0, pEntity.itemStackHandler.getStackInSlot(0));
                if (canInsertAmountIntOutputSlot(inventory) && canInsertItemInOutputSlot(inventory,
                        new ItemStack(pEntity.chunkBlockAndSize.get(pEntity.getThisChunkHash()).getChunkBlock()))) {

                    //pEntity.itemStackHandler.extractItem(0, 1, false);

                    pEntity.itemStackHandler.setStackInSlot(0, new ItemStack(pEntity.chunkBlockAndSize.get(pEntity.getThisChunkHash()).getChunkBlock(),
                            pEntity.itemStackHandler.getStackInSlot(0).getCount() + 1));
                    pEntity.timer = 0;
                }
            }
        }
    }
}
