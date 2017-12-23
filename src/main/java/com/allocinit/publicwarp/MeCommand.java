package com.allocinit.publicwarp;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class MeCommand extends SubCommand
{
	public MeCommand(PublicWarp publicwarp)
	{
		super(publicwarp);
	}

	@Override
	public void doCommand(CommandSender sender, String [] args) throws Exception
	{
		Player player = (Player) sender;

		checkPerm(player, "publicwarp.use");

		if (args.length != 0)
			throw new UsageException();

		Location loc = publicwarp.getDB().getWarp(player);
		player.teleport(loc);

		sender.sendMessage(
			"[" + ChatColor.GOLD + "Public Warp" + ChatColor.WHITE + "] "
				+ ChatColor.GREEN + "Teleported to your publicwarp.");
	}

	@Override
	public void writeUsage(CommandSender player)
	{
		player.sendMessage(ChatColor.RED + "/pwme " + ChatColor.GREEN
			+ "- Go to your personal publicwarp.");
	}

}
