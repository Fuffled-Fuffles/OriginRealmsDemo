package com.fuffles.demo.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.fuffles.demo.inventory.DemoMenuInventory;

public class MenuCommand implements CommandExecutor
{
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] arguments) 
	{
		if (sender instanceof Player)
		{
			Player player = (Player)sender;
			
			player.openInventory(new DemoMenuInventory().getInventory());
			
			return true;
		}
		return false;
	}

}
