package com.arcunis.core;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public abstract class Command extends org.bukkit.command.Command {

    protected JavaPlugin plugin;
    private boolean playerOnly = false;

    Command(@NotNull JavaPlugin plugin, @NotNull String name) {
        super(name);
        this.plugin = plugin;
        register();
    }

    Command(@NotNull JavaPlugin plugin, @NotNull String name, @NotNull String description, @NotNull String usage, @NotNull List<String> aliases) {
        super(name, description, usage, aliases);
        this.plugin = plugin;
        register();
    }

    private void register() {
        Bukkit.getCommandMap().register(plugin.getName(), this);
    }

    protected void setPlayerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String[] args) {
        if (playerOnly && !(sender instanceof Player)) {
            sender.sendMessage(Component.text("This is a player only command"));
            return false;
        } else if (this.getPermission() != null && !sender.hasPermission(this.getPermission())) {
            sender.sendMessage(Component.text("You are not permitted to use this command"));
            return false;
        }
        onCommand(sender, args);
        return false;
    }

    public abstract void onCommand(@NotNull CommandSender sender, @NotNull String[] args);

}
