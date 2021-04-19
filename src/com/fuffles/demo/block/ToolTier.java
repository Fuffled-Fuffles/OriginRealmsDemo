package com.fuffles.demo.block;

import org.bukkit.inventory.ItemStack;

public enum ToolTier 
{
	ANY(0, 1),
	WOOD(1, 2),
	STONE(2, 4),
	IRON(3, 6),
	GOLD(1, 12),
	DIAMOND(4, 8),
	NETHERITE(5, 9);
	
	public final int order;
	public final int speed;
	
	private ToolTier(int order, int speed)
	{
		this.order = order;
		this.speed = speed;
	}
	
	public static ToolTier fromItemStack(ItemStack stack)
	{
		switch(stack.getType())
		{
			case WOODEN_SWORD: case WOODEN_PICKAXE: case WOODEN_AXE: case WOODEN_SHOVEL: case WOODEN_HOE:
				return WOOD;
			case STONE_SWORD: case STONE_PICKAXE: case STONE_AXE: case STONE_SHOVEL: case STONE_HOE:
				return STONE;
			case IRON_SWORD: case IRON_PICKAXE: case IRON_AXE: case IRON_SHOVEL: case IRON_HOE:
				return IRON;
			case GOLDEN_SWORD: case GOLDEN_PICKAXE: case GOLDEN_AXE: case GOLDEN_SHOVEL: case GOLDEN_HOE:
				return GOLD;
			case DIAMOND_SWORD: case DIAMOND_PICKAXE: case DIAMOND_AXE: case DIAMOND_SHOVEL: case DIAMOND_HOE:
				return DIAMOND;
			case NETHERITE_SWORD: case NETHERITE_PICKAXE: case NETHERITE_AXE: case NETHERITE_SHOVEL: case NETHERITE_HOE:
				return NETHERITE;
			default:
				return ANY;
		}
	}
}
