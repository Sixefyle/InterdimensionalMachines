package be.sixefyle.transdimquarry.items.tools;

import be.sixefyle.transdimquarry.energy.ItemEnergyStorage;
import be.sixefyle.transdimquarry.items.EnergyCapabilityProvider;
import be.sixefyle.transdimquarry.utils.NumberUtil;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.EnergyStorage;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TransdimSword extends SwordItem {
    private static final IEnergyStorage EMPTY_ENERGY_STORAGE = new EnergyStorage(0);
    private int hitEnergyCost = 12500;
    private int infusedEnergyNeeded = 5000000;

    public TransdimSword() {
        super(Tiers.NETHERITE, 33, 0, new Properties().rarity(Rarity.EPIC).fireResistant().setNoRepair());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        return super.use(level, player, interactionHand);
    }

    @Override
    public boolean hurtEnemy(ItemStack itemStack, LivingEntity target, LivingEntity damager) {
        if(getEnergyStorage(itemStack).getEnergyStored() > hitEnergyCost) {
            getEnergyStorage(itemStack).extractEnergy(hitEnergyCost, false);
        }

        return true;
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getAttributeModifiers(EquipmentSlot slot, ItemStack stack) {
        if(slot == EquipmentSlot.MAINHAND){
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            int addedDamage = stack.getTag().getInt("added_damage");

            if(getEnergyStorage(stack).getEnergyStored() >= hitEnergyCost) {
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
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new EnergyCapabilityProvider(new ItemEnergyStorage(stack, 1000000));
    }

    public IEnergyStorage getEnergyStorage(ItemStack itemStack){
        if(ForgeCapabilities.ENERGY == null)
            return EMPTY_ENERGY_STORAGE;

        return itemStack.getCapability(ForgeCapabilities.ENERGY).orElse(EMPTY_ENERGY_STORAGE);
    }

    @Override
    public boolean isBarVisible(ItemStack p_150899_) {
        return true;
    }

    @Override
    public int getBarWidth(ItemStack itemStack) {
        IEnergyStorage energyStorage = getEnergyStorage(itemStack);
        return (int) ((double) energyStorage.getEnergyStored() / energyStorage.getMaxEnergyStored() * 13.0);
    }

    public void addInfusedEnergy(ItemStack itemStack, int amount){
        if(!itemStack.hasTag())
            return;

        if(!itemStack.getTag().contains("infused_energy")) {
            itemStack.getTag().putInt("infused_energy", 0);
            itemStack.getTag().putInt("added_damage", 0);
        }

        int newInfusedEnergy = itemStack.getTag().getInt("infused_energy") + amount;

        if(newInfusedEnergy < infusedEnergyNeeded){
            itemStack.getTag().putInt("infused_energy", newInfusedEnergy);
        } else {
            increaseDamage(itemStack);
        }
    }

    public void increaseDamage(ItemStack itemStack){
        if(!itemStack.hasTag())
            return;

        if(!itemStack.getTag().contains("infused_energy"))
            return;

        itemStack.getTag().putInt("infused_energy", 0);
        itemStack.getTag().putInt("added_damage", itemStack.getTag().getInt("added_damage") + 1);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> components, TooltipFlag tooltipFlag) {
        super.appendHoverText(itemStack, level, components, tooltipFlag);

        IEnergyStorage energyStorage = getEnergyStorage(itemStack);
        int storedEnergy = energyStorage.getEnergyStored();
        int maxEnergy = energyStorage.getMaxEnergyStored();


        components.add(Component.literal(
                String.format("Energy: %s/%s FE", NumberUtil.format(storedEnergy), NumberUtil.format(maxEnergy))));

        if(itemStack.getTag() != null){
            int addedDamage = itemStack.getTag().getInt("added_damage");
            int infusedEnergy = itemStack.getTag().getInt("infused_energy");

            components.add(Component.empty());
            components.add(Component.literal(String.format("Damage added: %d", addedDamage)));
            components.add(Component.literal(String.format("Infused Energy %s/%s FE", NumberUtil.format(infusedEnergy), NumberUtil.format(infusedEnergyNeeded))));
        }
    }
}
