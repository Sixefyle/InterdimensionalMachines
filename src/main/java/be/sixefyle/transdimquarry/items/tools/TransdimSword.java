package be.sixefyle.transdimquarry.items.tools;

import be.sixefyle.transdimquarry.enums.EnumColor;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TransdimSword extends InfusedTool {

    public static final double DAMAGE_PER_INFUSE = 0.1;

    public static final List<Enchantment> ALLOWED_ENCHANT =
            List.of(Enchantments.SHARPNESS, Enchantments.SMITE, Enchantments.FIRE_ASPECT,
                    Enchantments.MOB_LOOTING, Enchantments.KNOCKBACK);

    public TransdimSword() {
        super(new Properties().rarity(Rarity.EPIC).fireResistant().setNoRepair(),
                5_000_000, 12_500, 50_000);
    }

    public double getDamageAdded(ItemStack itemStack){
        if(itemStack.hasTag() && !itemStack.getTag().contains("added_damage")){
            itemStack.getTag().putDouble("added_damage", 0);
        }

        return itemStack.hasTag() ? itemStack.getTag().getDouble("added_damage") : 0;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        return super.use(level, player, interactionHand);
    }


    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return Tiers.NETHERITE.getEnchantmentValue();
    }

    @Override
    public boolean isEnchantable(ItemStack pStack) {
        return true;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return ALLOWED_ENCHANT.stream().anyMatch(enchantment1 -> enchantment1.equals(enchantment));
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity damager) {
        if(getEnergyStorage(itemStack).getEnergyStored() > getBaseEnergyCost()) {
            getEnergyStorage(itemStack).extractEnergy(getBaseEnergyCost(), false);
        }

        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if(slot == EquipmentSlot.MAINHAND){
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            double addedDamage = getDamageAdded(stack);

            if(getEnergyStorage(stack).getEnergyStored() >= getBaseEnergyCost()) {
                builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 33 + addedDamage, AttributeModifier.Operation.ADDITION));
                builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", 1.6, AttributeModifier.Operation.ADDITION));
            } else {
                builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 1, AttributeModifier.Operation.ADDITION));
                builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", 1.6, AttributeModifier.Operation.ADDITION));
            }

            return builder.build();
        }
        return super.getDefaultAttributeModifiers(slot);
    }

    @Override
    public void onInfuseLevelUp(ItemStack itemStack) {
        if(!itemStack.hasTag())
            return;

        itemStack.getTag().putDouble("added_damage", getDamageAdded(itemStack) + DAMAGE_PER_INFUSE);
    }

    @Override
    public double getNeededInfusedEnergy(ItemStack itemStack) {
        return getBaseNeededInfusedEnergy() + (getDamageAdded(itemStack) * 150_000);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> tooltip, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, tooltip, tooltipFlag);

        double addedDamage = getDamageAdded(itemStack);
        tooltip.add(Component.empty());
        if(Screen.hasShiftDown()) {
            tooltip.add(EnumColor.GRAY.getColoredComponent("Tool Informations:"));

            tooltip.add(EnumColor.BLUE.getColoredComponent("Damage Added: ")
                    .append(EnumColor.TEAL.getColoredComponent(String.format("%.2f", addedDamage))));
        } else {
            tooltip.add(EnumColor.GRAY.getColoredComponent("Hold [")
                    .append(EnumColor.PURPLE.getColoredComponent("SHIFT"))
                    .append(EnumColor.GRAY.getColoredComponent("] to show tool details")));
        }
    }
}
