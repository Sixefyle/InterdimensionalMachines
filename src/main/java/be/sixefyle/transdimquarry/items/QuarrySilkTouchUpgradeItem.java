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

public class QuarrySilkTouchUpgradeItem extends Item implements QuarryUpgrade {
    public QuarrySilkTouchUpgradeItem() {
        super(new Properties().stacksTo(1).rarity(Rarity.COMMON));
    }

    @Override
    public void onPlaced(TransdimQuarryBlockEntity blockEntity) {
        blockEntity.setSilkTouch(true);
        blockEntity.setEnergyCostMultiplier(blockEntity.getEnergyCostMultiplier() + 2.5);
    }

    @Override
    public void onRemoved(TransdimQuarryBlockEntity blockEntity) {
        blockEntity.setSilkTouch(false);
        blockEntity.setEnergyCostMultiplier(blockEntity.getEnergyCostMultiplier() - 2.5);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, components, tooltipFlag);

        components.add(Component.literal("Add silk touch effect to mined block").withStyle(ChatFormatting.YELLOW));
        components.add(Component.literal("  - Add Silk Touch").withStyle(ChatFormatting.GREEN));
        components.add(Component.literal("  - Energy Cost Multiplier: +250%").withStyle(ChatFormatting.RED));
        components.add(Component.literal("Cannot be combined with Fortune upgrade !").withStyle(ChatFormatting.GRAY));
    }
}
