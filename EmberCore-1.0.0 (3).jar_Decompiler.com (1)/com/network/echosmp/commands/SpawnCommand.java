package com.network.echosmp.commands;

import com.network.echosmp.Main;
import com.network.echosmp.commands.admin.SetSpawnCommand;
import com.network.echosmp.utility.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class SpawnCommand implements CommandExecutor {
   public SpawnCommand(JavaPlugin plugin) {
      plugin.getCommand("spawn").setExecutor(this);
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      final Location spawn = SetSpawnCommand.getExactSpawnLocation();
      if (spawn == null) {
         sender.sendMessage(ColorUtil.color("&cSpawn location is not set."));
         return true;
      } else {
         final Player player;
         if (args.length == 1) {
            if (!sender.hasPermission("echosmp.spawn.others")) {
               sender.sendMessage(ColorUtil.color("&cYou do not have permission to teleport others to spawn."));
               return true;
            } else {
               player = Bukkit.getPlayerExact(args[0]);
               if (player == null) {
                  sender.sendMessage(ColorUtil.color("&cThat player is not online."));
                  return true;
               } else {
                  player.teleport(spawn);
                  player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                  player.sendActionBar(ColorUtil.color("&7You were teleported to spawn"));
                  sender.sendMessage(ColorUtil.color("&7You teleported &#35F527" + player.getName() + " &7to spawn."));
                  return true;
               }
            }
         } else if (sender instanceof Player) {
            player = (Player)sender;
            World world = player.getWorld();
            boolean var8 = player.hasPermission("echosmp.bypass.spawn");
            if (!var8 && !world.getName().equalsIgnoreCase("spawn") && !world.getName().equalsIgnoreCase("afk")) {
               final int[] countdown = new int[]{5};
               final Location initialLocation = player.getLocation().clone();
               (new BukkitRunnable() {
                  public void run() {
                     if (!player.isOnline()) {
                        this.cancel();
                     } else if (countdown[0] <= 0) {
                        player.teleport(spawn);
                        player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
                        player.sendActionBar(ColorUtil.color("&7You were teleported to spawn"));
                        this.cancel();
                     } else if (SpawnCommand.this.hasMoved(initialLocation, player.getLocation())) {
                        player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
                        player.sendActionBar(ColorUtil.color("&cTeleport cancelled because you moved."));
                        this.cancel();
                     } else {
                        player.sendActionBar(ColorUtil.color("&7Teleporting in &#27BEF5" + countdown[0] + " &7seconds..."));
                        player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
                        int var10002 = countdown[0]--;
                     }
                  }
               }).runTaskTimer(Main.getPlugin(Main.class), 0L, 20L);
               return true;
            } else {
               player.teleport(spawn);
               player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0F, 1.0F);
               player.sendActionBar(ColorUtil.color("&7You were teleported to spawn"));
               return true;
            }
         } else {
            sender.sendMessage("Only players can run this command without a target!");
            return true;
         }
      }
   }

   private boolean hasMoved(Location from, Location to) {
      return from.getBlockX() != to.getBlockX() || from.getBlockY() != to.getBlockY() || from.getBlockZ() != to.getBlockZ();
   }
}
