package gg.eris.core.command;

import gg.eris.commons.bukkit.command.Command;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.Argument;
import gg.eris.commons.bukkit.command.argument.PlayerArgument;
import gg.eris.commons.bukkit.command.argument.StringArgument;
import gg.eris.commons.bukkit.permission.Permission;
import gg.eris.commons.bukkit.util.CC;
import gg.eris.commons.core.identifier.Identifier;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class MessageCommand implements CommandProvider {

    public static final Identifier PERMISSION = Identifier.of("eris", "message");

    @Override
    public Command.Builder getCommand(CommandManager manager) {
        return manager.newCommandBuilder(
                "message",
                "sends a message to a player",
                PERMISSION,
                "msg"
        ).noArgsHandler(context -> {
            System.out.println("no args handler is called");
            context.getCommandSender().sendMessage("Bad usage");
        }).withSubCommand()
                .argument(PlayerArgument.of("player"))
                .variableArgument(StringArgument.of("message"), 1) //label arg will be removed soon, ignore error
                .handler(context -> {
                    List<String> messageStrings = context.getArgument("message");
                    String message = "";
                    for(String messageObject : messageStrings){
                        message = message + messageObject + " ";
                    }
                    Player sender = context.getSenderAsPlayer();
                    Player receiver = context.getArgument("player");

                    receiver.sendMessage(CC.GREEN.underline() + "FROM: " + CC.GOLD.underline() + sender.getName() + CC.WHITE + message);

                }).finished();
    }
}
