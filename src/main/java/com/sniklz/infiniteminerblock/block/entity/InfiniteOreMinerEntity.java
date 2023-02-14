package com.sniklz.infiniteminerblock.block.entity;

import com.sniklz.infiniteminerblock.screen.InfiniteOreMinerMenu;
import com.sniklz.infiniteminerblock.util.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
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
import net.minecraft.world.level.block.Blocks;
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

import java.util.Optional;
import java.util.Random;

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

    public void setRandomElement(Block randomElement) {
        this.randomElement = randomElement;
    }

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
        nbt.putString("randomElement", randomElement.getRegistryName().toString());

    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        itemStackHandler.deserializeNBT(nbt.getCompound("inventory"));
        String name = nbt.getString("randomElement");
        Block block = ForgeRegistries.BLOCKS.getValue(new ResourceLocation(name));
        randomElement = block;
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

    private Block randomElement;

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, InfiniteOreMinerEntity pEntity) {
        if (level.isClientSide) {
            return;
        }

        pEntity.timer += 1;
        if (pEntity.timer >= 60) {

            if (pEntity.randomElement != null) {

                int slotsCount = pEntity.itemStackHandler.getSlots();
                SimpleContainer inventory = new SimpleContainer(slotsCount);
                inventory.setItem(0, pEntity.itemStackHandler.getStackInSlot(0));
                if (canInsertAmountIntOutputSlot(inventory) && canInsertItemInOutputSlot(inventory, new ItemStack(pEntity.randomElement))) {

                    //pEntity.itemStackHandler.extractItem(0, 1, false);

                    pEntity.itemStackHandler.setStackInSlot(0, new ItemStack(pEntity.randomElement,
                            pEntity.itemStackHandler.getStackInSlot(0).getCount() + 1));
                    pEntity.timer = 0;
                }
            }
        }
    }
}
