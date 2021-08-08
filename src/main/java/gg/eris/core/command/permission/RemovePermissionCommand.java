package gg.eris.core.command.permission;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.StringArgument;
import gg.eris.commons.bukkit.text.TextController;
import gg.eris.commons.bukkit.text.TextType;
import gg.eris.commons.core.identifier.Identifier;
import gg.eris.core.ErisCore;
import gg.eris.core.ErisCoreIdentifiers;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
public final class RemovePermissionCommand implements CommandProvider {

  private final ErisCore plugin;

  @Override
  public Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "removepermission",
        "removes a permission from a player",
        "removepermission <player> <permission>",
        ErisCoreIdentifiers.REMOVEPERMISSION_PERMISSION
    ).withSubCommand()
        .argument(StringArgument.of("target"))
        .argument(StringArgument.of("permission"))
        .handler(context -> {
          String target = context.getArgument("target");
          String permission = context.getArgument("permission");

          if (!Identifier.isValid(permission)) {
            TextController.send(
                context.getCommandSender(),
                TextType.ERROR,
                "Identifier <h>{0}</h> is not valid.",
                permission
            );
            return;
          }

          Identifier identifier = Identifier.fromString(permission);

          TextController.send(
              context.getCommandSender(),
              TextType.INFORMATION,
              "Removing permission <h>{0}</h> to <h>{1}</h>.",
              permission,
              target
          );

          Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            UUID uuid =
                this.plugin.getCommons().getErisPlayerManager().getOfflineDataManager()
                    .getUuid(target);

            if (uuid == null) {
              TextController.send(
                  context.getCommandSender(),
                  TextType.ERROR,
                  "Could not find player <h>{0}</h>.",
                  target
              );
              return;
            }

            boolean modified = this.plugin.getCommons().getErisPlayerManager()
                .getOfflineDataManager().removePermission(uuid, identifier);

            if (modified) {
              TextController.send(
                  context.getCommandSender(),
                  TextType.SUCCESS,
                  "Revoked permission '<h>{0}</h>' from <h>{1}</h>.",
                  identifier.toString(),
                  target
              );
            } else {
              TextController.send(
                  context.getCommandSender(),
                  TextType.ERROR,
                  "No permissions have been updated. They may already have it. If not, try again."
              );
            }
          });
        }).finished();
  }
}
