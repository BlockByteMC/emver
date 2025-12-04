package com.network.echosmp.listener;

import com.network.echosmp.utility.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class ArenaReset {
   private final JavaPlugin plugin;

   public ArenaReset(JavaPlugin plugin) {
      this.plugin = plugin;
      this.startTask();
   }

   private void startTask() {
      Bukkit.getScheduler().runTaskTimer(this.plugin, () -> {
         Bukkit.broadcastMessage(ColorUtil.color("&#31BCFF&lEMBER &7» &cThe arena is resetting now..."));
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "platinumarenas:arena reset arena extreme");
      }, 0L, 12000L);
   }
}
