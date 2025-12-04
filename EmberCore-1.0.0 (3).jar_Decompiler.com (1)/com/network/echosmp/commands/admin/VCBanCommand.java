package com.network.echosmp.commands.admin;

import com.network.echosmp.Main;
import com.network.echosmp.database.DatabaseManager;
import com.network.echosmp.utility.ColorUtil;
import com.network.echosmp.utility.LuckPermsHelper;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class VCBanCommand implements CommandExecutor {
   private final DatabaseManager db;
   private final JavaPlugin plugin;

   public VCBanCommand(JavaPlugin plugin) {
      this.plugin = plugin;
      this.db = Main.getInstance().getDatabaseManager();
      plugin.getCommand("vcban").setExecutor(this);
      this.startExpirationChecker();
   }

   public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
      if (args.length < 2) {
         sender.sendMessage(ColorUtil.color("&cUsage: /vcban <player> <time> <reason>"));
         return true;
      } else if (sender instanceof Player) {
         Player player = (Player)sender;
         if (!player.hasPermission("echosmp.command.vcban")) {
            player.sendMessage(ColorUtil.color("&cYou do not have permission to execute this command."));
            return true;
         } else {
            Player target = Bukkit.getPlayerExact(args[0]);
            if (target == null) {
               sender.sendMessage(ColorUtil.color("&cPlayer not found!"));
               return true;
            } else {
               long durationMs;
               try {
                  durationMs = this.parseTime(args[1]);
               } catch (IllegalArgumentException var13) {
                  sender.sendMessage(ColorUtil.color("&cInvalid time format! Examples: 10m, 1h, 30s"));
                  return true;
               }

               String reason = args.length > 2 ? String.join(" ", (CharSequence[])Arrays.copyOfRange(args, 2, args.length)) : "No reason";
               UUID targetUUID = target.getUniqueId();
               String targetName = target.getName();
               String banner = sender.getName();
               this.db.isBanned(targetUUID).thenAccept((banned) -> {
                  if (banned) {
                     sender.sendMessage(ColorUtil.color("&cPlayer is already voice chat banned!"));
                  } else {
                     this.db.banPlayer(targetUUID, targetName, banner, reason, durationMs).join();
                     LuckPermsHelper.setVoicechatPermissions(targetUUID, false);
                     target.sendMessage(ColorUtil.color("&#ff5555You have been voice chat banned for &#ffffff" + args[1] + "&#ff5555. Reason: &#ffffff" + reason));
                     sender.sendMessage(ColorUtil.color("&#8ca8ffPlayer &#ffffff" + targetName + " &#8ca8ffhas been voice chat banned."));
                  }
               });
               return true;
            }
         }
      } else {
         sender.sendMessage(ColorUtil.color("&cOnly players can use this command."));
         return true;
      }
   }

   private void startExpirationChecker() {
      Bukkit.getScheduler().runTaskTimerAsynchronously(this.plugin, () -> {
         String selectSql = "SELECT uuid, ban_time, duration FROM vc_bans WHERE unmuted = false";
         String updateSql = "UPDATE vc_bans SET unmuted = true WHERE uuid = ?";

         try {
            Connection conn = this.db.getConnection();

            try {
               PreparedStatement ps = conn.prepareStatement(selectSql);

               try {
                  ResultSet rs = ps.executeQuery();

                  try {
                     PreparedStatement updatePs = conn.prepareStatement(updateSql);

                     try {
                        while(rs.next()) {
                           UUID playerUUID = UUID.fromString(rs.getString("uuid"));
                           long banTime = rs.getLong("ban_time");
                           long duration = rs.getLong("duration");
                           long expiresAt = banTime + duration;
                           if (System.currentTimeMillis() >= expiresAt) {
                              Bukkit.getScheduler().runTask(this.plugin, () -> {
                                 LuckPermsHelper.setVoicechatPermissions(playerUUID, true);
                                 Player player = Bukkit.getPlayer(playerUUID);
                                 if (player != null) {
                                    player.sendMessage(ColorUtil.color("&#55ff55Your voice chat ban has expired!"));
                                 }

                              });
                              updatePs.setString(1, playerUUID.toString());
                              updatePs.executeUpdate();
                           }
                        }
                     } catch (Throwable var18) {
                        if (updatePs != null) {
                           try {
                              updatePs.close();
                           } catch (Throwable var17) {
                              var18.addSuppressed(var17);
                           }
                        }

                        throw var18;
                     }

                     if (updatePs != null) {
                        updatePs.close();
                     }
                  } catch (Throwable var19) {
                     if (rs != null) {
                        try {
                           rs.close();
                        } catch (Throwable var16) {
                           var19.addSuppressed(var16);
                        }
                     }

                     throw var19;
                  }

                  if (rs != null) {
                     rs.close();
                  }
               } catch (Throwable var20) {
                  if (ps != null) {
                     try {
                        ps.close();
                     } catch (Throwable var15) {
                        var20.addSuppressed(var15);
                     }
                  }

                  throw var20;
               }

               if (ps != null) {
                  ps.close();
               }
            } catch (Throwable var21) {
               if (conn != null) {
                  try {
                     conn.close();
                  } catch (Throwable var14) {
                     var21.addSuppressed(var14);
                  }
               }

               throw var21;
            }

            if (conn != null) {
               conn.close();
            }

         } catch (SQLException var22) {
            throw new RuntimeException("Failed to check ban expiration!", var22);
         }
      }, 20L, 20L);
   }

   private long parseTime(String time) throws IllegalArgumentException {
      char unit = time.charAt(time.length() - 1);
      long value = Long.parseLong(time.substring(0, time.length() - 1));
      long var10000;
      switch(unit) {
      case 'd':
         var10000 = TimeUnit.DAYS.toMillis(value);
         break;
      case 'h':
         var10000 = TimeUnit.HOURS.toMillis(value);
         break;
      case 'm':
         var10000 = TimeUnit.MINUTES.toMillis(value);
         break;
      case 's':
         var10000 = TimeUnit.SECONDS.toMillis(value);
         break;
      default:
         throw new IllegalArgumentException("Invalid time unit");
      }

      return var10000;
   }
}
