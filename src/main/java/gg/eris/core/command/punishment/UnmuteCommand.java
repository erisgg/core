package gg.eris.core.command.punishment;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.StringArgument;
import gg.eris.commons.bukkit.player.ErisPlayer;
import gg.eris.commons.bukkit.text.TextController;
import gg.eris.commons.bukkit.text.TextType;
import gg.eris.core.ErisCore;
import gg.eris.core.ErisCoreIdentifiers;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
public final class UnmuteCommand implements CommandProvider {

  private final ErisCore plugin;

  @Override
  public Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "unmute",
        "unmute command",
        "unmute [player]",
        ErisCoreIdentifiers.UNMUTE_PERMISSION
    ).withSubCommand()
        .argument(StringArgument.of("target"))
        .handler(context -> {
          String targetName = context.getArgument("target");

          TextController.send(
              context.getCommandSender(),
              TextType.INFORMATION,
              "Attempting to unmute <h>{0}</h>.",
              targetName
          );

          Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            UUID uuid = this.plugin.getCommons().getOfflineDataManager().getUuid(targetName);
            if (uuid == null) {
              TextController.send(
                  context.getCommandSender(),
                  TextType.ERROR,
                  "Player <h>{0}</h> could not be found.",
                  targetName
              );
              return;
            }

            this.plugin.getCommons().getOfflineDataManager().addUnmute(uuid);

            ErisPlayer erisPlayer =
                this.plugin.getCommons().getErisPlayerManager().getPlayer(uuid);

            if (erisPlayer != null) {
              erisPlayer.getPunishmentProfile().unmute();
            }

            TextController.send(
                context.getCommandSender(),
                TextType.SUCCESS,
                "Successfully unmuted <h>{0}</h>.",
                targetName
            );
          });
        }).finished();
  }
}
