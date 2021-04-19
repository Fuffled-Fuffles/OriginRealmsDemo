package com.fuffles.demo.entity.pathfinder;

import java.util.EnumSet;

import net.minecraft.server.v1_16_R3.PathfinderGoal;

//Readable Vanilla Copy
public abstract class Goal extends PathfinderGoal
{
	public abstract boolean canStart();
	
	@Override
	@Deprecated
	public boolean a()
	{
		return this.canStart();
	}
	
	public boolean canContinue()
	{
		return this.a();
	}
	
	@Override
	@Deprecated
	public boolean b()
	{
		return this.canContinue();
	}
	
	public boolean canBeCanceled()
	{
		return true;
	}
	
	@Override
	@Deprecated
	public boolean C_()
	{
		return this.canBeCanceled();
	}
	
	public void onStart() {}
	
	@Override
	@Deprecated
	public void c()
	{
		this.onStart();
	}
	
	public void onStop() {}
	
	@Override
	@Deprecated
	public void d()
	{
		this.onStop();
	}
	
	public void onTick() {}
	
	@Override
	@Deprecated
	public void e()
	{
		this.onTick();
	}
	
	public void setFlags(EnumSet<PathfinderGoal.Type> flags)
	{
		this.a(flags);
	}
	
	@Override
	@Deprecated
	public void a(EnumSet<PathfinderGoal.Type> flags)
	{
		super.a(flags);
	}
	
	public EnumSet<PathfinderGoal.Type> getFlags()
	{
		return this.i();
	}
	
	@Override
	@Deprecated
	public EnumSet<PathfinderGoal.Type> i()
	{
		return super.i();
	}
}
