package com.network.echosmp.commands;

import com.network.echosmp.utility.ColorUtil;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class LiveCommand implements CommandExecutor, TabCompleter {
   private static final Map<String, String> PLATFORMS = new LinkedHashMap();
   private static final long COOLDOWN_TIME_MS = 120000L;
   private final Map<UUID, Long> cooldowns = new HashMap();

   public LiveCommand(JavaPlugin plugin) {
      plugin.getCommand("live").setExecutor(this);
      plugin.getCommand("live").setTabCompleter(this);
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      if (sender instanceof Player) {
         Player player = (Player)sender;
         if (!player.hasPermission("echosmp.command.live")) {
            player.sendMessage(ColorUtil.color("&#FB2F2FYou don't have permission to use this command."));
            return true;
         } else {
            if (!player.hasPermission("echosmp.command.live.bypass")) {
               Long lastUsed = (Long)this.cooldowns.get(player.getUniqueId());
               long now = System.currentTimeMillis();
               if (lastUsed != null && now - lastUsed < 120000L) {
                  long remainingSeconds = (120000L - (now - lastUsed)) / 1000L;
                  player.sendMessage(ColorUtil.color("&#FB2F2FYou must wait " + remainingSeconds + " seconds before using this command again."));
                  return true;
               }
            }

            if (args.length != 0 && PLATFORMS.containsKey(args[0].toLowerCase())) {
               String platform = args[0].toLowerCase();
               String urlPrefix = (String)PLATFORMS.get(platform);
               if (args.length < 2) {
                  player.sendMessage(ColorUtil.color("&#FB2F2FPlease provide an valid streaming URL."));
                  return true;
               } else {
                  String link = args[1];
                  if (!link.startsWith(urlPrefix)) {
                     player.sendMessage(ColorUtil.color("&#FB2F2FPlease provide an valid streaming URL."));
                     return true;
                  } else {
                     String afterPrefix = link.substring(urlPrefix.length());
                     if (afterPrefix.length() >= 3 && !afterPrefix.contains(" ")) {
                        this.cooldowns.put(player.getUniqueId(), System.currentTimeMillis());
                        char var10000 = Character.toUpperCase(platform.charAt(0));
                        String prettyPlatform = var10000 + platform.substring(1);
                        CharSequence[] var10001 = new CharSequence[]{"&r", "&#27BEF5&lLIVESTREAM AD", "&r", null, null, null};
                        String var10004 = player.getName();
                        var10001[3] = "&#27BEF5" + var10004 + " &7is now livestreaming on &#27BEF5" + prettyPlatform + "&7.";
                        var10001[4] = "&7Go check them out: &#27BEF5" + link;
                        var10001[5] = "&r";
                        String message = String.join("\n", var10001);
                        sender.getServer().broadcastMessage(ColorUtil.color(message));
                        return true;
                     } else {
                        player.sendMessage(ColorUtil.color("&#FB2F2FThe streaming URL must have at least 3 characters after the prefix, and contain no spaces."));
                        return true;
                     }
                  }
               }
            } else {
               player.sendMessage(ColorUtil.color("&#FB2F2FPlease provide an valid streaming platform."));
               return true;
            }
         }
      } else {
         sender.sendMessage(ColorUtil.color("&#FB2F2FOnly players can run this command."));
         return true;
      }
   }

   public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
      String platform;
      if (args.length == 1) {
         platform = args[0].toLowerCase();
         return (List)PLATFORMS.keySet().stream().filter((p) -> {
            return p.startsWith(platform);
         }).collect(Collectors.toList());
      } else {
         if (args.length == 2) {
            platform = args[0].toLowerCase();
            if (PLATFORMS.containsKey(platform)) {
               return Collections.singletonList((String)PLATFORMS.get(platform));
            }
         }

         return Collections.emptyList();
      }
   }

   static {
      PLATFORMS.put("tiktok", "https://tiktok.com/@");
      PLATFORMS.put("youtube", "https://youtube.com/");
      PLATFORMS.put("twitch", "https://twitch.tv/");
   }
}
