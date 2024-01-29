package org.s1queence.plugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.s1queence.plugin.NBTChangeCommands;

import java.util.Arrays;

import static org.s1queence.api.S1TextUtils.getConvertedTextFromConfig;
import static org.s1queence.api.S1TextUtils.removeAllChatColorCodesFromString;
import static org.s1queence.api.S1Utils.notifyAdminsAboutCommand;

public class RenameCommand extends NBTChangeCommand implements CommandExecutor {
    public RenameCommand(@NotNull NBTChangeCommands plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (!plugin.isRenameCommand()) {
            player.sendMessage(getConvertedTextFromConfig(textConfig, "command_disabled_msg", pName));
            return true;
        }

        if (args.length == 0) return false;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().equals(Material.AIR)) {
            player.sendMessage(getConvertedTextFromConfig(textConfig, "item_is_null", pName));
            return true;
        }

        StringBuilder name = new StringBuilder();
        for (String s1 : args) {
            if (args.length == Arrays.asList(args).indexOf(s1) + 1) {
                name.append(s1);
                break;
            }
            name.append(s1).append(" ");
        }
        name = new StringBuilder(ChatColor.translateAlternateColorCodes('&', name.toString()));

        if (plugin.isNbtChangeException(item, exceptions) && !player.hasPermission("nbtcc.perms.bypass")) {
            player.sendMessage(getConvertedTextFromConfig(textConfig, "cant_change_nbt_msg", pName));
            return true;
        }

        String nameWithoutFormatting = removeAllChatColorCodesFromString(name.toString());
        if (exceptions.contains(nameWithoutFormatting)) {
            player.sendMessage(getConvertedTextFromConfig(textConfig, "rename.is_exception", pName));
            return true;
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return true;
        itemMeta.setDisplayName(name.toString());
        item.setItemMeta(itemMeta);
        player.sendMessage(getConvertedTextFromConfig(textConfig, "rename.successfully", pName));
        notifyAdminsAboutCommand(plugin.getServer().getOnlinePlayers(), command.getName(),  player.getName() + ": " + name, player.getName(), null);
        return true;
    }
}
