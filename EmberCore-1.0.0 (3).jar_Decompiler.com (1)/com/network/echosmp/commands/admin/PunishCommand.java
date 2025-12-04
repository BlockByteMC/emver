package com.network.echosmp.commands.admin;

import com.network.echosmp.Main;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;

public class PunishCommand implements CommandExecutor, TabCompleter {
   private final Main plugin;
   private final ExecutorService asyncExecutor;

   public PunishCommand(Main plugin) {
      this.plugin = plugin;
      this.asyncExecutor = Executors.newSingleThreadExecutor();
      if (plugin.getCommand("punish") != null) {
         plugin.getCommand("punish").setExecutor(this);
         plugin.getCommand("punish").setPermission("echosmp.command.punish");
      }

   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      if (!sender.hasPermission("echosmp.command.punish")) {
         sender.sendMessage("§cYou do not have permission to use this command.");
         return true;
      } else if (args.length < 2) {
         sender.sendMessage("§cUsage: /punish <player> <category>");
         return true;
      } else {
         String target = args[0];
         String categoryInput = String.join(" ", (CharSequence[])Arrays.copyOfRange(args, 1, args.length));
         PunishCommand.PunishCategory category = PunishCommand.PunishCategory.fromString(categoryInput);
         if (category == null) {
            sender.sendMessage("§cInvalid category. Available: " + PunishCommand.PunishCategory.listAll());
            return true;
         } else {
            String consoleCmd = category.getCommandPattern() != null ? String.format(category.getCommandPattern(), target) : null;
            if (consoleCmd != null) {
               this.asyncExecutor.execute(() -> {
                  Bukkit.getScheduler().runTask(this.plugin, () -> {
                     this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), consoleCmd);
                  });
               });
            }

            sender.sendMessage("§aPunished §e" + target + "§a for §e" + category.getDisplayName() + "§a (" + category.getDescription() + ")");
            return true;
         }
      }
   }

   public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String[] args) {
      if (args.length == 1) {
         return Bukkit.getOnlinePlayers().stream().map((p) -> {
            return p.getName();
         }).filter((name) -> {
            return name.toLowerCase().startsWith(args[0].toLowerCase());
         }).toList();
      } else {
         return args.length == 2 ? PunishCommand.PunishCategory.matches(args[1]) : List.of();
      }
   }

   private static enum PunishCategory {
      RACISM("Racism/Discriminatory Behavior", "mute %s 1d", "Used racist language in chat"),
      HATE_SPEECH("Hate Speech", "mute %s 1d", "Promoted hate speech against others"),
      HARASSMENT("Harassment", "mute %s 1h", "Harassing other players repeatedly"),
      SPAMMING("Spamming", "mute %s 30m", "Spamming chat with messages or caps"),
      BYPASS_CHAT_FILTERS("Bypassing Chat Filters", "mute %s 6h", "Attempted to bypass chat filters"),
      TOXIC_BEHAVIOR("Toxic Behavior", (String)null, "Exhibited toxic behavior toward players"),
      DEATH_THREATS("Death Threats", "mute %s 3d", "Threatened another player's life"),
      EXTREME_TOXICITY("Extreme Toxicity/Threats", "kick %s", "Extreme toxicity or threats in chat"),
      SEVERE_HARASSMENT("Severe Harassment", "ban %s 3d", "Severely harassed a player"),
      SEVERE_HATE_RACISM("Severe Hate / Racism", "ban %s 3d", "Severe hate speech or racist behavior"),
      ADVERTISING("Advertising", "mute %s 2d", "Promoted external servers"),
      CHEATING_EXPLOITING("Cheating/Exploiting", (String)null, "Suspected cheating or exploiting"),
      CHEATING_SOFTWARE_OPEN("Cheating Software Open", "ban %s 1d", "Used cheating software openly"),
      XRAY_ESP("X-ray / ESP", "ban %s 2d", "Used X-ray or ESP hacks"),
      AUTO_TOTEM("Auto Totem", "ban %s 7d", "Used auto-totem exploit"),
      SPEED_FLY("Speed / Fly Exploits", "ban %s 10d", "Speed hacking or flying illegally"),
      DUPING("Duping", "ban %s 60d", "Duplicated items illegally"),
      LAG_MACHINE("Trying to make a Lag Machine", "banip %s", "Attempted to crash the server"),
      BAN_EVADING("Ban Evading", "ban %s 90d", "Evading previous bans"),
      KILL_AURA("Kill Aura / Crystal etc.", "ban %s 3d", "Using Kill Aura or similar hacks"),
      HEALTH_INDICATORS("Health Indicators", "ban %s 1d", "Using health indicators cheat");

      private final String displayName;
      private final String commandPattern;
      private final String description;

      private PunishCategory(String param3, String param4, String param5) {
         this.displayName = displayName;
         this.commandPattern = commandPattern;
         this.description = description;
      }

      public String getDisplayName() {
         return this.displayName;
      }

      public String getCommandPattern() {
         return this.commandPattern;
      }

      public String getDescription() {
         return this.description;
      }

      public static PunishCommand.PunishCategory fromString(String input) {
         PunishCommand.PunishCategory[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            PunishCommand.PunishCategory c = var1[var3];
            if (c.displayName.equalsIgnoreCase(input)) {
               return c;
            }
         }

         return null;
      }

      public static String listAll() {
         StringBuilder sb = new StringBuilder();
         PunishCommand.PunishCategory[] var1 = values();
         int var2 = var1.length;

         for(int var3 = 0; var3 < var2; ++var3) {
            PunishCommand.PunishCategory c = var1[var3];
            sb.append(c.displayName).append(", ");
         }

         if (sb.length() > 2) {
            sb.setLength(sb.length() - 2);
         }

         return sb.toString();
      }

      public static List<String> matches(String prefix) {
         String lower = prefix.toLowerCase();
         return Arrays.stream(values()).map(PunishCommand.PunishCategory::getDisplayName).filter((name) -> {
            return name.toLowerCase().startsWith(lower);
         }).toList();
      }

      // $FF: synthetic method
      private static PunishCommand.PunishCategory[] $values() {
         return new PunishCommand.PunishCategory[]{RACISM, HATE_SPEECH, HARASSMENT, SPAMMING, BYPASS_CHAT_FILTERS, TOXIC_BEHAVIOR, DEATH_THREATS, EXTREME_TOXICITY, SEVERE_HARASSMENT, SEVERE_HATE_RACISM, ADVERTISING, CHEATING_EXPLOITING, CHEATING_SOFTWARE_OPEN, XRAY_ESP, AUTO_TOTEM, SPEED_FLY, DUPING, LAG_MACHINE, BAN_EVADING, KILL_AURA, HEALTH_INDICATORS};
      }
   }
}
