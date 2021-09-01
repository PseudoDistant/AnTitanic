package io.github.farlandercraft.antitanic;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class BoatSensor {
    public Block block0;
    public Block block1;
    public Block block2;
    public Block block3;
    public Direction boatDirection;

    public BoatSensor(BlockPos blockPos, World world, Direction direction) {
        BlockPos pos0 = blockPos.north();
        BlockPos pos1 = blockPos.south();
        BlockPos pos2 = blockPos.east();
        BlockPos pos3 = blockPos.west();

        this.block0 = world.getBlockState(pos0).getBlock();
        this.block1 = world.getBlockState(pos1).getBlock();
        this.block2 = world.getBlockState(pos2).getBlock();
        this.block3 = world.getBlockState(pos3).getBlock();
        this.boatDirection = direction;
    }

    public boolean titanicMoment() {
        var block = switch(boatDirection) {
            case WEST -> block0;
            case EAST -> block1;
            case NORTH -> block2;
            case SOUTH -> block3;
            default -> null;
        };
        if (block != null) {
            return block == Blocks.ICE || block == Blocks.PACKED_ICE;
        } else { return false; }
    }
}
