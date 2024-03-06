package com.arcunis.core.abstracts;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class BaseRegistry {

    protected final ArcunisPlugin plugin;

    public BaseRegistry(ArcunisPlugin plugin) {
        this.plugin = plugin;
    }

    public abstract void registerCommands();
    public abstract void registerPermissions();
    public abstract void registerEvents();
    public abstract void registerCustom();

}
