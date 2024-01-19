package be.sixefyle.transdimquarry.items.quarryupgrades.simple;

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

public class QuarryEnergyOptimizationUpgradeItem extends Item implements QuarryUpgrade {

    private final int BASE_ENERGY_COST_REDUCTION = 25;
    private final int TICK_ADDED = 45;

    public QuarryEnergyOptimizationUpgradeItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.COMMON));
    }

    @Override
    public void onPlaced(QuarryBaseBlockEntity blockEntity) {
        blockEntity.setTimeToMine(blockEntity.getRoundTimeToMine() * (1 + (TICK_ADDED) / 100f));
        blockEntity.setEnergyNeeded((int) (blockEntity.getNeededEnergy() * (1 - (BASE_ENERGY_COST_REDUCTION / 100f))));
    }

    @Override
    public void onRemoved(QuarryBaseBlockEntity blockEntity) {
        blockEntity.setTimeToMine(blockEntity.getRoundTimeToMine() / (1 + (TICK_ADDED) / 100f));
        blockEntity.setEnergyNeeded((int) (blockEntity.getNeededEnergy() / (1 - (BASE_ENERGY_COST_REDUCTION / 100f))));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);

        tooltip.add(EnumColor.GRAY.getColoredComponent("Reduce the energy usage of"));
        tooltip.add(EnumColor.GRAY.getColoredComponent("the Quarry but at the cost of speed"));
        tooltip.add(Component.empty());
        tooltip.add(EnumColor.WHITE.getColoredComponent("Quarry change:"));
        tooltip.add(EnumColor.GREEN.getColoredComponent("  - Energy cost: -" + BASE_ENERGY_COST_REDUCTION + "%"));
        tooltip.add(EnumColor.RED.getColoredComponent("  - Time to mine: +" + TICK_ADDED +"%"));
    }

    @Override
    public boolean canPlaceMultiple() {
        return true;
    }
}
