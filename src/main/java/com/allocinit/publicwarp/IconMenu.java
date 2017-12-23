package com.allocinit.publicwarp;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;


public class IconMenu implements Listener
{
	private String name;
	private int size;
	private IconClickEventHandler handler;
	private Plugin plugin;

	private String [] optionNames;
	private ItemStack [] optionIcons;
	private InventoryView currentView;
	private Object [] optionContext;

	public IconMenu(String name, int size, IconClickEventHandler handler,
			Plugin plugin)
	{
		this.name = name;
		this.size = size;
		this.handler = handler;
		this.plugin = plugin;
		this.optionNames = new String [size];
		this.optionIcons = new ItemStack [size];
		this.optionContext = new Object [size];

		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public IconMenu setOption(int position, ItemStack icon, String name,
			String... info)
	{
		optionNames [position] = name;
		optionIcons [position] = setItemNameAndLore(icon, name, info);
		return this;
	}

	public void setOptionContext(int position, Object context)
	{
		optionContext [position] = context;
	}

	public void open(Player player)
	{
		Inventory inventory = Bukkit.createInventory(player, size, name);
		for (int i = 0; i < optionIcons.length; i++)
		{
			if (optionIcons [i] != null)
			{
				inventory.setItem(i, optionIcons [i]);
			}
		}

		currentView = player.openInventory(inventory);
	}

	public void destroy()
	{
		HandlerList.unregisterAll(this);
		handler = null;
		plugin = null;
		optionNames = null;
		optionIcons = null;
	}

	@EventHandler(priority = EventPriority.HIGHEST)
	void onInventoryClick(InventoryClickEvent event)
	{
		if (event.getView() != currentView)
			return;

		event.setCancelled(true);

		if (event.getClick() != ClickType.LEFT)
			return;

		int slot = event.getRawSlot();
		if (slot >= 0 && slot < size && optionNames [slot] != null)
		{
			Plugin plugin = this.plugin;
			final Player p = (Player) event.getWhoClicked();

			OptionClickEvent e =
					new OptionClickEvent(p, slot, optionNames [slot],
							optionIcons [slot], optionContext [slot]);

			handler.onOptionClick(e);

			p.updateInventory();

			if (e.willClose())
			{
				Bukkit.getScheduler().scheduleSyncDelayedTask(plugin,
					new Runnable()
					{
						public void run()
						{
							p.closeInventory();
							if (e.getAfterCloseHandler() != null)
								e.getAfterCloseHandler().run();
						}
					});
			}

			if (e.willDestroy())
				destroy();
		}
	}

	public interface IconClickEventHandler
	{
		public void onOptionClick(OptionClickEvent event);
	}

	public class OptionClickEvent
	{
		private Player player;
		private int position;
		private String name;
		private boolean close;
		private boolean destroy;
		private ItemStack item;
		private Object context;
		private Runnable afterCloseHandler;

		public OptionClickEvent(Player player, int position, String name,
				ItemStack item, Object context)
		{
			this.player = player;
			this.position = position;
			this.name = name;
			this.close = true;
			this.destroy = false;
			this.item = item;
			this.context = context;
		}

		public Player getPlayer()
		{
			return player;
		}

		public int getPosition()
		{
			return position;
		}

		public String getName()
		{
			return name;
		}

		public boolean willClose()
		{
			return close;
		}

		public boolean willDestroy()
		{
			return destroy;
		}

		public void setWillClose(boolean close)
		{
			this.close = close;
		}

		public void setWillDestroy(boolean destroy)
		{
			this.destroy = destroy;
		}

		public ItemStack getItem()
		{
			return item;
		}

		public Object getContext()
		{
			return context;
		}

		public Runnable getAfterCloseHandler()
		{
			return afterCloseHandler;
		}

		public void setAfterCloseHandler(Runnable afterCloseHandler)
		{
			this.afterCloseHandler = afterCloseHandler;
		}
	}

	private ItemStack setItemNameAndLore(ItemStack item, String name,
			String [] lore)
	{
		ItemMeta im = item.getItemMeta();
		im.setDisplayName(name);
		im.setLore(Arrays.asList(lore));
		item.setItemMeta(im);
		return item;
	}

}