package codyhuh.reefredux.common.world;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class MediumReefRockFeature extends AbstractReefRockFeature {

    public MediumReefRockFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public int[][] getConfigs() {
        return new int[][]{
                {4, 12},
                {5, 4},
                {7, 2}
        };
    }
}