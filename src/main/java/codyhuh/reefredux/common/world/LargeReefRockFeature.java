package codyhuh.reefredux.common.world;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class LargeReefRockFeature extends AbstractReefRockFeature {

    public LargeReefRockFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public int[][] getConfigs() {
        return new int[][]{
                {6, 18},
                {8, 7},
                {10, 3}
        };
    }
}