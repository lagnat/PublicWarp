package com.allocinit.publicwarp;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class SetCommand extends SubCommand
{
	public SetCommand(PublicWarp publicwarp)
	{
		super(publicwarp);
	}

	@Override
	public void doCommand(CommandSender sender, String [] args) throws Exception
	{
		Player player = (Player) sender;

		checkPerm(player, "publicwarp.set");

		if (args.length != 0)
			throw new UsageException();

		publicwarp.getDB().saveWarp(player);

		player.sendMessage("[" + ChatColor.GOLD + "Public Warp"
			+ ChatColor.WHITE + "] "
			+ "Public Warp has been set! Set a new one by simply typing the command again.");
	}

	@Override
	public void writeUsage(CommandSender player)
	{
		player.sendMessage(ChatColor.RED + "/pwset " + ChatColor.GREEN
			+ "- Set your publicwarp.");
	}
}
