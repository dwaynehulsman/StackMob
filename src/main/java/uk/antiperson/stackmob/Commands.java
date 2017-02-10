package uk.antiperson.stackmob;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import uk.antiperson.stackmob.utils.EntityUtils;
import uk.antiperson.stackmob.utils.Updater;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by nathat on 02/10/16.
 */
public class Commands implements CommandExecutor{


    private StackMob m;
    private String pluginTag = ChatColor.DARK_PURPLE + "[" + ChatColor.AQUA + "StackMob" + ChatColor.DARK_PURPLE + "] ";
    private String div = ChatColor.GRAY + " - ";
    private String beforeBig = ChatColor.GRAY + "------" + pluginTag.replace(" ", "") + ChatColor.GRAY + "------------------------------";
    public Commands(StackMob st){
        m = st;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String string, String[] args){
        if(sender instanceof Player){
            Player p = (Player) sender;
            if(args.length == 0){
                if(p.hasPermission("stackmob.listcommands") || p.hasPermission("stackmob.*")){
                    p.sendMessage(beforeBig);
                    p.sendMessage(ChatColor.YELLOW + "/sm removeall" + div + ChatColor.GOLD + "Removes all stacked entities in loaded chunks.");
                    p.sendMessage(ChatColor.YELLOW + "/sm remove [x] [y] [z]" + div + ChatColor.GOLD + "Removes all stacked entities in a radius around you.");
                    p.sendMessage(ChatColor.YELLOW + "/sm unstack [x] [y] [z]" + div + ChatColor.GOLD + "Unstacks all stacked entities in a radius around you.");
                    p.sendMessage(ChatColor.YELLOW + "/sm stats" + div + ChatColor.GOLD + "Shows details about all the currently stacked mobs.");
                    p.sendMessage(ChatColor.YELLOW + "/sm wand" + div + ChatColor.GOLD + "Gives you the wand of stacked mobs.");
                    p.sendMessage(ChatColor.YELLOW + "Page 1/2 of command help. Type /sm 2 for next page.");
                }else{
                    p.sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "You do not have the permission to do this!");
                }
            }else if(args.length == 1){
                if(args[0].equalsIgnoreCase("2")){
                    if(p.hasPermission("stackmob.listcommands") || p.hasPermission("stackmob.*")) {
                        p.sendMessage(beforeBig);
                        p.sendMessage(ChatColor.YELLOW + "/sm reset" + div + ChatColor.GOLD + "Resets the config file to default values.");
                        p.sendMessage(ChatColor.YELLOW + "/sm reload" + div + ChatColor.GOLD + "Reloads configuration values into memory.");
                        p.sendMessage(ChatColor.YELLOW + "/sm update" + div + ChatColor.GOLD + "Checks SpigotMC for plugin updates.");
                        p.sendMessage(ChatColor.YELLOW + "/sm download" + div + ChatColor.GOLD + "Downloads the latest version.");
                        p.sendMessage(ChatColor.YELLOW + "/sm about" + div + ChatColor.GOLD + "Shows information about this plugin.");
                        p.sendMessage(ChatColor.YELLOW + "Page 2/2 of command help.");
                    }else{
                        p.sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "You do not have the permission to do this!");
                    }
                }else if(args[0].equalsIgnoreCase("about")){
                    if(p.hasPermission("stackmob.admin") || p.hasPermission("stackmob.*")){
                        p.sendMessage(beforeBig);
                        p.sendMessage(ChatColor.YELLOW + "StackMob v" + m.getDescription().getVersion() + " by antiPerson");
                        p.sendMessage(ChatColor.YELLOW + "Find out more at " + m.getDescription().getWebsite());
                        p.sendMessage(ChatColor.YELLOW + "Has this plugin benefited your server? Please leave a review - it helps a lot.");
                    }else{
                        p.sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "You do not have the permission to do this!");
                    }
                }else if(args[0].equalsIgnoreCase("update")){
                    if(p.hasPermission("stackmob.admin") || p.hasPermission("stackmob.*")){
                        Updater up = new Updater(m);
                        up.checkUpdate(true, p);
                    }else{
                        p.sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "You do not have the permission to do this!");
                    }
                }else if(args[0].equalsIgnoreCase("removeall")){
                    if(p.hasPermission("stackmob.admin") || p.hasPermission("stackmob.*")){
                        removeAll(p);
                    }else{
                        p.sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "You do not have the permission to do this!");
                    }
                }else if(args[0].equalsIgnoreCase("reset")){
                    if(p.hasPermission("stackmob.admin") || p.hasPermission("stackmob.*")){
                        Configuration config = m.config;
                        config.getFile().delete();
                        config.makeConfig();
                        m.config.reloadConfig();
                        p.sendMessage(pluginTag + ChatColor.GREEN + "The configuration file has been reset successfully!");
                    }else{
                        p.sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "You do not have the permission to do this!");
                    }
                }else if(args[0].equalsIgnoreCase("stats")){
                    if(p.hasPermission("stackmob.admin") || p.hasPermission("stackmob.*")){
                        displayStats(p);
                    }else{
                        p.sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "You do not have the permission to do this!");
                    }
                }else if(args[0].equalsIgnoreCase("remove")){
                    if(p.hasPermission("stackmob.admin") || p.hasPermission("stackmob.*")){
                        p.sendMessage(pluginTag + ChatColor.YELLOW + "Usage: /sm remove [x] [y] [z]");
                    }else{
                        p.sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "You do not have the permission to do this!");
                    }
                }else if(args[0].equalsIgnoreCase("unstack")){
                    if(p.hasPermission("stackmob.admin") || p.hasPermission("stackmob.*")){
                        p.sendMessage(pluginTag + ChatColor.YELLOW + "Usage: /sm unstack [x] [y] [z]");
                    }else{
                        p.sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "You do not have the permission to do this!");
                    }
                }else if(args[0].equalsIgnoreCase("reload")){
                    if(p.hasPermission("stackmob.admin") || p.hasPermission("stackmob.*")){
                        m.config.reloadConfig();
                        p.sendMessage(pluginTag + ChatColor.GREEN + "The configuration has been reloaded successfully!");
                    }else{
                        p.sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "You do not have the permission to do this!");
                    }
                }else if(args[0].equalsIgnoreCase("wand")){
                    if(p.hasPermission("stackmob.admin") || p.hasPermission("stackmob.*")){
                        ItemStack is = new ItemStack(Material.STICK, 1);
                        ItemMeta im = is.getItemMeta();
                        im.setDisplayName(ChatColor.AQUA + "The wand of stacked monsters.");
                        im.setLore(Arrays.asList(ChatColor.YELLOW + "The wand that can do anything to a stacked monster.", " ", ChatColor.YELLOW + "Right click on any entity to view information."));
                        is.setItemMeta(im);
                        p.getInventory().addItem(is);
                        p.sendMessage(pluginTag + ChatColor.GREEN + "The wand has been added to your inventory.");
                    }else{
                        p.sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "You do not have the permission to do this!");
                    }
                }else if(args[0].equalsIgnoreCase("download")){
                    if(p.hasPermission("stackmob.admin") || p.hasPermission("stackmob.*")){
                        Updater up = new Updater(m);
                        p.sendMessage(pluginTag + ChatColor.GREEN + "Downloading latest update from spigotmc.org...");
                        try{
                            up.downloadUpdate();
                            p.sendMessage(pluginTag + ChatColor.GREEN + "Downloaded the latest file! Make sure to restart your sever for this to take effect!");
                        }catch (Exception e){
                            p.sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "Something went wrong while trying to do that, check console for more details.");
                            e.printStackTrace();
                        }

                    }else{
                        p.sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "You do not have the permission to do this!");
                    }
                }else {
                    p.sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "Invaild arguments!");
                }
                }else if(args.length == 2){
                if(args[0].equalsIgnoreCase("remove")  && (p.hasPermission("stackmob.admin") || p.hasPermission("stackmob.*"))){
                    p.sendMessage(pluginTag + ChatColor.YELLOW + "Usage: /sm remove [x] [y] [z]");
                }else if(args[0].equalsIgnoreCase("unstack")  && (p.hasPermission("stackmob.admin") || p.hasPermission("stackmob.*"))){
                    p.sendMessage(pluginTag + ChatColor.YELLOW + "Usage: /sm unstack [x] [y] [z]");
                }else{
                    p.sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "Invaild arguments!");
                }
            }else if(args.length == 3){
                if(args[0].equalsIgnoreCase("remove") && (p.hasPermission("stackmob.admin") || p.hasPermission("stackmob.*"))){
                    p.sendMessage(pluginTag + ChatColor.YELLOW + "Usage: /sm remove [x] [y] [z]");
                }else if(args[0].equalsIgnoreCase("unstack")  && (p.hasPermission("stackmob.admin") || p.hasPermission("stackmob.*"))){
                    p.sendMessage(pluginTag + ChatColor.YELLOW + "Usage: /sm unstack [x] [y] [z]");
                }else{
                    p.sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "Invaild arguments!");
                }
            }else if(args.length == 4){
                if(args[0].equalsIgnoreCase("remove") && (p.hasPermission("stackmob.admin") || p.hasPermission("stackmob.*"))){
                    if(checkDouble(args[1]) && checkDouble(args[2]) && checkDouble(args[3])){
                        removeAll(p, Double.valueOf(args[1]), Double.valueOf(args[2]), Double.valueOf(args[3]));
                    }else{
                        p.sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "The values you entered are not doubles!");
                    }
                }else if(args[0].equalsIgnoreCase("unstack") && (p.hasPermission("stackmob.admin") || p.hasPermission("stackmob.*"))){
                    if(checkDouble(args[1]) && checkDouble(args[2]) && checkDouble(args[3])){
                        unStack(p, Double.valueOf(args[1]), Double.valueOf(args[2]), Double.valueOf(args[3]));
                    }else{
                        p.sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "The values you entered are not doubles!");
                    }
                }else{
                    p.sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "Invaild arguments!");
                }

            }else{
                p.sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "Invaild arguments!");
            }

        }else{
            sender.sendMessage(pluginTag + ChatColor.RED + ChatColor.BOLD + "Error: " + ChatColor.RESET + ChatColor.RED + "Not a player!");
        }
        return false;
    }

    private void removeAll(Player p){
        int counter = 0;
        int counterAll = 0;
        for(World world : Bukkit.getWorlds()){
            for(LivingEntity le : world.getLivingEntities()){
                if(m.amountMap.containsKey(le.getUniqueId())){
                    le.remove();
                    counter++;
                    counterAll = counterAll + m.amountMap.get(le.getUniqueId());
                    m.amountMap.remove(le.getUniqueId());
                }
            }
        }
        p.sendMessage(pluginTag + ChatColor.GREEN + "Removed " + counter + " (" + counterAll + " stacked) entities!");
    }

    private void removeAll(Player p, double x, double y, double z){
        int counter = 0;
        int counterAll = 0;
        for(Entity le : p.getNearbyEntities(x, y, z)){
            if(m.amountMap.containsKey(le.getUniqueId())){
                le.remove();
                counter++;
                counterAll = counterAll + m.amountMap.get(le.getUniqueId());
                m.amountMap.remove(le.getUniqueId());
            }
        }
        p.sendMessage(pluginTag + ChatColor.GREEN + "Removed " + counter + " (" + counterAll + " stacked) entities!");
    }

    public void displayStats(Player p){
        int count1 = 0;
        int countMegered1 = 0;
        HashMap<EntityType, Integer> mobAmounts = new HashMap<EntityType, Integer>();
        for(World world : Bukkit.getWorlds()){
            for(LivingEntity le : world.getLivingEntities()){
                if(m.amountMap.containsKey(le.getUniqueId())){
                    count1++;
                    countMegered1 = countMegered1 + m.amountMap.get(le.getUniqueId());
                    if(mobAmounts.containsKey(le.getType())){
                        mobAmounts.put(le.getType(), (mobAmounts.get(le.getType()) + m.amountMap.get(le.getUniqueId())));
                    }else{
                        mobAmounts.put(le.getType(), m.amountMap.get(le.getUniqueId()));
                    }
                }
            }
        }
        int countChunk = 0;
        int countChunkMergered =  0;
        for(Entity e : p.getLocation().getChunk().getEntities()){
            if(e instanceof LivingEntity){
                if(m.amountMap.containsKey(e.getUniqueId())){
                    countChunk++;
                    countChunkMergered = countChunkMergered + m.amountMap.get(e.getUniqueId());
                }
            }
        }
        int countTotal = 0;
        for(UUID uuid : m.amountMap.keySet()){
            countTotal = countTotal + m.amountMap.get(uuid);
        }
        p.sendMessage(beforeBig);
        p.sendMessage(ChatColor.YELLOW + "There are " + count1 + " ("+ countMegered1 +  " merged) stack entities loaded and in this chunk there are " +
                countChunk + " (" + countChunkMergered + " merged) stack entities. There is a total of " + m.amountMap.size() + " (" + countTotal + " merged) stack entities somewhere on this server.");
        String str = "";
        for(EntityType et : mobAmounts.keySet()){
            str = str + mobAmounts.get(et) + "x " + et.toString() + ", ";
        }
        p.sendMessage(ChatColor.BLUE + "Stack amount breakdown by entity type (loaded entities):");
        p.sendMessage(ChatColor.GOLD + str);
    }

    public void unStack(Player p, double x, double y, double z){
        int counta = 0;
        int countb = 1;
        EntityUtils eu = new EntityUtils(m);
        for(Entity e : p.getNearbyEntities(x,y,z)){
            if(e instanceof LivingEntity){
                if(m.amountMap.containsKey(e.getUniqueId())){
                    counta++;
                    for(int i = 1; i < m.amountMap.get(e.getUniqueId()); i++){
                        countb++;
                        Entity eas = eu.createEntity(e, false, true);
                        m.noStack.add(eas.getUniqueId());
                    }
                    m.amountMap.remove(e.getUniqueId());
                    e.setCustomName(null);
                    e.setCustomNameVisible(false);
                }
            }
        }
        p.sendMessage(pluginTag + ChatColor.GREEN + "Divided " + counta + " stacked entities into " + countb + " unstacked entities");
    }


    private boolean checkDouble(String input){
        try{
            return Double.valueOf(input) != null;
        }catch(NumberFormatException e){
            return false;
        }
    }
}
