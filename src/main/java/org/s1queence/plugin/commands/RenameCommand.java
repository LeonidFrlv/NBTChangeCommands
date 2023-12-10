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

import static org.s1queence.api.S1TextUtils.removeAllChatColorCodesFromString;
import static org.s1queence.api.S1Utils.notifyAdminsAboutCommand;
import static org.s1queence.plugin.utils.Utils.getTextFromCfg;
import static org.s1queence.plugin.utils.Utils.isNbtChangeException;

public class RenameCommand extends NBTChangeCommand implements CommandExecutor {
    public RenameCommand(@NotNull NBTChangeCommands plugin) {
        super(plugin);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if (!(sender instanceof Player)) return true;
        Player player = (Player) sender;
        if (!plugin.isRenameCommand()) {
            player.sendMessage(getTextFromCfg("command_disabled_msg", textConfig));
            return true;
        }

        if (args.length == 0) return false;

        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType().equals(Material.AIR)) {
            player.sendMessage(getTextFromCfg("item_is_null", textConfig));
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

        if (isNbtChangeException(item, exceptions)) {
            player.sendMessage(getTextFromCfg("cant_change_nbt_msg", textConfig));
            return true;
        }


        String nameWithoutFormatting = removeAllChatColorCodesFromString(name.toString());
        if (exceptions.contains(nameWithoutFormatting)) {
            player.sendMessage(getTextFromCfg("rename.is_exception", textConfig));
            return true;
        }

        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) return true;
        itemMeta.setDisplayName(name.toString());
        item.setItemMeta(itemMeta);
        player.sendMessage(getTextFromCfg("rename.successfully", textConfig));
        notifyAdminsAboutCommand(plugin.getServer().getOnlinePlayers(), command.getName(),  player.getName() + ": " + name, player.getName(), null);
        return true;
    }
}
