package com.fuffles.demo.lib;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import com.fuffles.demo.block.CustomBlock;
import com.fuffles.demo.block.CustomBlockMetaBuilder;
import com.fuffles.demo.block.ToolTier;
import com.fuffles.demo.block.ToolType;
import com.fuffles.demo.block.noteblock.Instrument;
import com.fuffles.demo.item.CustomBlockItem;

public class BlockLib
{
	public static final CustomBlockItem CRYSTAL_BLOCK_ITEM = new CustomBlockItem("Crystal Block", 1);
	public static final CustomBlock CRYSTAL_BLOCK = new CustomBlock(CRYSTAL_BLOCK_ITEM,
			new CustomBlockMetaBuilder()
				.asNoteBlock(Instrument.BANJO, 2)
				.setHardness(3F)
				.setNeedsTool(ToolType.PICKAXE, ToolTier.IRON)
				.setLoot(new ItemStack(Material.PRISMARINE_CRYSTALS))
				.setSilkTouchLoot(CRYSTAL_BLOCK_ITEM.getItem())
				.setSoundBank(
					"minecraft:block.amethyst.place", 
					"minecraft:block.amethyst.hit", 
					"minecraft:block.amethyst.break")
				.build()
		);
	
	public static final CustomBlockItem UNINSPIRED_LOG_STRIPPED_ITEM = new CustomBlockItem("Stripped Mystery Log", 3);
	public static final CustomBlock UNINSPIRED_LOG_STRIPPED_YAXIS = new CustomBlock(UNINSPIRED_LOG_STRIPPED_ITEM,
			new CustomBlockMetaBuilder()
				.asNoteBlock(Instrument.VIBRA, 3)
				.setHardness(1F)
				.setNeedsTool(ToolType.AXE, ToolTier.ANY)
				.setLoot(UNINSPIRED_LOG_STRIPPED_ITEM.getItem())
				.build()
		);
	public static final CustomBlock UNINSPIRED_LOG_STRIPPED_XAXIS = new CustomBlock(UNINSPIRED_LOG_STRIPPED_ITEM,
			new CustomBlockMetaBuilder()
				.asNoteBlock(Instrument.XYLO, 15)
				.setHardness(1F)
				.setNeedsTool(ToolType.AXE, ToolTier.ANY)
				.setLoot(UNINSPIRED_LOG_STRIPPED_ITEM.getItem())
				.build()
		);
	public static final CustomBlock UNINSPIRED_LOG_STRIPPED_ZAXIS = new CustomBlock(UNINSPIRED_LOG_STRIPPED_ITEM,
			new CustomBlockMetaBuilder()
				.asNoteBlock(Instrument.CHIME, 6)
				.setHardness(1F)
				.setNeedsTool(ToolType.AXE, ToolTier.ANY)
				.setLoot(UNINSPIRED_LOG_STRIPPED_ITEM.getItem())
				.build()
		);
	
	public static final CustomBlockItem UNINSPIRED_LOG_ITEM = new CustomBlockItem("Mystery Log", 2);
	public static final CustomBlock UNINSPIRED_LOG_YAXIS = new CustomBlock(UNINSPIRED_LOG_ITEM,
			new CustomBlockMetaBuilder()
				.asNoteBlock(Instrument.BANJO, 20, true)
				.setHardness(1F)
				.setNeedsTool(ToolType.AXE, ToolTier.ANY)
				.setLoot(UNINSPIRED_LOG_ITEM.getItem())
				.setStripable(UNINSPIRED_LOG_STRIPPED_YAXIS)
				.build()
		);
	public static final CustomBlock UNINSPIRED_LOG_XAXIS = new CustomBlock(UNINSPIRED_LOG_ITEM,
			new CustomBlockMetaBuilder()
				.asNoteBlock(Instrument.BANJO, 7)
				.setHardness(1F)
				.setNeedsTool(ToolType.AXE, ToolTier.ANY)
				.setLoot(UNINSPIRED_LOG_ITEM.getItem())
				.setStripable(UNINSPIRED_LOG_STRIPPED_XAXIS)
				.build()
		);
	public static final CustomBlock UNINSPIRED_LOG_ZAXIS = new CustomBlock(UNINSPIRED_LOG_ITEM,
			new CustomBlockMetaBuilder()
				.asNoteBlock(Instrument.GUITAR, 20, true)
				.setHardness(1F)
				.setNeedsTool(ToolType.AXE, ToolTier.ANY)
				.setLoot(UNINSPIRED_LOG_ITEM.getItem())
				.setStripable(UNINSPIRED_LOG_STRIPPED_ZAXIS)
				.build()
		);
	
	public static final List<CustomBlockItem> BLOCK_PLACERS = Arrays.asList(
		CRYSTAL_BLOCK_ITEM.finalizePlacer(CRYSTAL_BLOCK),
		UNINSPIRED_LOG_ITEM.finalizeAxisPlacer(UNINSPIRED_LOG_YAXIS, UNINSPIRED_LOG_XAXIS, UNINSPIRED_LOG_ZAXIS),
		UNINSPIRED_LOG_STRIPPED_ITEM.finalizeAxisPlacer(UNINSPIRED_LOG_STRIPPED_YAXIS, UNINSPIRED_LOG_STRIPPED_XAXIS, UNINSPIRED_LOG_STRIPPED_ZAXIS)
	);
	public static final List<CustomBlock> BLOCKS = Arrays.asList(
		CRYSTAL_BLOCK, 
		UNINSPIRED_LOG_YAXIS, 
		UNINSPIRED_LOG_XAXIS, 
		UNINSPIRED_LOG_ZAXIS, 
		UNINSPIRED_LOG_STRIPPED_YAXIS,
		UNINSPIRED_LOG_STRIPPED_XAXIS,
		UNINSPIRED_LOG_STRIPPED_ZAXIS
	);
}
