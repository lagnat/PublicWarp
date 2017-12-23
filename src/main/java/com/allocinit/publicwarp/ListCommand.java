package com.allocinit.publicwarp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;


public class ListCommand extends SubCommand
{
	public ListCommand(PublicWarp publicwarp)
	{
		super(publicwarp);
	}

	@Override
	public void doCommand(CommandSender player, String [] args) throws Exception
	{
		checkPerm(player, "publicwarp.list");

		if (args.length > 1)
			throw new UsageException();

		List<String> keys =
				new ArrayList<>(publicwarp.getDB().getAllPlayerKeys());
		keys.sort(String.CASE_INSENSITIVE_ORDER);

		int howMany = 10;
		int page = 1;
		int nPages = (keys.size() + howMany - 1) / howMany;

		if (args.length == 1)
			page = Integer.parseInt(args [0]);

		player.sendMessage(
			"[" + ChatColor.GOLD + "Public Warp" + ChatColor.WHITE + "] ");

		int first = (page - 1) * howMany;
		int last = Math.min(first + howMany, keys.size());

		for (int i = first; i < last; i++)
		{
			String oneKey = keys.get(i);
			UUID uuid = UUID.fromString(oneKey);

			OfflinePlayer aPlayer =
					publicwarp.getServer().getOfflinePlayer(uuid);
			Location aLocation = publicwarp.getDB().getWarp(aPlayer);

			if (aPlayer != null)
			{
				player.sendMessage("" + ChatColor.GRAY + (i + 1) + ". "
					+ ChatColor.AQUA + aPlayer.getName() + ChatColor.GRAY + " ("
					+ (int) aLocation.getX() + "/" + (int) aLocation.getY()
					+ "/" + (int) aLocation.getZ() + "/"
					+ aLocation.getWorld().getName() + ")");
			}
		}

		String morePages = "";
		if (nPages > page)
			morePages = " Use \"" + ChatColor.AQUA + "/pwlist " + (page + 1)
				+ "\"" + ChatColor.WHITE + " to see page " + (page + 1);

		player.sendMessage("Page " + page + "/" + nPages + "." + morePages);
	}

	@Override
	public void writeUsage(CommandSender player)
	{
		player.sendMessage(ChatColor.RED + "/pwlist " + ChatColor.GREEN
			+ "- List public warps.");
	}
}
