package com.network.echosmp.commands;

import com.network.echosmp.menu.RulesMenu;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class RulesCommand implements CommandExecutor {
   private final RulesMenu rulesMenu;

   public RulesCommand(JavaPlugin plugin, RulesMenu rulesMenu) {
      this.rulesMenu = rulesMenu;
      plugin.getCommand("rules").setExecutor(this);
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (sender instanceof Player) {
         Player player = (Player)sender;
         this.rulesMenu.open(player);
         return true;
      } else {
         sender.sendMessage("§cOnly players can use this command!");
         return true;
      }
   }
}
