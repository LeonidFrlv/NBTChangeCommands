package org.s1queence.plugin.commands;

import org.jetbrains.annotations.NotNull;
import org.s1queence.plugin.NBTChangeCommands;
import org.s1queence.plugin.libs.YamlDocument;

import java.util.List;

public class NBTChangeCommand {
    protected final NBTChangeCommands plugin;
    protected final List<String> exceptions;
    protected final YamlDocument textConfig;
    protected final String pName;

    public NBTChangeCommand(@NotNull NBTChangeCommands plugin) {
        this.plugin = plugin;
        this.textConfig = plugin.getTextConfig();
        this.pName = plugin.getName();
        this.exceptions = plugin.getNbtChangeExceptions();
    }

}
