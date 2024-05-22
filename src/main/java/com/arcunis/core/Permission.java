package com.arcunis.core;

import org.bukkit.Bukkit;
import org.bukkit.permissions.PermissionDefault;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedHashMap;
import java.util.Map;

public class Permission {

    public final String name;
    public final String description;
    public PermissionDefault defaultValue;

    public Permission(@NotNull String name) {
        this(name, null, null, null);
    }

    public Permission(@NotNull String name, @Nullable String description) {
        this(name, description, null, null);
    }

    public Permission(@NotNull String name, @Nullable PermissionDefault defaultValue) {
        this(name, null, defaultValue, null);
    }

    public Permission(@NotNull String name, @Nullable String description, @Nullable PermissionDefault defaultValue) {
        this(name, description, defaultValue, null);
    }

    public Permission(@NotNull String name, @Nullable Map<String, Boolean> children) {
        this(name, null, null, children);
    }

    public Permission(@NotNull String name, @Nullable String description, @Nullable Map<String, Boolean> children) {
        this(name, description, null, children);
    }

    public Permission(@NotNull String name, @Nullable PermissionDefault defaultValue, @Nullable Map<String, Boolean> children) {
        this(name, null, defaultValue, children);
    }

    Permission(@NotNull String name, @Nullable String description, @Nullable PermissionDefault defaultValue, @Nullable Map<String, Boolean> children) {
        this.name = name;
        this.description = (description == null) ? "" : description;
        this.defaultValue = (defaultValue == null) ? PermissionDefault.OP : defaultValue;

        org.bukkit.permissions.Permission perm = new org.bukkit.permissions.Permission(name, description, defaultValue, children);
        Bukkit.getPluginManager().addPermission(perm);
    }

}