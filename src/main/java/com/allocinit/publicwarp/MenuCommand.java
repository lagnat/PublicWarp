package com.allocinit.publicwarp;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.allocinit.publicwarp.IconMenu.IconClickEventHandler;
import com.allocinit.publicwarp.IconMenu.OptionClickEvent;


public class MenuCommand extends SubCommand
{
	public MenuCommand(PublicWarp publicwarp)
	{
		super(publicwarp);
	}

	@Override
	public void doCommand(CommandSender sender, String [] args) throws Exception
	{
		Player player = (Player) sender;

		checkPerm(player, "publicwarp.list");

		if (args.length != 0)
			throw new UsageException();

		showPage(player, 0);
	}

	private void showPage(Player player, int page)
	{
		int howManySlots = 9 * 6;

		List<String> keys =
				new ArrayList<>(publicwarp.getDB().getAllPlayerKeys());
		keys.sort(String.CASE_INSENSITIVE_ORDER);

		// Page 1 will have N-1 items
		// Every interior page will have N-2 items
		// The last page will have whatever is left.

		int totalWarps = keys.size();
		int nPages = 1;

		totalWarps -= howManySlots - 1;

		if (totalWarps > 0)
			nPages += (totalWarps + totalWarps - 1) / (howManySlots - 2);

		int first = 0;

		if (page > 0)
		{
			first += howManySlots - 1;
			first += (page - 1) * (howManySlots - 2);
		}

		final IconMenu menu = new IconMenu(
				"Personal Warps page " + (page + 1) + " of " + nPages,
				howManySlots, new IconClickEventHandler()
				{
					@Override
					public void onOptionClick(OptionClickEvent event)
					{
						event.setWillClose(true);
						event.setWillDestroy(true);

						if (event.getContext() != null)
						{
							WarpCommand.doWarp(publicwarp, player,
								(OfflinePlayer) event.getContext());
						}
						else if (event.getPosition() == 0)
						{
							event.setAfterCloseHandler(new Runnable()
							{
								@Override
								public void run()
								{
									showPage(player, page - 1);
								}
							});
						}
						else
						{
							event.setAfterCloseHandler(new Runnable()
							{
								@Override
								public void run()
								{
									showPage(player, page + 1);
								}
							});
						}
					}
				}, publicwarp);

		int which = first;

		for (int i = 0; i < howManySlots; i++)
		{
			if (which >= keys.size())
				break;

			if (i == 0 && page > 0)
			{
				makeArrowSlot(menu, i, page);
				continue;
			}

			if (i == howManySlots - 1)
			{
				makeArrowSlot(menu, i, page + 2);
				continue;
			}

			String oneKey = keys.get(which++);
			UUID uuid = UUID.fromString(oneKey);

			OfflinePlayer aPlayer =
					publicwarp.getServer().getOfflinePlayer(uuid);
			Location aLocation = publicwarp.getDB().getWarp(aPlayer);

			if (aPlayer != null && aPlayer.getName() != null)
			{
				ItemStack stack = new ItemStack(Material.SKULL_ITEM, 1,
						(short) 0, (byte) 3);
				SkullMeta skullmeta = (SkullMeta) stack.getItemMeta();
				skullmeta.setOwner(aPlayer.getName());
				stack.setItemMeta(skullmeta);

				menu.setOption(i, stack, aPlayer.getName(),
					(int) aLocation.getX() + "/" + (int) aLocation.getY() + "/"
						+ (int) aLocation.getZ() + "/"
						+ aLocation.getWorld().getName());

				menu.setOptionContext(i, aPlayer);
			}
		}

		menu.open(player);
	}

	private void makeArrowSlot(IconMenu menu, int slot, int forPage)
	{
		ItemStack stack = new ItemStack(Material.ARROW);
		menu.setOption(slot, stack, "Page " + forPage);
	}

	@Override
	public void writeUsage(CommandSender player)
	{
		player.sendMessage(ChatColor.RED + "/pwmenu " + ChatColor.GREEN
			+ "- Show the warp list as a menu.");
	}

}
