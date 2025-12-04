package com.network.echosmp.commands.admin;

import com.network.echosmp.Main;
import com.network.echosmp.database.DatabaseManager;
import com.network.echosmp.utility.ColorUtil;
import com.network.echosmp.utility.LuckPermsHelper;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class VCunmuteCommand implements CommandExecutor {
   private final DatabaseManager db;
   private final JavaPlugin plugin;

   public VCunmuteCommand(JavaPlugin plugin) {
      this.plugin = plugin;
      this.db = Main.getInstance().getDatabaseManager();
      plugin.getCommand("vcunmute").setExecutor(this);
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      if (sender instanceof Player) {
         Player player = (Player)sender;
         if (!player.hasPermission("echosmp.command.vcban")) {
            player.sendMessage(ColorUtil.color("&cYou do not have permission to execute this command."));
            return true;
         } else if (args.length != 1) {
            player.sendMessage(ColorUtil.color("&cUsage: /vcunmute <player>"));
            return true;
         } else {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
               player.sendMessage(ColorUtil.color("&cPlayer not found."));
               return true;
            } else {
               UUID uuid = target.getUniqueId();
               this.db.isBanned(uuid).thenAcceptAsync((isBanned) -> {
                  if (!isBanned) {
                     player.sendMessage(ColorUtil.color("&cPlayer is not voice chat banned."));
                  } else {
                     this.db.removeBan(uuid).join();
                     LuckPermsHelper.setVoicechatPermissions(uuid, true);
                     player.sendMessage(ColorUtil.color("&aPlayer " + target.getName() + " has been unmuted."));
                     target.sendMessage(ColorUtil.color("&aYour voice chat ban has been lifted."));
                  }
               });
               return true;
            }
         }
      } else {
         sender.sendMessage(ColorUtil.color("&cOnly players can use this command."));
         return true;
      }
   }
}
