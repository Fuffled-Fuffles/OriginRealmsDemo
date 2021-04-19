package com.fuffles.demo.entity;

import javax.annotation.Nullable;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;

import net.minecraft.server.v1_16_R3.DifficultyDamageScaler;
import net.minecraft.server.v1_16_R3.EntityAgeable;
import net.minecraft.server.v1_16_R3.EntityAnimal;
import net.minecraft.server.v1_16_R3.EntityCat;
import net.minecraft.server.v1_16_R3.EntityTypes;
import net.minecraft.server.v1_16_R3.EnumMobSpawn;
import net.minecraft.server.v1_16_R3.GroupDataEntity;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.WorldAccess;

public abstract class EntityAbstractCat extends EntityCat
{	
	public EntityAbstractCat(Location loc)
	{
		this(loc, true);
	}
	
	protected EntityAbstractCat(Location loc, boolean b) 
	{
		super(EntityTypes.CAT, ((CraftWorld)loc.getWorld()).getHandle());
		this.setPosition(loc.getX(), loc.getY(), loc.getZ());
	}
	
	@Override
	protected void initPathfinder() {}
	
	@Override
	public void mobTick() {}
	
	@Override
	public boolean mate(EntityAnimal other)
	{
		return false;
	}
	
	@Nullable
	public GroupDataEntity prepare(WorldAccess worldaccess, DifficultyDamageScaler difficultydamagescaler, EnumMobSpawn enummobspawn, @Nullable GroupDataEntity groupdataentity, @Nullable NBTTagCompound nbttagcompound) 
	{
		return ((EntityAgeable)this).prepare(worldaccess, difficultydamagescaler, enummobspawn, groupdataentity, nbttagcompound);
	}
}
