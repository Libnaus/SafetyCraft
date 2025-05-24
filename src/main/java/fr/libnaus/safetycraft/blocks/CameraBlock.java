package fr.libnaus.safetycraft.blocks;

import fr.libnaus.safetycraft.utils.BlockUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CameraBlock extends Block {

    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    private static final VoxelShape SHAPE = Block.box(5, 4, 0, 11, 15, 16);
    private static final VoxelShape SHAPE_UP = Block.box(5, 0, 5, 11, 2, 11);
    private static final VoxelShape SHAPE_DOWN = Block.box(5, 14, 5, 11, 16, 11);

    private static final VoxelShape SHAPE_SOUTH = BlockUtils.rotateShape(Direction.NORTH, Direction.SOUTH, SHAPE);
    private static final VoxelShape SHAPE_WEST = BlockUtils.rotateShape(Direction.NORTH, Direction.WEST, SHAPE);
    private static final VoxelShape SHAPE_EAST = BlockUtils.rotateShape(Direction.NORTH, Direction.EAST, SHAPE);


    public CameraBlock() {
        super(BlockBehaviour.Properties.of()
                .strength(2.0F)
                .requiresCorrectToolForDrops()
                .noOcclusion()
        );
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
            case UP -> SHAPE_UP;
            case DOWN -> SHAPE_DOWN;
            default -> SHAPE;
        };
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext content) {
        Direction direction = content.getClickedFace();
        return this.defaultBlockState().setValue(FACING, direction);
    }

    @Override
    public BlockState rotate(BlockState state, LevelAccessor level, BlockPos pos, Rotation direction) {
        return state.setValue(FACING, direction.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.setValue(FACING, mirror.mirror(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
