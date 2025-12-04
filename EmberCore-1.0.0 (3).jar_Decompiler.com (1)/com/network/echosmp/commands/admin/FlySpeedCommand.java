package com.network.echosmp.commands.admin;

import com.network.echosmp.utility.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class FlySpeedCommand implements CommandExecutor {
   public FlySpeedCommand(JavaPlugin plugin) {
      plugin.getCommand("flyspeed").setExecutor(this);
   }

   public boolean onCommand(CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      if (!sender.hasPermission("echosmp.command.flyspeed")) {
         sender.sendMessage(ColorUtil.color("&#FB0000You do not have permission to use this command."));
         return true;
      } else if (args.length == 0) {
         sender.sendMessage(ColorUtil.color("&#FB0000Usage: /flyspeed <1-10> [player]"));
         return true;
      } else {
         int speed;
         try {
            speed = Integer.parseInt(args[0]);
         } catch (NumberFormatException var7) {
            sender.sendMessage(ColorUtil.color("&#FB0000Please enter a valid number between 1 and 10."));
            return true;
         }

         if (speed >= 1 && speed <= 10) {
            Player target;
            if (args.length == 1) {
               if (!(sender instanceof Player)) {
                  sender.sendMessage(ColorUtil.color("&#FB0000Only players can set their own fly speed."));
                  return true;
               }

               target = (Player)sender;
            } else {
               target = Bukkit.getPlayerExact(args[1]);
               if (target == null) {
                  sender.sendMessage(ColorUtil.color("&#FB0000That player is not online."));
                  return true;
               }
            }

            target.setFlySpeed((float)speed / 10.0F);
            target.sendMessage(ColorUtil.color("&#27BEF5Your fly speed has been set to &7" + speed));
            if (!target.equals(sender)) {
               String var10001 = target.getName();
               sender.sendMessage(ColorUtil.color("&#27BEF5You have set &7" + var10001 + "'s &#78c7fefly speed to &7" + speed));
            }

            return true;
         } else {
            sender.sendMessage(ColorUtil.color("&#FB0000Please enter a number between 1 and 10."));
            return true;
         }
      }
   }
}
