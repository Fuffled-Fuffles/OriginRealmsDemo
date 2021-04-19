package com.fuffles.demo.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;
import org.bukkit.event.entity.EntityTargetEvent.TargetReason;

import com.fuffles.demo.entity.pathfinder.GoalMaleDecideOnLioness;
import com.fuffles.demo.entity.pathfinder.TargetGoalByHurtAndAlertPack;
import com.google.common.collect.ImmutableList;

import net.minecraft.server.v1_16_R3.AttributeProvider.Builder;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.ChatComponentText;
import net.minecraft.server.v1_16_R3.EntityAnimal;
import net.minecraft.server.v1_16_R3.EntityHuman;
import net.minecraft.server.v1_16_R3.EntityInsentient;
import net.minecraft.server.v1_16_R3.EntityLiving;
import net.minecraft.server.v1_16_R3.EntityPlayer;
import net.minecraft.server.v1_16_R3.GenericAttributes;
import net.minecraft.server.v1_16_R3.PathfinderGoalFloat;
import net.minecraft.server.v1_16_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_16_R3.PathfinderGoalMeleeAttack;
import net.minecraft.server.v1_16_R3.PathfinderGoalNearestAttackableTarget;
import net.minecraft.server.v1_16_R3.PathfinderGoalRandomLookaround;
import net.minecraft.server.v1_16_R3.PathfinderGoalRandomStrollLand;

public class EntityLionMale extends EntityAbstractOcelot
{
	public static final Predicate<EntityLiving> NOT_LION = (EntityLiving ent) -> {
		return !(ent.getClass().equals(EntityLionMale.class) || ent.getClass().equals(EntityLionFemale.class) || ent.getClass().equals(EntityLionBabeh.class));
	};
	
	private List<EntityLionFemale> activePackMembers = new ArrayList<>();
	
	private World bukkit_world;
	public final BlockPosition territoryCenter;
	public final int territoryRange;
	public final int maxMembers;
	public boolean doLook;
	
	private EntityLionFemale pendingFemale;
	private int packTicks;
	private boolean isNewJoining;
	
	public boolean acceptNextForDemo = false;
	
	//Core
	private PathfinderGoalMeleeAttack attackGoal;
	private PathfinderGoalRandomStrollLand moveGoal;
	
	public EntityLionMale(Location loc, SpawnReason spawnReason) 
	{
		super(loc);
		this.enableCore();
		this.setCustomName(new ChatComponentText("Lion"));
		this.setCustomNameVisible(true);
		this.bukkit_world = loc.getWorld();
		this.doLook = false;
		this.territoryCenter = new BlockPosition(loc.getX(), loc.getY(), loc.getZ());
		this.territoryRange = this.random.nextInt(30) + 30;
		this.maxMembers = this.random.nextInt(10) + 5;
		if (spawnReason.equals(SpawnReason.DEFAULT))
		{
			((CraftWorld)loc.getWorld()).getHandle().addEntity(new EntityLionFemale(loc, this));
			for (int i = 0; i < this.random.nextInt(this.maxMembers - 1); i++)
			{
				Location rnd_loc = loc.add((this.random.nextInt(2) - 1) * this.random.nextDouble(), 0, (this.random.nextInt(2) - 1) * this.random.nextDouble());
				boolean female = this.random.nextBoolean();
				if (female)
				{
					((CraftWorld)loc.getWorld()).getHandle().addEntity(new EntityLionFemale(rnd_loc, this));
				}
				else
				{
					((CraftWorld)loc.getWorld()).getHandle().addEntity(new EntityLionBabeh(rnd_loc));
				}
			}
		}
		//this.drawTerritory(loc.getWorld());
	}
	
