package com.fuffles.demo.lib;

import java.util.Arrays;
import java.util.List;

import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.player.PlayerInteractEvent;

import com.fuffles.demo.entity.EntityLionBabeh;
import com.fuffles.demo.entity.EntityLionFemale;
import com.fuffles.demo.entity.EntityLionMale;
import com.fuffles.demo.item.CustomItem;

public class ItemLib 
{
	//Abandonned, Im not rewriting the entire EntityInsentient class and components for the PlayerEntity, especially for just a demo
	//ProfileID as keepsake
	/*
	private static final GameProfile CrimsonSporentProfile()
	{
		GameProfile sporentProfile = new GameProfile(UUID.fromString("92057686-4c48-49c3-94b4-6c0ac1a1dba6"), "Dummy");
		sporentProfile.getProperties().put("textures", new Property("textures", "ewogICJ0aW1lc3RhbXAiIDogMTYxNjY0MjE0MjQ2NCwKICAicHJvZmlsZUlkIiA6ICI5MjA1NzY4NjRjNDg0OWMzOTRiNDZjMGFjMWExZGJhNiIsCiAgInByb2ZpbGVOYW1lIiA6ICJGdWZmbGVzXyIsCiAgInNpZ25hdHVyZVJlcXVpcmVkIiA6IHRydWUsCiAgInRleHR1cmVzIiA6IHsKICAgICJTS0lOIiA6IHsKICAgICAgInVybCIgOiAiaHR0cDovL3RleHR1cmVzLm1pbmVjcmFmdC5uZXQvdGV4dHVyZS9iYThkNjUwMGRjZTdmZjA3YTUxNTk4MjJiMmVlMjBmYmY4NTI2MWExNTVhYzQ1MjhjMzllN2ZkZGM4ZWQ1NzFiIgogICAgfQogIH0KfQ==", "m8dfme/BupsTGFI4iLHaGoogTx53Q4etOk90VWJOxhjXZa5AeQKBgn7PxGqg6TelF6YZELenm8n8GfZbnO2/T91kqlC+nMDWq3W/idXLt+JiWY5kvgugGFKuP+pb0E7Qb06c4gyRriTDpW18lAYfrUogL1wYAgIEJXFavtOKpfwEIfxGSLqXv2Nx7gEUQ51fSEpks3yJLCjPBTvbAyi6j65cpFdgbu9LtmULqKEZCqmGT1dF1hCEjh2KfGJGK2sWtJZwrzvbUX7fy8irumnBumutd812PVAudxIRJwI8CNtKTGJOEMqQ8MF+D8uTaQnJIuBCtwzF3+bIYAvuvgOI/fosY4y+NCdzzAJqf4m8sQmGUIOuII63KNh7/bhs7oGWBGCc4zB5Q9YHtzCMZEsoxOIeV0OrjC5OTiP4zW5X0tLkBVEcDQhjMypQP/nckePTyFrS5g5pmKPamVVg+qfNJZEHQbvy3g03vomeHPrKy9KsnWLBynmjahVH0Nx1VaDBwOuvcsG+FghOjxXA7eU5adm9MBrz9mdbjPNRGdsScEG2v0KgvkpboD63F2YUBS/1xZ2EfOQ2Tdsfy1W1H4QYZVC5aSO4BkNmgO9WVyPLFy5DKhxzi+U8KLNzq2T2ORyLKUNvNH6sqLFnc5BRBAe8cnIs+diB4Evlc0NUd9wFSKM="));
		return sporentProfile;
	}
	*/
	//public static final CustomItem SPAWNEGG_SPORENT = new CustomItem("Spawn Crimson Sporent", 1000);
	
	/* To-Do
	 * - Fix Targetting AI
	 * - Make Dragon Blood Logs
	 * - Add Stripable stuffs
	 * - Should be done?
	 */
	public static final CustomItem SPAWNEGG_LION = new CustomItem("Spawn Lion", 11).setOnClickFunction((PlayerInteractEvent event) -> {
		EntityLionMale lion = new EntityLionMale(event.getClickedBlock().getLocation().add(event.getBlockFace().getDirection()).add(0.5D, 0, 0.5D), SpawnReason.CUSTOM);
		((CraftWorld)event.getPlayer().getWorld()).getHandle().addEntity(lion);
	});
	public static final CustomItem SPAWNEGG_LIONESS = new CustomItem("Spawn Lioness", 12).setOnClickFunction((PlayerInteractEvent event) -> {
		EntityLionFemale lioness = new EntityLionFemale(event.getClickedBlock().getLocation().add(event.getBlockFace().getDirection()).add(0.5D, 0, 0.5D), null);
		((CraftWorld)event.getPlayer().getWorld()).getHandle().addEntity(lioness);
	});
	public static final CustomItem SPAWNEGG_CUB = new CustomItem("Spawn Lion Cub", 13).setOnClickFunction((PlayerInteractEvent event) -> {
		EntityLionBabeh cub = new EntityLionBabeh(event.getClickedBlock().getLocation().add(event.getBlockFace().getDirection()).add(0.5D, 0, 0.5D));
		((CraftWorld)event.getPlayer().getWorld()).getHandle().addEntity(cub);
	});
	
	//Im lazy, not feeling like fancifying the lore for the demo
	public static final CustomItem SOUL_OF_LION = new CustomItem("Soul of the Pride", Arrays.asList("Summons a Pride of Lions"), 14).setOnClickFunction((PlayerInteractEvent event) -> {
		EntityLionMale leader = new EntityLionMale(event.getClickedBlock().getLocation().add(event.getBlockFace().getDirection()).add(0.5D, 0, 0.5D), SpawnReason.DEFAULT);
		((CraftWorld)event.getPlayer().getWorld()).getHandle().addEntity(leader);
	});
	
	public static final List<CustomItem> ITEMS = Arrays.asList(SPAWNEGG_LION, SPAWNEGG_LIONESS, SPAWNEGG_CUB, SOUL_OF_LION);
}
