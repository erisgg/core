package gg.eris.core;

import gg.eris.commons.bukkit.permission.Permission;
import gg.eris.commons.core.identifier.Identifier;
import lombok.experimental.UtilityClass;
import org.bukkit.Utility;

@UtilityClass
public final class ErisCoreIdentifiers {

  public static final Identifier GAMEMODE_PERMISSION = permission("gamemode");
  public static final Identifier TELEPORT_PERMISSION = permission("teleport");

  private static Identifier permission(String name) {
    return Permission.DEFAULT_IDENTIFIER_PROVIDER.id(name);
  }

}
