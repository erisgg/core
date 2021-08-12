package gg.eris.core.command.nick;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.StringArgument;
import gg.eris.commons.bukkit.player.ErisPlayer;
import gg.eris.commons.bukkit.player.ErisPlayerManager;
import gg.eris.commons.bukkit.text.TextController;
import gg.eris.commons.bukkit.text.TextType;
import gg.eris.core.ErisCoreIdentifiers;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public final class UnnickCommand implements CommandProvider {

  private final ErisPlayerManager erisPlayerManager;

  @Override
  public Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "unnickname",
        "nick a player",
        "nick <name>",
        ErisCoreIdentifiers.UNNICK_PERMISSION,
        "unnick"
    ).noArgsHandler(context -> {
      ErisPlayer player = this.erisPlayerManager.getPlayer(context.getSenderAsPlayer());
      if (!player.getNicknameProfile().isNicked()) {
        TextController.send(
            context.getSenderAsPlayer(),
            TextType.ERROR,
            "You are not currently nicked."
        );
      } else {
        String oldName = player.getDisplayName();
        player.getNicknameProfile().unnick();
        TextController.send(
            context.getSenderAsPlayer(),
            TextType.SUCCESS,
            "You have removed your nickname of <h>{0}</h>.",
            oldName
        );
      }
    }, true);
  }
}
