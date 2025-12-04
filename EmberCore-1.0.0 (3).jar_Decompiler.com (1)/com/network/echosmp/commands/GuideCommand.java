package com.network.echosmp.commands;

import com.network.echosmp.menu.GuideMenu;
import com.network.echosmp.utility.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class GuideCommand implements CommandExecutor {
   private GuideMenu guideMenu;

   public GuideCommand(JavaPlugin plugin, GuideMenu guideMenu) {
      this.guideMenu = guideMenu;
      plugin.getCommand("guide").setExecutor(this);
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
      if (sender instanceof Player) {
         Player player = (Player)sender;
         this.guideMenu.open(player);
         return true;
      } else {
         sender.sendMessage(ColorUtil.color("&cOnly players can open the guide."));
         return true;
      }
   }
}
