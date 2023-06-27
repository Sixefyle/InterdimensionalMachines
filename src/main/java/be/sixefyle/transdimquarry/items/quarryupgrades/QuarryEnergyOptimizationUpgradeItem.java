package be.sixefyle.transdimquarry.items.quarryupgrades;

import be.sixefyle.transdimquarry.blocks.quarry.TransdimQuarryBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class QuarryEnergyOptimizationUpgradeItem extends Item implements QuarryUpgrade {

    private final int BASE_ENERGY_COST_REDUCTION = 4500;
    private final int TICK_ADDED = 3;

    public QuarryEnergyOptimizationUpgradeItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.COMMON));
    }

    @Override
    public void onPlaced(TransdimQuarryBlockEntity blockEntity) {
        blockEntity.setTimeToMine(blockEntity.getTimeToMine() + TICK_ADDED);
        blockEntity.setBaseEnergyNeeded(blockEntity.getBaseEnergyNeeded() - BASE_ENERGY_COST_REDUCTION);
    }

    @Override
    public void onRemoved(TransdimQuarryBlockEntity blockEntity) {
        blockEntity.setTimeToMine(blockEntity.getTimeToMine() - TICK_ADDED);
        blockEntity.setBaseEnergyNeeded(blockEntity.getBaseEnergyNeeded() + BASE_ENERGY_COST_REDUCTION);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, components, tooltipFlag);

        components.add(Component.literal("Reduce the energy usage of the Quarry but at the cost of speed").withStyle(ChatFormatting.YELLOW));
        components.add(Component.literal("  - Base Energy Cost: -" + BASE_ENERGY_COST_REDUCTION + " FE/t").withStyle(ChatFormatting.GREEN));
        components.add(Component.literal("  - Tick Needed: +" + TICK_ADDED +" ticks").withStyle(ChatFormatting.RED));
    }
}
