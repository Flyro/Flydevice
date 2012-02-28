package flydev.ice.contracts;

import java.util.logging.Logger;

import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import flydev.ice.contracts.cmd.FDcCmds;

public class FDc extends JavaPlugin {

    private static final Logger log = Logger.getLogger("Minecraft");
    final FDcPL playerListener = new FDcPL(this);

    private FDcCmds FDExecutor;

    @Override
    public void onEnable() {		
        log.info("[Flydevice] Version " + this.getDescription().getVersion() + " has been enabled!");

        this.getConfig().options().copyDefaults(true);
        saveConfig();

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(this.playerListener, this);

        FDExecutor = new FDcCmds(this);
        getCommand("fdc").setExecutor(FDExecutor);
    }

    public void onDisable() {
        log.info("[Flydevice] Disabled");
    }
}