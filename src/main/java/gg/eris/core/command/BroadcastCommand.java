package gg.eris.core.command;

import gg.eris.commons.bukkit.command.Command;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.PlayerArgument;
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
                "" //TODO add any aliases
        ).noArgsHandler(context -> {
            System.out.println("no args handler is called");
            context.getCommandSender().sendMessage("Bad usage");
        }).withSubCommand()
                .argument(PlayerArgument.of("message"))
                .asPlayerOnly()
                .handler(context -> {
                    String message = context.getArgument("message");
                    TextController.Builder builder = builder(message, TextType.INFORMATION);
                    TextController.send(builder, (Collection<Player>) Bukkit.getOnlinePlayers());
                }).finished()
                .withSubCommand()
                .argument(PlayerArgument.of("message"))
                .argument(PlayerArgument.of("texttype"))
                .handler(context -> {
                    String message = context.getArgument("message");
                    TextType textType = TextType.valueOf(context.getArgument("texttype"));
                    TextController.Builder builder = builder(message, textType);
                    TextController.send(builder, (Collection<Player>) Bukkit.getOnlinePlayers());
                }).finished();
    }
}
