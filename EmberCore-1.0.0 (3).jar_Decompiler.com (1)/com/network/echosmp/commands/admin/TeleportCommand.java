package com.network.echosmp.commands.admin;

import com.network.echosmp.utility.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class TeleportCommand implements CommandExecutor {
   private final JavaPlugin plugin;

   public TeleportCommand(JavaPlugin plugin) {
      this.plugin = plugin;
      plugin.getCommand("tp").setExecutor(this);
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if (sender instanceof Player) {
         Player player = (Player)sender;
         if (!player.hasPermission("echosmp.command.tp")) {
            player.sendActionBar(ColorUtil.color("&cYou don't have permission to use this command!"));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
            return true;
         } else {
            try {
               Player target;
               if (args.length == 1) {
                  target = Bukkit.getPlayer(args[0]);
                  if (target != null) {
                     player.teleportAsync(target.getLocation(), TeleportCause.PLUGIN).thenAccept((success) -> {
                        if (success) {
                           player.sendMessage(ColorUtil.color("&#27BEF5Teleported to &f" + target.getName()));
                        } else {
                           player.sendMessage(ColorUtil.color("&#ff5555A Problem occurred while teleporting, please check the console."));
                        }

                     });
                  } else {
                     player.sendMessage(ColorUtil.color("&#ff5555Player is not found."));
                  }
               } else if (args.length == 2) {
                  target = Bukkit.getPlayer(args[0]);
                  Player to = Bukkit.getPlayer(args[1]);
                  if (target == null || to == null) {
                     player.sendMessage(ColorUtil.color("&#ff5555One or both players not found."));
                     return true;
                  }

                  target.teleportAsync(to.getLocation(), TeleportCause.PLUGIN).thenAccept((success) -> {
                     if (success) {
                        String var10001 = target.getName();
                        player.sendMessage(ColorUtil.color("&#27BEF5Teleported &f" + var10001 + " &7to &f" + to.getName()));
                     } else {
                        player.sendMessage(ColorUtil.color("&#ff5555A Problem occurred while teleporting, please check the console."));
                     }

                  });
               } else if (args.length == 3) {
                  double x = Double.parseDouble(args[0]);
                  double y = Double.parseDouble(args[1]);
                  double z = Double.parseDouble(args[2]);
                  Location loc = new Location(player.getWorld(), x, y, z);
                  player.teleportAsync(loc, TeleportCause.PLUGIN).thenAccept((success) -> {
                     if (success) {
                        player.sendMessage(ColorUtil.color("&#27BEF5Teleported to coordinates &f(" + x + ", " + y + ", " + z + ")"));
                     } else {
                        player.sendMessage(ColorUtil.color("&#ff5555An unknown error occurred."));
                     }

                  });
               } else if (args.length == 4) {
                  target = Bukkit.getPlayer(args[0]);
                  if (target == null) {
                     player.sendMessage(ColorUtil.color("&#ff5555Player not found."));
                     return true;
                  }

                  double x = Double.parseDouble(args[1]);
                  double y = Double.parseDouble(args[2]);
                  double z = Double.parseDouble(args[3]);
                  Location loc = new Location(target.getWorld(), x, y, z);
                  target.teleportAsync(loc, TeleportCause.PLUGIN).thenAccept((success) -> {
                     if (success) {
                        player.sendMessage(ColorUtil.color("&#27BEF5Teleported &f" + target.getName() + " &7to &f(" + x + ", " + y + ", " + z + ")"));
                     } else {
                        player.sendMessage(ColorUtil.color("&#ff5555An unknown error occurred."));
                     }

                  });
               } else {
                  player.sendMessage(ColorUtil.color("&#ff5555Usage:"));
                  player.sendMessage(ColorUtil.color("&#27BEF5/tp <player>"));
                  player.sendMessage(ColorUtil.color("&#27BEF5/tp <player1> <player2>"));
                  player.sendMessage(ColorUtil.color("&#27BEF5/tp <x> <y> <z>"));
                  player.sendMessage(ColorUtil.color("&#27BEF5/tp <player> <x> <y> <z>"));
               }
            } catch (NumberFormatException var14) {
               player.sendMessage(ColorUtil.color("&#ff5555Invalid coordinates."));
            }

            return true;
         }
      } else {
         sender.sendMessage("Only players can use this command.");
         return true;
      }
   }
}
