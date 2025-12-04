package com.network.echosmp.commands;

import com.network.echosmp.utility.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class StoreCommand implements CommandExecutor {
   public StoreCommand(JavaPlugin plugin) {
      plugin.getCommand("store").setExecutor(this);
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (sender instanceof Player) {
         Player player = (Player)sender;
         player.sendMessage(ColorUtil.color("&r"));
         player.sendMessage(ColorUtil.color("&#00FF00&lSERVER STORE"));
         player.sendMessage(ColorUtil.color("&r"));
         player.sendMessage(ColorUtil.color("&fWanting to unlock rank perks, or get"));
         player.sendMessage(ColorUtil.color("&fkeys for crates? Visit our website!"));
         player.sendMessage(ColorUtil.color("&r"));
         player.sendMessage(ColorUtil.color("&#00FF00store.echosmp.com"));
         player.sendMessage(ColorUtil.color("&r"));
         return true;
      } else {
         return true;
      }
   }
}
