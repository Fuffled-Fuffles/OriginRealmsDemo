package com.fuffles.demo.entity.pathfinder;

import java.util.EnumSet;

import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import com.fuffles.demo.entity.EntityLionFemale;
import com.fuffles.demo.entity.EntityLionMale;

import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.PathfinderTargetCondition;

public class TargetGoalByHurtAndAlertPack extends TargetGoal
{
	private static final PathfinderTargetCondition HURT_BY_TARGETTING = (new PathfinderTargetCondition()).c().e();
	private final boolean isUserMale;
	private int timestamp;
	
	public TargetGoalByHurtAndAlertPack(EntityLionFemale user) 
	{
		super(user, true, true);
		this.isUserMale = false;
		this.setFlags(EnumSet.of(Type.TARGET));
	}
	
	public TargetGoalByHurtAndAlertPack(EntityLionMale user)
	{
		super(user, true, true);
		this.isUserMale = true;
		this.setFlags(EnumSet.of(Type.TARGET));
	}

	@Override
	public boolean canStart() 
	{
		int i = this.user.da();
		EntityLiving entityliving = this.user.getLastDamager();
		if (i != this.timestamp && entityliving != null) 
		{
			return this.canAttack(entityliving, HURT_BY_TARGETTING);
		}
		return false;
	}
	
	@Override
	public void onStart() 
	{
		this.user.setGoalTarget(this.user.getLastDamager(), TargetReason.TARGET_ATTACKED_ENTITY, true);
		this.target = this.user.getGoalTarget();
		this.timestamp = this.user.da();
		this.unseenMemoryTicks = 300;
		if (this.isUserMale)
		{
			EntityLionMale lion = (EntityLionMale)this.user;
			for (EntityLionFemale lioness : lion.getActivePackMembers())
			{
				if (lioness != null && lioness.isAlive())
				{	
					lioness.setGoalTarget(this.target, TargetReason.FOLLOW_LEADER, true);
				}
			}
		}
		else
		{
			EntityLionFemale lioness = (EntityLionFemale)this.user;
			if (lioness.hasLeader())
			{
				lioness.getLeader().setGoalTarget(this.target, TargetReason.REINFORCEMENT_TARGET, true);
				for (EntityLionFemale lioness2 : lioness.getLeader().getActivePackMembers())
				{
					if (lioness2 != null && lioness2.isAlive() && !lioness2.equals(lioness))
					{
						lioness2.setGoalTarget(this.target, TargetReason.FOLLOW_LEADER, true);
					}
				}
			}
		}
		super.onStart();
	}
	
}
