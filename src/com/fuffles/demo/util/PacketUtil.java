package com.fuffles.demo.util;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.plugin.java.JavaPlugin;

import com.mojang.datafixers.util.Pair;

import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.EnumItemSlot;
import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.Packet;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_16_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_16_R3.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_16_R3.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_16_R3.PlayerConnection;

public class PacketUtil 
{
	@SafeVarargs
	private static void sendPackets(Supplier<Packet<?>>... packets)
	{
		for (Player p : Bukkit.getOnlinePlayers())
		{
			PlayerConnection c = ((CraftPlayer)p).getHandle().playerConnection;
			for (Supplier<Packet<?>> packet : packets)
			{
				c.sendPacket(packet.get());
			}
		}
	}
	
	public static void spawnPlayerEntity(EntityPlayer ent, Location spawnLocation)
	{
		ent.setLocation(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(), spawnLocation.getYaw(), spawnLocation.getPitch());
		
		sendPackets(() -> {
			return new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ent);
		},
		() -> {
			return new PacketPlayOutNamedEntitySpawn(ent);
		},
		() -> {
			return new PacketPlayOutEntityHeadRotation(ent, (byte)(ent.yaw * 256 / 360));
		});
	}
	
	public static void updateSkinLayers(EntityPlayer ent, JavaPlugin plugin)
	{
		sendPackets(() -> {
			return new PacketPlayOutEntityMetadata(ent.getId(), ent.getDataWatcher(), true);
		});
	}
	
	public static void updateVisibleEquipment(EntityHuman ent)
	{
		EntityEquipment eq = ent.getBukkitEntity().getEquipment();
		List<Pair<EnumItemSlot, ItemStack>> equipment = Arrays.asList(
			Pair.of(EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(eq.getHelmet())),
			Pair.of(EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(eq.getChestplate())),
			Pair.of(EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(eq.getLeggings())),
			Pair.of(EnumItemSlot.FEET, CraftItemStack.asNMSCopy(eq.getBoots())),
			Pair.of(EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(eq.getItemInMainHand())),
			Pair.of(EnumItemSlot.OFFHAND, CraftItemStack.asNMSCopy(eq.getItemInOffHand()))
		);
		sendPackets(() -> {
			return new PacketPlayOutEntityEquipment(ent.getId(), equipment);
		});
	}
}
