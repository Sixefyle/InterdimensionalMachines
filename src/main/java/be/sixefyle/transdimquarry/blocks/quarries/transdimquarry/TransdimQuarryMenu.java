package be.sixefyle.transdimquarry.blocks.quarries.transdimquarry;

import be.sixefyle.transdimquarry.blocks.TransDimMachineMenu;
import be.sixefyle.transdimquarry.registries.BlockRegister;
import be.sixefyle.transdimquarry.registries.MenuRegister;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;

public class TransdimQuarryMenu extends TransDimMachineMenu<TransdimQuarryBlockEntity> {
    public TransdimQuarryMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(6));
    }

    public TransdimQuarryMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(MenuRegister.TRANSDIMENSIONAL_QUARRY.get(), id, inv, entity, data);
        checkContainerSize(inv, TransdimQuarryBlockEntity.CONTAINER_SIZE);

        playerInventoryPos = new Vec2(8,152);
        playerToolBarPos = new Vec2(8,210);

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            for(int i = 0; i < (blockEntity.getItemSlotsSize()) / 9; ++i) {
                for(int j = 0; j < 9; ++j) {
                    this.addSlot(new SlotItemHandler(handler, i * 9 + j, 8 + j * 18, 16 + i * 18));
                }
            }

            for (int i = 0; i < blockEntity.getUpgradeSlotsSize(); i++) {
                this.addSlot(new SlotItemHandler(handler, blockEntity.getItemSlotsSize() + i, 14 + i * 18, 82));
            }
        });

        addPlayerInventory(inv);
        addPlayerHotbar(inv);
    }

    @Override
    public double getScaledProgression() {
        return (double) getProgression() / getTimeToMine();
    }

    // i should use maxProgression instead of this
    public int getTimeToMine(){
        return data.get(3);
    }
    public int getFortuneLevel(){
        return data.get(4);
    }
    public boolean isSilkTouch(){
        return data.get(5) > 0;
    }

    @Override
    public Block getBlock() {
        return BlockRegister.TRANSDIMENSIONAL_QUARRY.get();
    }
}
