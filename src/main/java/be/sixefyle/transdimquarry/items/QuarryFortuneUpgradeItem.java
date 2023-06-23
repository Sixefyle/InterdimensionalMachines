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

public class QuarryFortuneUpgradeItem extends Item implements QuarryUpgrade {

    private final int COST_MULTIPLIER = 3;

    public QuarryFortuneUpgradeItem() {
        super(new Properties().stacksTo(1).tab(ItemRegister.creativeTab).rarity(Rarity.COMMON));
    }

    @Override
    public void onPlaced(TransdimQuarryBlockEntity blockEntity) {
        blockEntity.setFortuneLevel(blockEntity.getFortuneLevel() + 3);
        blockEntity.setEnergyCostMultiplier(blockEntity.getEnergyCostMultiplier() + COST_MULTIPLIER);
    }

    @Override
    public void onRemoved(TransdimQuarryBlockEntity blockEntity) {
        blockEntity.setFortuneLevel(blockEntity.getFortuneLevel() - 3);
        blockEntity.setEnergyCostMultiplier(blockEntity.getEnergyCostMultiplier() - COST_MULTIPLIER);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, components, tooltipFlag);

        components.add(Component.literal("Add a fortune effect to mined block (Can be combined with other fortune upgrade)").withStyle(ChatFormatting.YELLOW));
        components.add(Component.literal("  - Fortune Power: +" + 3).withStyle(ChatFormatting.GREEN));
        components.add(Component.literal("  - Energy Cost Multiplier: +" + COST_MULTIPLIER * 100 + "%").withStyle(ChatFormatting.RED));
        components.add(Component.literal("Cannot be combined with Silk Touch upgrade !").withStyle(ChatFormatting.GRAY));
    }
}
