package com.fuffles.demo.entity.pathfinder;

import com.fuffles.demo.entity.EntityLionFemale;
import com.fuffles.demo.entity.EntityLionMale;

import net.minecraft.server.v1_16_R3.EntityInsentient;

public class GoalLionessFollowLeader extends Goal
{
	private final EntityInsentient user;
	private EntityLionMale toFollow;
	private final double speedMod;
	private int delay;

	public GoalLionessFollowLeader(EntityLionFemale user, EntityLionMale toFollow, double speedMod) 
	{
		this.user = user;
		this.toFollow = toFollow;
		this.speedMod = speedMod;
	}
	
	@Override
	public boolean canStart() 
	{
		double distance = this.user.h(this.toFollow);
		if (this.toFollow == null || !this.toFollow.isAlive())
		{
			return false;
		}
		else if (distance < 16D)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	@Override
	public boolean canContinue() 
	{
		if (!this.toFollow.isAlive()) 
		{
			return false;
		} 
		else 
		{
			double distance = this.user.h(this.toFollow);
			return distance >= 16D && distance <= 256D;
		}
	}

	@Override
	public void onStart() 
	{
		this.delay = 0;
	}
	
	@Override
	public void onTick() 
	{
		if (--this.delay <= 0) 
		{
			this.delay = 10;
			this.user.getNavigation().a(this.toFollow, this.speedMod);
		}
	}
}