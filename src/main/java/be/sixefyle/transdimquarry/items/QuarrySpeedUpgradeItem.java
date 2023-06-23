package be.sixefyle.transdimquarry.items;

import be.sixefyle.transdimquarry.ItemRegister;
import be.sixefyle.transdimquarry.blocks.entity.TransdimQuarryBlockEntity;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class QuarrySpeedUpgradeItem extends Item implements QuarryUpgrade {

    private final double COST_MULTIPLIER = 2.25;

    public QuarrySpeedUpgradeItem() {
        super(new Item.Properties().stacksTo(1).tab(ItemRegister.creativeTab).rarity(Rarity.COMMON));
    }

    @Override
    public void onPlaced(TransdimQuarryBlockEntity blockEntity) {
        blockEntity.setTimeToMine(blockEntity.getTimeToMine() - 3);
        blockEntity.setEnergyCostMultiplier(blockEntity.getEnergyCostMultiplier() + COST_MULTIPLIER);
    }

    @Override
    public void onRemoved(TransdimQuarryBlockEntity blockEntity) {
        blockEntity.setTimeToMine(blockEntity.getTimeToMine() + 3);
        blockEntity.setEnergyCostMultiplier(blockEntity.getEnergyCostMultiplier() - COST_MULTIPLIER);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, components, tooltipFlag);

        components.add(Component.literal("Increase quarry's mining speed").withStyle(ChatFormatting.YELLOW));
        components.add(Component.literal("  - Tick Needed: -3 ticks").withStyle(ChatFormatting.GREEN));
        components.add(Component.literal("  - Energy Cost Multiplier: +" + String.format("%.0f", COST_MULTIPLIER * 100) + "%").withStyle(ChatFormatting.RED));
    }
}
