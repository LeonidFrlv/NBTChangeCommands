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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.s1queence.api.S1TextUtils.getConvertedTextFromConfig;
import static org.s1queence.api.S1Utils.notifyAdminsAboutCommand;

public class SignCommand extends NBTChangeCommand implements CommandExecutor {
    public SignCommand(@NotNull NBTChangeCommands plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (!plugin.isSignCommand()) {
            player.sendMessage(getConvertedTextFromConfig(textConfig, "command_disabled_msg", pName));
            return true;
        }

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().equals(Material.AIR)) {
            player.sendMessage(getConvertedTextFromConfig(textConfig, "item_is_null", pName));
            return true;
        }

        StringBuilder sign = new StringBuilder();
        if (args.length == 0) {
            sign.append(ChatColor.DARK_GRAY).append(getConvertedTextFromConfig(textConfig, "sign.sign_without_any_text_prefix", pName)).append(player.getName());
        } else {
            for (String s1 : args) {
                if (args.length == Arrays.asList(args).indexOf(s1) + 1) {
                    sign.append(s1);
                    break;
                }
                sign.append(s1).append(" ");
            }
        }

        if (plugin.isNbtChangeException(item, exceptions) && !player.hasPermission("nbtcc.perms.bypass")) {
            player.sendMessage(getConvertedTextFromConfig(textConfig, "cant_change_nbt_msg", pName));
            return true;
        }

        Collection<? extends Player> onlinePlayers = plugin.getServer().getOnlinePlayers();

        sign = new StringBuilder(ChatColor.translateAlternateColorCodes('&', sign.toString()));
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return true;
        List<String> lore = itemMeta.getLore() == null ? new ArrayList<>() : itemMeta.getLore();
        lore.add(sign.toString());
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);
        notifyAdminsAboutCommand(onlinePlayers, command.getName(), player.getName() + ": " + sign, player.getName(), null);
        player.sendMessage(getConvertedTextFromConfig(textConfig, "sign.successfully", pName));
        String warning = getConvertedTextFromConfig(textConfig, "sign.warning", pName);
        if (sign.length() >= 50 && !warning.equalsIgnoreCase("none")) player.sendMessage(warning);
        return true;
    }
}
