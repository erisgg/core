package gg.eris.core.command;

import com.fasterxml.jackson.databind.ObjectMapper;
import gg.eris.commons.bukkit.command.Command;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.PlayerArgument;
import gg.eris.commons.bukkit.command.argument.StringArgument;
import gg.eris.commons.bukkit.text.TextController;
import gg.eris.commons.bukkit.text.TextType;
import gg.eris.commons.bukkit.util.PlayerUtil;
import gg.eris.core.ErisCoreIdentifiers;
import org.bukkit.entity.Player;

public final class SendCommand implements CommandProvider {

  @Override
  public Command.Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "send",
        "sends a player to a server",
        "send <player> <server>",
        ErisCoreIdentifiers.SEND_PERMISSION
    ).withSubCommand()
        .argument(PlayerArgument.of("player"))
        .argument(StringArgument.of("server"))
        .handler(context -> {
          Player player = context.getArgument("player");

          if (player == null) {
            TextController.send(
                context.getCommandSender(),
                TextType.INFORMATION,
                "Player <h>{0}</h> could not be found.",
                context.getRawArgs()[0]
            );

            return;
          }

          String server = context.getArgument("server");
          PlayerUtil.sendToServer(player, server);

          TextController.send(
              context.getCommandSender(),
              TextType.SUCCESS,
              "Sending <h>{0}</h> to <h>{1}</h>.",
              player.getName(),
              server
          );
        }).finished();
  }
}