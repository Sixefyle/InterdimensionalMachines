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

public class QuarryOreFinderUpgradeItem extends Item implements QuarryUpgrade {

    private final double COST_MULTIPLIER = 1.75;

    public QuarryOreFinderUpgradeItem() {
        super(new Properties().stacksTo(1).tab(ItemRegister.creativeTab).rarity(Rarity.COMMON));
    }

    @Override
    public void onPlaced(TransdimQuarryBlockEntity blockEntity) {
        blockEntity.setOreFindChange(blockEntity.getOreFindChange() + .08);
        blockEntity.setEnergyCostMultiplier(blockEntity.getEnergyCostMultiplier() + COST_MULTIPLIER);
    }

    @Override
    public void onRemoved(TransdimQuarryBlockEntity blockEntity) {
        blockEntity.setOreFindChange(blockEntity.getOreFindChange() - .08);
        blockEntity.setEnergyCostMultiplier(blockEntity.getEnergyCostMultiplier() - COST_MULTIPLIER);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, components, tooltipFlag);

        components.add(Component.literal("Increase chance to find ore").withStyle(ChatFormatting.YELLOW));
        components.add(Component.literal("  - Ore Find Chance: +8%").withStyle(ChatFormatting.GREEN));
        components.add(Component.literal("  - Energy Cost Multiplier: +" + String.format("%.0f", COST_MULTIPLIER * 100) + "%").withStyle(ChatFormatting.RED));
    }
}
