package com.arcunis.core.utils;

import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class Permission {

    private String name;

    public Permission(String name, String description, @Nullable PermissionDefault defaultValue, @Nullable Map<String, Boolean> children) {
        Bukkit.getLogger().info("Registring permission " + name);
        Bukkit.getPluginManager().addPermission(new org.bukkit.permissions.Permission(name, description, defaultValue, children));
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
