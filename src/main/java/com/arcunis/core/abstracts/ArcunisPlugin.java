package com.arcunis.core.abstracts;

import com.arcunis.core.Core;
import com.arcunis.core.reload.ReloadEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

public abstract class ArcunisPlugin extends JavaPlugin implements Listener {

    public Core core = (Core) Bukkit.getPluginManager().getPlugin("Core");

    public void register(BaseRegistry registry) {
        Bukkit.getPluginManager().registerEvents(this, this);
        registry.registerPermissions();
        registry.registerCommands();
        registry.registerEvents();
        registry.registerCustom();
    }

    public static boolean isFolia() {
        try {
            Class.forName("io.papermc.paper.threadedregions.RegionizedServer");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @EventHandler
    public abstract void onReload(ReloadEvent event);

}
