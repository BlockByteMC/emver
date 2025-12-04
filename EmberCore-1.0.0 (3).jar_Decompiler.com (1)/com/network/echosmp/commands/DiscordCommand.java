package com.network.echosmp.commands;

import com.network.echosmp.utility.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class DiscordCommand implements CommandExecutor {
   public DiscordCommand(JavaPlugin plugin) {
      plugin.getCommand("discord").setExecutor(this);
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (sender instanceof Player) {
         Player player = (Player)sender;
         player.sendMessage(ColorUtil.color("&r"));
         player.sendMessage(ColorUtil.color("&#00A2F8&lDISCORD"));
         player.sendMessage(ColorUtil.color("&r"));
         player.sendMessage(ColorUtil.color("&fCome and join our Discord Server to"));
         player.sendMessage(ColorUtil.color("&fmeet other community members!"));
         player.sendMessage(ColorUtil.color("&r"));
         player.sendMessage(ColorUtil.color("&#00A2F8discord.gg/echosmp"));
         player.sendMessage(ColorUtil.color("&r"));
         return true;
      } else {
         return true;
      }
   }
}
