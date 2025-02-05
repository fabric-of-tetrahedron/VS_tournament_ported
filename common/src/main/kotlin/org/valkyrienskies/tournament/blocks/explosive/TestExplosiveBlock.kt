package org.valkyrienskies.tournament.blocks.explosive

import net.minecraft.core.BlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.SoundType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.level.block.state.properties.BlockStateProperties
import net.minecraft.world.level.material.MapColor
import org.valkyrienskies.mod.common.util.toJOMLD
import org.valkyrienskies.tournament.util.algo.Algo3d
import org.valkyrienskies.tournament.util.extension.toBlock

class TestExplosiveBlock : Block(
    Properties.of()
        .mapColor(MapColor.WOOL)
        .sound(SoundType.WOOL)
        .strength(1.0f, 2.0f)
) {

    init {
        registerDefaultState(defaultBlockState().setValue(BlockStateProperties.POWER, 0))
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        super.createBlockStateDefinition(builder)
        builder.add(BlockStateProperties.POWER)
    }

    override fun neighborChanged(
        state: BlockState,
        level: Level,
        pos: BlockPos,
        block: Block,
        fromPos: BlockPos,
        isMoving: Boolean
    ) {
        super.neighborChanged(state, level, pos, block, fromPos, isMoving)

        if (level as? ServerLevel == null) return

        val signal = level.getBestNeighborSignal(pos)
        if (signal > 0) {
            level.removeBlock(pos, false)
            Algo3d.cone(pos.toJOMLD(), 5.0, -10.0).forEach {
                level.removeBlockEntity(it.toBlock())
                level.removeBlock(it.toBlock(), false)
            }
            //Algo3d.sphere(pos.toJOMLD(), 5.0).forEach {
            //    level.removeBlockEntity(it.toBlock())
            //    level.removeBlock(it.toBlock(), false)
            //}
        }
    }
}