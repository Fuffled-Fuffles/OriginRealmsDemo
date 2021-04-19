package com.fuffles.demo;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.SoundCategory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
//import com.comphenix.protocol.wrappers.BlockPosition;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerDigType;
import com.fuffles.demo.block.CustomBlock;
import com.fuffles.demo.block.ToolTier;
import com.fuffles.demo.block.ToolType;
import com.fuffles.demo.util.CustomUtil;

public class BlockDigHandler extends PacketAdapter
{
	private final Demo plugin;
	private Map<Player, Integer> diggers = new HashMap<>();
	
	public BlockDigHandler(final Demo plugin) 
	{
		super(plugin, PacketType.Play.Client.BLOCK_DIG);
		this.plugin = plugin;
	}
	
	@Override
	public void onPacketReceiving(final PacketEvent event) 
	{
		Player player = event.getPlayer();
		if (player != null && player.isOnline() && player.getGameMode().equals(GameMode.SURVIVAL))
		{
			BlockPosition pos = event.getPacket().getBlockPositionModifier().read(0);
			PlayerDigType digType = event.getPacket().getPlayerDigTypes().read(0);
			if (digType.equals(PlayerDigType.START_DESTROY_BLOCK))
			{
				Location loc = pos.toLocation(player.getWorld());
				CustomBlock block = CustomUtil.getCustomBlock(loc.getBlock());
				if (block != null)
				{
					ItemStack handItem = player.getInventory().getItem(EquipmentSlot.HAND);
					float digSpeed = CustomUtil.getDigSpeed(player, block);
					diggers.put(player, Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
						float ticks = 0;
						float progress = 0;
						int current_stage = 0;
						@Override
						public void run() 
						{
							this.ticks++;
							this.progress += digSpeed;
							float next_stage_tresh = (this.current_stage + 1) * 0.1F;
							if (this.ticks % 4 == 0)
							{
								player.playSound(loc, block.getBlockMeta().hitSound, SoundCategory.BLOCKS, 0.25F, 0.5F);
							}
							if (this.progress > next_stage_tresh)
							{
								this.current_stage = (int)Math.floor(this.progress * 10F);
								if (this.current_stage <= 9)
								{
									PacketContainer breakFx = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
									breakFx.getIntegers().write(0, 0).write(1, this.current_stage - 1);
									breakFx.getBlockPositionModifier().write(0, pos);
									ProtocolLibrary.getProtocolManager().broadcastServerPacket(breakFx);
								}
							}
							
							if (progress > 1F)
							{
								//temp
								Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
									@Override
									public void run() 
									{
										PacketContainer breakFx = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
										//-1 is a reset for the block cracks!
										breakFx.getIntegers().write(0, 0).write(1, -1);
										breakFx.getBlockPositionModifier().write(0, pos);
										ProtocolLibrary.getProtocolManager().broadcastServerPacket(breakFx);
									}
								}, 1);
								Bukkit.getScheduler().cancelTask(diggers.remove(player));
								loc.getWorld().playSound(loc, block.getBlockMeta().breakSound, SoundCategory.BLOCKS, 1F, 0.8F);
								block.doParticlePop(loc.getWorld(), loc.getBlock());
								if (block.canBreakWith(ToolType.fromItemStack(handItem), ToolTier.fromItemStack(handItem)))
								{
									if (handItem.containsEnchantment(Enchantment.SILK_TOUCH) && block.getBlockMeta().silkTouchDrops != null)
									{
										for (ItemStack stack : block.getBlockMeta().silkTouchDrops)
										{
											loc.getWorld().dropItemNaturally(loc, stack);
										}
									}
									else if (block.getBlockMeta().normalDrops != null)
									{
										for (ItemStack stack : block.getBlockMeta().normalDrops)
										{
											loc.getWorld().dropItemNaturally(loc, stack);
										}
									}
								}
								if (ToolType.fromItemStack(handItem) != ToolType.NONE)
								{
									if (handItem.getItemMeta() instanceof Damageable)
									{
										ItemMeta meta = handItem.getItemMeta();
										((Damageable)meta).setDamage(((Damageable)meta).getDamage() + 1);
										handItem.setItemMeta(meta);
									}
								}
								loc.getBlock().setType(Material.AIR);
							}
						}
					}, 0, 1));
				}
				else if (CustomUtil.isWoodenBlock(loc.getBlock(), "HIT"))
				{
					diggers.put(player, Bukkit.getScheduler().scheduleSyncRepeatingTask(this.plugin, new Runnable() {
						@Override
						public void run() 
						{
							player.playSound(loc, "minecraft:block.wood.original.hit", SoundCategory.BLOCKS, 0.25F, 0.5F);
						}
					}, 0, 4));
				}
			}
			else if ((digType.equals(PlayerDigType.STOP_DESTROY_BLOCK) || digType.equals(PlayerDigType.ABORT_DESTROY_BLOCK)) && diggers.containsKey(player))
			{
				PacketContainer breakFx = ProtocolLibrary.getProtocolManager().createPacket(PacketType.Play.Server.BLOCK_BREAK_ANIMATION);
				breakFx.getIntegers().write(0, 0).write(1, -1);
				breakFx.getBlockPositionModifier().write(0, pos);
				ProtocolLibrary.getProtocolManager().broadcastServerPacket(breakFx);
				Bukkit.getScheduler().cancelTask(diggers.get(player));
			}
		}
	}
}
