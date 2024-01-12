package be.sixefyle.transdimquarry.items.quarryupgrades.cosmic;

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

public class CosmicQuarrySpeedUpgradeItem extends Item implements QuarryUpgrade {

    private final int COST_MULTIPLIER = 6500;
    private final int TIME_MULTIPLIER = 95;
    private final int ENERGY_MULTIPLIER = 36;


    public CosmicQuarrySpeedUpgradeItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public void onPlaced(QuarryBaseBlockEntity blockEntity) {
        blockEntity.setTimeToMine(blockEntity.getTimeToMine() * (1 - (TIME_MULTIPLIER) / 100f));
        blockEntity.setEnergyNeeded((int) (blockEntity.getNeededEnergy() * (1 + (COST_MULTIPLIER / 100f))));
        blockEntity.setEnergyCapacity(blockEntity.getEnergyCapacity() + (blockEntity.getBaseEnergyCapacity() * ENERGY_MULTIPLIER));
    }

    @Override
    public void onRemoved(QuarryBaseBlockEntity blockEntity) {
        blockEntity.setTimeToMine(blockEntity.getTimeToMine() / (1 - (TIME_MULTIPLIER) / 100f));
        blockEntity.setEnergyNeeded((int) (blockEntity.getNeededEnergy() / (1 + (COST_MULTIPLIER / 100f))));
        blockEntity.setEnergyCapacity(blockEntity.getEnergyCapacity() - (blockEntity.getBaseEnergyCapacity() * ENERGY_MULTIPLIER));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);

        tooltip.add(EnumColor.GRAY.getColoredComponent("Increase quarry's mining speed"));
        tooltip.add(Component.empty());
        tooltip.add(EnumColor.WHITE.getColoredComponent("Quarry change:"));
        tooltip.add(EnumColor.GREEN.getColoredComponent("  - Time to mine: -" + TIME_MULTIPLIER + "%"));
        tooltip.add(EnumColor.RED.getColoredComponent("  - Energy cost: +" + COST_MULTIPLIER +"%"));
    }

    @Override
    public boolean canPlaceMultiple() {
        return false;
    }
}
