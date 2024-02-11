package be.sixefyle.transdimquarry.blocks.soulharverster.cosmic;

import be.sixefyle.transdimquarry.blocks.soulharverster.SoulHarvesterMenu;
import be.sixefyle.transdimquarry.registries.BlockRegister;
import be.sixefyle.transdimquarry.registries.MenuRegister;
import be.sixefyle.transdimquarry.utils.Vec2i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class CosmicSoulHarvesterMenu extends SoulHarvesterMenu {

    public CosmicSoulHarvesterMenu(int id, Inventory inv, FriendlyByteBuf extraData) {
        this(id, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(3));
    }

    public CosmicSoulHarvesterMenu(int id, Inventory inv, BlockEntity entity, ContainerData data) {
        super(MenuRegister.COSMIC_SOUL_MANIPULATOR.get(), id, inv, entity, data);
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
                new Vec2i(100, 49),
                new Vec2i(118, 49),
                new Vec2i(136, 49),
//                new Vec2i(100, 67),
//                new Vec2i(118, 67),
//                new Vec2i(136, 67),
//                new Vec2i(100, 86),
//                new Vec2i(118, 86),
//                new Vec2i(136, 86),
//                new Vec2i(100, 106),
//                new Vec2i(118, 106),
//                new Vec2i(136, 106),

                new Vec2i(15, 67),
                new Vec2i(33, 67),
                new Vec2i(51, 67),
                new Vec2i(69, 67)
        );
    }

    @Override
    public Block getBlock() {
        return BlockRegister.COSMIC_SOUL_MANIPULATOR.get();
    }
}
