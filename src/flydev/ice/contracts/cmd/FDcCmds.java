package flydev.ice.contracts.cmd;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import flydev.ice.contracts.FDc;

public class FDcCmds implements CommandExecutor {

    private FDc plugin;

    public FDcCmds(FDc plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String args[]) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            // FD base
            if (command.getName().equalsIgnoreCase("fdc"))
            {
                if (args.length == 0)
                {
                    return GetHelpCmd(player);
                }
                else
                {
                    // Reload all command
                    if (args[0].equalsIgnoreCase("reload"))
                    {
                        GetReloadCmd(sender, player, args);
                        return true;
                    }
                    // contract commands
                    else if (args[0].equalsIgnoreCase("add"))
                    {
                        if (args.length >= 2)
                        {
                            // owner commands
                            if (args[1].equalsIgnoreCase("own"))
                            {
                                return SetOwnerCmd(sender,player, args);
                            }
                            // flag commands
                            else if (args[1].equalsIgnoreCase("flag"))
                            {
                                return SetFlagCmd(sender,player, args);
                            }
                        }
                    }
                    return GetHelpCmd(player);
                }
            }
        }
        return false;
    }

    private void FDmsg (Player player, String msg) {
        player.sendMessage(ChatColor.DARK_RED + "[" + ChatColor.GOLD + "FlyDevice" + ChatColor.DARK_RED + "]: " + msg);
    }
    private void FDhelpMsg (Player player, String cmd, String does, String info) {
        player.sendMessage(ChatColor.DARK_RED + "[" + ChatColor.GOLD + "/fdc " + cmd + ChatColor.DARK_RED + "]:");
        player.sendMessage(ChatColor.DARK_RED + "[Does]: " + does);
        player.sendMessage(ChatColor.DARK_RED + "[Info]: " + info);
    }

    // Get info about the commands!  
    public boolean GetHelpCmd(Player player)
    {
        FDhelpMsg(player, ChatColor.DARK_PURPLE + "reload", ChatColor.DARK_PURPLE + "Reloads config", ChatColor.DARK_PURPLE + "Upgrade config when localy edited");
        FDhelpMsg(player, ChatColor.DARK_PURPLE + "add own <item> <region>", ChatColor.WHITE + "Creates a owner certificate(item) for a region", ChatColor.DARK_PURPLE + "<item> example: stick0, map5, etc.");
        FDhelpMsg(player, ChatColor.DARK_PURPLE + "add flag <item> <flag> <lv> <rv>", ChatColor.WHITE + "Creates a license(item) to set a flag", ChatColor.DARK_PURPLE + "lv/rv represents the two flag values");
        return true;
    }


    public boolean SetOwnerCmd(CommandSender sender, Player player, String[] args) {
        if (!player.isOp())
        {
            FDmsg(player, ChatColor.RED + "You are no allowed to use that command!");
            return true;
        }
        String item = args[2].toLowerCase();
        String region = args[3].toLowerCase();
        String fcmd = "regions info " + region;

        plugin.getConfig().set("Regions." + item, item);
        plugin.getConfig().set("Regions."+ item + ".Type", "owner");
        plugin.getConfig().set("Regions."+ item + ".Region", region);
        plugin.getConfig().set("Regions."+ item + ".Command", fcmd);
        plugin.saveConfig();
        FDmsg(player, ChatColor.GOLD + "Land certificate edited!");
        return true;
    }
    
    public boolean SetFlagCmd(CommandSender sender, Player player, String[] args) {
        if (!player.isOp()) {
            FDmsg(player, ChatColor.RED + "You are not allowed to use that command!");
            return true;
        }
        String item = args[2].toLowerCase();
        String flag = args[3].toLowerCase();
        String Lv = args[4].toLowerCase();
        String Rv = args[5].toLowerCase();

        plugin.getConfig().set("Regions."+ item, item);
        plugin.getConfig().set("Regions."+ item + ".Type", "flag");
        plugin.getConfig().set("Regions."+ item + ".Flag", flag);
        plugin.getConfig().set("Regions."+ item + ".Lclcvalue", Lv);
        plugin.getConfig().set("Regions."+ item + ".Rclcvalue", Rv);
        plugin.saveConfig();
        FDmsg(player, ChatColor.GOLD + "Flag license edited!");
        return true;
    }

    // Reload the plugin!
    public boolean GetReloadCmd(CommandSender sender, Player player, String args[])
    {
        if (!player.isOp())
        {
            FDmsg(player, ChatColor.RED + "You are not allowed to use that command!");
            return true;
        }

        plugin.reloadConfig();
        FDmsg(player, ChatColor.GREEN + "Reloaded!");

        return true;
    }
}