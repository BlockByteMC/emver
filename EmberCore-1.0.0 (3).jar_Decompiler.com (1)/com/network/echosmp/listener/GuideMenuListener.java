package com.network.echosmp.listener;

import com.network.echosmp.menu.GuideMenu;
import java.util.Iterator;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class GuideMenuListener implements Listener {
   public GuideMenuListener(Plugin plugin) {
      Bukkit.getPluginManager().registerEvents(this, plugin);
   }

   @EventHandler
   public void onInventoryClick(@NotNull InventoryClickEvent event) {
      if (event.getView().getTitle().equals(GuideMenu.TITLE)) {
         event.setCancelled(true);
         event.setResult(Result.DENY);
         ClickType click = event.getClick();
         if (click == ClickType.NUMBER_KEY || click == ClickType.SWAP_OFFHAND) {
            event.setCancelled(true);
         }

         InventoryAction action = event.getAction();
         switch(action) {
         case MOVE_TO_OTHER_INVENTORY:
         case COLLECT_TO_CURSOR:
         case HOTBAR_SWAP:
         case HOTBAR_MOVE_AND_READD:
         case DROP_ALL_CURSOR:
         case DROP_ONE_CURSOR:
         case DROP_ALL_SLOT:
         case DROP_ONE_SLOT:
         case UNKNOWN:
            event.setCancelled(true);
         default:
         }
      }
   }

   @EventHandler
   public void onInventoryDrag(@NotNull InventoryDragEvent event) {
      if (event.getView().getTitle().equals(GuideMenu.TITLE)) {
         int topSize = event.getView().getTopInventory().getSize();
         Iterator var3 = event.getRawSlots().iterator();

         int raw;
         do {
            if (!var3.hasNext()) {
               event.setCancelled(true);
               event.setResult(Result.DENY);
               return;
            }

            raw = (Integer)var3.next();
         } while(raw >= topSize);

         event.setCancelled(true);
         event.setResult(Result.DENY);
      }
   }
}
