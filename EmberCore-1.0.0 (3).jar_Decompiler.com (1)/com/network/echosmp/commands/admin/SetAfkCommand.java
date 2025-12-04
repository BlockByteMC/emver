package com.network.echosmp.commands.admin;

import com.network.echosmp.Main;
import com.network.echosmp.utility.ColorUtil;
import java.io.File;
import java.io.IOException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class SetAfkCommand implements CommandExecutor {
   private static Location afkLocation;

   public SetAfkCommand(JavaPlugin plugin) {
      plugin.getCommand("setafk").setExecutor(this);
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (!(sender instanceof Player)) {
         sender.sendMessage("Only players can run this command!");
         return true;
      } else {
         Player player = (Player)sender;
         if (!player.hasPermission("silosmp.command.setafk")) {
            player.sendActionBar(ColorUtil.color("&#ff0000You don't have permission to use this command."));
            return true;
         } else {
            afkLocation = player.getLocation().clone();
            this.saveAfkToFile(afkLocation);
            player.sendActionBar(ColorUtil.color("&#35F527You have successfully set the AFK location!"));
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
            return true;
         }
      }
   }

   private void saveAfkToFile(Location loc) {
      File file = new File(((Main)Main.getPlugin(Main.class)).getDataFolder(), "afk.yml");
      FileConfiguration config = YamlConfiguration.loadConfiguration(file);
      config.set("afk.world", loc.getWorld().getName());
      config.set("afk.x", loc.getX());
      config.set("afk.y", loc.getY());
      config.set("afk.z", loc.getZ());
      config.set("afk.yaw", loc.getYaw());
      config.set("afk.pitch", loc.getPitch());

      try {
         config.save(file);
      } catch (IOException var5) {
         var5.printStackTrace();
      }

   }

   public static void loadAfkFromFile() {
      File file = new File(Main.getInstance().getDataFolder(), "afk.yml");
      if (file.exists()) {
         FileConfiguration config = YamlConfiguration.loadConfiguration(file);
         String worldName = config.getString("afk.world");
         double x = config.getDouble("afk.x");
         double y = config.getDouble("afk.y");
         double z = config.getDouble("afk.z");
         float yaw = (float)config.getDouble("afk.yaw");
         float pitch = (float)config.getDouble("afk.pitch");
         if (worldName != null) {
            afkLocation = new Location(Bukkit.getServer().getWorld(worldName), x, y, z, yaw, pitch);
         }

      }
   }

   public static Location getAfkLocation() {
      return afkLocation;
   }
}
