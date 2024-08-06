package org.s1queence.plugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.s1queence.plugin.commands.RenameCommand;
import org.s1queence.plugin.commands.ResignCommand;
import org.s1queence.plugin.commands.SignCommand;
import org.s1queence.plugin.libs.YamlDocument;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.s1queence.api.S1TextUtils.*;

public final class NBTChangeCommands extends JavaPlugin implements CommandExecutor {
    private boolean sign_command;
    private boolean resign_command;
    private boolean rename_command;
    private List<String> nbtChangeExceptions;
    private YamlDocument textConfig;
    private YamlDocument optionsConfig;

    @Override
    public void onEnable() {
        try {
            textConfig = YamlDocument.create(new File(getDataFolder(), "text.yml"), Objects.requireNonNull(getResource("text.yml")));
            optionsConfig = YamlDocument.create(new File(getDataFolder(), "options.yml"), Objects.requireNonNull(getResource("options.yml")));
        } catch (IOException ignored) {

        }

        sign_command = optionsConfig.getBoolean("commands_enabler.sign");
        resign_command = optionsConfig.getBoolean("commands_enabler.resign");
        rename_command = optionsConfig.getBoolean("commands_enabler.rename");
        nbtChangeExceptions = optionsConfig.getStringList("nbt_change_exceptions");

        Objects.requireNonNull(getServer().getPluginCommand("rename")).setExecutor(new RenameCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("sign")).setExecutor(new SignCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("resign")).setExecutor(new ResignCommand( this));
        Objects.requireNonNull(getServer().getPluginCommand("nbtchangecommands")).setExecutor(this);


        consoleLog(getConvertedTextFromConfig(textConfig, "enable_msg", getName()), this);
    }

    @Override
    public void onDisable() {
        consoleLog(getConvertedTextFromConfig(textConfig, "disable_msg", getName()), this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) return false;
        if (!args[0].equalsIgnoreCase("reload")) return false;

        try {
            File optionsCfgFile = new File(getDataFolder(), "options.yml");
            File textCfgFile = new File(getDataFolder(), "text.yml");

            if (!optionsCfgFile.exists()) optionsConfig = YamlDocument.create(new File(getDataFolder(), "options.yml"), Objects.requireNonNull(getResource("options.yml")));
            if (!textCfgFile.exists()) textConfig = YamlDocument.create(new File(getDataFolder(), "text.yml"), Objects.requireNonNull(getResource("text.yml")));

            if (optionsConfig.hasDefaults()) Objects.requireNonNull(optionsConfig.getDefaults()).clear();

            optionsConfig.reload();
            textConfig.reload();
        } catch (IOException ignored) {

        }

        sign_command = optionsConfig.getBoolean("commands_enabler.sign");
        resign_command = optionsConfig.getBoolean("commands_enabler.resign");
        rename_command = optionsConfig.getBoolean("commands_enabler.rename");
        nbtChangeExceptions = optionsConfig.getStringList("nbt_change_exceptions");

        Objects.requireNonNull(getServer().getPluginCommand("rename")).setExecutor(new RenameCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("sign")).setExecutor(new SignCommand(this));
        Objects.requireNonNull(getServer().getPluginCommand("resign")).setExecutor(new ResignCommand( this));

        String reloadMsg = getConvertedTextFromConfig(textConfig, "reload_msg", getName());
        if (sender instanceof Player) sender.sendMessage(reloadMsg);
        consoleLog(reloadMsg, this);

        return true;
    }

    public boolean isNbtChangeException(ItemStack item, List<String> exceptions) {
        if (exceptions.contains(item.getType().toString())) return true;

        ItemMeta im = item.getItemMeta();
        if (im == null || !im.hasDisplayName()) return false;

        return exceptions.contains(removeAllChatColorCodesFromString(item.getItemMeta().getDisplayName()));
    }
    public YamlDocument getTextConfig() {return textConfig;}
    public List<String> getNbtChangeExceptions() {return nbtChangeExceptions;}
    public boolean isResignCommand() {return resign_command;}
    public boolean isSignCommand() {return sign_command;}
    public boolean isRenameCommand() {return rename_command;}

}
