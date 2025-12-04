package com.network.echosmp.commands;

import com.network.echosmp.utility.ColorUtil;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class PingCommand implements CommandExecutor {
   public PingCommand(JavaPlugin plugin) {
      plugin.getCommand("ping").setExecutor(this);
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if (sender instanceof Player) {
         Player player = (Player)sender;
         int ping = player.getPing();
         if (args.length == 0) {
            player.sendMessage(ColorUtil.color("&#78CB00⚡ &fYour ping is &#0398fc" + ping + "ms"));
            return true;
         } else if (args.length == 1) {
            if (args[0].equalsIgnoreCase(player.getName())) {
               player.sendMessage(ColorUtil.color("&#78CB00⚡ &fYour ping is &#0398fc" + ping + "ms"));
               return true;
            } else {
               Player target = player.getServer().getPlayerExact(args[0]);
               if (target != null && target.isOnline()) {
                  int targetPing = target.getPing();
                  String var10001 = target.getName();
                  player.sendMessage(ColorUtil.color("&#78CB00⚡ &f" + var10001 + "'s ping is &#0398fc" + targetPing + "ms"));
               } else {
                  player.sendActionBar(ColorUtil.color("&cThat player is not online."));
                  player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
               }

               return true;
            }
         } else {
            return true;
         }
      } else {
         sender.sendMessage("Only players can use this command.");
         return true;
      }
   }
}
