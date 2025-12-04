package com.network.echosmp.utility;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;

public class LuckPermsHelper {
   private static final LuckPerms luckPerms = LuckPermsProvider.get();

   public static void setVoicechatPermissions(UUID uuid, boolean canSpeak) {
      CompletableFuture<User> userFuture = luckPerms.getUserManager().loadUser(uuid);
      userFuture.thenAccept((user) -> {
         user.data().remove(Node.builder("voicechat.speak").build());
         if (canSpeak) {
            user.data().add(Node.builder("voicechat.speak").value(true).build());
         } else {
            user.data().add(Node.builder("voicechat.speak").value(false).build());
         }

         luckPerms.getUserManager().saveUser(user);
      });
   }
}
