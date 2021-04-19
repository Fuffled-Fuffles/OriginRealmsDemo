package com.fuffles.demo.entity.pathfinder;

import java.util.List;

import com.fuffles.demo.entity.EntityLionBabeh;
import com.fuffles.demo.entity.EntityLionFemale;

public class GoalCubFollowLioness extends Goal 
{
	private final EntityLionBabeh user;
	private EntityLionFemale toFollow;
	private final double speedMod;
	private int delay;

	public GoalCubFollowLioness(EntityLionBabeh user, double speedMod) 
	{
		this.user = user;
		this.speedMod = speedMod;
	}

	@Override
	public boolean canStart() 
	{
		List<EntityLionFemale> list = this.user.world.a(EntityLionFemale.class, this.user.getBoundingBox().grow(8.0D, 4.0D, 8.0D));
		EntityLionFemale newToFollow = null;
		double bestDistToFollow = Double.MAX_VALUE;

		for (EntityLionFemale living : list)
		{
			double distToTarget = this.user.h(living);
			if (!(distToTarget > bestDistToFollow))
			{
				bestDistToFollow = distToTarget;
				newToFollow = living;
			}
		}
		
		if (newToFollow == null)
		{
			return false;
		}
		else if (bestDistToFollow < 9.0D)
		{
			return false;
		}
		else
		{
			this.toFollow = newToFollow;
			this.user.setMommy(this.toFollow);
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
			return distance >= 9D && distance <= 256D;
		}
	}

	@Override
	public void onStart() 
	{
		this.delay = 0;
	}

	@Override
	public void onStop() 
	{
		this.toFollow = null;
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