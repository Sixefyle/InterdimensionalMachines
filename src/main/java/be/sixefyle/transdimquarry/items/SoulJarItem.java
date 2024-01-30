package be.sixefyle.transdimquarry.items;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import be.sixefyle.transdimquarry.blocks.TransDimMachine;
import be.sixefyle.transdimquarry.config.CommonConfig;
import be.sixefyle.transdimquarry.enums.EnumColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class SoulJarItem extends Item {
    private static final int MAX_SOULS = 16;

    public SoulJarItem() {
        super(new Properties().stacksTo(1));
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        ItemProperties.register(this, new ResourceLocation(TransdimensionalMachines.MODID, "fill"),
                (pStack, pLevel, pEntity, pSeed) -> pStack.getOrCreateTag().getFloat("fill"));
    }


    @Override
    public InteractionResult useOn(UseOnContext context) {
        InteractionHand hand = context.getHand();
        Player player = context.getPlayer();
        if (player == null) {
            return InteractionResult.PASS;
        }
        ItemStack stack = player.getItemInHand(hand);

        String entityKey = getEntitytypeId(stack);
        if(entityKey == null) return InteractionResult.PASS;

        Optional<EntityType<?>> entityType = EntityType.byString(entityKey);
        if(entityType.isPresent() && getSoulAmount(stack) > 0){
            Entity entity = entityType.get().create(player.level());
            if(entity != null){
                addSoulAmount(stack, -1);
                entity.setPos(context.getClickLocation());
                player.level().addFreshEntity(entity);

                if(getSoulAmount(stack) <= 0){
                    removeEntityType(stack);
                }

                return InteractionResult.SUCCESS;
            }
        }

        return super.useOn(context);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity target, InteractionHand hand) {
        String entityId = target.getEncodeId();
        if(CommonConfig.BLACKLISTED_ENTITIES.get().stream().anyMatch(s -> s.equals(entityId)))
            return InteractionResult.PASS;

        ItemStack stack = player.getItemInHand(hand);
        CompoundTag tag = stack.getOrCreateTag();

        if(entityId == null) return InteractionResult.PASS;

        if(!tag.contains("entityType")){
            setEntityType(stack, entityId);
        }

        if(getSoulAmount(stack) >= MAX_SOULS) return InteractionResult.PASS;

        if(getEntitytypeId(stack).equals(entityId)){
            addSoulAmount(stack, 1);
            target.remove(Entity.RemovalReason.KILLED);

            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public Component getName(ItemStack pStack) {
        return super.getName(pStack);
    }

    @Override
    public void appendHoverText(ItemStack itemStack, @Nullable Level pLevel, List<Component> tooltip, TooltipFlag pIsAdvanced) {
        super.appendHoverText(itemStack, pLevel, tooltip, pIsAdvanced);

        String entitytypeId = getEntitytypeId(itemStack);
        if(entitytypeId == null) return;

        Optional<EntityType<?>> entityType = EntityType.byString(entitytypeId);
        if(entityType.isEmpty()) return;
        tooltip.add(EnumColor.GRAY.getColoredComponent("Stored entity: ").append(entityType.get().getDescription()));
        tooltip.add(EnumColor.GRAY.getColoredComponent("Amount: " + getSoulAmount(itemStack) + "/" + MAX_SOULS ));
    }

    public String getEntitytypeId(ItemStack itemStack){
        CompoundTag tag = itemStack.getOrCreateTag();
        if(!tag.contains("entityType")) return null;
        return tag.getString("entityType");
    }

    public void setEntityType(ItemStack itemStack, String entityTypeId){
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.putString("entityType", entityTypeId);
        tag.putFloat("fill", 1F);
        itemStack.setTag(tag);
    }

    public void removeEntityType(ItemStack itemStack){
        CompoundTag tag = itemStack.getOrCreateTag();
        tag.remove("entityType");
        tag.putFloat("fill", 0F);
        itemStack.setTag(tag);
    }

    public int getSoulAmount(ItemStack itemStack){
        CompoundTag tag = itemStack.getOrCreateTag();
        if(!tag.contains("soulAmount")) return 0;
        return tag.getInt("soulAmount");
    }

    public void addSoulAmount(ItemStack itemStack, int toAdd){
        CompoundTag tag = itemStack.getOrCreateTag();
        if(!tag.contains("soulAmount")) {
            tag.putInt("soulAmount", toAdd);
        } else {
            tag.putInt("soulAmount", tag.getInt("soulAmount") + toAdd);
        }
        itemStack.setTag(tag);
    }

}
