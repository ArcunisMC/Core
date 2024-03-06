package com.arcunis.core.permissions;

import com.arcunis.core.Core;
import com.arcunis.core.abstracts.ArcunisPlugin;
import com.arcunis.core.abstracts.Command;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PermissionsCommand extends Command {

    public PermissionsCommand(ArcunisPlugin plugin) {
        super(plugin, "permissions");
        this.setDescription("Manage permissions");
        this.setPermission("arcunis.core.permissions");
        String[] aliases = new String[]{
                "perms"
        };
        this.setAliases(Arrays.asList(aliases));
        register();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(Component.text());
            return false;
        }

        Player executor = (Player) sender;

        PermissionsManager permissionsManager = ((Core) plugin).permissionsManager;

        if (args[0].equalsIgnoreCase("menu")) {
            PermissionsMenu menu = new PermissionsMenu((Core) plugin, executor);
            menu.show(null);
            return false;
        } else if (args.length < 3) {
            sender.sendMessage(Component.text("Invalid usage. /permissions (player|group|menu) [options...]"));
            return false;
        }

        if (args[0].equalsIgnoreCase("player")) {
            if (!args[2].equalsIgnoreCase("parent")) {
                sender.sendMessage(Component.text("Invalid usage. /permissions player parrent set <GROUPNAME>"));
                return false;
            }
            if (!args[3].equalsIgnoreCase("set")) {
                sender.sendMessage(Component.text("Invalid usage. /permissions player parrent set <GROUPNAME>"));
                return false;
            }
            OfflinePlayer player = Bukkit.getOfflinePlayer(args[1]);
            permissionsManager.players.put(player.getUniqueId(), args[4]);
        } else if (args[0].equalsIgnoreCase("group")) {
            if (args[1].equalsIgnoreCase("create")) {
                permissionsManager.groups.put(args[2], new ArrayList<>());
                sender.sendMessage(Component.text("Group '" + args[2] + "' created"));
            } else if (args[1].equalsIgnoreCase("delete")) {
                permissionsManager.groups.remove(args[2]);
                sender.sendMessage(Component.text("Group '" + args[2] + "' deleted"));
            } else if (args[1].equalsIgnoreCase("set") && args.length == 5) {
                List<String> group = permissionsManager.groups.get(args[2]);
                if (args[3].equalsIgnoreCase("add")) {
                    group.add(args[4]);
                } else if (args[3].equalsIgnoreCase("remove")) {
                    group.remove(args[4]);
                }
                permissionsManager.groups.put(args[2], group);
                sender.sendMessage(Component.text("Set permission '" + args[3] + "' to '" + args[4] + "' for group '" + args[2] + "'"));
            } else {
                sender.sendMessage(Component.text("Invalid usage. /permissions group set <PERMISSION> (true|false)"));
            }
        }
        if (args[0].equalsIgnoreCase("group")) {
            for (Map.Entry<UUID, String> entry : permissionsManager.players.entrySet()) {
                if (!entry.getValue().equalsIgnoreCase(args[2])) continue;
                Player player = Bukkit.getPlayer(entry.getKey());
                if (player == null) return false;
                permissionsManager.reloadPerms(player);
            }
        } else if (args[0].equalsIgnoreCase("player")) {
            Player player = Bukkit.getPlayer(args[1]);
            if (player == null) return false;
            permissionsManager.reloadPerms(player);
        }
        return false;
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) {
        List<String> options = new ArrayList<>();
        if (args.length == 1) {
            options.add("menu");
            options.add("player");
            options.add("group");
        } else if (args.length == 2) {
            if (args[0].equalsIgnoreCase("player")) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    options.add(player.getName());
                }
            } else if (args[0].equalsIgnoreCase("group")) {
                options.add("create");
                options.add("delete");
                options.add("set");
            }
        } else if (args.length == 3) {
            if (args[0].equalsIgnoreCase("player")) {
                options.add("parent");
            } else if (args[0].equalsIgnoreCase("group")) {
                if (args[1].equalsIgnoreCase("create")) {
                    options.add("<GROUPNAME>");
                } else {
                    options.addAll(((Core) plugin).permissionsManager.groups.keySet());
                }
            }
        } else if (args.length == 4) {
            if (args[0].equalsIgnoreCase("player")) {
                options.add("set");
            } else if (args[0].equalsIgnoreCase("group") && args[1].equalsIgnoreCase("set")) {
                options.add("add");
                options.add("remove");
            }
        } else if (args.length == 5) {
            if (args[0].equalsIgnoreCase("player")) {
                for (String group : ((Core) plugin).permissionsManager.groups.keySet()) {
                    options.add(group);
                }
            } else if (args[0].equalsIgnoreCase("group") && args[1].equalsIgnoreCase("set")) {
                for (Permission permission : Bukkit.getPluginManager().getPermissions()) {
                    options.add(permission.getName());
                }
            }
        }
        return options;
    }

}
