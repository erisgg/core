package gg.eris.core.command;

import gg.eris.commons.bukkit.command.Command;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.util.CC;
import gg.eris.commons.core.identifier.Identifier;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;

public class HubCommand implements CommandProvider {

    public static final Identifier PERMISSION = Identifier.of("eris", "hub");

    @Override
    public Command.Builder getCommand(CommandManager manager) {
        return manager.newCommandBuilder(
                "hub",
                "returns the player to the main hub",
                PERMISSION
        ).noArgsHandler(context -> {
            Player player = context.getSenderAsPlayer();
            player.sendMessage(CC.RED.bold() + "Teleporting to hub...");
            //teleport to main hub
        });
    }
}
