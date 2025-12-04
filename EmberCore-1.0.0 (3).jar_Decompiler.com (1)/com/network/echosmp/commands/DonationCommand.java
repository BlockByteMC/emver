package com.network.echosmp.commands;

import com.network.echosmp.utility.ColorUtil;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class DonationCommand implements CommandExecutor, Listener {
   private final JavaPlugin plugin;
   private boolean ggWaveActive = false;
   private final List<String> ggColors = List.of("&#F54927", "&#27F554", "&#27BEF5", "&#F5E327", "&#F527E0");
   private final Random random = new Random();

   public DonationCommand(JavaPlugin plugin) {
      this.plugin = plugin;
      plugin.getCommand("donation").setExecutor(this);
      plugin.getServer().getPluginManager().registerEvents(this, plugin);
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (!sender.hasPermission("echosmp.command.donation")) {
         sender.sendMessage(ColorUtil.color("&#31BCFF&lECHO &7» &#FF0000You don't have permission to use this command"));
         return true;
      } else if (args.length != 2) {
         sender.sendMessage(ColorUtil.color("&#31BCFF&lECHO &7» &#FF0000Usage: /donation <player> <amount>"));
         return true;
      } else {
         Player target = Bukkit.getPlayer(args[0]);
         if (target == null) {
            sender.sendMessage(ColorUtil.color("&#31BCFF&lECHO &7» &#FF0000Player not found"));
            return true;
         } else {
            String amount = args[1] + "$";
            String var10000 = target.getName();
            Bukkit.broadcastMessage(ColorUtil.color("&7Thank you &#27BEF5" + var10000 + " &7for spending &#27BEF5&l" + amount + " &7on the store"));
            Bukkit.broadcastMessage(ColorUtil.color("&r"));
            Bukkit.broadcastMessage(ColorUtil.color("&r"));
            Bukkit.broadcastMessage(ColorUtil.color("&#27BEF5&lGG WAVE HAS STARTED FOR 20 SECONDS!"));
            this.ggWaveActive = true;
            (new BukkitRunnable() {
               public void run() {
                  DonationCommand.this.ggWaveActive = false;
                  Bukkit.broadcastMessage(ColorUtil.color("&#27BEF5&lGG WAVE HAS ENDED, THANK YOU FOR PARTICIPATING"));
               }
            }).runTaskLater(this.plugin, 400L);
            return true;
         }
      }
   }

   @EventHandler
   public void onPlayerChat(AsyncPlayerChatEvent event) {
      if (this.ggWaveActive) {
         String msg = event.getMessage();
         if (msg.equalsIgnoreCase("gg")) {
            event.setCancelled(true);
            Player player = event.getPlayer();
            String prefix = "";
            LuckPerms luckPerms = LuckPermsProvider.get();
            User user = luckPerms.getUserManager().getUser(player.getUniqueId());
            if (user != null && user.getCachedData().getMetaData().getPrefix() != null) {
               prefix = user.getCachedData().getMetaData().getPrefix();
            }

            String randomColor = (String)this.ggColors.get(this.random.nextInt(this.ggColors.size()));
            String formatted = ColorUtil.color(prefix + player.getName() + " " + randomColor + "&lGG ");
            Iterator var9 = Bukkit.getOnlinePlayers().iterator();

            while(var9.hasNext()) {
               Player p = (Player)var9.next();
               p.sendMessage(formatted);
            }
         }

      }
   }
}
