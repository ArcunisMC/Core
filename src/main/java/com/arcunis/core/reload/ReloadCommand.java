package com.arcunis.core.reload;

import com.arcunis.core.abstracts.ArcunisPlugin;
import com.arcunis.core.abstracts.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReloadCommand extends Command {

    public ReloadCommand(ArcunisPlugin plugin) {
        super(plugin, "reload");
        this.setDescription("Reload all plugins using Core");
        this.setPermission("arcunis.core.reload");
        register();
    }

    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String label, @NotNull String[] args) {
        return false;
    }

}
