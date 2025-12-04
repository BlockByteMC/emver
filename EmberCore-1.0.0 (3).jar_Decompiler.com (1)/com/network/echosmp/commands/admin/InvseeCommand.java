package com.network.echosmp.commands.admin;

import com.network.echosmp.utility.ColorUtil;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

public class InvseeCommand implements CommandExecutor, TabCompleter, Listener {
   public InvseeCommand(JavaPlugin plugin) {
      plugin.getCommand("invsee").setExecutor(this);
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (sender instanceof Player) {
         Player player = (Player)sender;
         if (!player.hasPermission("echosmp.command.invsee")) {
            player.sendMessage(ColorUtil.color("&#FA2800You do not have permission to use this command"));
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
            return true;
         } else if (args.length != 1) {
            player.sendMessage("&aUsage: /invsee <player>");
            player.playSound(player.getLocation(), Sound.ENTITY_VILLAGER_NO, 1.0F, 1.0F);
            return true;
         } else {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
               player.sendMessage("Player not found");
               return true;
            } else {
               Inventory targetInv = target.getInventory();
               player.openInventory(targetInv);
               return true;
            }
         }
      } else {
         sender.sendMessage("Only players can use this command");
         return true;
      }
   }

   public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
      List<String> suggestions = new ArrayList();
      if (args.length == 1) {
         Iterator var6 = Bukkit.getOnlinePlayers().iterator();

         while(var6.hasNext()) {
            Player p = (Player)var6.next();
            if (p.getName().toLowerCase().startsWith(args[0].toLowerCase())) {
               suggestions.add(p.getName());
            }
         }
      }

      return suggestions;
   }
}
