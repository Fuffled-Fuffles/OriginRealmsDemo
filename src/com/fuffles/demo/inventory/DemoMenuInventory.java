package com.fuffles.demo.inventory;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.fuffles.demo.item.CustomItem;
import com.fuffles.demo.lib.BlockLib;
import com.fuffles.demo.lib.ItemLib;
import com.fuffles.demo.util.MathUtil;

public class DemoMenuInventory implements Listener
{
	private int current = 0;
	private final Inventory menu;
	
	public DemoMenuInventory()
	{
		this.menu = Bukkit.createInventory(null, 9*6, "Demo Menu");
		this.addRim();
		this.menu.setItem(49, this.filter(this.current));
		this.clearContents(this.menu);
		this.addContents(this.menu);
	}
	
	private void addRim()
	{
		Consumer<Integer> addRim = (Integer i) -> {
			this.menu.setItem(i, this.rim());
		};
		MathUtil.forAllBut(0, 9, 10, 10, addRim);
		MathUtil.forAllBut(9, 18, 11, 15, addRim);
		MathUtil.forAllBut(18, 27, 20, 24, addRim);
		MathUtil.forAllBut(27, 36, 29, 33, addRim);
		MathUtil.forAllBut(36, 45, 38, 42, addRim);
		MathUtil.forAllBut(45, 54, 49, 49, addRim);
	}
	
	private void clearContents(Inventory inventory)
	{
		Consumer<Integer> addEmpty = (Integer i) -> {
			inventory.setItem(i, new ItemStack(Material.AIR));
		};
	
		MathUtil.forAllBut(11, 16, 0, 0, addEmpty);
		MathUtil.forAllBut(20, 25, 0, 0, addEmpty);
		MathUtil.forAllBut(29, 34, 0, 0, addEmpty);
		MathUtil.forAllBut(38, 43, 0, 0, addEmpty);
	}
	
	private void addContents(Inventory inventory)
	{
		for (CustomItem block : BlockLib.BLOCK_PLACERS)
		{
			inventory.addItem(block.getItem());
		}
		for (CustomItem item : ItemLib.ITEMS)
		{
			inventory.addItem(item.getItem());
		}
	}
	
	private ItemStack rim()
	{
		ItemStack stack = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§r");
		stack.setItemMeta(meta);
		return stack;
	}
	
	private ItemStack filter(int i)
	{
		ItemStack stack = new ItemStack(Material.ITEM_FRAME);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName("§r§6Content Filter");
		List<String> lore = Arrays.asList(new String[] {
				"§r",
				"§r§e> None",
		});
		meta.setLore(lore);
		stack.setItemMeta(meta);
		return stack;
	}
	
	public Inventory getInventory()
	{
		return this.menu;
	}
	
	//This is the scrappiest menu ever buuuuuut ya know
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInventoryClick(InventoryClickEvent event)
	{
		if (event.getView() != null && event.getView().getTitle().equals("Demo Menu") && event.getView().getTopInventory() != null && event.getView().getTopInventory().getItem(0).equals(this.rim()))
		{
			Inventory inventory = event.getView().getTopInventory();
			ItemStack stack = event.getCurrentItem();
			if (!event.getView().getBottomInventory().equals(event.getClickedInventory()))
			{
				if (inventory.contains(stack))
				{
					if (!(stack.equals(this.rim()) || stack.getType().equals(this.filter(this.current).getType())))
					{
						if (event.getCursor().getType().equals(Material.AIR))
						{
							if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY))
							{
								ItemStack stack2 = stack.clone();
								stack2.setAmount(stack2.getMaxStackSize());
								event.setCursor(stack2);
							}
							else
							{
								event.setCursor(stack);
							}
						}
					}
					this.clearContents(event.getClickedInventory());
					this.addContents(event.getClickedInventory());
					event.setCancelled(true);
				}
				else if (event.getAction().equals(InventoryAction.NOTHING) || event.getAction().equals(InventoryAction.PLACE_ALL) || event.getAction().equals(InventoryAction.PLACE_SOME) || event.getAction().equals(InventoryAction.PLACE_ONE))
				{
					event.setCursor(new ItemStack(Material.AIR));
					event.setCancelled(true);
				}
			}
			else if (event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY))
			{
				event.setCancelled(true);
			}
		}
	}
}
