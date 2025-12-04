package com.network.echosmp.commands.admin;

import com.network.echosmp.Main;
import com.network.echosmp.database.BanRecord;
import com.network.echosmp.database.DatabaseManager;
import com.network.echosmp.utility.ColorUtil;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class VChistCommand implements CommandExecutor {
   private final DatabaseManager db;
   private final JavaPlugin plugin;

   public VChistCommand(JavaPlugin plugin) {
      this.plugin = plugin;
      this.db = Main.getInstance().getDatabaseManager();
      plugin.getCommand("vchist").setExecutor(this);
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      if (args.length != 1) {
         sender.sendMessage(ColorUtil.color("&cUsage: /vchist <player>"));
         return true;
      } else if (sender instanceof Player) {
         Player player = (Player)sender;
         if (!player.hasPermission("echosmp.command.vcban")) {
            player.sendMessage(ColorUtil.color("&cYou do not have permission to execute this command."));
            return true;
         } else {
            UUID uuid = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
            this.db.getHistory(uuid).thenAccept((history) -> {
               if (history.isEmpty()) {
                  sender.sendMessage(ColorUtil.color("&#8ca8ffNo voice chat ban history for &#ffffff" + args[0]));
               } else {
                  SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
                  sender.sendMessage(ColorUtil.color("&#8ca8ff--- Voice Chat Ban History for &#ffffff" + args[0] + " &#8ca8ff---"));
                  int index = 1;
                  Iterator var5 = history.iterator();

                  while(var5.hasNext()) {
                     BanRecord record = (BanRecord)var5.next();
                     long expiresAt = record.getBanTime() + record.getDuration();
                     String expires = record.getDuration() == 0L ? "&cNever" : "&f" + sdf.format(expiresAt);
                     int var10001 = index++;
                     sender.sendMessage(ColorUtil.color("&#ff8a8a#" + var10001 + " &7By: &f" + record.getBannedBy() + " &8| &7Until: " + expires + " &8| &7Reason: &f" + record.getReason()));
                  }

               }
            });
            return true;
         }
      } else {
         sender.sendMessage(ColorUtil.color("&cOnly players can run this command."));
         return true;
      }
   }
}
