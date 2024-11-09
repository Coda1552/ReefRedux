package codyhuh.reefredux.common.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CoralPlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Optional;

public class CoralPatchFeature extends Feature<NoneFeatureConfiguration> {

    public CoralPatchFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
        WorldGenLevel worldGenLevel = pContext.level();
        BlockPos blockPos = pContext.origin();
        RandomSource random = pContext.random();

        //Minecraft.getInstance().getChatListener().handleSystemMessage(Component.literal("Where am I? " + blockPos), false);
        BlockPos heightmapPos = worldGenLevel.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR_WG, blockPos);

        Optional<Block> corals = BuiltInRegistries.BLOCK.getTag(BlockTags.CORAL_PLANTS).flatMap((holders) -> holders.getRandomElement(worldGenLevel.getRandom())).map(Holder::value);

        if (worldGenLevel.getFluidState(heightmapPos).is(FluidTags.WATER)) {
            BlockState block = corals.map(Block::defaultBlockState).orElseGet(Blocks.HORN_CORAL::defaultBlockState).setValue(CoralPlantBlock.WATERLOGGED, true);

            worldGenLevel.setBlock(blockPos, block, 3);

            return true;
        }
        else {
            return false;
        }
    }
}