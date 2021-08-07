package gg.eris.core.command;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.text.TextController;
import gg.eris.commons.bukkit.text.TextType;
import gg.eris.commons.bukkit.util.PlayerUtil;
import gg.eris.core.ErisCoreIdentifiers;

public final class HubCommand implements CommandProvider {

  private static final String LOBBY_HANDLE = "lobby";

  @Override
  public Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "hub",
        "returns the player to the main hub",
        "hub",
        ErisCoreIdentifiers.HUB_PERMISSION,
        "lobby"
    ).noArgsHandler(context -> {
      TextController.send(
          context.getSenderAsPlayer(),
          TextType.INFORMATION,
          "Sending you to hub..."
      );
      PlayerUtil.sendToServer(context.getSenderAsPlayer(), LOBBY_HANDLE);
    }, true);
  }
}
