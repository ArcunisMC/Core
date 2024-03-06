package com.arcunis.core;

import com.arcunis.core.abstracts.ArcunisPlugin;
import com.arcunis.core.permissions.PermissionsManager;
import com.arcunis.core.reload.ReloadEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerLoadEvent;

public final class Core extends ArcunisPlugin implements Listener {

    public PermissionsManager permissionsManager;

    @Override
    public void onEnable() {
        saveResource("groups.yml", false);
        saveResource("players.yml", false);
        register(new Registry(this));
    }

    @Override
    public void onDisable() {
        permissionsManager.save();
    }

    @Override
    public void onReload(ReloadEvent event) {
        permissionsManager.save();
        permissionsManager.load();
    }

    @EventHandler
    public void onServerLoad(ServerLoadEvent event) {
        // Broken AF prob not gona enable unless i get the will to work on this back
//        permissionsManager = new PermissionsManager(this);
//        permissionsManager.load();
    }

}
