package com.fuffles.demo.block;

import org.bukkit.inventory.ItemStack;

public enum ToolType 
{
	NONE,
	SWORD,
	PICKAXE,
	AXE,
	SHOVEL,
	HOE,
	SHEARS;
	
	public static ToolType fromItemStack(ItemStack stack)
	{
		switch(stack.getType())
		{
			case SHEARS: 
				return SHEARS;
			case WOODEN_SWORD: case STONE_SWORD: case IRON_SWORD: case GOLDEN_SWORD: case DIAMOND_SWORD: case NETHERITE_SWORD:
				return SWORD;
			case WOODEN_PICKAXE: case STONE_PICKAXE: case IRON_PICKAXE: case GOLDEN_PICKAXE: case DIAMOND_PICKAXE: case NETHERITE_PICKAXE:
				return PICKAXE;
			case WOODEN_AXE: case STONE_AXE: case IRON_AXE: case GOLDEN_AXE: case DIAMOND_AXE: case NETHERITE_AXE:
				return AXE;
			case WOODEN_SHOVEL: case STONE_SHOVEL: case IRON_SHOVEL: case GOLDEN_SHOVEL: case DIAMOND_SHOVEL: case NETHERITE_SHOVEL:
				return SHOVEL;
			case WOODEN_HOE: case STONE_HOE: case IRON_HOE: case GOLDEN_HOE: case DIAMOND_HOE: case NETHERITE_HOE:
				return HOE;
			default: 
				return NONE;
		}
	}
	
}
