package com.network.echosmp.menu;

import com.network.echosmp.utility.ColorUtil;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class GuideMenu {
   public static final String TITLE = ColorUtil.color("&8ɢᴜɪᴅᴇ");
   private final JavaPlugin plugin;

   public GuideMenu(@NotNull JavaPlugin plugin) {
      this.plugin = plugin;
   }

   public void open(@NotNull Player player) {
      Inventory gui = Bukkit.createInventory((InventoryHolder)null, 45, TITLE);
      gui.setItem(20, this.createItem(Material.OAK_SIGN, "&eServer Rules", "&7Click to view all server rules.", "&8- Respect others", "&8- No griefing or stealing"));
      gui.setItem(21, this.createItem(Material.EMERALD, "&aEconomy Info", "&7Learn how money works on the server.", "&8- Player shops & trading", "&8- Earn by playing"));
      gui.setItem(13, this.createItem(Material.OAK_SAPLING, "&2Getting Started", "&7Helpful tips for new players.", "&8- /rtp to explore", "&8- /sethome to save"));
      gui.setItem(22, this.createItem(Material.BELL, "&6Ranks & Perks", "&7Information about ranks and benefits.", "&8- Unlock perks", "&8- /ranks for details"));
      gui.setItem(23, this.createItem(Material.GREEN_WOOL, "&aClaims & Protection", "&7Protect your builds from grief.", "&8- Use a golden shovel", "&8- Expand your claim"));
      gui.setItem(24, this.createItem(Material.CHEST, "&eCrates", "&7Crate types and how to obtain them.", "&8- Vote for keys", "&8- /warp crates"));
      gui.setItem(29, this.createItem(Material.GOLDEN_PICKAXE, "&6Jobs / Skills", "&7Level up as you play.", "&8- /jobs to join", "&8- Improve your skills"));
      gui.setItem(30, this.createItem(Material.HAY_BLOCK, "&eDaily Rewards", "&7Claim your daily login rewards.", "&8- Keep your streak!", "&8- Better streaks = better loot"));
      gui.setItem(31, this.createItem(Material.BOOK, "&dUseful Commands", "&7Commands every player should know.", "&8- /spawn /home /tpa", "&8- /rules /discord /guide"));
      gui.setItem(32, this.createItem(Material.WRITABLE_BOOK, "&9Discord Info", "&7Connect with the community.", "&8- Events, support, giveaways"));
      gui.setItem(33, this.createItem(Material.AMETHYST_CLUSTER, "&bCosmetics", "&7Customize your look and feel.", "&8- Trails, hats, particles"));
      ItemStack filler = this.createFiller();

      for(int i = 0; i < gui.getSize(); ++i) {
         if (gui.getItem(i) == null) {
            gui.setItem(i, filler);
         }
      }

      player.openInventory(gui);
   }

   @NotNull
   private ItemStack createItem(@NotNull Material material, @NotNull String name, @NotNull String... loreLines) {
      ItemStack item = new ItemStack(material);
      ItemMeta meta = item.getItemMeta();
      if (meta != null) {
         meta.setDisplayName(ColorUtil.color(name));
         List<String> lore = new ArrayList(loreLines.length);
         String[] var7 = loreLines;
         int var8 = loreLines.length;

         for(int var9 = 0; var9 < var8; ++var9) {
            String line = var7[var9];
            lore.add(ColorUtil.color(line));
         }

         meta.setLore(lore);
         item.setItemMeta(meta);
      }

      return item;
   }

   @NotNull
   private ItemStack createFiller() {
      ItemStack filler = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
      ItemMeta meta = filler.getItemMeta();
      if (meta != null) {
         meta.setDisplayName(ColorUtil.color("&r"));
         filler.setItemMeta(meta);
      }

      return filler;
   }
}
