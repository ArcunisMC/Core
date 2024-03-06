package com.arcunis.core;

import com.arcunis.core.abstracts.ArcunisPlugin;
import com.arcunis.core.abstracts.BaseRegistry;
import com.arcunis.core.permissions.PermissionsCommand;
import com.arcunis.core.reload.ReloadCommand;
import com.arcunis.core.utils.Permission;
import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionDefault;

public class Registry extends BaseRegistry {

    public Registry(ArcunisPlugin plugin) {
        super(plugin);
    }

    @Override
    public void registerCommands() {
        new ReloadCommand(plugin);
        new PermissionsCommand(plugin);
    }

    @Override
    public void registerPermissions() {
        new Permission("arcunis.core.reload", "Reload plugins", PermissionDefault.OP, null);
        new Permission("arcunis.core.permissions", "Manage permissions", PermissionDefault.OP, null);
    }

    @Override
    public void registerEvents() {
    }

    @Override
    public void registerCustom() {

    }

}
