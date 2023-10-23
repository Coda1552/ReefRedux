package codyhuh.reefredux.common.world;

import codyhuh.reefredux.FastNoiseLite;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CoralWallFanBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.Optional;

public abstract class AbstractReefRockFeature extends Feature<NoneFeatureConfiguration> {

    public AbstractReefRockFeature(Codec<NoneFeatureConfiguration> pCodec) {
        super(pCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> pContext) {
        WorldGenLevel worldGenLevel = pContext.level();
        BlockPos blockPos = pContext.origin();
        RandomSource random = pContext.random();

        FastNoiseLite noise = createNoise(worldGenLevel.getSeed() + random.nextLong(), 0.15F);

        //Minecraft.getInstance().getChatListener().handleSystemMessage(Component.literal("Where am I? " + blockPos), false);
        BlockPos heightmapPos = worldGenLevel.getHeightmapPos(Heightmap.Types.OCEAN_FLOOR_WG, blockPos);
        blockPos = heightmapPos.offset(0, -4, 0); // Offset the origin once instead of multiple times

        if (worldGenLevel.getBlockState(heightmapPos.below()).is(BlockTags.SAND) && worldGenLevel.getBlockState(blockPos.offset(0, getConfigs()[0][1], 0)).is(Blocks.WATER) && random.nextFloat() > 0.75F) {
            createRock(worldGenLevel, blockPos, noise);
            return true;
        }
        else {
            return false;
        }
    }

    public abstract int[][] getConfigs();

    public void createRock(WorldGenLevel worldgenlevel, BlockPos origin, FastNoiseLite noise) {
        int radius;
        int height;

        for (int i = 0; i < getConfigs().length; i++) {
            radius = getConfigs()[i][0];
            height = getConfigs()[i][1];
            boolean finalSection = i + 1 >= getConfigs().length;
            boolean topSection = i == 0;

            createRockSection(worldgenlevel, origin, radius, height, Blocks.WATER.defaultBlockState(), noise, topSection);
        }

    }

    private static void createRockSection(WorldGenLevel worldgenlevel, BlockPos origin, int radius, int height, BlockState block, FastNoiseLite noise, boolean finalSection) {
        int heightLower = 0;

        Optional<Block> coral = BuiltInRegistries.BLOCK.getTag(BlockTags.CORAL_BLOCKS).flatMap((p_224980_) -> {
            return p_224980_.getRandomElement(worldgenlevel.getRandom());
        }).map(Holder::value);

        if (finalSection) {
            heightLower = -height;
        }

        for (int x = -radius; x < radius; x++) {
            for (int y = heightLower; y < height; y++) {
                for (int z = -radius; z < radius; z++) {
                    BlockPos pos = origin.offset(x, y, z);

                    double distance = distance(x, y, z, radius, height, radius);
                    float f = noise.GetNoise(x, (float) y, z);

                    if (distance < 1) {
                        if (finalSection && f < -0.15 && f > -0.17) {
                            worldgenlevel.setBlock(pos, coral.map(Block::defaultBlockState).orElseGet(Blocks.STONE::defaultBlockState), 3);
                            createPlates(worldgenlevel, pos, worldgenlevel.getBlockState(pos));
                        }
                        else if (f < 0 && f > -0.3) {
                            worldgenlevel.setBlock(pos, Blocks.STONE.defaultBlockState(), 3);
                        } else if (f <= 0.4 && f > 0) {
                            worldgenlevel.setBlock(pos, Blocks.STONE.defaultBlockState(), 3);
                        } else if (f > 0.4 && f < 0.9) {
                            worldgenlevel.setBlock(pos, Blocks.STONE.defaultBlockState(), 3);
                        } else if (!worldgenlevel.getBlockState(pos).is(BlockTags.CORAL_BLOCKS) && pos.getY() < 63) {
                            worldgenlevel.setBlock(pos, Blocks.WATER.defaultBlockState(), 3);
                        }
                        else {
                            if (!worldgenlevel.getBlockState(pos).is(BlockTags.CORAL_BLOCKS)) {
                                worldgenlevel.setBlock(pos, block, 3);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void createPlates(WorldGenLevel worldgenlevel, BlockPos coralPos, BlockState coralType) {
        Optional<Block> coral = BuiltInRegistries.BLOCK.getTag(BlockTags.WALL_CORALS).flatMap((p_224980_) -> {
            return p_224980_.getRandomElement(worldgenlevel.getRandom());
        }).map(Holder::value);

        if (worldgenlevel.getRandom().nextFloat() > 0.6F) {

            for (int i = -2; i <= 2; i++) {
                for (int j = -2; j <= 2; j++) {
                    BlockPos pos = coralPos.offset(i, 0, j);
                    double distance = distance(i, 1, j, 3, 1, 3);
                    if (distance < 1.8) {
                        worldgenlevel.setBlock(pos, coralType, 3);

                        for (Direction dir : Direction.Plane.HORIZONTAL) {
                            BlockState block = coral.map(Block::defaultBlockState).orElseGet(Blocks.HORN_CORAL_WALL_FAN::defaultBlockState).setValue(CoralWallFanBlock.WATERLOGGED, true).setValue(CoralWallFanBlock.FACING, dir);

                            // is there a worse way to do this?
                            BlockPos fanPos0 = coralPos.relative(dir, 3);
                            BlockPos fanPos1 = coralPos.relative(dir, 3).relative(dir.getClockWise());
                            BlockPos fanPos2 = coralPos.relative(dir, 3).relative(dir.getCounterClockWise());
                            if (worldgenlevel.getBlockState(fanPos0).is(Blocks.WATER)) {
                                worldgenlevel.setBlock(fanPos0, block, 3);
                            }
                            if (worldgenlevel.getBlockState(fanPos1).is(Blocks.WATER)) {
                                worldgenlevel.setBlock(fanPos1, block, 3);
                            }
                            if (worldgenlevel.getBlockState(fanPos2).is(Blocks.WATER)) {
                                worldgenlevel.setBlock(fanPos2, block, 3);
                            }
                        }
                    }
                }
            }
        }
        else {
            for (int i = -1; i <= 1; i++) {
                for (int j = -1; j <= 1; j++) {
                    BlockPos pos = coralPos.offset(i, 0, j);
                    double distance = distance(i, 1, j, 2, 1, 2);
                    if (distance < 1.8) {
                        worldgenlevel.setBlock(pos, coralType, 3);
                    }
                }
            }
        }
    }

    public static double distance(double x, double y, double z, double xRadius, double yRadius, double zRadius) {
        return Mth.square(x / (xRadius)) + Mth.square(y / (yRadius)) + Mth.square(z / (zRadius));
    }

    private static FastNoiseLite createNoise(long seed, float frequency) {
        FastNoiseLite noise = new FastNoiseLite((int) seed);
        noise.SetNoiseType(FastNoiseLite.NoiseType.OpenSimplex2S);
        noise.SetFrequency(frequency);
        return noise;
    }
}