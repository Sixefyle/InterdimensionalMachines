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

public class QuarrySilkTouchUpgradeItem extends Item implements QuarryUpgrade {

    private final int ENERGY_COST = 250;

    public QuarrySilkTouchUpgradeItem() {
        super(new Properties().stacksTo(4).rarity(Rarity.COMMON));
    }

    @Override
    public void onPlaced(QuarryBaseBlockEntity blockEntity) {
        blockEntity.setSilkTouch(true);
        blockEntity.setEnergyNeeded((int) (blockEntity.getNeededEnergy() * (1 +(ENERGY_COST / 100f))));
    }

    @Override
    public void onRemoved(QuarryBaseBlockEntity blockEntity) {
        blockEntity.setSilkTouch(false);
        blockEntity.setEnergyNeeded((int) (blockEntity.getNeededEnergy() / (1 +(ENERGY_COST / 100f))));
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);

        tooltip.add(EnumColor.GRAY.getColoredComponent("Add silk touch effect to mined block"));
        tooltip.add(Component.empty());
        tooltip.add(EnumColor.WHITE.getColoredComponent("Quarry change:"));
        tooltip.add(EnumColor.GREEN.getColoredComponent("  - Add Silk Touch"));
        tooltip.add(EnumColor.RED.getColoredComponent("  - Energy cost: +250%"));
        tooltip.add(EnumColor.DARK_GRAY.getColoredComponent("Cannot be combined with Fortune upgrade!"));
    }

    @Override
    public boolean canPlaceMultiple() {
        return false;
    }
}
