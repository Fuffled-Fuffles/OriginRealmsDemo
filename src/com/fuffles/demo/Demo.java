package com.fuffles.demo;

import java.util.logging.Logger;

import org.bukkit.plugin.java.JavaPlugin;

import com.comphenix.protocol.ProtocolLibrary;
import com.fuffles.demo.command.MenuCommand;
import com.fuffles.demo.inventory.DemoMenuInventory;

public class Demo extends JavaPlugin 
{
	public static final Logger LOG = Logger.getLogger( "OR Demo" );
	
	@Override
	public void onEnable()
	{
		this.setupCommands();
		this.getServer().getPluginManager().registerEvents(new DemoMenuInventory(), this);
		this.getServer().getPluginManager().registerEvents(new DemoEventHandler(this), this);
		ProtocolLibrary.getProtocolManager().addPacketListener(new BlockDigHandler(this));
	}
	
	@Override
	public void onDisable()
	{
		ProtocolLibrary.getProtocolManager().removePacketListeners(this);
	}
	
	private void setupCommands()
	{
		this.getCommand("menu").setExecutor(new MenuCommand());
	}
}
