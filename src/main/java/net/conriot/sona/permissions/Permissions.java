package net.conriot.sona.permissions;

import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Permissions extends JavaPlugin {
	private static PermissionManager pm;
	
	@Override
	public void onEnable() {
		// Instantiate a single permission manager
		Permissions.pm = new PermissionManager(this);
	}
	
	@Override
	public void onDisable() {
		// Nothing to do here
	}
	
	public static boolean hasPerm(Player player, String perm) {
		if(pm == null) {
			Bukkit.getLogger().warning("Could not check permission for \"" + player.getName() + "\"!");
			Bukkit.getLogger().warning("Permission manager is not loaded!");
			return false;
		}
		// Check for a single permission
		return pm.hasPerm(player, perm);
	}
	
	public static boolean hasAnyPerms(Player player, Set<String> perms) {
		if(pm == null) {
			Bukkit.getLogger().warning("Could not check permission for \"" + player.getName() + "\"!");
			Bukkit.getLogger().warning("Permission manager is not loaded!");
			return false;
		}
		// Check for any matching permissions
		return pm.hasAnyPerms(player, perms);
	}
	
	public static boolean hasAllPerms(Player player, Set<String> perms) {
		if(pm == null) {
			Bukkit.getLogger().warning("Could not check permission for \"" + player.getName() + "\"!");
			Bukkit.getLogger().warning("Permission manager is not loaded!");
			return false;
		}
		// Check if all permissions match
		return pm.hasAllPerms(player, perms);
	}
	
	public static boolean hasPermInNamespace(Player player, String namespace, String perm) {
		if(pm == null) {
			Bukkit.getLogger().warning("Could not check permission for \"" + player.getName() + "\"!");
			Bukkit.getLogger().warning("Permission manager is not loaded!");
			return false;
		}
		// Check for a single permission in a namespace
		return pm.hasPermInNamespace(player, namespace, perm);
	}
	
	public static boolean hasAnyPermsInNamespace(Player player, String namespace, Set<String> perms) {
		if(pm == null) {
			Bukkit.getLogger().warning("Could not check permission for \"" + player.getName() + "\"!");
			Bukkit.getLogger().warning("Permission manager is not loaded!");
			return false;
		}
		// Check for any matching permissions in a namespace
		return pm.hasAnyPermsInNamespace(player, namespace, perms);
	}
	
	public static boolean hasAllPermsInNamespace(Player player, String namespace, Set<String> perms) {
		if(pm == null) {
			Bukkit.getLogger().warning("Could not check permission for \"" + player.getName() + "\"!");
			Bukkit.getLogger().warning("Permission manager is not loaded!");
			return false;
		}
		// Check if all permission match in a namespace
		return pm.hasAllPermsInNamespace(player, namespace, perms);
	}
	
	public static boolean addPerm(Player player, String perm) {
		if(pm == null) {
			Bukkit.getLogger().warning("Could not add permission for \"" + player.getName() + "\"!");
			Bukkit.getLogger().warning("Permission manager is not loaded!");
			return false;
		}
		// Try to add a permission node to a player
		return pm.addPerm(player, perm);
	}
	
	public static boolean removePerm(Player player, String perm) {
		if(pm == null) {
			Bukkit.getLogger().warning("Could not remove permission for \"" + player.getName() + "\"!");
			Bukkit.getLogger().warning("Permission manager is not loaded!");
			return false;
		}
		// try to remove a permission node from a player
		return pm.removePerm(player, perm);
	}
}
