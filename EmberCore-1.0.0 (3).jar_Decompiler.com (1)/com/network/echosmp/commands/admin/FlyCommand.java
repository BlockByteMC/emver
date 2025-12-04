package com.network.echosmp.commands.admin;

import com.network.echosmp.utility.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class FlyCommand implements CommandExecutor {
   public FlyCommand(JavaPlugin plugin) {
      plugin.getCommand("fly").setExecutor(this);
   }

   public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      if (!sender.hasPermission("echosmp.command.fly")) {
         sender.sendMessage(ColorUtil.color("&#FB0000You do not have permission to use this command."));
         return true;
      } else {
         Player player = null;
         if (sender instanceof Player) {
            player = (Player)sender;
         }

         if (player == null && args.length == 0) {
            sender.sendMessage(ColorUtil.color("&#FB0000Only players can use this command without specifying a target."));
            return true;
         } else {
            if (args.length == 0) {
               this.toggleFly(player, player);
            } else {
               Player target = Bukkit.getPlayerExact(args[0]);
               if (target == null) {
                  sender.sendMessage(ColorUtil.color("&#FB0000That player is not online."));
                  return true;
               }

               this.toggleFly(sender, target);
            }

            return true;
         }
      }
   }

   private void toggleFly(CommandSender executor, Player target) {
      boolean enable = !target.getAllowFlight();
      target.setAllowFlight(enable);
      if (!enable && target.getGameMode() != GameMode.CREATIVE) {
         target.setFlying(false);
      }

      target.sendMessage(enable ? ColorUtil.color("&#27BEF5our flight mode has been enabled.") : ColorUtil.color("&#27BEF5Your flight mode has been disabled."));
      if (!target.equals(executor)) {
         executor.sendMessage(ColorUtil.color("&#27BEF5You have " + (enable ? "enabled" : "disabled") + " flight for &7" + target.getName()));
      }

   }
}
