package com.sniklz.infiniteminerblock.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.sniklz.infiniteminerblock.Infiniteminerblock;
import com.sniklz.infiniteminerblock.networking.ModMessages;
import com.sniklz.infiniteminerblock.networking.packet.RequestDataFromServerC2SPacket;
import com.sniklz.infiniteminerblock.screen.renderer.EnergyInfoArea;
import com.sniklz.infiniteminerblock.util.MouseUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

import java.util.Optional;

public class InfiniteOreMinerScreen extends AbstractContainerScreen<InfiniteOreMinerMenu> {

    private InfiniteOreMinerMenu menu;
    private EnergyInfoArea energyInfoArea;

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Infiniteminerblock.MODID, "textures/gui/tech_craft_gui.png");


    public InfiniteOreMinerScreen(InfiniteOreMinerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        menu = pMenu;
        this.inventoryLabelY =  this.imageHeight - 90;

    }

    @Override
    protected void init() {
        super.init();

        assignEnergyInfoArea();
    }

    //################################################################

    private void assignEnergyInfoArea() {
        int x = (width - imageWidth) /2;
        int y = (height - imageHeight) /2;

        energyInfoArea = new EnergyInfoArea(x+156, y+ 13, menu.blockEntity.getEnergyStorage()); // change position of red fill gradient
    }

    @Override
    protected void renderLabels(PoseStack pPoseStack, int pMouseX, int pMouseY) {
        int x = (width - imageWidth) /2;
        int y = (height - imageHeight) /2;

        rendererEnergyAreaTooltips(pPoseStack, pMouseX, pMouseY, x, y);
    }

    private void rendererEnergyAreaTooltips(PoseStack pPoseStack, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 156, 13, 8, 64)) {
            renderTooltip(pPoseStack, energyInfoArea.getTooltips(),
                    Optional.empty(), pMouseX-x, pMouseY-y);
        }
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y+offsetY, width, height);
    }

    //##########################################

    private static PoseStack poseStack;

    @Override
    public InfiniteOreMinerMenu getMenu() {
        return menu;
    }

    @Override
    protected void renderBg(PoseStack pPoseStack, float pPartialTick, int pMouseX, int pMouseY) {
        if (poseStack == null)
            poseStack = pPoseStack;
        RenderSystem.setShader(GameRenderer::getPositionColorTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        this.blit(pPoseStack, x, y, 0, 0, imageWidth, imageHeight);

        ModMessages.sendToServer(new RequestDataFromServerC2SPacket(this.menu.blockEntity.getBlockPos()));
        TextComponent countOreString = (TextComponent) getCountOreString(menu.blockEntity.getOreSize());
        double stringSize = (countOreString.getString().length() / 2d) * 5d;
        this.font.draw(pPoseStack, getCountOreString(menu.blockEntity.getOreSize()).withStyle(ChatFormatting.BLACK), x + (80 - Math.round(stringSize)), y+64, 0x404040);
        this.font.draw(pPoseStack, getCountOreString(menu.blockEntity.getOreSize()), x + (79 - Math.round(stringSize)), y+63, 0x404040);

        energyInfoArea.draw(pPoseStack);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pPoseStack, pMouseX, pMouseY);
    }

    private MutableComponent getCountOreString(int oreSize) {

        if(oreSize == 0) {
            return new TextComponent("No ore!").withStyle(ChatFormatting.RED);
        } else if (oreSize > 0 && oreSize < 500) {
            return new TextComponent("Very few!").withStyle(ChatFormatting.GREEN);
        } else if(oreSize > 500 && oreSize < 1000) {
            return new TextComponent("Few!").withStyle(ChatFormatting.BLUE);
        } else if (oreSize>1000 && oreSize<1500) {
            return new TextComponent("Medium").withStyle(ChatFormatting.YELLOW);
        } else if (oreSize>1500) {
            return new TextComponent("Large!").withStyle(ChatFormatting.GOLD);
        }
        return new TextComponent("No ore!").withStyle(ChatFormatting.RED);
    }
}
