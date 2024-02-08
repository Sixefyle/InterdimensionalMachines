package be.sixefyle.transdimquarry.blocks.soulharverster;

import be.sixefyle.transdimquarry.blocks.TransDimMachineMenu;
import be.sixefyle.transdimquarry.registries.BlockRegister;
import be.sixefyle.transdimquarry.registries.MenuRegister;
import be.sixefyle.transdimquarry.utils.Vec2i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec2;

import java.util.List;

public class SoulHarvesterMenu extends TransDimMachineMenu<SoulHarvesterBlockEntity> {

    public SoulHarvesterMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(3));
    }

    public SoulHarvesterMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(MenuRegister.SOUL_MANIPULATOR.get(), id, inv, entity, data);

        playerInventoryPos = new Vec2(8,114);
        playerToolBarPos = new Vec2(8,172);

        initSlots(inv);
    }

    @Override
    public List<Vec2i> getSlotsLocation() {
        return List.of(
                new Vec2i(100, 13),
                new Vec2i(118, 13),
                new Vec2i(136, 13),
                new Vec2i(100, 31),
                new Vec2i(118, 31),
                new Vec2i(136, 31),

                new Vec2i(15, 67),
                new Vec2i(33, 67)
        );
    }

    @Override
    public Block getBlock() {
        return BlockRegister.SOUL_MANIPULATOR.get();
    }
}
