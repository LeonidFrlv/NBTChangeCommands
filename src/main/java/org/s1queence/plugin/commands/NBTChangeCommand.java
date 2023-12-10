package org.s1queence.plugin.commands;

import dev.dejvokep.boostedyaml.YamlDocument;
import org.jetbrains.annotations.NotNull;
import org.s1queence.plugin.NBTChangeCommands;

import java.util.List;

public class NBTChangeCommand {
    protected final NBTChangeCommands plugin;
    protected final List<String> exceptions;
    protected final YamlDocument textConfig;

    public NBTChangeCommand(@NotNull NBTChangeCommands plugin) {
        this.plugin = plugin;
        this.textConfig = plugin.getTextConfig();
        this.exceptions = plugin.getNbtChangeExceptions();
    }

}
