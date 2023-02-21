package com.sniklz.infiniteminerblock.screen;

import com.sniklz.infiniteminerblock.block.BlockRegister;
import com.sniklz.infiniteminerblock.block.entity.InfiniteOreMinerEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class InfiniteOreMinerMenu extends AbstractContainerMenu {

    public final InfiniteOreMinerEntity blockEntity;
    private final Level level;
    //private final ContainerData data;

    public InfiniteOreMinerMenu(int id, Inventory inventory, FriendlyByteBuf extraData) {
        this(id, inventory, inventory.player.level.getBlockEntity(extraData.readBlockPos()));
    }

    public InfiniteOreMinerMenu(int id, Inventory inventory, BlockEntity entity) {
        super(ModMenuTypes.INFINITE_ORE_MINER_MENU.get(), id);

        checkContainerSize(inventory, 1);
        blockEntity = (InfiniteOreMinerEntity) entity;
        this.level = inventory.player.level;

        addPlayerInventory(inventory);
        addPlayerHotbar(inventory);

        this.blockEntity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).ifPresent(iItemHandler -> {
            this.addSlot(new SlotItemHandler(iItemHandler, 0, 70, 31));
        });

    }

    @Override
    public boolean stillValid(Player pPlayer) {

        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, BlockRegister.INFINITE_ORE_MINER.get());
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for(int i =0; i<3; i++) {
            for(int l = 0; l<9; l++) {
                this.addSlot(new Slot(playerInventory, l+i * 9 + 9, 8 + l * 18, 86+i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for(int i =0; i<9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8+ i * 18, 144));
        }
    }

    public InfiniteOreMinerEntity getBlockEntity() {
        return this.blockEntity;
    }

}
