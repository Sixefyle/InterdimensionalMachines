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

public class AdvancedQuarryFortuneUpgradeItem extends Item implements QuarryUpgrade {

    private final int COST_MULTIPLIER = 550;
    private final int FORTUNE_POWER = 6;

    public AdvancedQuarryFortuneUpgradeItem() {
        super(new Properties().stacksTo(4).rarity(Rarity.COMMON));
    }

    @Override
    public void onPlaced(QuarryBaseBlockEntity blockEntity) {
        blockEntity.setFortuneLevel(blockEntity.getFortuneLevel() + FORTUNE_POWER);
        blockEntity.setEnergyNeeded((int) (blockEntity.getNeededEnergy() * (1 +(COST_MULTIPLIER / 100f))));
    }

    @Override
    public void onRemoved(QuarryBaseBlockEntity blockEntity) {
        blockEntity.setFortuneLevel(blockEntity.getFortuneLevel() - FORTUNE_POWER);
        blockEntity.setEnergyNeeded((int) (blockEntity.getNeededEnergy() / (1 +(COST_MULTIPLIER / 100f))));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);

        tooltip.add(EnumColor.GRAY.getColoredComponent("Add a fortune effect to mined block"));
        tooltip.add(EnumColor.GRAY.getColoredComponent("(Can be combined with other fortune upgrade)"));
        tooltip.add(Component.empty());
        tooltip.add(EnumColor.WHITE.getColoredComponent("Quarry change:"));
        tooltip.add(EnumColor.GREEN.getColoredComponent("  - Fortune Power: +" + FORTUNE_POWER));
        tooltip.add(EnumColor.RED.getColoredComponent("  - Energy cost: +" + COST_MULTIPLIER +"%"));
        tooltip.add(EnumColor.DARK_GRAY.getColoredComponent("Cannot be combined with Silk Touch upgrade !"));
    }

    @Override
    public boolean canPlaceMultiple() {
        return true;
    }
}