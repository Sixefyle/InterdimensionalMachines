package be.sixefyle.transdimquarry.blocks.iteminfuser;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import be.sixefyle.transdimquarry.customrecipes.iteminfuser.ItemInfuserRecipe;
import be.sixefyle.transdimquarry.customrecipes.iteminfuser.ItemInfuserRecipeRegister;
import be.sixefyle.transdimquarry.enums.EnumColor;
import be.sixefyle.transdimquarry.utils.MouseUtil;
import be.sixefyle.transdimquarry.utils.NumberUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.*;

public class ItemInfuserScreen extends AbstractContainerScreen<ItemInfuserMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(TransdimensionalMachines.MODID, "textures/gui/item_infuser.png");

    Map<String, Integer> blackScreenLabels = new LinkedHashMap<>();

    public ItemInfuserScreen(ItemInfuserMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);

        imageHeight = 173;
        imageWidth = 176;
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1,1,1,1);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);
        renderProgressEnergyBar(guiGraphics, x, y);
    }

    private void renderProgressEnergyBar(GuiGraphics guiGraphics, int x, int y){
        guiGraphics.blit(TEXTURE, x + 7, y + 14, 0, imageHeight, (int) (menu.getScaledEnergy() * 117), 10, 256, 256);
    }

    String[] dots = {"", ".", "..", "..."};
    int dotIndex = 0;
    int ticksToWait = 30;
    int lastTick = ticksToWait;

    /**
     * Show error message with 5 message max
     * @param guiGraphics
     * @param messages
     */
    private void showErrorMessage(GuiGraphics guiGraphics, String... messages){
        if(lastTick >= ticksToWait){
            dotIndex = (dotIndex + 1) % (dots.length);
            lastTick = 0;
        }
        lastTick++;

        int baseY = 54;
        guiGraphics.drawString(this.font, "ERROR" + dots[dotIndex], 54, baseY, 0xff2020, false);

        for (int i = 0; i < messages.length; i++) {
            if(i > 5) return;
            guiGraphics.drawString(this.font, messages[i], 54, baseY + 10 + (i * 10), 0xff2020, false);
        }
    }

    private void addBlackScreenLabel(String text, Integer hexColor){
        blackScreenLabels.put(text, hexColor);
    }

    private void showBlackScreenLabels(GuiGraphics guiGraphics){
        int i = 0;
        int hexColor;
        for (String text : blackScreenLabels.keySet()){
            hexColor = blackScreenLabels.get(text);
            guiGraphics.drawString(this.font, text, 54, 54 + (i * 10), hexColor, false);
            i++;
        }
    }

    private void clearRegisteredLabels(){
        blackScreenLabels.clear();
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        //Draw titles
        guiGraphics.drawString(this.font, this.title, 7, 5, 4210752, false);
        guiGraphics.drawString(this.font, this.playerInventoryTitle, 7, 80, 4210752, false);

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(.6f,.6f,.6f);
        clearRegisteredLabels();

        ItemInfuserBlockEntity blockEntity = menu.blockEntity;

        ItemStack calibrator = blockEntity.getItem(ItemInfuserBlockEntity.CALIBRATOR_SLOT);
        ItemStack input = blockEntity.getItem(ItemInfuserBlockEntity.INPUT_SLOT);
        ItemStack harmonisationMatrix = blockEntity.getItem(ItemInfuserBlockEntity.HARMONIZATION_MATRIX_SLOT);
        ItemInfuserRecipe recipe = ItemInfuserRecipe.getRecipe(input, harmonisationMatrix);
        boolean haveEnoughEnergy = menu.getPowerConsumption() <= 0 || menu.getEnergyStored() > menu.getPowerConsumption();

        if(input.isEmpty() || calibrator.isEmpty() || !haveEnoughEnergy || harmonisationMatrix.isEmpty()){
            List<String> strings = new ArrayList<>();
            if(calibrator.isEmpty()){
                strings.add("- No calibrator detected!");
            }
            if(input.isEmpty()){
                strings.add("- No item to infuse detected!");
            }
            if(harmonisationMatrix.isEmpty()){
                strings.add("- No harmonization matrix");
                strings.add("  detected!");
            }
            if(!haveEnoughEnergy){
                strings.add("- Not enought energy!");
                strings.add(String.format("  Need %s to work", NumberUtil.formatToEnergy(menu.getPowerConsumption() - menu.getEnergyStored())));
                if(recipe != null){
                    strings.add("");
                    strings.add("Current output:");
                    strings.add(" -" + recipe.getOutput().getDisplayName().getString());
                }
            }
            showErrorMessage(guiGraphics, strings.toArray(new String[0]));
        } else {
            if(recipe != null){
                addBlackScreenLabel(String.format("Progression: %.0f%%", menu.getScaledProgression() * 100), 0xfffbe7);
                addBlackScreenLabel(String.format("Calibrator damage: %d/%d", calibrator.getDamageValue(), calibrator.getMaxDamage()), 0xfffbe7);
                addBlackScreenLabel("Crafting:", 0xfffbe7);
                addBlackScreenLabel(" -" + recipe.getOutput().getDisplayName().getString(), 0xfffbe7);
            } else {
                showErrorMessage(guiGraphics, "No craft detected!", "", "Try change input or", "harmonization matrix!");
            }
        }

        showBlackScreenLabels(guiGraphics);

        guiGraphics.pose().popPose();
        if(calibrator.isEmpty()){
            renderSlotAreaTooltips(guiGraphics, mouseX, mouseY, x, y, 10, 29, "Calibrator");
        }
        if(harmonisationMatrix.isEmpty()){
            renderSlotAreaTooltips(guiGraphics, mouseX, mouseY, x, y, 10, 59, "Harmonization Matrix");
        }
        if(input.isEmpty()){
            renderSlotAreaTooltips(guiGraphics, mouseX, mouseY, x, y, 131, 44, "Item to infuse");
        }
        renderEnergyAreaTooltips(guiGraphics, mouseX, mouseY, x, y);
    }

    private void renderSlotAreaTooltips(GuiGraphics guiGraphics, int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, String text) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, offsetX, offsetY, 16, 16)) {
            guiGraphics.renderTooltip(this.font,
                    EnumColor.TEAL.getColoredComponent(text), pMouseX - x, pMouseY - y);
        }
    }

    private void renderEnergyAreaTooltips(GuiGraphics guiGraphics, int pMouseX, int pMouseY, int x, int y) {
        if(isMouseAboveArea(pMouseX, pMouseY, x, y, 7, 14, 117, 10)) {
            guiGraphics.renderTooltip(this.font,
                    Component.literal(NumberUtil.formatToEnergy(menu.getEnergyStored())+"/"+NumberUtil.formatToEnergy(menu.getMaxEnergyStored())), pMouseX - x, pMouseY - y);
        }
    }

    private boolean isMouseAboveArea(int pMouseX, int pMouseY, int x, int y, int offsetX, int offsetY, int width, int height) {
        return MouseUtil.isMouseOver(pMouseX, pMouseY, x + offsetX, y + offsetY, width, height);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
