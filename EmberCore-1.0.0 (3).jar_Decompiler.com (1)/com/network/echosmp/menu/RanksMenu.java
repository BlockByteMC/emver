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

public class RanksMenu implements Listener {
   private final String title = ColorUtil.color("Ranks Menu");

   public RanksMenu(Plugin plugin) {
      Bukkit.getPluginManager().registerEvents(this, plugin);
   }

   private List<String> colorList(List<String> list) {
      return (List)list.stream().map(ColorUtil::color).collect(Collectors.toList());
   }

   public void open(Player player) {
      Inventory gui = Bukkit.createInventory((InventoryHolder)null, 27, this.title);
      ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
      ItemMeta fillerMeta = filler.getItemMeta();
      fillerMeta.setDisplayName(ColorUtil.color("&r"));
      filler.setItemMeta(fillerMeta);

      for(int i = 0; i < gui.getSize(); ++i) {
         gui.setItem(i, filler);
      }

      ItemStack soldier = new ItemStack(Material.ELDER_GUARDIAN_SPAWN_EGG);
      ItemMeta soldierMeta = soldier.getItemMeta();
      soldierMeta.setDisplayName(ColorUtil.color("&#B0C4DE&lSOLDIER &r&#B0C4DE Rank"));
      soldierMeta.setLore(this.colorList(Arrays.asList("&8Information", "&r", "&bPerks:", "&#B0C4DE♦ &f3x Homes", "&#B0C4DE♦ &fAccess to Soldier Kit", "&#B0C4DE♦ &fAccess to /hat", "&#B0C4DE♦ &fAccess to /workbench", "&#B0C4DE♦ &fGet 3 homes", "&r", "&e➟ &e&lCLICK &r&efor our Discord")));
      soldier.setItemMeta(soldierMeta);
      gui.setItem(11, soldier);
      ItemStack gladiator = new ItemStack(Material.STRIDER_SPAWN_EGG);
      ItemMeta gladiatorMeta = gladiator.getItemMeta();
      gladiatorMeta.setDisplayName(ColorUtil.color("&#DC143C&lGLADIATOR &r&#DC143C Rank"));
      gladiatorMeta.setLore(this.colorList(Arrays.asList("&8Information", "&r", "&bPerks:", "&#DC143C♦ &f5 Homes", "&#DC143C♦ &fAccess to Soldier Kit, Gladiator Kit", "&#DC143C♦ &fAccess to /hat", "&#DC143C♦ &fAccess to /workbench (crafting table)", "&#DC143C♦ &fAccess to /grindstone", "&r", "&e➟ &e&lCLICK &r&efor our Discord")));
      gladiator.setItemMeta(gladiatorMeta);
      gui.setItem(12, gladiator);
      ItemStack champion = new ItemStack(Material.CAT_SPAWN_EGG);
      ItemMeta championMeta = champion.getItemMeta();
      championMeta.setDisplayName(ColorUtil.color("&#FFBA7D&lCHAMPION &r&#FFBA7D Rank"));
      championMeta.setLore(this.colorList(Arrays.asList("&8Information", "&r", "&bPerks:", "&#FFBA7D♦ &f8 Homes", "&#FFBA7D♦ &fPremium Shop", "&#FFBA7D♦ &fAccess to Soldier Kit, Gladiator Kit, Champion Kit", "&#FFBA7D♦ &fAccess to /hat", "&#FFBA7D♦ &fAccess to /workbench (crafting table)", "&#FFBA7D♦ &fAccess to /grindstone", "&#FFBA7D♦ &fAccess to /anvil", "&#FFBA7D♦ &fAccess to /playtime", "&#FFBA7D♦ &fAccess to /afkslot 1", "&r", "&e➟ &e&lCLICK &r&efor our Discord")));
      champion.setItemMeta(championMeta);
      gui.setItem(13, champion);
      ItemStack warlord = new ItemStack(Material.BLAZE_SPAWN_EGG);
      ItemMeta warlordMeta = warlord.getItemMeta();
      warlordMeta.setDisplayName(ColorUtil.color("&#FFD700&lWARLORD &r&#FFD700 Rank"));
      warlordMeta.setLore(this.colorList(Arrays.asList("&8Information", "&r", "&bPerks:", "&#FFD700♦ &f12 Homes", "&#FFD700♦ &fPremium Shop", "&#FFD700♦ &fAccess to Warlord kit & all previous.", "&#FFD700♦ &fAccess to /hat", "&#FFD700♦ &fAccess to /workbench (crafting table)", "&#FFD700♦ &fAccess to /grindstone", "&#FFD700♦ &fAccess to /anvil", "&#FFD700♦ &fAccess to /playtime", "&#FFD700♦ &fAccess to /afk", "&#FFD700♦ &fAccess to /smithingtable", "&#FFD700♦ &fAccess to /disposal", "&r", "&e➟ &e&lCLICK &r&efor our Discord")));
      warlord.setItemMeta(warlordMeta);
      gui.setItem(14, warlord);
      ItemStack titan = new ItemStack(Material.STRIDER_SPAWN_EGG);
      ItemMeta titanMeta = titan.getItemMeta();
      titanMeta.setDisplayName(ColorUtil.color("&#CB0000&lTITAN &r&#CB0000 Rank"));
      titanMeta.setLore(this.colorList(Arrays.asList("&8Information", "&r", "&bPerks:", "&#CB0000♦ &f20 Homes", "&#CB0000♦ &fAccess to Titan Kit & all previous.", "&#CB0000♦ &fTITAN Kit contains Protection 5", "&#CB0000♦ &fPremium Shop", "&#CB0000♦ &fAccess to /hat", "&#CB0000♦ &fAccess to /workbench (crafting table)", "&#CB0000♦ &fAccess to /grindstone", "&#CB0000♦ &fAccess to /anvil", "&#CB0000♦ &fAccess to /playtime", "&#CB0000♦ &fAccess to /afk", "&#CB0000♦ &fAccess to /smithingtable", "&#CB0000♦ &fAccess to /disposal", "&r", "&e➟ &e&lCLICK &r&efor our Discord")));
      titan.setItemMeta(titanMeta);
      gui.setItem(15, titan);
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
