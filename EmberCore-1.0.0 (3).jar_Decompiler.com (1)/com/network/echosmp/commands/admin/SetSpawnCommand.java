package com.network.echosmp.commands.admin;

import com.network.echosmp.Main;
import com.network.echosmp.utility.ColorUtil;
import java.io.File;
import java.io.IOException;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class SetSpawnCommand implements CommandExecutor {
   private static Location exactSpawnLocation;

   public SetSpawnCommand(JavaPlugin plugin) {
      plugin.getCommand("setspawn").setExecutor(this);
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      if (sender instanceof Player) {
         Player player = (Player)sender;
         if (!player.hasPermission("echosmp.command.setspawn")) {
            player.sendActionBar(ColorUtil.color("&#ff0000You don't have permission to use this command."));
            return true;
         } else {
            exactSpawnLocation = player.getLocation().clone();
            this.saveSpawnToFile(exactSpawnLocation);
            player.getWorld().setSpawnLocation(exactSpawnLocation.getBlockX(), exactSpawnLocation.getBlockY(), exactSpawnLocation.getBlockZ());
            player.sendActionBar(ColorUtil.color("&#27BEF5You have successfully set the spawn!"));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
            return true;
         }
      } else {
         sender.sendMessage("Only players can run this command!");
         return true;
      }
   }

   private void saveSpawnToFile(Location loc) {
      File file = new File(((Main)Main.getPlugin(Main.class)).getDataFolder(), "spawn.yml");
      FileConfiguration config = YamlConfiguration.loadConfiguration(file);
      config.set("spawn.world", loc.getWorld().getName());
      config.set("spawn.x", loc.getX());
      config.set("spawn.y", loc.getY());
      config.set("spawn.z", loc.getZ());
      config.set("spawn.yaw", loc.getYaw());
      config.set("spawn.pitch", loc.getPitch());

      try {
         config.save(file);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public static void loadSpawnFromFile() {
      File file = new File(((Main)Main.getPlugin(Main.class)).getDataFolder(), "spawn.yml");
      if (file.exists()) {
         FileConfiguration config = YamlConfiguration.loadConfiguration(file);
         String worldName = config.getString("spawn.world");
         double x = config.getDouble("spawn.x");
         double y = config.getDouble("spawn.y");
         double z = config.getDouble("spawn.z");
         float yaw = (float)config.getDouble("spawn.yaw");
         float pitch = (float)config.getDouble("spawn.pitch");
         if (worldName != null) {
            exactSpawnLocation = new Location(((Main)Main.getPlugin(Main.class)).getServer().getWorld(worldName), x, y, z, yaw, pitch);
         }

      }
   }

   public static Location getExactSpawnLocation() {
      return exactSpawnLocation;
   }
}
