package com.arcunis.core.enums;

import net.kyori.adventure.text.format.TextColor;

public enum ChatColor {

    DARK_RED(TextColor.color(11141120)),
    RED(TextColor.color(16733525)),
    GOLD(TextColor.color(16755200)),
    YELLOW(TextColor.color(16777045)),
    DARK_GREEN(TextColor.color(43520)),
    GREEN(TextColor.color(5635925)),
    AQUA(TextColor.color(5636095)),
    DARK_AQUA(TextColor.color(43690)),
    DARK_BLUE(TextColor.color(170)),
    BLUE(TextColor.color(5592575)),
    LIGHT_PURPLE(TextColor.color(16733695)),
    DARK_PURPLE(TextColor.color(11141290)),
    WHITE(TextColor.color(16777215)),
    GRAY(TextColor.color(11184810)),
    DARK_GRAY(TextColor.color(5592405)),
    BLACK(TextColor.color(0));

    public final TextColor color;

    ChatColor(TextColor color) {
        this.color = color;
    }
}