package com.fuffles.demo.entity.pathfinder;

import com.fuffles.demo.entity.EntityLionMale;

public class GoalMaleDecideOnLioness extends Goal
{
	private final EntityLionMale user;
	private int ticksUntilStart;
	private boolean accept;
	
	public GoalMaleDecideOnLioness(EntityLionMale user)
	{
		this.user = user;
	}
	
	protected boolean isMaxCapacity()
	{
		return this.user.getActivePackMembers().size() - 1 >= this.user.maxMembers;
	}
	
	@Override
	public boolean canStart() 
	{
		return !this.isMaxCapacity() && this.user.getPending() != null;
	}
	
	@Override
	public boolean canContinue() 
	{
		return this.user.getPending() != null;
	}
	
	@Override
	public void onStart() 
	{
		this.accept = this.user.getRandom().nextBoolean();
		//this.accept = this.user.acceptNextForDemo;
		this.ticksUntilStart = 90;
	}
	
	@Override
	public void onTick() 
	{
		if (this.user.getPending() != null)
		{
			//hack to keep male in place during this
			this.user.getNavigation().o();
			this.user.getControllerLook().a(this.user.getPending(), this.user.ep(), this.user.O());
			if (this.ticksUntilStart == 60)
			{
				if (this.accept) { this.user.doAccept(this.user.getPending()); }
				else { this.user.doRejection(); }
			}
			--this.ticksUntilStart;
			if (this.ticksUntilStart <= 0)
			{
				this.user.getPending().enableCore();
				this.user.getPending().doLook = false;
				//this.user.enableCore();
				this.user.doLook = false;
				if (this.accept)
				{
					this.user.getPending().updateBehaviour();
					this.user.getPending().setLeader(this.user);
				}
				else
				{
					this.user.getPending().addRejectedBy(this.user);
					this.user.acceptNextForDemo = true;
				}
				this.user.clearPending();
			}
		}
	}
}
