package gg.eris.core.command;

import gg.eris.commons.bukkit.command.Command;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.LiteralArgument;
import gg.eris.commons.bukkit.command.argument.PlayerArgument;
import gg.eris.commons.bukkit.command.argument.StringArgument;
import gg.eris.commons.bukkit.text.TextController;
import gg.eris.commons.bukkit.text.TextType;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Collection;

import static gg.eris.commons.bukkit.text.TextController.builder;

public class BroadcastCommand implements CommandProvider {

    public static final String PERMISSION = "broadcast";

    @Override
    public Command.Builder getCommand(CommandManager manager) {
        return manager.newCommandBuilder(
                "broadcast",
                "broadcasts a message to all players",
                PERMISSION,
                "bc"
        ).noArgsHandler(context -> {
            System.out.println("no args handler is called");
            context.getCommandSender().sendMessage("Bad usage");
        }).withSubCommand()
                .argument(StringArgument.of("message"))
                .asPlayerOnly()
                .handler(context -> {
                    String message = context.getArgument("message");
                    TextController.Builder builder = builder(message, TextType.INFORMATION);
                    TextController.send(builder, (Collection<Player>) Bukkit.getOnlinePlayers());
                }).finished();
    }
}
