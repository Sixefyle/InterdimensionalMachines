package be.sixefyle.transdimquarry.integration.jade;

import be.sixefyle.transdimquarry.blocks.quarries.QuarryBaseBlockEntity;
import be.sixefyle.transdimquarry.blocks.quarries.transdimquarry.TransdimQuarryBlock;
import snownee.jade.api.*;

@WailaPlugin
public class JadeRegister implements IWailaPlugin {

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(TransdimensionalQuarryComponentProvider.INSTANCE, QuarryBaseBlockEntity.class);
    }

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(TransdimensionalQuarryComponentProvider.INSTANCE, TransdimQuarryBlock.class);
    }
}
