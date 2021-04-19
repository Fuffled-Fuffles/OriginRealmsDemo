package com.fuffles.demo.block;

import java.util.HashMap;
import java.util.function.Function;

import org.bukkit.block.BlockFace;

public class PlacerStyle
{
	private static final Function<BlockFace, PlacerResult> ANY = (face) -> {
		return PlacerResult.ANY;
	};
	public static final Function<BlockFace, PlacerResult> AXIS_BASED = (face) -> {
		if (face.equals(BlockFace.EAST) || face.equals(BlockFace.WEST))
		{
			return PlacerResult.X_AXIS;
		}
		else if (face.equals(BlockFace.UP) || face.equals(BlockFace.DOWN))
		{
			return PlacerResult.Y_AXIS;
		}
		else if (face.equals(BlockFace.NORTH) || face.equals(BlockFace.SOUTH))
		{
			return PlacerResult.Z_AXIS;
		}
		return PlacerResult.Y_AXIS;
	};
	
	private HashMap<PlacerResult, CustomBlock> map;
	private Function<BlockFace, PlacerResult> test_fn;
	
	public PlacerStyle() 
	{
		this.map = new HashMap<>();
		this.test_fn = ANY;
	}
	
	public PlacerStyle withTestFunction(Function<BlockFace, PlacerResult> tester)
	{
		this.test_fn = tester;
		return this;
	}
	
	public Function<BlockFace, PlacerResult> getTestFunction()
	{
		return this.test_fn;
	}
	
	public PlacerStyle withResultFor(PlacerResult result, CustomBlock block)
	{
		this.map.put(result, block);
		return this;
	}
	
	public CustomBlock getResultFor(PlacerResult result)
	{
		return this.map.get(result);
	}
}
