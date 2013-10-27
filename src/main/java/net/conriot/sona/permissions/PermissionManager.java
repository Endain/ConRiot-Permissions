package net.conriot.sona.permissions;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

class PermissionManager implements Listener {
	@SuppressWarnings("unused")
	private Permissions plugin;
	private HashMap<String, OnlinePerms> perms;
	
	public PermissionManager(Permissions plugin) {
		this.plugin = plugin;
		this.perms = new HashMap<String, OnlinePerms>();
	}
	
	public boolean hasPerm(Player player, String perm) {
		// Check if the player is online
		OnlinePerms playerPerms = this.perms.get(player.getName());
		if(playerPerms != null) {
			// Return if the player has the permission
			return playerPerms.hasPerm(perm);
		}
		
		// Player was not online, default to false
		return false;
	}
	
	public boolean hasAnyPerms(Player player, Set<String> perms) {
		// Check if the player is online
		OnlinePerms playerPerms = this.perms.get(player.getName());
		if(playerPerms != null) {
			// Return if the player has the permission
			return playerPerms.hasAnyPerms(perms);
		}
		
		// Player was not online, default to false
		return false;
	}
	
	public boolean hasAllPerms(Player player, Set<String> perms) {
		// Check if the player is online
		OnlinePerms playerPerms = this.perms.get(player.getName());
		if(playerPerms != null) {
			// Return if the player has the permission
			return playerPerms.hasAllPerms(perms);
		}
		
		// Player was not online, default to false
		return false;
	}
	
	public boolean hasPermInNamespace(Player player, String namespace, String perm) {
		// Check if the player is online
		OnlinePerms playerPerms = this.perms.get(player.getName());
		if(playerPerms != null) {
			// Return if the player has the permission
			return playerPerms.hasPermInNamespace(namespace, perm);
		}
		
		// Player was not online, default to false
		return false;
	}
	
	public boolean hasAnyPermsInNamespace(Player player, String namespace, Set<String> perms) {
		// Check if the player is online
		OnlinePerms playerPerms = this.perms.get(player.getName());
		if(playerPerms != null) {
			// Return if the player has the permission
			return playerPerms.hasAnyPermsInNamespace(namespace, perms);
		}
		
		// Player was not online, default to false
		return false;
	}
	
	public boolean hasAllPermsInNamespace(Player player, String namespace, Set<String> perms) {
		// Check if the player is online
		OnlinePerms playerPerms = this.perms.get(player.getName());
		if(playerPerms != null) {
			// Return if the player has the permission
			return playerPerms.hasAllPermsInNamespace(namespace, perms);
		}
		
		// Player was not online, default to false
		return false;
	}
	
	public boolean addPerm(Player player, String perm) {
		// Check if the player is online
		OnlinePerms playerPerms = this.perms.get(player.getName());
		if(playerPerms != null) {
			// Try to add the permission node
			playerPerms.addPerm(perm);
			// Return success based on if the permission now exists or not
			return playerPerms.hasPerm(perm);
		} else {
			// Log that the player did not have their permissions object loaded for some reason
			Bukkit.getLogger().warning("Could not add permission \"" + perm + "\" to player \"" + player.getName() + "\"!");
			Bukkit.getLogger().warning("Permissions object was not loaded for this player!");
		}
		
		// Permission was not added due to error, return false
		return false;
	}
	
	public boolean removePerm(Player player, String perm) {
		// Check if the player is online
		OnlinePerms playerPerms = this.perms.get(player.getName());
		if(playerPerms != null) {
			// Try to add the permission node
			playerPerms.removePerm(perm);
			// Return success based on if the permission now exists or not
			return !playerPerms.hasPerm(perm);
		} else {
			// Log that the player did not have their permissions object loaded for some reason
			Bukkit.getLogger().warning("Could not remove permission \"" + perm + "\" from player \"" + player.getName() + "\"!");
			Bukkit.getLogger().warning("Permissions object was not loaded for this player!");
		}
		
		// Permission was not added due to error, return false
		return false;
	}
}
