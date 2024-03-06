package com.arcunis.core.permissions;

import com.arcunis.core.Core;
import com.arcunis.core.enums.ChatColor;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PermissionsMenu implements Listener {

    private final PermissionsManager permissionsManager;
    private final Player player;
    private final Inventory inventory;
    private int menu = 0; // 0 = main, 1 = players, 2 = groupSelector, 3 = groups, 4 = permissionsExplorer
    private String permPath = null;
    private int page = 0;
    private final int itemsPerPage = 45;
    private int pageItems = 0;
    private String editing;

    public PermissionsMenu(Core plugin, Player player) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.permissionsManager = plugin.permissionsManager;
        this.player = player;
        inventory = Bukkit.createInventory(player, 54, Component.text("Permissions manager").color(ChatColor.GOLD.color));
    }

    public void generateInventory() {
        inventory.clear();
        // 0 = main, 1 = players, 2 = groupSelector, 3 = groups, 4 = permissionsExplorer
        if (menu == 0) {
            inventory.setItem(21, getItem(Material.PLAYER_HEAD, Component.text("Manage players")));
            inventory.setItem(23, getItem(Material.CHEST, Component.text("Manage groups")));
        } else if (menu == 1) {
            OfflinePlayer[] players = Bukkit.getOfflinePlayers();
            for (OfflinePlayer player : Arrays.copyOfRange(players, itemsPerPage * page, itemsPerPage * page + itemsPerPage)) {
                if (player == null) break;
                ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
                SkullMeta meta = (SkullMeta) skull.getItemMeta();
                meta.setOwningPlayer(player);
                skull.setItemMeta(meta);
                inventory.addItem(skull);
            }
            pageItems = players.length;
        } else if (menu == 2) {
            String[] groups = permissionsManager.groups.keySet().toArray(new String[]{});
            String playerGroup = permissionsManager.players.get(UUID.fromString(editing));
            for (String group : Arrays.copyOfRange(groups, itemsPerPage * page, itemsPerPage * page + itemsPerPage)) {
                if (group == null) break;
                ItemStack item;
                if (playerGroup.equalsIgnoreCase(group)) item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
                else item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                ItemMeta meta = item.getItemMeta();
                meta.displayName(Component.text(group));
                item.setItemMeta(meta);
                inventory.addItem(item);
            }
            pageItems = permissionsManager.groups.size();
        } else if (menu == 3) {
            String[] groups = permissionsManager.groups.keySet().toArray(new String[]{});
            for (String group : Arrays.copyOfRange(groups, itemsPerPage * page, itemsPerPage * page + itemsPerPage)) {
                if (group == null) break;
                ItemStack item = new ItemStack(Material.PAPER);
                ItemMeta meta = item.getItemMeta();
                meta.displayName(Component.text(group));
                item.setItemMeta(meta);
                inventory.addItem(item);
            }
            pageItems = permissionsManager.groups.size();
        } else if (menu == 4) {
            List<String> groupPerms = permissionsManager.groups.get(editing);
            Set<String> keys = null;
            if (permPath == null) {
                keys = permissionsManager.permissions.getKeys(false);
            } else {
                ConfigurationSection section = permissionsManager.permissions.getConfigurationSection(permPath);
                if (section != null) keys = section.getKeys(false);
                else {
                    player.sendMessage(Component.text("could not get children from " + permPath));
                }
            }
            if (keys != null && keys.size() > 0) {
                for (String permission : keys) {
                    ItemStack item;
                    String path;
                    if (permPath == null) path = permission;
                    else path = permPath + "." + permission;
                    if (groupPerms.contains(path)) {
                        item = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);
                    } else {
                        item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
                    }
                    ItemMeta meta = item.getItemMeta();
                    meta.displayName(Component.text(path));
                    Permission perm = Bukkit.getPluginManager().getPermission(path);
                    if (perm == null) continue;
                    perm.getDescription();
                    List<Component> lore = new ArrayList<>();
                    lore.add(Component.text(perm.getDescription()));
                    meta.lore(lore);
                    item.setItemMeta(meta);
                    inventory.addItem(item);
                }
            }
            pageItems = keys.size();
        }
        addButtons();
    }

    public void addButtons() {
        inventory.setItem(45, getItem(Material.GRAY_STAINED_GLASS_PANE, Component.text("")));
        inventory.setItem(46, getItem(Material.GRAY_STAINED_GLASS_PANE, Component.text("")));
        inventory.setItem(47, getItem(Material.GRAY_STAINED_GLASS_PANE, Component.text("")));
        inventory.setItem(48, getItem(Material.ARROW, Component.text("Previous page")));
        inventory.setItem(49, getItem(Material.BARRIER, Component.text("Close menu")));
        inventory.setItem(50, getItem(Material.ARROW, Component.text("Next page")));
        inventory.setItem(51, getItem(Material.GRAY_STAINED_GLASS_PANE, Component.text("")));
        inventory.setItem(52, getItem(Material.GRAY_STAINED_GLASS_PANE, Component.text("")));
        inventory.setItem(53, getItem(Material.GRAY_STAINED_GLASS_PANE, Component.text("")));
    }

    public void nextPage(Inventory inv) {
        if (pageItems > itemsPerPage * page + itemsPerPage) page += 1;
        show(inv);
    }

    public void prevPage(Inventory inv) {
        if (page > 0) page -= 1;
        show(inv);
    }

    public void show(@Nullable Inventory inv) {
        generateInventory();
        if (inv == null) player.openInventory(inventory);
        else inv.setContents(inventory.getContents());
    }

    private ItemStack getItem(Material material, Component name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.displayName(name);
        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Inventory inv = event.getInventory();
        if (!event.getView().title().equals(Component.text("Permissions manager").color(ChatColor.GOLD.color))) return;
        event.setCancelled(true);
        if (event.getSlot() == 48) prevPage(inv);
        else if (event.getSlot() == 50) nextPage(inv);
        else if (event.getSlot() == 49) {
            if (menu == 0) {
                inventory.close();
                InventoryClickEvent.getHandlerList().unregister(this);
                InventoryCloseEvent.getHandlerList().unregister(this);
            } else {
                editing = null;
                permPath = null;
                menu = 0;
                show(inv);
            }
        }
        else if (event.getCurrentItem() != null && event.getSlot() < 45 && event.getClickedInventory().equals(inventory)) {
            // 0 = main, 1 = players, 2 = groupSelector, 3 = groups, 4 = permissionsExplorer
            if (menu == 0) {
                if (event.getSlot() == 21) {
                    menu = 1;
                } else if (event.getSlot() == 23) {
                    menu = 3;
                }
            } else if (menu == 1) {
                SkullMeta skull = (SkullMeta) event.getCurrentItem().getItemMeta();
                editing = skull.getOwningPlayer().getUniqueId().toString();
                menu = 2;
            } else if (menu == 2) {
                permissionsManager.players.put(UUID.fromString(editing), ((TextComponent) event.getCurrentItem().getItemMeta().displayName()).content());
            } else if (menu == 3) {
                ItemMeta item = event.getCurrentItem().getItemMeta();
                editing = ((TextComponent) item.displayName()).content();
                menu = 4;
            } else if (menu == 4) {
                String path = ((TextComponent) event.getCurrentItem().getItemMeta().displayName()).content();
                if (permissionsManager.permissions.getConfigurationSection(path) != null && permissionsManager.permissions.getConfigurationSection(path).getKeys(false).size() > 0) permPath = path;
                else {
                    List<String> perms = permissionsManager.groups.get(editing);
                    if (perms.contains(path)) {
                        perms.remove(path);
                        permissionsManager.groups.put(editing, perms);
                    } else {
                        perms.add(path);
                        permissionsManager.groups.put(editing, perms);
                    }
                }
            }
            show(inv);
        }
    }

    @EventHandler
    public void onInvClose(InventoryCloseEvent event) {
        if (!event.getView().title().equals(Component.text("Permissions manager").color(ChatColor.GOLD.color))) return;
        InventoryClickEvent.getHandlerList().unregister(this);
        InventoryCloseEvent.getHandlerList().unregister(this);
    }

}
