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
		
        //strings
		String typeRegi = "owner";
		String Lv = "deny";
		String Rv = "allow";
		String typeFlag = "flag";
		String itemname = "itemid";
		String region = "region-id";
		String command = "regions info " + region;
	    String flag = "flagtype";
	    
		//regions
		this.getConfig().set("Regions.itemid", itemname);
	    this.getConfig().set("Regions.itemid.Type", typeRegi);
		this.getConfig().set("Regions.itemid.Region", region);
		this.getConfig().set("Regions.itemid.Command", command);
		
		//flags
	    this.getConfig().set("Regions.itemid2", itemname);
	    this.getConfig().set("Regions.itemid2.Type", typeFlag);
	    this.getConfig().set("Regions.itemid2.Flag", flag);
	    this.getConfig().set("Regions.itemid2.Lclcvalue", Lv);
	    this.getConfig().set("Regions.itemid2.Rclcvalue", Rv);
		this.saveConfig();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this.playerListener, this);
	}

	public void onDisable() {
		log.info("[Flydevice] Disabled");
	}

}