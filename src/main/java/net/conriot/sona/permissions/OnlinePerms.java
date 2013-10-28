package net.conriot.sona.permissions;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import lombok.Getter;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.conriot.sona.mysql.IOCallback;
import net.conriot.sona.mysql.MySQL;
import net.conriot.sona.mysql.Query;
import net.conriot.sona.mysql.Result;

class OnlinePerms implements IOCallback {
	private HashMap<String, HashSet<String>> namespaces;
	private HashSet<String> perms;
	private Player player;
	@Getter private boolean loaded;
	
	public OnlinePerms(Player player) {
		this.player = player;
		this.namespaces = new HashMap<String, HashSet<String>>();
		this.perms = new HashSet<String>();
		this.loaded = false;
		
		// Attempt to load permission data from the database
		loadPerms();
	}
	
	public boolean hasPerm(String perm) {
		// Default to false if perms are not loaded for safety
		if(!this.loaded)
			return false;
		
		// Return if the player has the permission
		return this.perms.contains(perm);
	}
	
	public boolean hasAnyPerms(Set<String> perms) {
		// Default to false if perms are not loaded for safety
		if(!this.loaded)
			return false;
		
		// Check if any of the given perms match with player perms
		for(String perm : perms)
			if(this.perms.contains(perm))
				return true;
		
		// No perms matched, return false
		return false;
	}
	
	public boolean hasAllPerms(Set<String> perms) {
		// Default to false if perms are not loaded for safety
		if(!this.loaded)
			return false;
		
		// Check if any of the given perms do not match, if so return false
		for(String perm : perms)
			if(!this.perms.contains(perm))
				return false;
		
		// All perms matched, return true
		return true;
	}
	
	public boolean hasPermInNamespace(String namespace, String perm) {
		// Default to false if perms are not loaded for safety
		if(!this.loaded)
			return false;
		
		// If the namespace exists then return if the player has that permission
		HashSet<String> ns = this.namespaces.get(namespace);
		if(ns != null)
			return ns.contains(perm);
		
		// The player didn't have the permission, return false
		return false;
	}
	
	public boolean hasAnyPermsInNamespace(String namespace, Set<String> perms) {
		// Default to false if perms are not loaded for safety
		if(!this.loaded)
			return false;
		
		// If the namespace exists then check it for permissions
		HashSet<String> ns = this.namespaces.get(namespace);
		if(ns != null) {
			// Check if any of the given perms match with player perms
			for(String perm : perms)
				if(ns.contains(perm))
					return true;
		}
		
		// No perms matched, return false
		return false;
	}
	
	public boolean hasAllPermsInNamespace(String namespace, Set<String> perms) {
		// Default to false if perms are not loaded for safety
		if(!this.loaded)
			return false;
		
		// If the namespace exists then check it for permissions
		HashSet<String> ns = this.namespaces.get(namespace);
		if(ns != null) {
			// Check if any of the given perms do not match, if so return false
			for(String perm : perms)
				if(!ns.contains(perm))
					return false;
		}
		
		// All perms matched, return true
		return true;
	}
	
	public void addPerm(String perm) {
		// Find the namespace of the permission if there is one
		String ns = getNamespace(perm);
		
		// Try adding it the the namespace
		addPermToNamespace(ns, perm);
		
		// Add the permission node the the global list of permissions
		this.perms.add(perm);
		
		// Create a query to save the player's new permission
		Query q = MySQL.makeQuery();
		q.setQuery("INSERT INTO permissions (name,permission) VALUES (?,?)");
		q.add(this.player.getName());
		q.add(perm);
		
		// Execute query to asynchronously save the permission
		MySQL.execute(this, "add", q);
	}
	
	public void removePerm(String perm) {
		// Find the namespace of the permission if there is one
		String ns = getNamespace(perm);
		
		// Try removing it from the namespace if it exists
		if(ns != null) {
			HashSet<String> set = this.namespaces.get(ns);
			if(set != null)
				set.remove(perm);
			else {
				Bukkit.getLogger().warning("Could not remove permission \"" + perm + "\" from namespace \"" + ns + "\"");
				Bukkit.getLogger().warning("No permissions exist under that namespace");
			}
		}
		
		// Remove the permission node the the global list of permissions
		this.perms.remove(perm);
		
		// Create a query to save the player's new permission
		Query q = MySQL.makeQuery();
		q.setQuery("DELETE FROM permissions WHERE name=? AND permission=?");
		q.add(this.player.getName());
		q.add(perm);
		
		// Execute query to asynchronously delete the permission
		MySQL.execute(this, "add", q);
	}
	
	private void loadPerms() {
		// Create a query to retrieve player's permissions
		Query q = MySQL.makeQuery();
		q.setQuery("SELECT permission FROM permissions WHERE name=?");
		q.add(this.player.getName());
		
		// Execute query to asynchronously load permissions
		MySQL.execute(this, "load", q);
	}
	
	private String getNamespace(String permission) {
		// Split the string to find the namespace of the node if there is one
		String[] split = permission.split("\\.");
		
		// If namespace found, return it
		if(split.length > 1)
			return split[0];
		
		// No namespace, return null
		return null;
	}
	
	private void addPermToNamespace(String ns, String perm) {
		// check if the node had a namespace, if so add it to that namespace
		if(ns != null) {
			if(this.namespaces.containsKey(ns)) {
				// Add to the namespace if it already exists
				this.namespaces.get(ns).add(perm);
			} else {
				// Otherwise create the namespace and add the node
				HashSet<String> namespace = new HashSet<String>();
				namespace.add(perm);
				this.namespaces.put(ns, namespace);
			}
		}
	}
	
	@Override
	public void complete(boolean success, Object tag, Result result) {
		if(tag == "load") {
			if(success) {
				while(result.next()) {
					// Split the string to find the namespace of the node if there is one
					String ns = getNamespace((String)result.get(0));
					
					// Try adding it the the namespace
					addPermToNamespace(ns, (String)result.get(0));
					
					// Add the permission node the the global list of permissions
					this.perms.add((String)result.get(0));
					
					// Flag this player as having their permissions loaded
					this.loaded = true;
				}
			} else {
				Bukkit.getLogger().warning("Could not load permissions for \"" + player.getName() + "\"");
			}
		}
	}
}
