package com.network.echosmp.commands;

import com.network.echosmp.Main;
import com.network.echosmp.commands.admin.SetAfkCommand;
import com.network.echosmp.utility.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class AfkCommand implements CommandExecutor {
   public AfkCommand(JavaPlugin plugin) {
      plugin.getCommand("afk").setExecutor(this);
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      final Location afkLoc = SetAfkCommand.getAfkLocation();
      if (afkLoc == null) {
         sender.sendMessage(ColorUtil.color("&cThe AFK location is not set."));
         return true;
      } else {
         final Player player;
         if (args.length == 1) {
            if (!sender.hasPermission("echo.command.afk.others")) {
               sender.sendMessage(ColorUtil.color("&cYou do not have permission to teleport others to AFK."));
               return true;
            } else {
               player = Bukkit.getPlayerExact(args[0]);
               if (player == null) {
                  sender.sendMessage(ColorUtil.color("&cThat player is not online."));
                  return true;
               } else {
                  player.teleport(afkLoc);
                  player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                  player.sendActionBar(ColorUtil.color("&7You were teleported to the AFK Zone."));
                  sender.sendMessage(ColorUtil.color("&7You teleported &#35F527" + player.getName() + " &7to the AFK Zone."));
                  return true;
               }
            }
         } else if (sender instanceof Player) {
            player = (Player)sender;
            if (player.hasPermission("echosmp.command.bypass.afk")) {
               player.teleport(afkLoc);
               player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
               player.sendActionBar(ColorUtil.color("&7You were teleported to the AFK Zone."));
               return true;
            } else {
               final int[] countdown = new int[]{5};
               final Location initialLocation = player.getLocation().clone();
               (new BukkitRunnable() {
                  public void run() {
                     if (!player.isOnline()) {
                        this.cancel();
                     } else if (countdown[0] <= 0) {
                        player.teleport(afkLoc);
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                        player.sendActionBar(ColorUtil.color("&7You were teleported to the AFK Zone."));
                        this.cancel();
                     } else if (AfkCommand.this.hasMoved(initialLocation, player.getLocation())) {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
                        player.sendActionBar(ColorUtil.color("&cTeleport cancelled because you moved."));
                        this.cancel();
                     } else {
                        player.sendActionBar(ColorUtil.color("&7Teleporting to AFK in &#27BEF5" + countdown[0] + " &7seconds..."));
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                        int var10002 = countdown[0]--;
                     }
                  }
               }).runTaskTimer(Main.getInstance(), 0L, 20L);
               return true;
            }
         } else {
            sender.sendMessage("Only players can use this command without specifying a target!");
            return true;
         }
      }
   }

   private boolean hasMoved(Location from, Location to) {
      return from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ();
   }
}
