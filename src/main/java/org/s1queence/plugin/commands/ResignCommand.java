package org.s1queence.plugin.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.s1queence.plugin.NBTChangeCommands;

import java.util.Collection;
import java.util.List;

import static org.s1queence.api.S1TextUtils.getConvertedTextFromConfig;
import static org.s1queence.api.S1Utils.notifyAdminsAboutCommand;

public class ResignCommand extends NBTChangeCommand implements CommandExecutor {
    public ResignCommand(@NotNull NBTChangeCommands plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (!plugin.isResignCommand()) {
            player.sendMessage(getConvertedTextFromConfig(textConfig, "command_disabled_msg", pName));
            return true;
        }

        if (args.length > 1) return false;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().equals(Material.AIR)) {
            player.sendMessage(getConvertedTextFromConfig(textConfig, "item_is_null", pName));
            return true;
        }

        ItemMeta im = item.getItemMeta();
        if (im == null) {
            player.sendMessage(getConvertedTextFromConfig(textConfig, "resign.nothing_to_clear", pName));
            return true;
        }

        if (plugin.isNbtChangeException(item, exceptions) && !player.hasPermission("nbtcc.perms.bypass")) {
            player.sendMessage(getConvertedTextFromConfig(textConfig, "cant_change_nbt_msg", pName));
            return true;
        }

        Collection<? extends Player> onlinePlayers = plugin.getServer().getOnlinePlayers();
        notifyAdminsAboutCommand(onlinePlayers, command.getName(), player.getName() + ": /resign", player.getName(), null);
        List<String> lore = im.getLore();

        if (lore == null || lore.isEmpty()) {
            player.sendMessage(getConvertedTextFromConfig(textConfig, "resign.nothing_to_clear", pName));
            return true;
        }

        if (args.length == 0) {
            player.sendMessage(getConvertedTextFromConfig(textConfig, "resign.successfully_last_row_cleared", pName));
            lore.remove(lore.size() -1);
            im.setLore(lore);
            item.setItemMeta(im);
            return true;
        }

        if (!args[0].equalsIgnoreCase("all")) {
            player.sendMessage(getConvertedTextFromConfig(textConfig, "resign.unexpected_argument", pName));
            return false;
        }

        player.sendMessage(getConvertedTextFromConfig(textConfig, "resign.successfully_all_cleared", pName));
        lore.clear();
        im.setLore(lore);
        item.setItemMeta(im);
        return true;
    }
}
