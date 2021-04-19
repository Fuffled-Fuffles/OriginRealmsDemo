package com.fuffles.demo.entity.pathfinder;

import javax.annotation.Nullable;

import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.GenericAttributes;
import net.minecraft.server.v1_16_R3.MathHelper;
import net.minecraft.server.v1_16_R3.PathEntity;
import net.minecraft.server.v1_16_R3.PathPoint;
import net.minecraft.server.v1_16_R3.PathfinderTargetCondition;
import net.minecraft.server.v1_16_R3.ScoreboardTeamBase;

//Readable Vanilla Copy
public abstract class TargetGoal extends Goal
{
	protected final EntityInsentient user;
	protected final boolean mustSee;
	private final boolean mustReach;
	private int reachCache;
	private int reachCacheTime;
	private int unseenTicks;
	protected EntityLiving target;
	protected int unseenMemoryTicks = 60;
	
	public TargetGoal(EntityInsentient user, boolean mustSee, boolean mustReach)
	{
		this.user = user;
		this.mustSee = mustSee;
		this.mustReach = mustReach;
	}
	
	@Override
	public boolean canContinue()
	{
		EntityLiving target = this.user.getGoalTarget();
		if (target == null)
		{
			target = this.target;
		}
		
		if (target == null || !target.isAlive())
		{
			return false;
		}
		else
		{
			ScoreboardTeamBase user_team = this.user.getScoreboardTeam();
	        ScoreboardTeamBase target_team = target.getScoreboardTeam();
	         if (user_team != null && target_team == user_team) 
	         {
	            return false;
	         } 
	         else 
	         {
	            double followDist = this.getUserFollowDistance();
	            if (this.user.h(target) > followDist * followDist) 
	            {
	               return false;
	            } 
	            else 
	            {
	               if (this.mustSee) 
	               {
	                  if (this.user.getEntitySenses().a(target)) 
	                  {
	                     this.unseenTicks = 0;
	                  } 
	                  else if (++this.unseenTicks > this.unseenMemoryTicks) 
	                  {
	                     return false;
	                  }
	               }

	               if (target instanceof EntityHuman && ((EntityHuman)target).abilities.isInvulnerable) 
	               {
	                  return false;
	               } 
	               else 
	               {
	                  this.user.setGoalTarget(target);
	                  return true;
	               }
	            }
	         }
		}
	}
	
	@Override
	public void onStart() 
	{
		this.reachCache = 0;
		this.reachCacheTime = 0;
		this.unseenTicks = 0;
	}
	
	@Override
	public void onStop() 
	{
		this.user.setGoalTarget(null, TargetReason.FORGOT_TARGET, true);
	    this.target = null;
	}
	
	protected double getUserFollowDistance()
	{
		return this.user.b(GenericAttributes.FOLLOW_RANGE);
	}
	
	private boolean canReach(EntityLiving target) 
	{
		this.reachCacheTime = 10 + this.user.getRandom().nextInt(5);
		PathEntity path = this.user.getNavigation().a(target, 0);
		if (path == null) 
		{
			return false;
	    } 
		else 
		{
			PathPoint pathpoint = path.d();
	        if (pathpoint == null) 
	        {
	        	return false;
	        } 
	        else 
	        {
	            int i = pathpoint.a - MathHelper.floor(target.locX());
	            int j = pathpoint.c - MathHelper.floor(target.locZ());
	            return (double)(i * i + j * j) <= 2.25D;
	        }
		}
	}
	
	protected boolean canAttack(@Nullable EntityLiving target, PathfinderTargetCondition condition) 
	{
		if (target == null) 
		{
			return false;
		} 
		else if (!condition.a(this.user, target)) 
		{
			return false;
		} 
		else if (!this.user.a(target.getChunkCoordinates())) 
		{
			return false;
		} 
		else 
		{
			if (this.mustReach) 
			{
				if (--this.reachCacheTime <= 0) 
				{
	               this.reachCache = 0;
	            }

	            if (this.reachCache == 0) 
	            {
	               this.reachCache = this.canReach(target) ? 1 : 2;
	            }

	            if (this.reachCache == 2) 
	            {
	               return false;
	            }
			}
			return true;
		}
	}
	
	public TargetGoal setUnseenMemoryTicks(int unseenMemTicks) 
	{
		this.unseenMemoryTicks = unseenMemTicks;
		return this;
	}
}
