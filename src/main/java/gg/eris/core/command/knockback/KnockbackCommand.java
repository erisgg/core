package gg.eris.core.command.knockback;

import gg.eris.commons.bukkit.command.Command.Builder;
import gg.eris.commons.bukkit.command.CommandManager;
import gg.eris.commons.bukkit.command.CommandProvider;
import gg.eris.commons.bukkit.command.argument.DoubleArgument;
import gg.eris.commons.bukkit.command.argument.StringArgument;
import gg.eris.commons.bukkit.text.TextController;
import gg.eris.commons.bukkit.text.TextType;
import gg.eris.core.ErisCoreIdentifiers;
import gg.eris.erisspigot.ErisSpigotKnockbackSettings;
import java.util.Locale;

public final class KnockbackCommand implements CommandProvider {

  @Override
  public Builder getCommand(CommandManager manager) {
    return manager.newCommandBuilder(
        "knockback",
        "modifies knockback",
        "knockback <type> [value]",
        ErisCoreIdentifiers.KNOCKBACK_PERMISSION,
        "kb"
    ).withSubCommand()
        .argument(StringArgument.of("type"))
        .handler(context -> {
          String type = context.getArgument("type");
          type = type.toLowerCase(Locale.ROOT);

          double value;

          switch (type) {
            case "friction":
              value = ErisSpigotKnockbackSettings.KNOCKBACK_FRICTION;
              break;
            case "horizontal":
              value = ErisSpigotKnockbackSettings.KNOCKBACK_HORIZONTAL;
              break;
            case "vertical":
              value = ErisSpigotKnockbackSettings.KNOCKBACK_VERTICAL;
              break;
            case "extra_horizontal":
              value = ErisSpigotKnockbackSettings.KNOCKBACK_EXTRA_HORIZONTAL;
              break;
            case "extra_vertical":
              value = ErisSpigotKnockbackSettings.KNOCKBACK_EXTRA_VERTICAL;
              break;
            case "vertical_limit":
              value = ErisSpigotKnockbackSettings.VERTICAL_LIMIT;
              break;
            default:
              TextController.send(
                  context.getCommandSender(),
                  TextType.INFORMATION,
                  "Type <h>{0}</h> type cannot be found.",
                  type
              );
              return;
          }

          TextController.send(
              context.getCommandSender(),
              TextType.INFORMATION,
              "Value of <h>{0}</h> is <h>{1}</h>.",
              type,
              value
          );
        }).finished()
        .withSubCommand()
        .argument(StringArgument.of("type"))
        .argument(DoubleArgument.of("value"))
        .handler(context -> {
          String type = context.getArgument("type");
          type = type.toLowerCase(Locale.ROOT);
          double value = context.getArgument("value");

          switch (type) {
            case "friction":
              ErisSpigotKnockbackSettings.KNOCKBACK_FRICTION = value;
              break;
            case "horizontal":
              ErisSpigotKnockbackSettings.KNOCKBACK_HORIZONTAL = value;
              break;
            case "vertical":
              ErisSpigotKnockbackSettings.KNOCKBACK_VERTICAL = value;
              break;
            case "extra_horizontal":
              ErisSpigotKnockbackSettings.KNOCKBACK_EXTRA_HORIZONTAL = value;
              break;
            case "extra_vertical":
              ErisSpigotKnockbackSettings.KNOCKBACK_EXTRA_VERTICAL = value;
              break;
            case "vertical_limit":
              ErisSpigotKnockbackSettings.VERTICAL_LIMIT = value;
              break;
            default:
              TextController.send(
                  context.getCommandSender(),
                  TextType.INFORMATION,
                  "Type <h>{0}</h> type cannot be found.",
                  type
              );
              return;
          }

          TextController.send(
              context.getCommandSender(),
              TextType.INFORMATION,
              "Set <h>{0}</h> to <h>{1}</h>.",
              type,
              value
          );

        }).finished();
  }

}