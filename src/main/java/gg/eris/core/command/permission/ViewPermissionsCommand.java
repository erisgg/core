package gg.eris.core.command.permission;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.StringArgument;
import gg.eris.commons.bukkit.text.TextController;
import gg.eris.commons.bukkit.text.TextType;
import gg.eris.core.ErisCore;
import gg.eris.core.ErisCoreIdentifiers;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;

@RequiredArgsConstructor
public final class ViewPermissionsCommand implements CommandProvider {

  private final ErisCore plugin;

  @Override
  public Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "viewpermissions",
        "shows the permissions of a player",
        "viewpermissions <player>",
        ErisCoreIdentifiers.VIEWPERMISSIONS_PERMISSION
    ).withSubCommand()
        .argument(StringArgument.of("target"))
        .handler(context -> {
          String target = context.getArgument("target");

          TextController.send(
              context.getCommandSender(),
              TextType.INFORMATION,
              "Looking up permissions for <h>{0}</h>.",
              target
          );

          Bukkit.getScheduler().runTaskAsynchronously(this.plugin, () -> {
            UUID uuid = this.plugin.getCommons().getErisPlayerManager().getOfflineDataManager()
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

            List<String> permissions =
                this.plugin.getCommons().getErisPlayerManager().getOfflineDataManager()
                    .getRawPermissions(uuid);

            StringBuilder message = new StringBuilder("<h>")
                .append(target)
                .append("'s Permissions</h>");

            for (String permission : permissions) {
              message.append("\n - ").append(permission);
            }

            TextController.send(
                context.getCommandSender(),
                TextType.SUCCESS,
                message.toString()
            );
          });
        }).finished();
  }


}
