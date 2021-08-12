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
import org.w3c.dom.Text;

@RequiredArgsConstructor
public final class NickCommand implements CommandProvider {

  private final ErisPlayerManager erisPlayerManager;

  @Override
  public Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "nickname",
        "nick a player",
        "nick <name>",
        ErisCoreIdentifiers.NICK_PERMISSION,
        "nick"
    ).withSubCommand()
        .argument(StringArgument.of("name"))
        .asPlayerOnly()
        .handler(context -> {
          Player sender = context.getSenderAsPlayer();
          String name = context.getArgument("name");
          if (name.length() < 3 || name.length() > 16) {
            TextController.send(
                sender,
                TextType.ERROR,
                "The name '<h>{0}</h>' is not a valid name.",
                name
            );
            return;
          }

          ErisPlayer player = this.erisPlayerManager.getPlayer(sender);
          player.getNicknameProfile().setNickName(name, null);
        }).finished();
  }
}
