package fly.fd2d.fd;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import fly.fd2d.fd.FDPL;

public class FD extends JavaPlugin {	


	private static final Logger log = Logger.getLogger("Minecraft");
	
	final FDPL playerListener = new FDPL(this);

	

//	public String reotype;
	public void onEnable() {		
		log.info("[Flydevice] Version " + this.getDescription().getVersion() + " has been enabled!");
		String itemname = "item-id";
		String regioon = "region-id";
		String command = "regions info" + regioon;
		this.getConfig().set("item-id", itemname);
		this.getConfig().set("item-id.Region", regioon);
		this.getConfig().set("item-id.Command", command);

//		reotype = sgetConfig().getString("map2.Type", "Poly");
		this.saveConfig();
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvents(this.playerListener, this);
	}
	
	public void onDisable() {
		log.info("[Flydevice] Disabled");
	}
}