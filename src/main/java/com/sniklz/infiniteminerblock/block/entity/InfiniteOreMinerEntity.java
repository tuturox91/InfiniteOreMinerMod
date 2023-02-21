package com.sniklz.infiniteminerblock.block.entity;

import com.sniklz.infiniteminerblock.config.ModCommonConfigs;
import com.sniklz.infiniteminerblock.networking.ModMessages;
import com.sniklz.infiniteminerblock.networking.packet.EnergySyncS2CPacket;
import com.sniklz.infiniteminerblock.saveData.SaveLoadMineChunk;
import com.sniklz.infiniteminerblock.screen.InfiniteOreMinerMenu;
import com.sniklz.infiniteminerblock.util.BlockAndSize;
import com.sniklz.infiniteminerblock.util.ModEnergyStorage;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.Containers;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InfiniteOreMinerEntity extends BlockEntity implements MenuProvider {

    protected Block mineableBlock;
    protected int oreSize;

    public Block getMineableBlock() {
        return mineableBlock;
    }

    public void setMineableBlock(Block mineableBlock) {
        this.mineableBlock = mineableBlock;
    }

    public int getOreSize() {
        return oreSize;
    }

    public void setOreSize(int oreSize) {
        this.oreSize = oreSize;
    }

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

    @Nullable
    @Override
    public Level getLevel() {
        return super.getLevel();
    }

    private LazyOptional<IItemHandler> lazyItemHelper = LazyOptional.empty();

    public InfiniteOreMinerEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntityRegister.INFINITE_ORE_MINER_ENTITY.get(), pPos, pBlockState);
    }

    @Override
    public Component getDisplayName() {
        return new TextComponent("Infinite Miner");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new InfiniteOreMinerMenu(pContainerId, pPlayerInventory, this);
    }

    private final ModEnergyStorage ENERGY_STORAGE = new ModEnergyStorage(ModCommonConfigs.ENERGY_CAPACITY.get(),
            ModCommonConfigs.ENERGY_TRANSFER_SIZE.get()) {
        @Override
        public void onEnergyChanged() {
            setChanged();
            ModMessages.sendToClients(new EnergySyncS2CPacket(this.energy, getBlockPos()));
        }
    };

    private static final int ENERGY_REQ = ModCommonConfigs.ENERGY_REQ_PER_TICK.get();
    private LazyOptional<IEnergyStorage> lazyEnergyHandler = LazyOptional.empty();

    @NotNull
    @Override
    public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == CapabilityEnergy.ENERGY) {
            return lazyEnergyHandler.cast();
        }


        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return lazyItemHelper.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHelper = LazyOptional.of(() -> itemStackHandler);
        lazyEnergyHandler = LazyOptional.of(() -> ENERGY_STORAGE);
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
        nbt.putInt("tech_craft_energy", ENERGY_STORAGE.getEnergyStored());
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
        ENERGY_STORAGE.setEnergy(nbt.getInt("tech_craft_energy"));
    }

    private static boolean canInsertItemInOutputSlot(SimpleContainer inventory, ItemStack itemStack) {
        return inventory.getItem(0).getItem() == itemStack.getItem() || inventory.getItem(0).isEmpty();
    }

    private static boolean canInsertAmountIntOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(0).getMaxStackSize() > inventory.getItem(0).getCount();
    }

    private int timer = 0;


    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemStackHandler.getSlots());
        inventory.setItem(0, itemStackHandler.getStackInSlot(0));

        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, InfiniteOreMinerEntity pEntity) {
        if (level.isClientSide) {
            return;
        }
        if (pEntity.getMineableBlock() == null && pEntity.getOreSize() != 0) {
            SaveLoadMineChunk saveLoadMineChunk = SaveLoadMineChunk.get(level);
            BlockAndSize blockAndSize = saveLoadMineChunk.FindBlockAndSizeByChunkPos(level.getChunkAt(blockPos).getPos());
            if (blockAndSize != null) {
                pEntity.setMineableBlock(blockAndSize.getChunkBlock());
                pEntity.setOreSize(blockAndSize.getBlockSize());
                pEntity.setChanged();
            }
        }

        if (pEntity.getMineableBlock() != null) {
            int slotsCount = pEntity.itemStackHandler.getSlots();
            SimpleContainer inventory = new SimpleContainer(slotsCount);
            inventory.setItem(0, pEntity.itemStackHandler.getStackInSlot(0));
            if (canInsertAmountIntOutputSlot(inventory) && canInsertItemInOutputSlot(inventory, new ItemStack(pEntity.getMineableBlock()))) {
                pEntity.timer += 1;
                extractEnergy(pEntity);
                if (pEntity.timer >= ModCommonConfigs.TICK_COUNT_TO_PRODUCE_ORE.get() && hasEnoughEnergy(pEntity)) {

                    pEntity.itemStackHandler.setStackInSlot(0, new ItemStack(pEntity.getMineableBlock(),
                            pEntity.itemStackHandler.getStackInSlot(0).getCount() + 1));
                    pEntity.timer = 0;
                    int oreSize1 = pEntity.getOreSize();
                    oreSize1 -= 1;
                    pEntity.setOreSize(oreSize1);
                    SaveLoadMineChunk saveLoadMineChunk = SaveLoadMineChunk.get(level);
                    saveLoadMineChunk.updateOreSize(level.getChunkAt(blockPos).getPos(), oreSize1--);
                    // }
                }

            }
        }
    }

    private static void extractEnergy(InfiniteOreMinerEntity pEntity) {
        pEntity.ENERGY_STORAGE.extractEnergy(ENERGY_REQ, false);
    }

    private static boolean hasEnoughEnergy(InfiniteOreMinerEntity pEntity) {
        return pEntity.ENERGY_STORAGE.getEnergyStored() >= ENERGY_REQ;
    }

    public IEnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    public void setEnergyLevel(int energy) {
        this.ENERGY_STORAGE.setEnergy(energy);
    }

}
