package codyhuh.reefredux;

import codyhuh.reefredux.registry.ModFeatures;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(ReefRedux.MOD_ID)
public class ReefRedux {
    public static final String MOD_ID = "reefredux";

    public ReefRedux() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        ModFeatures.FEATURES.register(bus);
    }
}
