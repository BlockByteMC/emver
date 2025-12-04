package com.network.echosmp.menu;

import com.network.echosmp.utility.ColorUtil;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

public class RulesMenu implements Listener {
   private final String title = ColorUtil.color("Rules Menu");

   public RulesMenu(Plugin plugin) {
      Bukkit.getPluginManager().registerEvents(this, plugin);
   }

   private List<String> colorList(List<String> list) {
      return (List)list.stream().map(ColorUtil::color).collect(Collectors.toList());
   }

   public void open(Player player) {
      Inventory gui = Bukkit.createInventory((InventoryHolder)null, 27, this.title);
      ItemStack serverRules = new ItemStack(Material.LIME_SHULKER_BOX);
      ItemMeta serverMeta = serverRules.getItemMeta();
      serverMeta.setDisplayName(ColorUtil.color("&#90FF31Server Rules"));
      serverMeta.setLore(this.colorList(Arrays.asList("&8Information", "&r", "&bDescription:", "&fYou must follow these rules, otherwise", "&fit leads to server decided punishments!", "&r", "&#90FF31♦ &fNo hacked or modifed Clients", "&#90FF31♦ &fMaximum up to 4 Alts allowed", "&#90FF31♦ &fNo Mods that gain an advantage", "&#90FF31♦ &fNo Health Indicator Mods", "&#90FF31♦ &fMacros of any type are disallowed", "&#90FF31♦ &fNo Harmful Usage Macros or Scripts", "&#90FF31♦ &fNo radar mods to search for Bases", "&r", "&e➟ &e&lCLICK &r&efor our Discord")));
      serverRules.setItemMeta(serverMeta);
      gui.setItem(11, serverRules);
      ItemStack generalRules = new ItemStack(Material.ORANGE_SHULKER_BOX);
      ItemMeta generalMeta = generalRules.getItemMeta();
      generalMeta.setDisplayName(ColorUtil.color("&#FB7C0BGeneral Rules"));
      generalMeta.setLore(this.colorList(Arrays.asList("&8Information", "&r", "&bDescription:", "&fYou must follow these rules, otherwise", "&fit leads to server decided punishments!", "&r", "&#FB7C0B♦ &fNo abusing bugs or glitches", "&#FB7C0B♦ &fNo duping of items or currency", "&#FB7C0B♦ &fNo cross-trading with other servers", "&#FB7C0B♦ &fNo excessive spawn killing", "&#FB7C0B♦ &fDo not exploit the economy", "&#FB7C0B♦ &fRespect staff decisions at all times", "&r", "&e➟ &e&lCLICK &r&efor our Discord")));
      generalRules.setItemMeta(generalMeta);
      gui.setItem(13, generalRules);
      ItemStack chatRules = new ItemStack(Material.LIGHT_BLUE_SHULKER_BOX);
      ItemMeta chatMeta = chatRules.getItemMeta();
      chatMeta.setDisplayName(ColorUtil.color("&#00A3FBChat Rules"));
      chatMeta.setLore(this.colorList(Arrays.asList("&8Information", "&r", "&bDescription:", "&fYou must follow these rules, otherwise", "&fit leads to server decided punishments!", "&r", "&#00A3FB♦ &fNo Spamming and Riotings", "&#00A3FB♦ &fNo Harassing & Abusing Bugs", "&#00A3FB♦ &fNo Sharing Others Informations", "&#00A3FB♦ &fNo Advertising or Promotions", "&#00A3FB♦ &fNo Racism, Discrimination", "&#00A3FB♦ &fNo Hate Speech & Threats", "&r", "&e➟ &e&lCLICK &r&efor our Discord")));
      chatRules.setItemMeta(chatMeta);
      gui.setItem(15, chatRules);
      player.openInventory(gui);
   }

   @EventHandler
   public void onClick(InventoryClickEvent event) {
      if (event.getView().getTitle().equals(this.title)) {
         Player player = (Player)event.getWhoClicked();
         player.closeInventory();
         player.performCommand("discord");
      }
   }
}
