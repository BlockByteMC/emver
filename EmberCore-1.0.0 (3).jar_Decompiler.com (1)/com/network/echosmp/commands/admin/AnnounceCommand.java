package com.network.echosmp.commands.admin;

import com.network.echosmp.utility.ColorUtil;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class AnnounceCommand implements CommandExecutor {
   public AnnounceCommand(JavaPlugin plugin) {
      plugin.getCommand("announce").setExecutor(this);
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if (!(sender instanceof Player)) {
         sender.sendMessage("Only Players can use this command.");
         return true;
      } else {
         Player player = (Player)sender;
         if (!player.hasPermission("echosmp.admin.announce")) {
            player.sendActionBar(ColorUtil.color("&cYou don't have permission to use this command!"));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
            return true;
         } else if (args.length == 0) {
            player.sendMessage("Usage: /announce <message>");
            return true;
         } else {
            String message = String.join(" ", args);
            String formattedMessage = ColorUtil.color(message);
            String title = ColorUtil.color("&#27BEF5&lANNOUNCEMENT");
            Iterator var9 = Bukkit.getOnlinePlayers().iterator();

            while(var9.hasNext()) {
               Player target = (Player)var9.next();
               target.sendTitle(title, formattedMessage, 10, 60, 10);
               target.playSound(target.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 1.0F, 1.0F);
            }

            return true;
         }
      }
   }
}
