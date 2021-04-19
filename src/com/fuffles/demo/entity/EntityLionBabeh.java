package com.fuffles.demo.entity;

import javax.annotation.Nullable;

import org.bukkit.Location;

import com.fuffles.demo.entity.pathfinder.GoalAvoidWithoutLioness;
import com.fuffles.demo.entity.pathfinder.GoalCubFollowLioness;

import net.minecraft.server.v1_16_R3.AttributeProvider.Builder;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.EntityAgeable;
import net.minecraft.server.v1_16_R3.EntityCat;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.GenericAttributes;
import net.minecraft.server.v1_16_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_16_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_16_R3.PathfinderGoalRandomStrollLand;
import net.minecraft.server.v1_16_R3.WorldServer;

public class EntityLionBabeh extends EntityAbstractCat
{
	/* Follows mommy
	 * if no mommy, runs away from anything but lions
	 */
	private EntityLionFemale mommy;
	
	public EntityLionBabeh(Location loc) 
	{
		super(loc);
		this.setCustomName(new ChatComponentText("Cub"));
		this.setCustomNameVisible(true);
		this.setBaby(true);
		this.setCatType(99);
	}

	@Override
	protected void initPathfinder()
	{
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(1, new GoalCubFollowLioness(this, 1.33D));
		this.goalSelector.a(2, new GoalAvoidWithoutLioness(this, 1D, 1.33D));
		this.goalSelector.a(3, new PathfinderGoalRandomStrollLand(this, 1D));
		this.goalSelector.a(10, new PathfinderGoalRandomLookaround(this));
	}
	
	public static Builder eK() 
	{
		return EntityInsentient.p().a(GenericAttributes.MAX_HEALTH, 10.0D).a(GenericAttributes.MOVEMENT_SPEED, 0.3D);
	}
	
	public boolean hasMommy()
	{
		return this.mommy != null;
	}
	
	public void setMommy(EntityLionFemale mom)
	{
		this.mommy = mom;
	}
	
	@Override
	public void setCatType(int i)
	{
		super.setCatType(6);
	}
	
	@Override
	public void mobTick()
	{
		if (this.getControllerMove().b()) 
		{
			double speed = this.getControllerMove().c();
			if (speed == 1.33D)
			{
				this.setSprinting(true);
			}
			else
			{
				this.setSprinting(false);
			}
		}
		else
		{
			this.setSprinting(false);
		}
	}
	
	@Override
	public void inactiveTick() 
	{
		super.inactiveTick();
		if (!this.isBaby())
		{
			this.setBaby(true);
		}
		if (!this.ageLocked)
		{
			this.ageLocked = true;
		}
		if (this.mommy != null && !this.mommy.isAlive())
		{
			this.mommy = null;
		}
	}
	
	@Override
	public @Nullable EntityCat createChild(WorldServer var0, EntityAgeable var1) 
	{
		throw new IllegalArgumentException("Is Baby!");
	}
}
