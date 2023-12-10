package org.s1queence.plugin;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.s1queence.plugin.commands.RenameCommand;
import org.s1queence.plugin.commands.ResignCommand;
import org.s1queence.plugin.commands.SignCommand;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

import static org.s1queence.api.S1TextUtils.consoleLog;
import static org.s1queence.plugin.utils.Utils.getTextFromCfg;

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


        consoleLog(getTextFromCfg("enable_msg", textConfig), this);

    }

    @Override
    public void onDisable() {
        consoleLog(getTextFromCfg("disable_msg", textConfig), this);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length != 1) return false;
        if (!args[0].equalsIgnoreCase("reload")) {
            sender.sendMessage(getTextFromCfg("only_reload_msg", textConfig));
            return true;
        }

        if (sender instanceof Player) {
            if (!sender.hasPermission("nbtcc.perms.reload")) {
                sender.sendMessage(getTextFromCfg("no_perm", textConfig));
                return true;
            }
        }

        try {
            File optionsCfgFile = new File(getDataFolder(), "options.yml");
            File textCfgFile = new File(getDataFolder(), "text.yml");
            if (!optionsCfgFile.exists()) optionsConfig = YamlDocument.create(new File(getDataFolder(), "options.yml"), Objects.requireNonNull(getResource("options.yml")));
            if (!textCfgFile.exists()) textConfig = YamlDocument.create(new File(getDataFolder(), "text.yml"), Objects.requireNonNull(getResource("text.yml")));
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

        String reloadMsg = getTextFromCfg("reload_msg", textConfig);
        if (sender instanceof Player) sender.sendMessage(reloadMsg);
        consoleLog(reloadMsg, this);

        return true;
    }
    public YamlDocument getTextConfig() {return textConfig;}
    public List<String> getNbtChangeExceptions() {return nbtChangeExceptions;}
    public boolean isResignCommand() {return resign_command;}
    public boolean isSignCommand() {return sign_command;}
    public boolean isRenameCommand() {return rename_command;}

}
