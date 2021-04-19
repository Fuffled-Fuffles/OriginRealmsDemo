package com.fuffles.demo.entity.pathfinder;

import java.util.function.Predicate;

import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import com.fuffles.demo.entity.EntityLionBabeh;
import com.fuffles.demo.entity.EntityLionFemale;
import com.fuffles.demo.entity.EntityLionMale;

import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.PathfinderTargetCondition;

public class TargetGoalNearestInTerritory extends TargetGoal
{
	private final EntityLionMale user2;
	protected Class<? extends EntityLiving> targetClass;
	protected int randomInterval;
	protected final Predicate<EntityLiving> NOT_LION = (EntityLiving ent) -> {
		return !(ent.getClass().equals(EntityLionMale.class) || ent.getClass().equals(EntityLionFemale.class) || ent.getClass().equals(EntityLionBabeh.class));
	};
	protected PathfinderTargetCondition conditionAll = new PathfinderTargetCondition().a(16D).a(NOT_LION);
	
	public TargetGoalNearestInTerritory(EntityLionMale user, Class<? extends EntityLiving> targetType) 
	{
		super(user, true, false);
		this.user2 = user;
		this.targetClass = targetType;
		this.randomInterval = 10;
	}

	@Override
	public boolean canStart() 
	{
		if (this.user2.getRandom().nextInt(10) != 0)
		{
			return false;
		}
		else
		{
			this.findTarget();
			return this.target != null && this.target.isAlive() && this.isTargetWithinTerritory();
		}
	}
	
	@Override
	public void onStart()
	{
		this.user2.setGoalTarget(this.target, TargetReason.CLOSEST_ENTITY, true);
		super.onStart();
		for (EntityLionFemale lioness : this.user2.getActivePackMembers())
		{
			if (lioness != null && lioness.isAlive())
			{	
				lioness.setGoalTarget(this.target, TargetReason.FOLLOW_LEADER, true);
			}
		}
	}
	
	@Override
	public void onStop()
	{
		super.onStop();
		for (EntityLionFemale lioness : this.user2.getActivePackMembers())
		{
			if (lioness != null && lioness.isAlive())
			{
				lioness.setGoalTarget(null, TargetReason.FORGOT_TARGET, true);
			}
		}
	}
	
	protected void findTarget()
	{
		if (this.targetClass == EntityHuman.class || this.targetClass == EntityPlayer.class)
		{
			this.target = this.user2.world.a(this.conditionAll, this.user2, this.user2.locX(), this.user2.getHeadY(), this.user2.locZ());
		}
		else
		{
			this.target = this.user2.world.b(this.targetClass, this.conditionAll, this.user2, this.user2.locX(), this.user2.getHeadY(), this.user2.locZ(), this.user.getBoundingBox().a(16D, 4D, 16D));
		}
	}
	
	protected boolean isTargetWithinTerritory()
	{
		return true;// this.user2.territoryCenter.j(this.target.getChunkCoordinates()) < (double)(this.user2.territoryRange * this.user2.territoryRange);
	}
	
}
