package com.sniklz.infiniteminerblock.screen;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.sniklz.infiniteminerblock.Infiniteminerblock;
import com.sniklz.infiniteminerblock.client.ClientData;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class InfiniteOreMinerScreen extends AbstractContainerScreen<InfiniteOreMinerMenu> {

    private InfiniteOreMinerMenu menu;

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(Infiniteminerblock.MODID, "textures/gui/tech_craft_gui.png");


    public InfiniteOreMinerScreen(InfiniteOreMinerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
        menu = pMenu;
        this.inventoryLabelY =  this.imageHeight - 90;
    }


    private static PoseStack poseStack;



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
        this.font.draw(pPoseStack, "My Text: " + ClientData.getOreSize(), x + 40, y+63, 0x404040);
    }

    @Override
    public void render(PoseStack pPoseStack, int pMouseX, int pMouseY, float pPartialTick) {
        renderBackground(pPoseStack);
        super.render(pPoseStack, pMouseX, pMouseY, pPartialTick);
        renderTooltip(pPoseStack, pMouseX, pMouseY);


    }
}
