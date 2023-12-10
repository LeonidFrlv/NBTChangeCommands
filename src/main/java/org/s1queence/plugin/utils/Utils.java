package org.s1queence.plugin.utils;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.ChatColor;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static java.util.Optional.ofNullable;

public class Utils {
    public static boolean isNbtChangeException(ItemStack item, List<String> exceptions) {
        return exceptions.contains(item.getType().toString()) || (item.hasItemMeta() && item.getItemMeta() != null && item.getItemMeta().hasDisplayName() && exceptions.contains(item.getItemMeta().getDisplayName()));
    }

    public static String getTextFromCfg(String path, YamlDocument config) {
        String msg = config.getString(path);
        String title = "[" + ChatColor.GOLD + "NBTChangeCommands" + ChatColor.WHITE + "]";

        if (msg == null)  {
            String nullMsgError = ofNullable(config.getString("msg_is_null")).orElse("&6%plugin% FATAL ERROR." + " We recommend that you delete the text.yml file from the plugin folder and use reload config.");
            return ChatColor.translateAlternateColorCodes('&', nullMsgError.replace("%plugin%", title).replace("%msg_path%", path));
        }

        return (ChatColor.translateAlternateColorCodes('&', msg.replace("%plugin%", title)));
    }
}
