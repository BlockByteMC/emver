package com.network.echosmp.database;

import com.network.echosmp.Main;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DatabaseManager {
   private static final Logger log = LoggerFactory.getLogger("EmberCore");
   private Connection connection;
   private final ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();

   public void initialize() throws SQLException {
      this.openConnection();
      this.createTables();
   }

   private void openConnection() throws SQLException {
      String url = "jdbc:sqlite:" + String.valueOf(Main.getInstance().getDataFolder()) + "/vcban.db";
      this.connection = DriverManager.getConnection(url);
      Statement stmt = this.connection.createStatement();

      try {
         stmt.execute("PRAGMA foreign_keys = ON");
         stmt.execute("PRAGMA journal_mode = WAL");
         stmt.execute("PRAGMA synchronous = NORMAL");
      } catch (Throwable var6) {
         if (stmt != null) {
            try {
               stmt.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (stmt != null) {
         stmt.close();
      }

   }

   private void createTables() throws SQLException {
      String sql = "CREATE TABLE IF NOT EXISTS vc_bans (id INTEGER PRIMARY KEY AUTOINCREMENT,uuid VARCHAR(36) NOT NULL,username VARCHAR(16),banned_by VARCHAR(36),reason TEXT,ban_time BIGINT,duration BIGINT,unmuted BOOLEAN DEFAULT 0,unmute_time BIGINT);";
      Statement stmt = this.connection.createStatement();

      try {
         stmt.executeUpdate(sql);
      } catch (Throwable var6) {
         if (stmt != null) {
            try {
               stmt.close();
            } catch (Throwable var5) {
               var6.addSuppressed(var5);
            }
         }

         throw var6;
      }

      if (stmt != null) {
         stmt.close();
      }

   }

   public Connection getConnection() throws SQLException {
      if (this.connection == null || this.connection.isClosed()) {
         this.openConnection();
      }

      return this.connection;
   }

   public void close() {
      try {
         if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
         }

         log.info("Closed database");
      } catch (SQLException var2) {
         log.error("Failed to close database connection", var2);
      }

   }

   public CompletableFuture<Boolean> isBanned(UUID uuid) {
      return CompletableFuture.supplyAsync(() -> {
         String sql = "SELECT COUNT(*) FROM vc_bans WHERE uuid = ? AND unmuted = 0";

         try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);

            Boolean var5;
            try {
               ps.setString(1, uuid.toString());
               ResultSet rs = ps.executeQuery();

               try {
                  var5 = rs.next() && rs.getInt(1) > 0;
               } catch (Throwable var9) {
                  if (rs != null) {
                     try {
                        rs.close();
                     } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                     }
                  }

                  throw var9;
               }

               if (rs != null) {
                  rs.close();
               }
            } catch (Throwable var10) {
               if (ps != null) {
                  try {
                     ps.close();
                  } catch (Throwable var7) {
                     var10.addSuppressed(var7);
                  }
               }

               throw var10;
            }

            if (ps != null) {
               ps.close();
            }

            return var5;
         } catch (SQLException var11) {
            throw new RuntimeException("Failed to check if player " + String.valueOf(uuid) + " is banned.", var11);
         }
      }, this.executorService);
   }

   public CompletableFuture<Void> removeBan(UUID uuid) {
      return CompletableFuture.runAsync(() -> {
         String sql = "UPDATE vc_bans SET unmuted = 1, unmute_time = ? WHERE uuid = ? AND unmuted = 0";

         try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);

            try {
               ps.setLong(1, System.currentTimeMillis());
               ps.setString(2, uuid.toString());
               ps.executeUpdate();
            } catch (Throwable var7) {
               if (ps != null) {
                  try {
                     ps.close();
                  } catch (Throwable var6) {
                     var7.addSuppressed(var6);
                  }
               }

               throw var7;
            }

            if (ps != null) {
               ps.close();
            }

         } catch (SQLException var8) {
            throw new RuntimeException("Failed to remove ban for player " + String.valueOf(uuid), var8);
         }
      }, this.executorService);
   }

   public CompletableFuture<Void> banPlayer(UUID uuid, String username, String bannedBy, String reason, long duration) {
      return CompletableFuture.runAsync(() -> {
         String sql = "INSERT INTO vc_bans (uuid, username, banned_by, reason, ban_time, duration, unmuted) VALUES (?, ?, ?, ?, ?, ?, ?)";

         try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);

            try {
               ps.setString(1, uuid.toString());
               ps.setString(2, username);
               ps.setString(3, bannedBy);
               ps.setString(4, reason);
               ps.setLong(5, System.currentTimeMillis());
               ps.setLong(6, duration);
               ps.setBoolean(7, false);
               ps.executeUpdate();
            } catch (Throwable var12) {
               if (ps != null) {
                  try {
                     ps.close();
                  } catch (Throwable var11) {
                     var12.addSuppressed(var11);
                  }
               }

               throw var12;
            }

            if (ps != null) {
               ps.close();
            }

         } catch (SQLException var13) {
            throw new RuntimeException("Failed to insert ban for player " + String.valueOf(uuid), var13);
         }
      }, this.executorService);
   }

   public CompletableFuture<List<BanRecord>> getHistory(UUID uuid) {
      return CompletableFuture.supplyAsync(() -> {
         List<BanRecord> history = new ArrayList();
         String sql = "SELECT username, banned_by, reason, ban_time, duration, unmuted FROM vc_bans WHERE uuid = ? ORDER BY ban_time DESC";

         try {
            PreparedStatement ps = this.getConnection().prepareStatement(sql);

            try {
               ps.setString(1, uuid.toString());
               ResultSet rs = ps.executeQuery();

               try {
                  while(rs.next()) {
                     history.add(new BanRecord(rs.getString("username"), rs.getString("banned_by"), rs.getString("reason"), rs.getLong("ban_time"), rs.getLong("duration"), rs.getBoolean("unmuted")));
                  }
               } catch (Throwable var10) {
                  if (rs != null) {
                     try {
                        rs.close();
                     } catch (Throwable var9) {
                        var10.addSuppressed(var9);
                     }
                  }

                  throw var10;
               }

               if (rs != null) {
                  rs.close();
               }
            } catch (Throwable var11) {
               if (ps != null) {
                  try {
                     ps.close();
                  } catch (Throwable var8) {
                     var11.addSuppressed(var8);
                  }
               }

               throw var11;
            }

            if (ps != null) {
               ps.close();
            }

            return history;
         } catch (SQLException var12) {
            throw new RuntimeException("Failed to get history for player " + String.valueOf(uuid), var12);
         }
      }, this.executorService);
   }
}
