package gg.eris.core.command;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.PlayerArgument;
import gg.eris.commons.bukkit.text.TextController;
import gg.eris.commons.bukkit.text.TextType;
import gg.eris.core.ErisCoreIdentifiers;
import org.bukkit.entity.Player;

// todo: messages
public final class TeleportCommand implements CommandProvider {

  private static final String USAGE = "tp <player> [other]";

  @Override
  public Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "teleport",
        "teleport players",
        USAGE,
        ErisCoreIdentifiers.TELEPORT_PERMISSION,
        "tp"
    ).noArgsHandler(context -> {
      TextController.send(context.getCommandSender(), "Invalid usage. Use <h>/" + USAGE + "</h>.");
    }).withSubCommand()
        .argument(PlayerArgument.of("to"))
        .asPlayerOnly()
        .handler(context -> {
          Player sender = context.getSenderAsPlayer();
          Player to = context.getArgument("to");

          if (to == null) {
            TextController.send(
                sender,
                TextType.ERROR,
                "Player <h>{0}</h> is not on your server.",
                context.getRawArgs()[0]
            );
          } else {
            sender.teleport(to);
          }
        }).finished()
        .withSubCommand()
        .argument(PlayerArgument.of("who"))
        .argument(PlayerArgument.of("to"))
        .handler(context -> {
          Player who = context.getArgument("who");
          Player to = context.getArgument("to");

          if (who == null) {
            TextController.send(
                context.getCommandSender(),
                TextType.ERROR,
                "Player <h>{0}</h> is not on your server.",
                context.getRawArgs()[0]
            );
          } else if (to == null) {
            TextController.send(
                context.getCommandSender(),
                TextType.ERROR,
                "Player <h>{0}</h> is not on your server.",
                context.getRawArgs()[1]
            );
          } else {
            who.teleport(to);
          }
        }).finished();
  }
}
