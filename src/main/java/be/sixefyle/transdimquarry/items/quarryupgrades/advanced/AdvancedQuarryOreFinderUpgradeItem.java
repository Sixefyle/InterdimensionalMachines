package be.sixefyle.transdimquarry.items.quarryupgrades.advanced;

import be.sixefyle.transdimquarry.blocks.quarries.QuarryBaseBlockEntity;
import be.sixefyle.transdimquarry.enums.EnumColor;
import be.sixefyle.transdimquarry.items.quarryupgrades.QuarryUpgrade;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AdvancedQuarryOreFinderUpgradeItem extends Item implements QuarryUpgrade {

    private final int COST_MULTIPLIER = 190;
    private final int FIND_CHANCE = 16;

    public AdvancedQuarryOreFinderUpgradeItem() {
        super(new Properties().stacksTo(4).rarity(Rarity.COMMON));
    }

    @Override
    public void onPlaced(QuarryBaseBlockEntity blockEntity) {
        blockEntity.setOreFindChange(blockEntity.getOreFindChange() + (FIND_CHANCE / 100f));
        blockEntity.setEnergyNeeded((int) (blockEntity.getNeededEnergy() * (1 +(COST_MULTIPLIER / 100f))));
    }

    @Override
    public void onRemoved(QuarryBaseBlockEntity blockEntity) {
        blockEntity.setOreFindChange(blockEntity.getOreFindChange() - (FIND_CHANCE / 100f));
        blockEntity.setEnergyNeeded((int) (blockEntity.getNeededEnergy() / (1 +(COST_MULTIPLIER / 100f))));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);

        tooltip.add(EnumColor.GRAY.getColoredComponent("Increase chance to find ore"));
        tooltip.add(Component.empty());
        tooltip.add(EnumColor.WHITE.getColoredComponent("Quarry change:"));
        tooltip.add(EnumColor.GREEN.getColoredComponent("  - Ore find chance: +" + FIND_CHANCE + "%"));
        tooltip.add(EnumColor.RED.getColoredComponent("  - Energy cost: +" + COST_MULTIPLIER +"%"));
    }

    @Override
    public boolean canPlaceMultiple() {
        return true;
    }
}
