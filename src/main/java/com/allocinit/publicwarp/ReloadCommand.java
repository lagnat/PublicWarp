package com.allocinit.publicwarp;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class ReloadCommand extends SubCommand
{
	public ReloadCommand(PublicWarp publicwarp)
	{
		super(publicwarp);
	}

	@Override
	public void doCommand(CommandSender sender, String [] args) throws Exception
	{
		Player player = (Player) sender;

		checkPerm(player, "publicwarp.admin");

		if (args.length != 0)
			throw new UsageException();

		publicwarp.getDB().reload();

		sender.sendMessage("[" + ChatColor.GOLD + "Public Warp"
			+ ChatColor.WHITE + "] " + ChatColor.GREEN + "Reloaded.");
	}

	@Override
	public void writeUsage(CommandSender player)
	{
		if (!player.hasPermission("publicwarp.admin"))
			player.sendMessage(ChatColor.RED + "/pwreload " + ChatColor.GREEN
				+ "- Reload the PublicWarp db.");
	}
}
