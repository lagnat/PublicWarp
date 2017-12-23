package com.allocinit.publicwarp;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class WarpCommand extends SubCommand
{
	public WarpCommand(PublicWarp publicwarp)
	{
		super(publicwarp);
	}

	@Override
	public void doCommand(CommandSender sender, String [] args) throws Exception
	{
		Player player = (Player) sender;

		checkPerm(player, "publicwarp.use");

		if (args.length != 1)
			throw new UsageException();

		OfflinePlayer warpTo = null;

		List<Player> players = publicwarp.getServer().matchPlayer(args [0]);
		if (players != null && !players.isEmpty())
			warpTo = players.get(0);

		if (warpTo == null)
			warpTo = publicwarp.getServer().getOfflinePlayer(args [0]);

		if (warpTo == null)
			throw new ErrorException("Unknown player " + args [0]);

		doWarp(publicwarp, player, warpTo);
	}

	public static void doWarp(PublicWarp publicwarp, Player player,
			OfflinePlayer warpTo)
	{
		Location loc = publicwarp.getDB().getWarp(warpTo);
		player.teleport(loc);

		player.sendMessage("[" + ChatColor.GOLD + "Public Warp"
			+ ChatColor.WHITE + "] " + ChatColor.GREEN + "Teleported to "
			+ warpTo.getName() + "'s publicwarp.");
	}

	@Override
	public void writeUsage(CommandSender player)
	{
		player.sendMessage(ChatColor.RED + "/pw PLAYER " + ChatColor.GREEN
			+ "- Go to PLAYER's publicwarp.");
	}

}
