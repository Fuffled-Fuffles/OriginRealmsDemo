package com.fuffles.demo;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.NotePlayEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.fuffles.demo.block.CustomBlock;
import com.fuffles.demo.block.PlacerStyle;
import com.fuffles.demo.block.ToolType;
import com.fuffles.demo.item.CustomBlockItem;
import com.fuffles.demo.item.CustomItem;
import com.fuffles.demo.util.CustomUtil;

public class DemoEventHandler implements Listener
{
	private final JavaPlugin plugin;
	
	public DemoEventHandler(JavaPlugin plugin)
	{
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		ItemStack handItem = event.getPlayer().getInventory().getItem(event.getHand());
		CustomItem customHandItem = CustomUtil.getCustomItem(handItem);
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK))
		{
			if (handItem != null && !handItem.getType().equals(Material.AIR))
			{
				PlayerInventory inventory = event.getPlayer().getInventory();
				boolean isInMain = inventory.getItem(EquipmentSlot.HAND).equals(handItem);
				boolean isInOff = inventory.getItem(EquipmentSlot.OFF_HAND).equals(handItem);
				EquipmentSlot usedHand = isInMain && isInOff ? EquipmentSlot.HAND : isInMain ? EquipmentSlot.HAND : EquipmentSlot.OFF_HAND;
				if (event.getHand().equals(usedHand))
				{
					if (ToolType.fromItemStack(handItem).equals(ToolType.AXE))
					{
						CustomBlock cBlock = CustomUtil.getCustomBlock(event.getClickedBlock());
						if (cBlock != null && cBlock.getBlockMeta().stripable)
						{
							event.getPlayer().getWorld().playSound(event.getClickedBlock().getLocation(), Sound.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1F, 1F);
							event.getPlayer().getWorld().getBlockAt(event.getClickedBlock().getLocation()).setBlockData(cBlock.getBlockMeta().stripableOutcome.getPlaceBlock());
							CustomUtil.doUse(event.getPlayer(), event.getHand(), true);
						}
					}
					else if (customHandItem != null)
					{
						if (customHandItem instanceof CustomBlockItem)
						{
							CustomBlockItem customBlockPlacer = (CustomBlockItem)customHandItem;
							World world = event.getPlayer().getWorld();
							Location loc = event.getClickedBlock().getLocation().add(event.getBlockFace().getDirection());
							if (CustomUtil.canPlaceAt(world, loc))
							{
								Bukkit.getScheduler().scheduleSyncDelayedTask(this.plugin, new Runnable() {
									@Override
									public void run() 
									{
										PlacerStyle style = customBlockPlacer.getPlacementStyle();
										CustomBlock cBlock = style.getResultFor(style.getTestFunction().apply(event.getBlockFace()));
										if (cBlock != null)
										{
											world.getBlockAt(loc).setBlockData(cBlock.getPlaceBlock());
										}
										else
										{
											Demo.LOG.info("Test Function returned null! ");
										}
										world.playSound(loc, cBlock.getBlockMeta().placeSound, SoundCategory.BLOCKS, 1F, 0.8F);
										CustomUtil.doUse(event.getPlayer(), usedHand, true);
									}
								});
							}
						}
						else if (customHandItem.getClickFunction() != null)
						{
							customHandItem.getClickFunction().accept(event);
							CustomUtil.doUse(event.getPlayer(), usedHand, true);
						}
					}
				}
			}
		}
		if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getClickedBlock().getType().equals(Material.NOTE_BLOCK))
		{
			if (CustomUtil.getCustomBlock(event.getClickedBlock()) != null)
			{
				event.setCancelled(true);
			}
		}
		/*if (handItem.getType().equals(Material.PAPER) && handItem.hasItemMeta() && handItem.getItemMeta().hasCustomModelData() && handItem.getItemMeta().getCustomModelData() == 1000)
				{
					MinecraftServer server = ((CraftServer)Bukkit.getServer()).getServer();
					WorldServer world = ((CraftWorld)Bukkit.getWorld(event.getPlayer().getWorld().getName())).getHandle();
					Location loc = event.getClickedBlock().getLocation().add(event.getBlockFace().getDirection());
					EntityCrimsonSporent sporent = new EntityCrimsonSporent(server, world);
					PacketUtil.spawnPlayerEntity(sporent, loc);
					
					ItemStack headAcc = new ItemStack(Material.PAPER);
					ItemMeta headMeta = headAcc.getItemMeta();
					headMeta.setCustomModelData(1003);
					headAcc.setItemMeta(headMeta);
					sporent.getBukkitEntity().getEquipment().setHelmet(headAcc);
					ItemStack armAcc = new ItemStack(Material.PAPER);
					ItemMeta armMeta = armAcc.getItemMeta();
					armMeta.setCustomModelData(1001);
					armAcc.setItemMeta(armMeta);
					sporent.getBukkitEntity().getEquipment().setItemInOffHand(armAcc);
					
					PacketUtil.updateVisibleEquipment(sporent);
					
					sporent.getDataWatcher().set(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte)127);
					PacketUtil.updateSkinLayers(sporent, this.plugin);
					
					Scoreboard s = Bukkit.getScoreboardManager().getMainScoreboard();
					Team no_names = s.getTeam("no_nametags");
					if (no_names == null)
					{
						no_names = s.registerNewTeam("no_nametags");
						no_names.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.NEVER);
					}
					
					no_names.addEntry(sporent.getName());
				}*/
	}
	
	@EventHandler
	public void onBlockPhysics(BlockPhysicsEvent event)
	{
		Block up = event.getBlock().getLocation().add(0, 1, 0).getBlock();
		if (up.getType() == Material.NOTE_BLOCK)
		{
			up.getState().update(true, true);
			doUpdateUpper(up.getLocation());
			event.setCancelled(true);
		}
		
		if (event.getBlock().getType().equals(Material.NOTE_BLOCK))
		{
			event.setCancelled(true);
			event.getBlock().getState().update(true);
		}
	}
	
	private void doUpdateUpper(Location loc)
	{
		Block up = loc.add(0, 1, 0).getBlock();
		if (up.getType() == Material.NOTE_BLOCK)
		{
			up.getState().update(true, true);
			doUpdateUpper(up.getLocation());
		}
	}
	
	@EventHandler
	public void onNotePlay(NotePlayEvent event)
	{
		CustomBlock cBlock = CustomUtil.getCustomBlock(event.getBlock());
		if (cBlock != null)
		{
			event.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent event)
	{
		if (CustomUtil.isWoodenBlock(event.getBlock(), "PLACE"))
		{
			event.getPlayer().getWorld().playSound(event.getBlock().getLocation(), "minecraft:block.wood.original.place", SoundCategory.BLOCKS, 1F, 0.8F);	
		}
	}
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent event)
	{
		CustomBlock cBlock = CustomUtil.getCustomBlock(event.getBlock());
		if (cBlock != null)
		{
			if (!event.getPlayer().getGameMode().equals(GameMode.CREATIVE))
			{
				event.setCancelled(true);
			}
			else
			{
				event.getPlayer().getWorld().playSound(event.getBlock().getLocation(), cBlock.getBlockMeta().breakSound, SoundCategory.BLOCKS, 1F, 0.8F);
			}
		}
		else if (CustomUtil.isWoodenBlock(event.getBlock(), "BREAK"))
		{
			event.getPlayer().getWorld().playSound(event.getBlock().getLocation(), "minecraft:block.wood.original.break", SoundCategory.BLOCKS, 1F, 0.8F);	
		}
	}
	
	//Enables custom breaking for custom blocks
	@EventHandler
	public void onBlockDamage(BlockDamageEvent event)
	{
		boolean flag = true;
		if (event.getBlock().getType().equals(Material.NOTE_BLOCK) && CustomUtil.getCustomBlock(event.getBlock()) != null)
		{
			flag = false;
			event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, 99999 * 20, -1, true, false, false));
		}
		if (flag && event.getPlayer().hasPotionEffect(PotionEffectType.SLOW_DIGGING))
		{
			event.getPlayer().removePotionEffect(PotionEffectType.SLOW_DIGGING);
		}
	}
}
