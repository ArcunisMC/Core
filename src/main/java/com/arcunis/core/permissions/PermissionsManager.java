package com.arcunis.core.permissions;

import com.arcunis.core.abstracts.ArcunisPlugin;
import com.arcunis.core.utils.Permission;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.MemoryConfiguration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.permissions.PermissionAttachment;
import org.bukkit.permissions.PermissionDefault;

import java.io.*;
import java.util.*;

public class PermissionsManager implements Listener {

    private ArcunisPlugin plugin;

    public MemoryConfiguration permissions = new MemoryConfiguration();
    public Map<String, List<String>> groups = new HashMap<>();
    public Map<UUID, String> players = new HashMap<>();
    HashMap<UUID,PermissionAttachment> attachments = new HashMap<>();

    public PermissionsManager(ArcunisPlugin plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public void load() {
        loadGroups();
        loadPlayers();

        for (Player player : Bukkit.getOnlinePlayers()) {
            reloadPerms(player);
        }
        int i = 0;
        for (org.bukkit.permissions.Permission permission : Bukkit.getPluginManager().getPermissions()) {
            ConfigurationSection section = permissions.getConfigurationSection(permission.getName());
            if (section != null && section.getKeys(false).size() > 0) continue;
            permissions.set(permission.getName(), true);
            i ++;
        }
    }

    public void save() {
        saveGroups();
        savePlayers();
    }

    public void loadGroups() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "groups.yml"));
        Set<String> groups = config.getKeys(false);
        for (String group : groups) {
            List<String> permissions = config.getStringList(group);
            Map<String, Boolean> perms = new HashMap<>();
            for (String permission : permissions) {
                perms.put(permission, true);
            }
            this.groups.put(group, permissions);
            new Permission("group." + group, "All permissions of " + group, PermissionDefault.FALSE, perms);
        }
    }

    public void saveGroups() {
        try {
            File file = new File(plugin.getDataFolder(), "groups.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);

            for (Map.Entry<String, List<String>> entry : groups.entrySet()) {
                String group = entry.getKey();
                List<String> permissions = entry.getValue();
                config.set(group, permissions);
            }

            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadPlayers() {
        FileConfiguration config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "players.yml"));
        Set<String> players = config.getKeys(false);
        for (String uuid : players) {
            this.players.put(UUID.fromString(uuid), config.getString(uuid));
        }
    }

    public void savePlayers() {
        try {
            File file = new File(plugin.getDataFolder(), "players.yml");
            FileConfiguration config = YamlConfiguration.loadConfiguration(file);
            for (Map.Entry<UUID, String> entry : players.entrySet()) {
                config.set(entry.getKey().toString(), entry.getValue());
            }
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void reloadPerms(Player player) {
        if (attachments.get(player.getUniqueId()) != null) {
            player.removeAttachment(attachments.get(player.getUniqueId()));
            attachments.remove(player.getUniqueId());
        }
        PermissionAttachment attachment = player.addAttachment(plugin);
        attachments.put(player.getUniqueId(), attachment);
        String group = players.get(player.getUniqueId());
        if (group == null) group = "default";
        for (String entry : groups.get(group)) {
            attachment.setPermission(entry, true);
        }
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        reloadPerms(player);
    }

}