package com.network.echosmp.commands;

import com.network.echosmp.menu.RanksMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RanksCommand implements CommandExecutor {
   private final RanksMenu ranksMenu;

   public RanksCommand(JavaPlugin plugin, RanksMenu ranksMenu) {
      this.ranksMenu = ranksMenu;
      plugin.getCommand("ranks").setExecutor(this);
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (sender instanceof Player) {
         Player player = (Player)sender;
         this.ranksMenu.open(player);
         return true;
      } else {
         sender.sendMessage("§cOnly players can use this command!");
         return true;
      }
   }
}
