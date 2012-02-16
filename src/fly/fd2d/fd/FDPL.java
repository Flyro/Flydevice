package fly.fd2d.fd;

import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;

import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.LocalPlayer;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.domains.DefaultDomain;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

public class FDPL implements Listener {
    private final FD plugin;
    private WorldGuardPlugin worldGuard;
    public FDPL(FD instance) {
        plugin = instance;
    }
    private static final Logger log = Logger.getLogger("Minecraft");

    private static Vector toVector(Location loc) {
        return new Vector(loc.getX(), loc.getY(), loc.getZ());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        try {
            if (action.equals(Action.RIGHT_CLICK_AIR)) {
                ItemStack iih = player.getItemInHand();
                String name = iih.getType().name();
                short idur = iih.getDurability();
                String type = plugin.getConfig().getString("Regions." + name.toLowerCase() + idur + ".Type");
                String Rclc = plugin.getConfig().getString("Regions." + name.toLowerCase() + idur + ".Rclcvalue");
                if(type.equalsIgnoreCase("owner")){
                    regOwn(player, event, name, idur);
                } 
                else if(type.equalsIgnoreCase("flag")){
                    regFlag(player, event, name, idur, Rclc);
                }
            } else if (action.equals(Action.LEFT_CLICK_AIR)) {
                ItemStack iih = player.getItemInHand();
                String name = iih.getType().name();
                short idur = iih.getDurability();
                String type = plugin.getConfig().getString("Regions." + name.toLowerCase() + idur + ".Type");
                String Lclc = plugin.getConfig().getString("Regions." + name.toLowerCase() + idur + ".Lclcvalue");
                if(type.equalsIgnoreCase("flag")){
                    regFlag(player, event, name, idur, Lclc);
                }
            } else {
                event.setCancelled(false);
            }
        } catch (NullPointerException e) {
            return;
        }
    }

    // Change The Regions Owner!
    private void regOwn(Player player, PlayerInteractEvent event, String name, short idur) {
        String rrid = plugin.getConfig().getString("Regions." + name.toLowerCase() + idur + ".Region");
        String rcmd = plugin.getConfig().getString("Regions." + name.toLowerCase() + idur + ".Command");
        World world = player.getWorld();
        PluginManager pm = Bukkit.getServer().getPluginManager();

        if (pm.getPlugin("WorldGuard") != null) {
            this.worldGuard = ((WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard"));
        }
        RegionManager regionManager = this.worldGuard.getRegionManager(world);
        ProtectedRegion region = regionManager.getRegion(rrid);
        LocalPlayer localPlayer = this.worldGuard.wrapPlayer(player);

        if (regionManager.getRegion(rrid) != null) {
            if (region.isOwner(localPlayer)) {
                if (!player.isOp()) {
                    player.setOp(true);
                    player.performCommand(rcmd);
                    player.setOp(false);
                } else {
                    player.performCommand(rcmd);
                }
                event.setCancelled(true);
            } else {
                region.setOwners(new DefaultDomain());
                region.getOwners().addPlayer(
                        this.worldGuard.wrapPlayer(player));
                try {
                    regionManager.save();
                } catch (Exception e) {
                    log.info("[FlydeViCe] WorldGuard Error [" + e + "] during Region File Save");
                }
                String stext = player.getName() + " is the new owner of " + rrid + "!";
                plugin.getServer().broadcastMessage(ChatColor.DARK_RED + "[" + ChatColor.GOLD + rrid + ChatColor.DARK_RED + "]: " + ChatColor.GOLD + stext);
                event.setCancelled(true);
            }
        } else {
            event.setCancelled(false);
        }
    }

    //Change The Regions Flag!
    private void regFlag(Player player, PlayerInteractEvent event, String name, short idur, String flagFv) {
        String fcmd = plugin.getConfig().getString("Regions." + name.toLowerCase() + idur + ".Flag");
        World world = player.getWorld();
        PluginManager pm = Bukkit.getServer().getPluginManager();
        if (pm.getPlugin("WorldGuard") != null) {
            this.worldGuard = ((WorldGuardPlugin) Bukkit.getServer().getPluginManager().getPlugin("WorldGuard"));
        }
        RegionManager regionManager = worldGuard.getRegionManager(world);

        Location location = player.getLocation();
        Vector pt = toVector(location);
        String regId = regionManager.getApplicableRegionsIDs(pt).toString();
        regId = regId.substring(1,regId.length()-1);

        LocalPlayer localPlayer = worldGuard.wrapPlayer(player);

        if (regionManager.getRegion(regId) != null) {
            ProtectedRegion region = regionManager.getRegion(regId);
            if (region.isOwner(localPlayer)) {
                //region flag <id> <flag> [<value>]
                String doCommand = "regions flag " + regId + " " + fcmd + " " + flagFv;
                // Allow command to all players!
                if (!player.isOp()) {
                    player.setOp(true);
                    player.performCommand(doCommand);
                    player.setOp(false);
                } else {
                    player.performCommand(doCommand);
                }
                player.sendMessage (ChatColor.DARK_RED + "[" + ChatColor.GOLD + fcmd + ChatColor.DARK_RED + "] value: " + ChatColor.GOLD + flagFv);
                event.setCancelled(true);
            } else {
                player.sendMessage (ChatColor.DARK_RED + "[" + ChatColor.GOLD + "Fail" + ChatColor.DARK_RED + "]: " + regId + " does not belong to you!");
                event.setCancelled(true);
            }
        } else {
            player.sendMessage (ChatColor.DARK_RED + "[" + ChatColor.GOLD + "Fail" + ChatColor.DARK_RED + "]: You must be inside a region!");
            event.setCancelled(true);
        }
    }
}