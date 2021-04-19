package com.fuffles.demo.item;

import com.fuffles.demo.block.CustomBlock;
import com.fuffles.demo.block.PlacerResult;
import com.fuffles.demo.block.PlacerStyle;

public class CustomBlockItem extends CustomItem
{
	private PlacerStyle placementStyle;
	
	public CustomBlockItem(String localName, int customModelId) 
	{
		super(localName, customModelId);
	}
	
	public CustomBlockItem finalizePlacer(CustomBlock result)
	{
		this.placementStyle = new PlacerStyle()
			.withResultFor(PlacerResult.ANY, result);
		return this;
	}
	
	public CustomBlockItem finalizeAxisPlacer(CustomBlock y, CustomBlock x, CustomBlock z)
	{
		this.placementStyle = new PlacerStyle()
			.withTestFunction(PlacerStyle.AXIS_BASED)
			.withResultFor(PlacerResult.Y_AXIS, y)
			.withResultFor(PlacerResult.X_AXIS, x)
			.withResultFor(PlacerResult.Z_AXIS, z);
		return this;
	}
	
	public PlacerStyle getPlacementStyle()
	{
		return this.placementStyle;
	}
}
