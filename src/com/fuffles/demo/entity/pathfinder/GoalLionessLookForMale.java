package com.fuffles.demo.entity.pathfinder;

import java.util.List;

import com.fuffles.demo.entity.EntityLionFemale;
import com.fuffles.demo.entity.EntityLionMale;


public class GoalLionessLookForMale extends Goal
{
	private final EntityLionFemale user;
	private final double speedMod;
	private EntityLionMale toAttempt;
	private boolean isClose = false;
	private boolean shouldStop = false;
	protected int delay;
	
	public GoalLionessLookForMale(EntityLionFemale user, double speedMod)
	{
		this.user = user;
		this.speedMod = speedMod;
	}
	
	@Override
	public boolean canStart() 
	{
		if (this.user.hasLeader())
		{
			return false;
		}
		else
		{
			List<EntityLionMale> list = this.user.world.a(EntityLionMale.class, this.user.getBoundingBox().grow(24.0D, 4.0D, 24.0D));
			EntityLionMale bestTemptation = null;
			double bestDist = Double.MAX_VALUE;
			for (EntityLionMale male : list)
			{
				if (!this.user.getRejectedByList().contains(male.getUniqueID()) && male.getPending() == null)
				{
					double distToTarget = this.user.h(male);
					if (!(distToTarget > bestDist))
					{
						bestDist = distToTarget;
						bestTemptation = male;
					}
				}
			}
			
			if (bestTemptation == null)
			{
				return false;
			}
			else
			{
				this.toAttempt = bestTemptation;
				return true;
			}
		}
	}
	
	@Override
	public boolean canContinue() 
	{
		if (!this.toAttempt.isAlive() || this.shouldStop)
		{
			return false;
		}
		else
		{
			return !this.user.getRejectedByList().contains(this.toAttempt.getUniqueID()) && this.toAttempt.getPending() == null && !this.user.hasLeader();	
		}
	}
	
	@Override
	public void onStart() 
	{
		this.delay = 0;
		this.shouldStop = false;
	}
	
	@Override
	public void onStop() 
	{
		this.toAttempt = null;
		this.shouldStop = false;
	}
	
	@Override
	public void onTick() 
	{
		double distance = this.user.h(this.toAttempt);
		this.isClose = distance <= 18D;
		if (this.isClose && !this.user.getRejectedByList().contains(this.toAttempt.getUniqueID()))
		{
			this.user.getNavigation().o();
			this.user.disableCore();
			this.user.getControllerLook().a(this.toAttempt, this.user.ep(), this.user.O());
			if (!this.shouldStop)
			{
				this.shouldStop = true;
				this.user.setJoining();
				this.toAttempt.setPending(this.user);
				this.toAttempt.doLook = true;
				//I have no fucking clue why this crashes the lion male every single god damn time, where as the female never crashes, literally same fn
				//this.toAttempt.disableCore();
			}
		}
		else if (!this.isClose && --this.delay <= 0)
		{
			this.delay = 10;
			this.user.getNavigation().a(this.toAttempt, this.speedMod);
		}
	}
}
