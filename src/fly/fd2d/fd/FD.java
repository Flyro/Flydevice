package fly.fd2d.fd;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fly.fd2d.fd.FDPL;

public class FD extends JavaPlugin {

	private static final Logger log = Logger.getLogger("Minecraft");
	
	final FDPL playerListener = new FDPL(this);
	
	public void onEnable() {		
		log.info("[Flydevice] Version " + this.getDescription().getVersion() + " has been enabled!");
        //regions
		String itemname = "itemid";
		String region = "region-id";
		String command = "regions info " + region;
		this.getConfig().set("Regions.itemid", itemname);
		this.getConfig().set("Regions.itemid.Region", region);
		this.getConfig().set("Regions.itemid.Command", command);
		//flags
	    String flag = "region flag " + region + " flagtype value";
	    this.getConfig().set("Flags.itemid", itemname);
	    this.getConfig().set("Flags.itemid.Region", region);
	    this.getConfig().set("Flags.itemid.Command", flag);
		this.saveConfig();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this.playerListener, this);
	}

	public void onDisable() {
		log.info("[Flydevice] Disabled");
	}

}