package com.arcunis.core.abstracts;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public abstract class Command extends BukkitCommand {

    protected final ArcunisPlugin plugin;

    public Command(ArcunisPlugin plugin, @NotNull String name) {
        super(name);
        this.plugin = plugin;
    }

    public abstract boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args);
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        return new ArrayList<>();
    }

    public void register() {
        if (this.getPermission() != null && Bukkit.getPluginManager().getPermissions().contains(Bukkit.getPluginManager().getPermission(this.getPermission()))) {
            Bukkit.getLogger().info("Registring command " + getName());
            Bukkit.getCommandMap().register(this.getName(), this);
        } else {
            Bukkit.getLogger().warning("Could not register command '" + this.getName()  + "' as it uses a permission that is not registered");
        }
    }

}
