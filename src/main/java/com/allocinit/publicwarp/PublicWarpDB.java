package com.allocinit.publicwarp;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;


public class PublicWarpDB
{
	private YamlConfiguration db;
	private File warpsFile;
	private PublicWarp publicwarp;

	public PublicWarpDB(PublicWarp publicwarp)
	{
		this.publicwarp = publicwarp;
		
		reload();
	}
	
	public void reload() 
	{
		File confDir = publicwarp.getDataFolder();
		confDir.mkdirs();

		warpsFile = new File(confDir, "warps.yml");

		db = YamlConfiguration.loadConfiguration(warpsFile);
	}

	public synchronized void saveWarp(Player player) throws IOException
	{
		String key = player.getUniqueId().toString();

		ConfigurationSection warpConf = db.getConfigurationSection(key);

		if (warpConf == null)
			warpConf = db.createSection(key);

		Location loc = player.getLocation();

		warpConf.set("x", loc.getX());
		warpConf.set("y", loc.getY());
		warpConf.set("z", loc.getZ());
		warpConf.set("yaw", loc.getYaw());
		warpConf.set("pitch", loc.getPitch());
		warpConf.set("world", loc.getWorld().getName());

		db.save(warpsFile);
	}

	public synchronized Set<String> getAllPlayerKeys()
	{
		return db.getKeys(false);
	}

	public synchronized Location getWarp(OfflinePlayer player)
	{
		ConfigurationSection warpConf =
				db.getConfigurationSection(player.getUniqueId().toString());

		if (warpConf == null)
			throw new ErrorException("No publicwarp for " + player.getName());

		if (!warpConf.contains("world") || !warpConf.contains("x")
			|| !warpConf.contains("y") || !warpConf.contains("z")
			|| !warpConf.contains("yaw") || !warpConf.contains("pitch"))
			throw new ErrorException(
					"Publicwarp for " + player.getName() + " is corrupted");

		double x = warpConf.getDouble("x");
		double y = warpConf.getDouble("y");
		double z = warpConf.getDouble("z");
		float yaw = (float) warpConf.getDouble("yaw");
		float pitch = (float) warpConf.getDouble("pitch");
		String worldName = warpConf.getString("world");

		World world = Bukkit.getServer().getWorld(worldName);

		if (world == null)
			throw new ErrorException("World " + worldName + " not found");

		return new Location(world, x, y, z, yaw, pitch);
	}
}