package gg.eris.core.command.nick;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.StringArgument;
import gg.eris.commons.bukkit.player.ErisPlayer;
import gg.eris.commons.bukkit.player.ErisPlayerManager;
import gg.eris.commons.bukkit.player.nickname.PlayerNicknamePipeline;
import gg.eris.commons.bukkit.text.TextController;
import gg.eris.commons.bukkit.text.TextType;
import gg.eris.core.ErisCoreIdentifiers;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public final class RealNameCommand implements CommandProvider {

  private final ErisPlayerManager erisPlayerManager;

  @Override
  public Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "realname",
        "real name of a player",
        "realname <name>",
        ErisCoreIdentifiers.REALNAME_PERMISSION
    ).withSubCommand()
        .argument(StringArgument.of("name"))
        .handler(context -> {
          Player sender = context.getSenderAsPlayer();
          String name = context.getArgument("name");
          if (!PlayerNicknamePipeline.isValidNickName(name)) {
            TextController.send(
                sender,
                TextType.ERROR,
                "The name '<h>{0}</h>' is not a valid name.",
                name
            );
            return;
          }

          for (ErisPlayer player : this.erisPlayerManager.getPlayers()) {
            if (player.getDisplayName().equalsIgnoreCase(name)) {
              TextController.send(
                  context.getCommandSender(),
                  TextType.SUCCESS,
                  "The nickname <h>{0}</h> belongs to <h>{1}</h>.",
                  player.getDisplayName(),
                  player.getName()
              );
              return;
            }
          }

          TextController.send(
              context.getCommandSender(),
              TextType.ERROR,
              "The nickname <h>{0}</h> is not present on your current server.",
              name
          );
        }).finished();
  }
}
