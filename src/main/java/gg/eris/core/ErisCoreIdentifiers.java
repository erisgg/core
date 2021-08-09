package gg.eris.core;

import gg.eris.commons.bukkit.permission.Permission;
import gg.eris.commons.bukkit.permission.PermissionGroup;
import gg.eris.commons.bukkit.permission.PermissionRegistry;
import gg.eris.commons.core.identifier.Identifier;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class ErisCoreIdentifiers {

  public static final Identifier GAMEMODE_PERMISSION = permission("gamemode",
      PermissionGroup.HIGHER_STAFF);
  public static final Identifier TELEPORT_PERMISSION = permission("teleport",
      PermissionGroup.STAFF);
  public static final Identifier MESSAGE_PERMISSION = permission("message", PermissionGroup.ALL);
  public static final Identifier HUB_PERMISSION = permission("hub", PermissionGroup.ALL);

  // Admin+ perms
  public static final Identifier ADDRANK_PERMISSION = permission("addrank",
      PermissionGroup.OWNER_DEVELOPER);
  public static final Identifier SETRANK_PERMISSION = permission("setrank",
      PermissionGroup.OWNER_DEVELOPER);
  public static final Identifier REMOVERANK_PERMISSION = permission("removerank",
      PermissionGroup.OWNER_DEVELOPER);
  public static final Identifier VIEWRANKS_PERMISSION = permission("viewranks",
      PermissionGroup.HIGHER_STAFF);
  public static final Identifier ADDPERMISSION_PERMISSION = permission("addpermission",
      PermissionGroup.OWNER_DEVELOPER);
  public static final Identifier REMOVEPERMISSION_PERMISSION = permission("removepermission",
      PermissionGroup.OWNER_DEVELOPER);
  public static final Identifier VIEWPERMISSIONS_PERMISSION = permission("viewpermissions",
      PermissionGroup.OWNER_DEVELOPER);
  public static final Identifier BROADCAST_PERMISSION = permission("broadcast",
      PermissionGroup.HIGHER_STAFF);
  public static final Identifier UUID_PERMISSION = permission("uuid", PermissionGroup.HIGHER_STAFF);

  private static Identifier permission(String name, PermissionGroup group) {
    return Permission.ofDefault(PermissionRegistry.get(), name, group).getIdentifier();
  }

}
