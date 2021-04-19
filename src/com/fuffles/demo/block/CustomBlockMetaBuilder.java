package com.fuffles.demo.block;


import java.util.Arrays;
import java.util.List;

import org.bukkit.inventory.ItemStack;

import com.fuffles.demo.block.noteblock.Instrument;
import com.fuffles.demo.block.noteblock.NoteBlockData;

public class CustomBlockMetaBuilder
{
	private String blockData = NoteBlockData.builder().build();
	private float hardness = 1F;
	private ToolType toolType = ToolType.NONE;
	private ToolTier requiredTier = ToolTier.ANY;
	private List<ItemStack> drops = null;
	private List<ItemStack> silkyDrops = null;
	private String place = "minecraft:block.wood.original.place";
	private String hit = "minecraft:block.wood.original.hit";
	private String breakv = "minecraft:block.wood.original.break";
	private CustomBlock stripable = null;
	
	public CustomBlockMetaBuilder() {}
	
	public CustomBlockMetaBuilder asNoteBlock(Instrument instrument, int note, boolean powered)
	{
		this.blockData = NoteBlockData.builder().withInstrument(instrument).withNote(note).withPowered(powered).build();
		return this;
	}
	
	public CustomBlockMetaBuilder asNoteBlock(Instrument instrument, int note)
	{
		this.blockData = NoteBlockData.builder().withInstrument(instrument).withNote(note).build();
		return this;
	}
	
	public CustomBlockMetaBuilder setHardness(float v)
	{
		this.hardness = v;
		return this;
	}
	
	public CustomBlockMetaBuilder setNeedsTool(ToolType toolType, ToolTier needsTier)
	{
		this.toolType = toolType;
		this.requiredTier = needsTier;
		return this;
	}
	
	public CustomBlockMetaBuilder setLoot(ItemStack... items)
	{
		this.drops = Arrays.asList(items);
		return this;
	}
	
	public CustomBlockMetaBuilder setSilkTouchLoot(ItemStack... items)
	{
		this.silkyDrops = Arrays.asList(items);
		return this;
	}
	
	public CustomBlockMetaBuilder setStripable(CustomBlock result)
	{
		this.stripable = result;
		return this;
	}
	
	public CustomBlockMetaBuilder setSoundBank(String placeSound, String hitSound, String breakSound)
	{
		this.place = placeSound;
		this.hit = hitSound;
		this.breakv = breakSound;
		return this;
	}
	
	public CustomBlockMeta build()
	{
		return new CustomBlockMeta(this.blockData, this.hardness, this.toolType, this.requiredTier, this.drops, this.silkyDrops, this.stripable, this.place, this.hit, this.breakv);
	}
	
	public class CustomBlockMeta
	{
		public final String blockState;
		public final float hardness;
		public final ToolType tool;
		public final ToolTier tier;
		public final List<ItemStack> normalDrops;
		public final List<ItemStack> silkTouchDrops;
		public final boolean stripable;
		public final CustomBlock stripableOutcome;
		public final String placeSound;
		public final String hitSound;
		public final String breakSound;
		
		private CustomBlockMeta(String blockState, float hardness, ToolType tool, ToolTier tier, List<ItemStack> drops, List<ItemStack> silkDrops, CustomBlock stripable, String place, String hit, String destroy)
		{
			this.blockState = blockState;
			this.hardness = hardness;
			this.tool = tool;
			this.tier = tier;
			this.normalDrops = drops;
			this.silkTouchDrops = silkDrops;
			this.stripable = stripable != null;
			this.stripableOutcome = stripable;
			this.placeSound = place;
			this.hitSound = hit;
			this.breakSound = destroy;
		}
	}
}
