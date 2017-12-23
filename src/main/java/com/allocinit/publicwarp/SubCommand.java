package com.allocinit.publicwarp;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;


public abstract class SubCommand implements CommandExecutor
{
	protected PublicWarp publicwarp;

	public SubCommand(PublicWarp publicwarp)
	{
		this.publicwarp = publicwarp;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String [] args)
	{
		try
		{
			doCommand(sender, args);
		}
		catch (UsageException e)
		{
			publicwarp.writeUsage(sender);
		}
		catch (PermissionDeniedException e)
		{
			sender.sendMessage(ChatColor.RED + "Permission Denied");
		}
		catch (ErrorException e)
		{
			sender.sendMessage(ChatColor.RED + e.getMessage());
		}
		catch (Exception e)
		{
			sender.sendMessage("Uncaught exception: " + e.getMessage());
		}

		return true;
	}

	public abstract void doCommand(CommandSender sender, String [] args)
			throws Exception;

	public abstract void writeUsage(CommandSender player);

	protected void checkPerm(CommandSender player, String perm)
	{
		if (!player.hasPermission(perm))
			throw new PermissionDeniedException();
	}
}
