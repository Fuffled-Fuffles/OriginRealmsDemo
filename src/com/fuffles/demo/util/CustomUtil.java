package com.fuffles.demo.util;

import java.util.Arrays;
import java.util.List;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundGroup;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffectType;

import com.fuffles.demo.block.CustomBlock;
import com.fuffles.demo.block.ToolTier;
import com.fuffles.demo.block.ToolType;
import com.fuffles.demo.item.CustomItem;
import com.fuffles.demo.lib.BlockLib;
import com.fuffles.demo.lib.ItemLib;

public class CustomUtil 
{		
	public static CustomBlock getCustomBlock(Block block)
	{
		if (block.getType().equals(Material.NOTE_BLOCK))
		{
			for (CustomBlock custom_block : BlockLib.BLOCKS)
			{
				if (custom_block.getPlaceBlock().equals(block.getBlockData()))
				{
					return custom_block;
				}
			}
		}
		return null;
	}
	
	public static CustomBlock getCustomBlockFromItem(ItemStack stack)
	{
		for (CustomBlock custom_block : BlockLib.BLOCKS)
		{
			if (custom_block.getPlacer().getItem().getType().equals(stack.getType()) && custom_block.getPlacer().getItem().getItemMeta().getCustomModelData() == stack.getItemMeta().getCustomModelData())
			{
				return custom_block;
			}
		}
		return null;
	}
	
	public static boolean canPlaceAt(World world, Location loc)
	{
		List<Material> valids = Arrays.asList(
			Material.AIR, 
			Material.WATER, 
			Material.LAVA, 
			Material.GRASS, 
			Material.FERN, 
			Material.SEAGRASS, 
			Material.TALL_GRASS, 
			Material.LARGE_FERN,
			Material.TALL_SEAGRASS,
			Material.VINE
		);
		
		Block block = world.getBlockAt(loc);
		if (valids.contains(block.getType()) && world.getNearbyEntities(loc, 0, 1, 0).isEmpty())
		{
			return true;
		}
		return false;
	}
	
	public static boolean isWoodenBlock(Block block, String key)
	{
		SoundGroup group = block.getBlockData().getSoundGroup();
		if (group.getBreakSound().equals(Sound.BLOCK_WOOD_BREAK) && key.toUpperCase().equals("BREAK"))
		{
			return true;
		}
		else if (group.getHitSound().equals(Sound.BLOCK_WOOD_HIT) && key.toUpperCase().equals("HIT"))
		{
			return true;
		}
		else if (group.getPlaceSound().equals(Sound.BLOCK_WOOD_PLACE) && key.toUpperCase().equals("PLACE"))
		{
			return true;
		}
		return false;
	}
	
	//
	
	public static CustomItem getCustomItem(ItemStack stack)
	{
		if (stack == null || stack.getType().equals(Material.AIR))
		{
			return null;
		}
		if (stack.getItemMeta().hasCustomModelData())
		{
			for (CustomItem custom_item : ItemLib.ITEMS)
			{
				if (custom_item.getItem().getItemMeta().getCustomModelData() == stack.getItemMeta().getCustomModelData())
				{
					return custom_item;
				}
			}
			for (CustomItem custom_block_item : BlockLib.BLOCK_PLACERS)
			{
				if (custom_block_item.getItem().getItemMeta().getCustomModelData() == stack.getItemMeta().getCustomModelData())
				{
					return custom_block_item;
				}
			}
		}
		return null;
	}
	
	@SuppressWarnings("deprecation")
	public static float getDigSpeed(Player player, CustomBlock block)
	{
		ItemStack heldItem = player.getInventory().getItem(EquipmentSlot.HAND);
		ToolTier tier = ToolTier.fromItemStack(heldItem);
		float base = tier.speed;
		
		if (heldItem.containsEnchantment(Enchantment.DIG_SPEED))
		{
			float level = heldItem.getEnchantmentLevel(Enchantment.DIG_SPEED);
			base += level * level + 1F;
		}
		
		if (player.hasPotionEffect(PotionEffectType.FAST_DIGGING))
		{
			float level = player.getPotionEffect(PotionEffectType.FAST_DIGGING).getAmplifier() + 1;
			base *= 1F + level * 0.2F;
		}
		
		if (player.isInWater() && !player.getInventory().getHelmet().containsEnchantment(Enchantment.WATER_WORKER))
		{
			base = base / 5F;
		}
		
		if (!player.isOnGround())
		{
			base = base / 5f;
		}
		
		float canBreak = block.canBreakWith(ToolType.fromItemStack(heldItem), tier) ? 30 : 100;
		float hardness = block.getBlockMeta().hardness; 
		return base / hardness / canBreak;
	}
	
	public static void doUse(Player player, EquipmentSlot usedHand, boolean consume)
	{
		if (usedHand.equals(EquipmentSlot.HAND))
		{
			player.swingMainHand();
		}
		else
		{
			player.swingOffHand();
		}
		if (!player.getGameMode().equals(GameMode.CREATIVE) && consume)
		{
			ItemStack usedItem = player.getInventory().getItem(usedHand);
			if (!usedItem.getType().equals(Material.PAPER))
			{
				if (usedItem.getItemMeta() instanceof Damageable)
				{
					ItemMeta meta = usedItem.getItemMeta();
					((Damageable)meta).setDamage(((Damageable)meta).getDamage() + 1);
					usedItem.setItemMeta(meta);	
				}    
			}
			else
			{
				if (usedItem.getAmount() > 1)
				{
					usedItem.setAmount(usedItem.getAmount() - 1);
				}
				else
				{
					player.getInventory().setItem(usedHand, new ItemStack(Material.AIR));
				}
			}
		}
	}
}
