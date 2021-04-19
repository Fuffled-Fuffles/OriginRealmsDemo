package com.fuffles.demo.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

import com.fuffles.demo.entity.pathfinder.GoalLionessFollowLeader;
import com.fuffles.demo.entity.pathfinder.GoalLionessLookForMale;
import com.fuffles.demo.entity.pathfinder.TargetGoalByHurtAndAlertPack;

import net.minecraft.server.v1_16_R3.AttributeProvider.Builder;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.EntityAnimal;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.GenericAttributes;
import net.minecraft.server.v1_16_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_16_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_16_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_16_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_16_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_16_R3.PathfinderGoalRandomStrollLand;

public class EntityLionFemale extends EntityAbstractCat
{
	private World bukkit_world;
	private EntityLionMale male;
	private List<UUID> rejectedBy;
	private int joiningTicks;
	public boolean doLook;
	
	//Core
	private PathfinderGoalMeleeAttack attackGoal;
	private PathfinderGoalRandomStrollLand moveGoal;
	
	//Specific
	private GoalLionessLookForMale lookForMale;
	private PathfinderGoalNearestAttackableTarget<EntityPlayer> attackPlayers;
	private PathfinderGoalNearestAttackableTarget<EntityAnimal> attackAnimals;
	private GoalLionessFollowLeader followMale;
	
	public EntityLionFemale(Location loc, EntityLionMale male) 
	{
		super(loc);
		this.enableCore();
		this.setCustomName(new ChatComponentText("Lioness"));
		this.setCustomNameVisible(true);
		this.bukkit_world = loc.getWorld();
		this.male = male;
		this.doLook = false;
		this.updateBehaviour();
		this.setCatType(99);
	}
	
	@Override
	protected void initPathfinder()
	{
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		//tbh I thought about making them circle their prey, but geez that would take so long that I decided it wouldnt be worth it for this short demo
		this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, 1D, true));
		this.goalSelector.a(5, new PathfinderGoalLookAtMale(this, 15F));
		this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 15.0F));
		this.goalSelector.a(7, new PathfinderGoalLookAtPlayer(this, EntityInsentient.class, 15.0F));
		this.goalSelector.a(8, new PathfinderGoalRandomLookaround(this));
		this.targetSelector.a(1, new TargetGoalByHurtAndAlertPack(this));
	}
	
	public static Builder eK() 
	{
		return EntityInsentient.p().a(GenericAttributes.MAX_HEALTH, 25.0D).a(GenericAttributes.MOVEMENT_SPEED, 0.3D).a(GenericAttributes.ATTACK_DAMAGE, 2D);
	}
	
	public void setLeader(EntityLionMale male)
	{
		this.male = male;
		this.updateBehaviour();
	}
	
	public EntityLionMale getLeader()
	{
		return this.male;
	}
	
	public boolean hasLeader()
	{
		return this.male != null;
	}
	
	@Override
	public void setCatType(int i)
	{
		super.setCatType(6);
	}
	
	public List<UUID> getRejectedByList()
	{
		if (this.rejectedBy == null)
		{
			this.rejectedBy = new ArrayList<UUID>();
		}
		return this.rejectedBy;
	}
	
	public void addRejectedBy(EntityLionMale male)
	{
		if (this.rejectedBy == null)
		{
			this.rejectedBy = new ArrayList<UUID>();
		}
		this.rejectedBy.add(male.getUniqueID());
	}
	
	public void setJoining()
	{
		this.joiningTicks = 60;
		this.doLook = true;
	}
	
	@Override
	public void inactiveTick() 
	{
		super.inactiveTick();
		if (this.male != null && !this.male.isAlive())
		{
			this.male = null;
			this.updateBehaviour();
		}
	}
	
	@Override
	public void movementTick()
	{
		super.movementTick();
		if (this.joiningTicks > 0)
		{
			this.joiningTicks--;
			if (this.joiningTicks % 5 == 0)
			{
				double dx = this.random.nextGaussian() * 0.02D;
				double dy = this.random.nextGaussian() * 0.02D;
				double dz = this.random.nextGaussian() * 0.02D;
				Location loc = new Location(this.bukkit_world, this.d(1D), this.cF() + 0.5D, this.g(1D));
				this.bukkit_world.spawnParticle(Particle.HEART, loc, 1, dx, dy, dz);
			}
		}
	}
	
	//to be absolutely fair, I realized how crashy this is a bit too late... I'd probably avoid using this switcheroo in the future
	public void updateBehaviour()
	{
		this.followMale = null;
		if (this.lookForMale == null)
		{
			this.lookForMale = new GoalLionessLookForMale(this, 1D);
		}
		if (this.attackPlayers == null)
		{
			this.attackPlayers = new PathfinderGoalNearestAttackableTarget<>(this, EntityPlayer.class, true);
		}
		if (this.attackAnimals == null)
		{
			this.attackAnimals = new PathfinderGoalNearestAttackableTarget<>(this, EntityAnimal.class, 10, true, true, EntityLionMale.NOT_LION);
		}
		if (this.followMale == null)
		{
			this.followMale = new GoalLionessFollowLeader(this, this.male, 1D);
		}
		
		this.goalSelector.a(this.lookForMale);
		this.targetSelector.a(this.attackPlayers);
		this.targetSelector.a(this.attackAnimals);
		this.goalSelector.a(this.followMale);
		if (this.male != null)
		{
			this.goalSelector.a(2, this.followMale);
		}
		else
		{
			this.goalSelector.a(3, this.lookForMale);
			this.targetSelector.a(2, this.attackPlayers);
			this.targetSelector.a(3, this.attackAnimals);
		}
	}
	
	private void refreshCore()
	{
		if (this.attackGoal == null)
		{
			this.attackGoal = new PathfinderGoalMeleeAttack(this, 1D, true);
		}
		if (this.moveGoal == null)
		{
			this.moveGoal = new PathfinderGoalRandomStrollLand(this, 1D);
		}
	}
	
	public void disableCore()
	{
		this.refreshCore();
		this.goalSelector.a(this.attackGoal);
		this.goalSelector.a(this.moveGoal);
	}
	
	public void enableCore()
	{
		this.disableCore();
		this.goalSelector.a(1, this.attackGoal);
		this.goalSelector.a(4, this.moveGoal);
	}
	
	public class PathfinderGoalLookAtMale extends PathfinderGoalLookAtPlayer
	{
		public PathfinderGoalLookAtMale(EntityInsentient user, float f1) 
		{
			super(user, EntityLionMale.class, f1, 1F);
		}
		
		@Override
		public boolean a()
		{
			if (((EntityLionFemale)this.a).doLook)
			{
				return super.a();
			}
			else
			{
				return false;
			}
		}
		
		@Override
		public boolean b()
		{
			if (((EntityLionFemale)this.a).doLook)
			{
				return super.b();
			}
			else
			{
				return false;
			}
		}
	}
}
