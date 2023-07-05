package be.sixefyle.transdimquarry.items.tools;

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
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TransdimSword extends InfusedTool {

    public TransdimSword() {
        super(new Properties().rarity(Rarity.EPIC).fireResistant().setNoRepair(),
                1_000_000, 12_500, 5_000_000);
    }

    public int getDamageAdded(ItemStack itemStack){
        if(itemStack.hasTag() && !itemStack.getTag().contains("added_damage")){
            itemStack.getTag().putInt("added_damage", 0);
        }

        return itemStack.hasTag() ? itemStack.getTag().getInt("added_damage") : 0;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        return super.use(level, player, interactionHand);
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
            int addedDamage = getDamageAdded(stack);

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

        itemStack.getTag().putInt("added_damage", getDamageAdded(itemStack) + 1);
    }

    @Override
    public int getInfusedEnergyNeeded(ItemStack itemStack) {
        return getBaseInfusedEnergyNeeded() + (getDamageAdded(itemStack) * 1000000);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, components, tooltipFlag);

        int addedDamage = getDamageAdded(itemStack);
        components.add(Component.empty());
        if(Screen.hasShiftDown()) {
            components.add(Component.literal("Tool Stats:"));
            components.add(Component.literal(String.format("Damage added: %d", addedDamage)));
        } else {
            components.add(Component.literal("§7Press §aSHIFT§7 to show tool details"));
        }
    }
}
