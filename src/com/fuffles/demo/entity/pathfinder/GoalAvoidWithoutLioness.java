package com.fuffles.demo.entity.pathfinder;

import java.util.EnumSet;
import java.util.function.Predicate;

import com.fuffles.demo.entity.EntityLionBabeh;
import com.fuffles.demo.entity.EntityLionFemale;
import com.fuffles.demo.entity.EntityLionMale;

import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.IEntitySelector;
import net.minecraft.server.v1_16_R3.PathEntity;
import net.minecraft.server.v1_16_R3.PathfinderTargetCondition;
import net.minecraft.server.v1_16_R3.RandomPositionGenerator;
import net.minecraft.server.v1_16_R3.Vec3D;

public class GoalAvoidWithoutLioness extends Goal
{
	private final EntityLionBabeh user;
	private final double speedWalkMod;
	private final double speedRunMod;
	private final PathfinderTargetCondition avoidPredicate;
	protected EntityLiving toAvoid;
	protected PathEntity path;
	
	public GoalAvoidWithoutLioness(EntityLionBabeh user, double speedMod, double speedMod2) 
	{
		//no creative/spectator
		this(user, speedMod, speedMod2, IEntitySelector.e::test);
	}

	protected GoalAvoidWithoutLioness(EntityLionBabeh user, double speedMod, double speedMod2, Predicate<EntityLiving> base)
	{
		this.user = user;
		this.speedWalkMod = speedMod;
		this.speedRunMod = speedMod2;
		Predicate<EntityLiving> notLions = (entity) -> {
			return !entity.getClass().equals(EntityLionBabeh.class) && !entity.getClass().equals(EntityLionFemale.class) && !entity.getClass().equals(EntityLionMale.class);
		};
		this.setFlags(EnumSet.of(Type.MOVE));
		this.avoidPredicate = new PathfinderTargetCondition().a(8D).a(base.and(notLions));
	}
	
	@Override
	public boolean canStart() 
	{
		if (this.user.hasMommy())
		{
			return false;
		}
		this.toAvoid = this.user.world.b(EntityLiving.class, this.avoidPredicate, this.user, this.user.locX(), this.user.locY(), this.user.locZ(), this.user.getBoundingBox().grow(8D, 3D, 8D));
		if (this.toAvoid == null)
		{
			return false;
		}
		else
		{
			Vec3D escapePos = RandomPositionGenerator.c(this.user, 16, 7, this.toAvoid.getPositionVector());
			if (escapePos == null)
			{
				return false;
			}
			else if (this.toAvoid.h(escapePos.x, escapePos.y, escapePos.z) < this.toAvoid.h(this.user)) 
			{
				return false;
			}
			else
			{
				this.path = this.user.getNavigation().a(escapePos.x, escapePos.y, escapePos.z, 0);
				return this.path != null;
			}
		}
	}
	
	@Override
	public boolean canContinue() 
	{
		return !this.user.getNavigation().m() && !this.user.hasMommy();
	}
	
	@Override
	public void onStart() 
	{
		this.user.getNavigation().a(this.path, this.speedWalkMod);
	}
	
	@Override
	public void onStop() 
	{
		this.toAvoid = null;
	}
	
	@Override
	public void onTick() 
	{
		if (this.user.h(this.toAvoid) < 49D)
		{
			this.user.getNavigation().a(this.speedRunMod);
		}
		else 
		{
			this.user.getNavigation().a(this.speedWalkMod);
		}
	}
}
