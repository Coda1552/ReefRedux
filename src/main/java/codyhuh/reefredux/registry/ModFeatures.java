package codyhuh.reefredux.registry;

import codyhuh.reefredux.ReefRedux;
import codyhuh.reefredux.common.world.LargeReefRockFeature;
import codyhuh.reefredux.common.world.MediumReefRockFeature;
import codyhuh.reefredux.common.world.SmallReefRockFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(ForgeRegistries.FEATURES, ReefRedux.MOD_ID);

    public static final RegistryObject<Feature<NoneFeatureConfiguration>> SMALL_REEF_ROCK = FEATURES.register("small_reef_rock", () -> new SmallReefRockFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> MEDIUM_REEF_ROCK = FEATURES.register("medium_reef_rock", () -> new MediumReefRockFeature(NoneFeatureConfiguration.CODEC));
    public static final RegistryObject<Feature<NoneFeatureConfiguration>> LARGE_REEF_ROCK = FEATURES.register("large_reef_rock", () -> new LargeReefRockFeature(NoneFeatureConfiguration.CODEC));
}