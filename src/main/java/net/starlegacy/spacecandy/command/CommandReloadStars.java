package net.starlegacy.spacecandy.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.starlegacy.spacecandy.SpaceRenderHandler;

public class CommandReloadStars extends CommandBase {
    @Override
    public String getName() {
        return "reloadstars";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "/reloadstars";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        SpaceRenderHandler.generateStars();
    }
}
