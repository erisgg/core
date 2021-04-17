package gg.eris.core.command;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.PlayerArgument;
import org.bukkit.entity.Player;

// todo: messages
public class TeleportCommand implements CommandProvider {

  @Override
  public Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "teleport",
        "teleport players",
        "teleport",
        "tp"
    ).noArgsHandler(context -> {
      System.out.println("no args handler is called");
      context.getCommandSender().sendMessage("Bad usage");
    }).withSubCommand()
        .argument(PlayerArgument.of("to"))
        .asPlayerOnly()
        .handler(context -> {
          Player sender = context.getSenderAsPlayer();
          Player to = context.getArgument("to");
          sender.teleport(to);
        }).finished()
        .withSubCommand()
        .argument(PlayerArgument.of("who"))
        .argument(PlayerArgument.of("to"))
        .handler(context -> {
          Player who = context.getArgument("who");
          Player to = context.getArgument("to");
          who.teleport(to);
        }).finished();
  }
}
