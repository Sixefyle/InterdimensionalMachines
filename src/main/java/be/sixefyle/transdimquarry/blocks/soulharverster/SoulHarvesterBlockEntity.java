package be.sixefyle.transdimquarry.blocks.soulharverster;

import be.sixefyle.transdimquarry.blocks.TransDimMachine;
import be.sixefyle.transdimquarry.items.SoulBottleItem;
import be.sixefyle.transdimquarry.registries.BlockEntityRegister;
import be.sixefyle.transdimquarry.registries.ItemRegister;
import be.sixefyle.transdimquarry.utils.LevelUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;

import java.util.*;

public class SoulHarvesterBlockEntity extends TransDimMachine {

    private int soulBottleSlotsAmount;
    private FakePlayer fakePlayer;
    private List<ItemStack> itemLeft = new ArrayList<>();

    public SoulHarvesterBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegister.SOUL_MANIPULATOR.get(), pos, state, 8, 50_000);

        soulBottleSlotsAmount = 2;
        setMaxProgress(250);
        setEnergyNeeded(12_500);
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Soul Harvester");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inv) {
        return new SoulHarvesterMenu(id, inv, this, getBaseData());
    }

    @Override
    public boolean canPlaceItem(int pIndex, ItemStack itemStack) {
        if(pIndex >= getContainerSize() - soulBottleSlotsAmount){
            return itemStack.is(ItemRegister.SOUL_BOTTLE.get());
        }

        return false;
    }

    @Override
    public int getItemSlotsSize() {
        return getContainerSize() - soulBottleSlotsAmount;
    }

    public Map<EntityType<?>, Integer> getStoredSouls(){
        Map<EntityType<?>, Integer> map = new HashMap<>(soulBottleSlotsAmount);
        ItemStack itemStack;
        for (int i = getContainerSize() - 1; i >= getContainerSize() - soulBottleSlotsAmount; i--) {
            itemStack = getItem(i);
            if(itemStack.is(ItemRegister.SOUL_BOTTLE.get())){
                SoulBottleItem soulBottleItem = (SoulBottleItem) itemStack.getItem();

                String entityTypeId = soulBottleItem.getEntitytypeId(itemStack);
                if(entityTypeId == null) continue;

                Optional<EntityType<?>> optionalEntityType = EntityType.byString(entityTypeId);
                int amount = soulBottleItem.getSoulAmount(itemStack);
                if(optionalEntityType.isPresent()){
                    EntityType<?> entityType = optionalEntityType.get();
                    if(!map.containsKey(optionalEntityType.get())){
                        map.put(entityType, 0);
                    }
                    map.replace(entityType, map.get(entityType) + amount);
                }
            }
        }
        return map;
    }

    public static void tick(Level level, BlockPos blockPos, BlockState blockState, SoulHarvesterBlockEntity blockEntity) {
        blockEntity.onTick(level, blockPos);

        if(level.isClientSide()) return;

        if(blockEntity.itemLeft.size() > 0){
            blockEntity.itemLeft = blockEntity.tryAddItemToAttachedContainer(blockEntity.itemLeft);
            return;
        }

        if(blockEntity.hasEnoughEnergy()){
            Map<EntityType<?>, Integer> storedSouls = blockEntity.getStoredSouls();
            if(!storedSouls.isEmpty()) {
                if(blockEntity.updateProgress(1)) {
                    blockEntity.addEnergy(-blockEntity.getNeededEnergy());

                    if (blockEntity.fakePlayer == null) {
                        blockEntity.fakePlayer = LevelUtil.getFakePlayer((ServerLevel) level, "TransDim-Player");
                    }

                    storedSouls.forEach((entityType, integer) -> {
                        ResourceLocation resourcelocation = entityType.getDefaultLootTable();
                        LootTable loottable = level.getServer().getLootData().getLootTable(resourcelocation);

                        LootParams.Builder builder = new LootParams.Builder((ServerLevel) level)
                                .withParameter(LootContextParams.DAMAGE_SOURCE, blockEntity.fakePlayer.damageSources().magic())
                                .withParameter(LootContextParams.ORIGIN, new Vec3(0, 0, 0))
                                .withParameter(LootContextParams.THIS_ENTITY, entityType.create(level));

                        LootParams lootParams = builder.create(LootContextParamSets.ENTITY);
                        ObjectArrayList<ItemStack> randomItems = loottable.getRandomItems(lootParams).clone();

                        if (integer > 1) {
                            for (ItemStack randomItem : randomItems) {
                                randomItem.setCount(randomItem.getCount() * (int) (Math.random() * integer));
                            }
                        }

                        blockEntity.itemLeft = blockEntity.tryAddItemToAttachedContainer(randomItems.clone());
                    });
                }
            } else {
                blockEntity.resetProgress();
            }
        }
    }
}