	@Override
	protected void initPathfinder()
	{
		this.goalSelector.a(0, new PathfinderGoalFloat(this));
		this.goalSelector.a(1, new PathfinderGoalMeleeAttack(this, 1D, true));
		this.goalSelector.a(2, new GoalMaleDecideOnLioness(this));
		this.goalSelector.a(4, new PathfinderGoalLookAtFemale(this, 15F));
		this.goalSelector.a(5, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 15.0F));
		this.goalSelector.a(6, new PathfinderGoalLookAtPlayer(this, EntityInsentient.class, 15.0F));
		this.goalSelector.a(7, new PathfinderGoalRandomLookaround(this));
		this.targetSelector.a(1, new TargetGoalByHurtAndAlertPack(this));
		this.targetSelector.a(2, new PathfinderGoalNearestAttackableWithGroup<>(this, EntityPlayer.class));
		this.targetSelector.a(3, new PathfinderGoalNearestAttackableWithGroup<>(this, EntityAnimal.class));
	}
	
	public static Builder eK() 
	{
		return EntityInsentient.p().a(GenericAttributes.MAX_HEALTH, 50.0D).a(GenericAttributes.MOVEMENT_SPEED, 0.3D).a(GenericAttributes.ATTACK_DAMAGE, 5D);
	}
	
	public void addPackMemberFemale(EntityLionFemale ent)
	{
		this.activePackMembers.add(ent);
	}
	
	public ImmutableList<EntityLionFemale> getActivePackMembers()
	{
		return ImmutableList.copyOf(this.activePackMembers.iterator());
	}
	
	public void setPending(EntityLionFemale female)
	{
		this.pendingFemale = female;
	}
	
	public EntityLionFemale getPending()
	{
		return this.pendingFemale;
	}
	
	public void clearPending()
	{
		this.pendingFemale = null;
	}
	
	public void doAccept(EntityLionFemale female)
	{
		this.isNewJoining = true;
		this.packTicks = 60;
		this.activePackMembers.add(female);
	}
	
	public void doRejection()
	{
		this.isNewJoining = false;
		this.packTicks = 60;
	}
	
	public void drawTerritory(World world)
	{
		int x = this.territoryCenter.getX();
		int y = this.territoryCenter.getY() - 1;
		int z = this.territoryCenter.getZ();
		world.getBlockAt(x, y, z).setType(Material.RED_WOOL);
		for (int i = 0; i < (this.territoryRange + 1) * 2; i++)
		{
			world.getBlockAt(x + this.territoryRange, y, z + this.territoryRange - i).setType(Material.YELLOW_WOOL);
			world.getBlockAt(x + this.territoryRange - i, y, z + this.territoryRange).setType(Material.YELLOW_WOOL);
			
			world.getBlockAt(x - this.territoryRange, y, z - this.territoryRange + i).setType(Material.YELLOW_WOOL);
			world.getBlockAt(x - this.territoryRange + i, y, z - this.territoryRange).setType(Material.YELLOW_WOOL);
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
		this.goalSelector.a(3, this.moveGoal);
	}
	
	@Override 
	public void movementTick()
	{
		super.movementTick();
		if (this.packTicks > 0)
		{
			this.packTicks--;
			if (this.packTicks % 5 == 0)
			{
				double dx = this.random.nextGaussian() * 0.02D;
				double dy = this.random.nextGaussian() * 0.02D;
				double dz = this.random.nextGaussian() * 0.02D;
				Location loc = new Location(this.bukkit_world, this.d(1D), this.cF() + 0.5D, this.g(1D));
				this.bukkit_world.spawnParticle(this.isNewJoining ? Particle.HEART : Particle.VILLAGER_ANGRY, loc, 1, dx, dy, dz);
			}
		}
	}
	
	@Override
	public void mobTick()
	{
		if (!this.ez())
		{
			this.a(new BlockPosition(this.territoryCenter.getX(), this.territoryCenter.getY(), this.territoryCenter.getZ()), this.territoryRange);
		}
	}
	
	public class PathfinderGoalLookAtFemale extends PathfinderGoalLookAtPlayer
	{
		public PathfinderGoalLookAtFemale(EntityInsentient user, float f1) 
		{
			super(user, EntityLionFemale.class, f1, 1F);
		}
		
		@Override
		public boolean a()
		{
			if (((EntityLionMale)this.a).doLook)
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
			if (((EntityLionMale)this.a).doLook)
			{
				return super.b();
			}
			else
			{
				return false;
			}
		}
	}
	
	public class PathfinderGoalNearestAttackableWithGroup<T extends EntityLiving> extends PathfinderGoalNearestAttackableTarget<T>
	{	
		public PathfinderGoalNearestAttackableWithGroup(EntityLionMale user, Class<T> targetclass) 
		{
			super(user, targetclass, 10, true, true, NOT_LION);
		}
		
		//start
		@Override
		public void c()
		{
			super.c();
			for (EntityLionFemale lioness : ((EntityLionMale)this.e).getActivePackMembers())
			{
				if (lioness != null && lioness.isAlive())
				{	
					lioness.setGoalTarget(this.c, TargetReason.FOLLOW_LEADER, true);
				}
			}
		}
		
		//stop
		@Override
		public void d()
		{
			super.d();
			for (EntityLionFemale lioness : ((EntityLionMale)this.e).getActivePackMembers())
			{
				if (lioness != null && lioness.isAlive())
				{	
					lioness.setGoalTarget(null, TargetReason.FORGOT_TARGET, true);
				}
			}
		}
	}
}
