package com.network.echosmp.database;

public class BanRecord {
   private final String username;
   private final String bannedBy;
   private final String reason;
   private final long banTime;
   private final long duration;
   private final boolean unmuted;

   public BanRecord(String username, String bannedBy, String reason, long banTime, long duration, boolean unmuted) {
      this.username = username;
      this.bannedBy = bannedBy;
      this.reason = reason;
      this.banTime = banTime;
      this.duration = duration;
      this.unmuted = unmuted;
   }

   public String getUsername() {
      return this.username;
   }

   public String getBannedBy() {
      return this.bannedBy;
   }

   public String getReason() {
      return this.reason;
   }

   public long getBanTime() {
      return this.banTime;
   }

   public long getDuration() {
      return this.duration;
   }

   public boolean isUnmuted() {
      return this.unmuted;
   }
}
