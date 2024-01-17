package be.sixefyle.transdimquarry.blocks.quarries.transdimquarry;

import be.sixefyle.transdimquarry.items.quarryupgrades.QuarryUpgrade;
import be.sixefyle.transdimquarry.registries.BlockEntityRegister;
import be.sixefyle.transdimquarry.blocks.quarries.QuarryBaseBlockEntity;
import be.sixefyle.transdimquarry.config.CommonConfig;
import be.sixefyle.transdimquarry.registries.ItemRegister;
import be.sixefyle.transdimquarry.utils.OreUtils;
import net.minecraft.core.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import java.util.*;

public class TransdimQuarryBlockEntity extends QuarryBaseBlockEntity {
    public static final int CONTAINER_SIZE = 30;
    public static final int[] UPGRADE_SLOTS = {27,28,29};

    public TransdimQuarryBlockEntity(BlockPos pos, BlockState state) {
        super(BlockEntityRegister.TRANSDIMENSIONAL_QUARRY.get(), pos, state, CONTAINER_SIZE, UPGRADE_SLOTS,
                CommonConfig.TRANSDIMENSIONAL_QUARRY_ENERGY_CAPACITY.get());

        setBaseEnergyNeeded(CommonConfig.TRANSDIMENSIONAL_QUARRY_BASE_ENERGY_COST.get());
        setBaseTimeToMine(CommonConfig.MINING_TIME.get());
        setTimeToMine(getBaseTimeToMine());
        setOreFindChange(CommonConfig.ORE_FIND_CHANCE.get());
    }

    @Override
    protected Component getDefaultName() {
        return Component.literal("Trans-dimensional Quarry");
    }

    @Override
    protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
        return new TransdimQuarryMenu(id, inventory, this, this.getBaseData());
    }

    @Override
    public void onLoad() {
        super.onLoad();

        ItemStack itemStack;
        for (int upgradeSlot : UPGRADE_SLOTS) {
            itemStack = getItems().get(upgradeSlot);
            if(itemStack.getItem() instanceof QuarryUpgrade upgrade){
                getUpgrades().replace(upgradeSlot, itemStack.copy());
                upgrade.onPlaced(this);
            }
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, TransdimQuarryBlockEntity quarry) {
        quarry.onTick(level, pos);
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        if(slot < UPGRADE_SLOTS[0]) return false;

        if(slot > UPGRADE_SLOTS[0]){
            if(isSilkTouch() && itemStack.is(ItemRegister.QUARRY_FORTUNE_UPGRADE.get()) || itemStack.is(ItemRegister.QUARRY_SILK_UPGRADE.get())){
                return false;
            } else if(getFortuneLevel() > 0 && itemStack.is(ItemRegister.QUARRY_SILK_UPGRADE.get())) {
                return false;
            } else if(!canPlaceUpgrade(itemStack)){
                return false;
            }
        }

        return itemStack.getItem() instanceof QuarryUpgrade;
    }

    @Override
    public void setItem(int slot, ItemStack itemStack) {
        this.getItems().set(slot, itemStack);

        if(slot >= UPGRADE_SLOTS[0]){
            if(!getUpgrades().get(slot).isEmpty() && getUpgrades().get(slot).getItem() instanceof QuarryUpgrade upgrade){
                upgrade.onRemoved(this);

                if (itemStack.isEmpty()){
                    getUpgrades().replace(slot, ItemStack.EMPTY);
                }
            }

            if(!itemStack.isEmpty() && itemStack.getItem() instanceof QuarryUpgrade upgrade) {
                upgrade.onPlaced(this);
                getUpgrades().replace(slot, itemStack.copy());
            }
        }

        setChanged();
    }

    private final static List<Map<Block, Double>> FILLING_BLOCKS = new ArrayList<>(){{
        add(new HashMap<>() {{
            put(Blocks.STONE, .5);
            put(Blocks.DEEPSLATE, .3);
            put(Blocks.SAND, .03);
            put(Blocks.GRAVEL, .07);
            put(Blocks.DIORITE, .05);
            put(Blocks.ANDESITE, .05);
        }});
        add(new HashMap<>() {{
            put(Blocks.NETHERRACK, .9);
            put(Blocks.GRAVEL, .06);
            put(Blocks.SOUL_SAND, .03);
            put(Blocks.SOUL_SOIL, .01);
        }});
        add(new HashMap<>() {{
            put(Blocks.END_STONE, 1.0);
        }});
    }};

    public static Block getRandomBlock(Random random) {
        Map<Block, Double> blockChances = FILLING_BLOCKS.get(random.nextInt(FILLING_BLOCKS.size()));
        if (blockChances != null) {
            double totalWeight = blockChances.values().stream().mapToDouble(Double::doubleValue).sum();
            double randomValue = random.nextDouble() * totalWeight;
            for (Map.Entry<Block, Double> entry : blockChances.entrySet()) {
                randomValue -= entry.getValue();
                if (randomValue <= 0) {
                    return entry.getKey();
                }
            }
        }
        return Blocks.STONE;
    }

    @Override
    protected List<ItemStack> mineNextBlock() {
        List<ItemStack> itemStacks = new ArrayList<>();
        if(level != null){
            boolean isOre;
            BlockState generatedBlockState;
            Random random = new Random();
            int blockAmount = new Random().nextInt(64) + 8;
            for (int i = 0; i < blockAmount; i++) {
                isOre = random.nextDouble() <= oreFindChange;
                if(!isOre){
                    generatedBlockState = getRandomBlock(random).defaultBlockState();
                } else {
                    generatedBlockState = OreUtils.getOres().get(random.nextInt(OreUtils.getOres().size())).defaultBlockState();
                }

                if(!canMineBlock(generatedBlockState, level, getBlockPos(),
                        getFakePlayer())) continue;

                LootParams.Builder context = new LootParams.Builder((ServerLevel) level)
                        .withParameter(LootContextParams.KILLER_ENTITY, getFakePlayer())
                        .withParameter(LootContextParams.TOOL, getHarvesterTool())
                        .withParameter(LootContextParams.ORIGIN, new Vec3(0, 0, 0))
                        .withOptionalParameter(LootContextParams.BLOCK_STATE, generatedBlockState);
                if (getFortuneLevel() > 0) {
                    context.withLuck(getFortuneLevel());
                }

                List<ItemStack> blockDrops = generatedBlockState.getDrops(context);
                itemStacks.addAll(blockDrops);

                if(!isContainerFull()) {
                    this.resetProgress();
                }
            }
        }
        return itemStacks;
    }
}
