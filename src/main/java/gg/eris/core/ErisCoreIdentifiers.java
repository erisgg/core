package gg.eris.core;

import gg.eris.commons.bukkit.ErisBukkitCommons;
import gg.eris.commons.bukkit.permission.Permission;
import gg.eris.commons.bukkit.permission.PermissionRegistry;
import gg.eris.commons.core.identifier.Identifier;
import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;

@UtilityClass
public final class ErisCoreIdentifiers {

  public static final Identifier GAMEMODE_PERMISSION = permission("gamemode");
  public static final Identifier TELEPORT_PERMISSION = permission("teleport");
  public static final Identifier MESSAGE_PERMISSION = permission("message");
  public static final Identifier HUB_PERMISSION = permission("hub");

  private static Identifier permission(String name) {
    PermissionRegistry registry =
        Bukkit.getServicesManager()
            .getRegistration(ErisBukkitCommons.class)
            .getProvider()
            .getPermissionRegistry();

    return Permission.ofDefault(registry, name).getIdentifier();
  }

}
