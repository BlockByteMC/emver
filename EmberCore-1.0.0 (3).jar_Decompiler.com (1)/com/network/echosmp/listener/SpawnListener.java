package com.network.echosmp.listener;

import com.network.echosmp.commands.admin.SetSpawnCommand;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.Plugin;

public class SpawnListener implements Listener {
   public SpawnListener(Plugin plugin) {
      Bukkit.getPluginManager().registerEvents(this, plugin);
   }

   @EventHandler
   public void onRespawn(PlayerRespawnEvent event) {
      if (SetSpawnCommand.getExactSpawnLocation() != null) {
         event.setRespawnLocation(SetSpawnCommand.getExactSpawnLocation());
      }

   }

   @EventHandler
   public void onPlayerFirstJoin(PlayerJoinEvent event) {
      if (!event.getPlayer().hasPlayedBefore() && SetSpawnCommand.getExactSpawnLocation() != null) {
         event.getPlayer().teleport(SetSpawnCommand.getExactSpawnLocation());
      }

   }
}
