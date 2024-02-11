package be.sixefyle.transdimquarry.blocks.soulharverster;

import be.sixefyle.transdimquarry.blocks.TransDimMachineScreen;
import be.sixefyle.transdimquarry.gui.Widgets;
import be.sixefyle.transdimquarry.utils.NumberUtil;
import be.sixefyle.transdimquarry.utils.Vec2i;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;

import java.util.Map;

public class SoulHarvesterScreen<T extends SoulHarvesterMenu> extends TransDimMachineScreen<T> {
    public SoulHarvesterScreen(T menu, Inventory inventory, Component component) {
        super(menu, inventory, component);

        this.titlePos = new Vec2i(7, 3);
        this.inventoryTitlePos = new Vec2i(7, 104);
        this.imageHeight = 196;
    }

    @Override
    protected void init() {
        super.init();

        this.blackScreenPos = new Vec2i(10, 18);
    }

    @Override
    protected String getTextureName() {
        return "soul_manipulator";
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderLabels(guiGraphics, mouseX, mouseY);

        Map<EntityType<?>, Integer> storedSouls = menu.blockEntity.getStoredSouls();
        int totalSouls = 0;

        for (Integer value : storedSouls.values()) {
            totalSouls += value;
        }

        clearRegisteredBlackScreenLabels();
        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(.7f,.7f,.7f);

        if(menu.getEnergyStored() < menu.getPowerConsumption()){
            showErrorMessage(guiGraphics,
                    "Not enough energy!",
                    String.format("  Need %s to work", NumberUtil.formatToEnergy(menu.getPowerConsumption() - menu.getEnergyStored()))
            );
        } else {
            addBlackScreenLabel(String.format("Progression: %.1f%%", menu.getScaledProgression() * 100), 0xffffff);
            addBlackScreenLabel("", 0xffffff);
            addBlackScreenLabel("Total souls types: " + storedSouls.size(), 0xffffff);
            addBlackScreenLabel("Total souls amount: " + totalSouls, 0xffffff);
        }

        showBlackScreenLabels(guiGraphics);

        guiGraphics.pose().popPose();
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        super.renderBg(guiGraphics, partialTick, mouseX, mouseY);

        for (Vec2i loc : menu.getSlotsLocation()) {
            Widgets.EMPTY_SLOT.showImage(guiGraphics, texturePos.add(loc).add(-1));
        }
    }
}
