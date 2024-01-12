package be.sixefyle.transdimquarry.integration.jade;

import be.sixefyle.transdimquarry.TransdimensionalMachines;
import be.sixefyle.transdimquarry.blocks.quarries.QuarryBaseBlockEntity;
import be.sixefyle.transdimquarry.enums.EnumColor;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import snownee.jade.addon.core.ModNameProvider;
import snownee.jade.addon.core.ObjectNameProvider;
import snownee.jade.addon.universal.EnergyStorageProvider;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

import java.util.concurrent.atomic.AtomicInteger;

public class TransdimensionalQuarryComponentProvider implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {
    static final TransdimensionalQuarryComponentProvider INSTANCE = new TransdimensionalQuarryComponentProvider();

    @Override
    public void appendTooltip(ITooltip tooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
        tooltip.clear();

        ObjectNameProvider.INSTANCE.appendTooltip(tooltip, blockAccessor, iPluginConfig);
        CompoundTag serverData = blockAccessor.getServerData();

        tooltip.append(serverData.getBoolean("isWorking") ? EnumColor.TEAL.getColoredComponent(" [ON]") : EnumColor.RED.getColoredComponent(" [OFF]"));
        tooltip.add(EnumColor.WHITE.getColoredComponent("Progress: ").append(EnumColor.YELLOW.getColoredComponent(serverData.getShort("progress") + "%")));

        if(serverData.getBoolean("haveUpgrade")){
            tooltip.add(EnumColor.WHITE.getColoredComponent("Upgrades: "));
            for (int i = 1; i <= 3; i++) {
                if(serverData.contains("upgrade" + i)){
                    tooltip.add(Component.literal(" - ").append(Component.Serializer.fromJson(serverData.getString("upgrade" + i))));
                }
            }
        }
        tooltip.add(Component.empty());


        EnergyStorageProvider.INSTANCE.appendTooltip(tooltip, blockAccessor, iPluginConfig);
        ModNameProvider.INSTANCE.appendTooltip(tooltip, blockAccessor, iPluginConfig);
    }

    @Override
    public void appendServerData(CompoundTag tag, BlockAccessor accessor) {
        QuarryBaseBlockEntity quarry = (QuarryBaseBlockEntity) accessor.getBlockEntity();
        tag.putBoolean("isWorking", quarry.isWorking());
        tag.putShort("progress", (short) (((double) quarry.getProgress() / quarry.getTimeToMine()) * (short) 100));

        boolean haveUpgrade = quarry.getUpgrades().values().stream().anyMatch(itemStack -> !itemStack.isEmpty());
        tag.putBoolean("haveUpgrade", haveUpgrade);

        AtomicInteger index = new AtomicInteger(1);
        quarry.getUpgrades().forEach((integer, itemStack) -> {
            if(!itemStack.isEmpty()){
                tag.putString("upgrade" + index.getAndIncrement(), Component.Serializer.toJson(itemStack.getHoverName()));
            }
        });
    }

    @Override
    public ResourceLocation getUid() {
        return TransdimensionalMachines.resourceLocation("jade_tooltip");
    }

    @Override
    public int getDefaultPriority() {
        return TooltipPosition.TAIL + 1;
    }
}
