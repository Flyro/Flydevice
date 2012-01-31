package fly.fd2d.fd;


import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class FDPL implements Listener {
	private WorldGuardPlugin worldGuard;
	private final FD plugin;
	public FDPL(FD instance)
		{
			plugin = instance;
		}
    private static final Logger log = Logger.getLogger("Minecraft");

	
	@EventHandler
  public void onPlayerInteract(PlayerInteractEvent event)
  {
    Player player = event.getPlayer();
    Action action = event.getAction();
    try {
    	if (action.equals(Action.RIGHT_CLICK_AIR)) {
    		ItemStack iih = player.getItemInHand();
    		String name = iih.getType().name() + iih.getDurability();
    		String id = plugin.getConfig().getString(name.toLowerCase() + ".Region");
    		String cmd = plugin.getConfig().getString(name.toLowerCase() + ".Command");
    		World world = player.getWorld();
    		PluginManager pm = Bukkit.getServer().getPluginManager();
    		if (pm.getPlugin("WorldGuard") != null) {
    			this.worldGuard = ((WorldGuardPlugin)Bukkit.getServer().getPluginManager().getPlugin("WorldGuard"));
    		}
    		RegionManager regionManager = this.worldGuard.getRegionManager(world);
    		ProtectedRegion region = regionManager.getRegion(id);
    		if (regionManager.getRegion(id) != null) {
 /**
 * Get info about your Region! 
 * */
    		    if (region.getOwners().equals(player)) {
    		        player.performCommand(cmd);
    		        event.setCancelled(true);
    		        }
 /**
 * Set owner of the region!
 * */
    		    else {
    		        region.setOwners(new DefaultDomain());
    		        region.getOwners().addPlayer(this.worldGuard.wrapPlayer(player));
    		        //regionManager.removeRegion(id);
    		        try {
    		            regionManager.save();
    		            } catch (IOException e) {
    		                log.info("[FlydeViCe] WorldGuard Error [" + e + "] during Region File Save");
    		                }
 /**
 * Notify the player!
 * */
    		        player.sendMessage(ChatColor.YELLOW + player.getName() + " is tHe new owner of " + id + "!");
    		        event.setCancelled(true);
    		        }
    		    }
    		else {
    			event.setCancelled(false);
    		}
    	}
    	else {
    		event.setCancelled(false);
    	}
    } catch (NullPointerException e) {
    	return;
    }
  }
}