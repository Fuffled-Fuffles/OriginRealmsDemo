package com.fuffles.demo.item;

import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CustomItem 
{
	protected final Material item;
	protected final String localizedKey;
	protected final List<String> desc;
	protected final int customModel;
	protected Consumer<PlayerInteractEvent> onClickFunction;
	
	public CustomItem(Material material, String localName, List<String> desc, int customModelId)
	{
		this.item = material;
		this.localizedKey = localName;
		this.desc = desc;
		this.customModel = customModelId;
	}
	
	public CustomItem(String localName, List<String> desc, int customModelId)
	{
		this(Material.PAPER, localName, desc, customModelId);
	}
	
	public CustomItem(Material material, String localName, int customModelId)
	{
		this(material, localName, null, customModelId);
	}
	
	public CustomItem(String localName, int customModelId)
	{
		this(Material.PAPER, localName, null, customModelId);
	}
	
	public CustomItem setOnClickFunction(Consumer<PlayerInteractEvent> function)
	{
		this.onClickFunction = function;
		return this;
	}
	
	public ItemStack getItem()
	{
		ItemStack stack = new ItemStack(this.item);
		ItemMeta meta = stack.getItemMeta();
		meta.setCustomModelData(this.customModel);
		meta.setDisplayName("§r" + this.localizedKey);
		if (this.desc != null)
		{
			meta.setLore(this.desc);
		}
		stack.setItemMeta(meta);
		return stack;
	}
	
	public Consumer<PlayerInteractEvent> getClickFunction()
	{
		return this.onClickFunction;
	}
}
