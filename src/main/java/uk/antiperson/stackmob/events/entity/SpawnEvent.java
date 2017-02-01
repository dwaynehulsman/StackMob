package uk.antiperson.stackmob.events.entity;

import net.elseland.xikage.MythicMobs.Mobs.ActiveMob;
import org.bukkit.Bukkit;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import uk.antiperson.stackmob.Configuration;
import uk.antiperson.stackmob.utils.EntityUtils;
import uk.antiperson.stackmob.api.events.EntityStackEvent;
import uk.antiperson.stackmob.api.MergeType;
import uk.antiperson.stackmob.plugins.MythicMobs;
import uk.antiperson.stackmob.StackMob;
import uk.antiperson.stackmob.utils.States;
import uk.antiperson.stackmob.plugins.WorldGuard;

import java.util.List;

/**
 * Created by nathat on 02/10/16.
 */
public class SpawnEvent implements Listener {

    private Configuration config;
    private StackMob st;
    private MythicMobs mm;
    public SpawnEvent(StackMob st) {
        config = st.config;
        this.st = st;
        if (Bukkit.getServer().getPluginManager().getPlugin("MythicMobs") != null) {
            mm = new MythicMobs(st);
        }
    }

    @EventHandler
    public void onEntitySpawn(final CreatureSpawnEvent e) {
        new BukkitRunnable(){
            @Override
            public void run(){
                Entity ea = e.getEntity();
                if(st.noStack.contains(ea.getUniqueId())){
                    st.noStack.remove(ea.getUniqueId());
                    return;
                }
                if(st.isLegacy()) {
                    if (ea.getType() == EntityType.ARMOR_STAND) {
                        return;
                    }
                }
                if(ea.getType() == EntityType.UNKNOWN){
                    return;
                }
                CreatureSpawnEvent.SpawnReason sr = e.getSpawnReason();
                double x = config.getFilecon().getDouble("creature.radius.x");
                double y = config.getFilecon().getDouble("creature.radius.y");
                double z = config.getFilecon().getDouble("creature.radius.z");
                int maxStack = config.getFilecon().getInt("creature.stack.max");
                boolean blacklistEnabled = config.getFilecon().getBoolean("creature.blacklist.enabled");
                boolean whitelistEnabled = config.getFilecon().getBoolean("creature.whitelist.enabled");
                boolean wgEnabled = config.getFilecon().getBoolean("worldguard.enabled");
                boolean wgBlackEnabled = config.getFilecon().getBoolean("worldguard.blacklist.enabled");
                boolean worldsEnabled = config.getFilecon().getBoolean("creature.worlds.enabled");
                boolean spawnReasonsEn = config.getFilecon().getBoolean("creature.spawnreasons.enabled");
                List blacklist = config.getFilecon().getList("creature.blacklist.list");
                List whitelist = config.getFilecon().getList("creature.whitelist.list");
                List<String> worlds = config.getFilecon().getStringList("creature.worlds.list");
                List<String> spawnReasons = config.getFilecon().getStringList("creature.spawnreasons.list");
                if (blacklistEnabled) {
                    if(blacklist.contains(ea.getType().toString())){
                        return;
                    }
                }
                if (whitelistEnabled) {
                    if(!whitelist.contains(ea.getType().toString())){
                        return;
                    }
                }
                if(spawnReasonsEn){
                    if(!spawnReasons.contains(sr.toString())){
                        return;
                    }
                }
                if(worldsEnabled){
                    if(!worlds.contains(ea.getWorld().getName())){
                        return;
                    }
                }

                Entity abc = null;
                States sta = States.ONE;
                if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
                    WorldGuard wg = new WorldGuard(st);
                    if(wg.checkEntitiyInRegion(ea)){
                        return;
                    }
                }
                for (Entity i : ea.getNearbyEntities(x, y, z)) {
                    if (i.getType() == ea.getType()) {
                        if (st.amountMap.containsKey(i.getUniqueId()) && i instanceof LivingEntity) {
                            sta = States.TWO;
                            if (Bukkit.getPluginManager().getPlugin("WorldGuard") != null) {
                                WorldGuard wg = new WorldGuard(st);
                                if(wg.checkEntitesInRegion(i, ea)){
                                    continue;
                                }else{
                                    sta = States.ONE;
                                }
                            }
                            if (checks(i, ea)) {
                                continue;
                            }
                            if(config.getFilecon().getInt("creature.special." + i.getType().toString().toLowerCase() + ".maxstack")  != 0){
                                maxStack = config.getFilecon().getInt("creature.special." + i.getType().toString().toLowerCase() + ".maxstack");
                            }
                            if (st.amountMap.get(i.getUniqueId()) < maxStack || maxStack == -1) {
                                abc = i;
                                break;
                            }
                        }
                    }
                }

                if (abc != null) {
                    ok(abc, ea);
                } else {
                    if (wgEnabled || wgBlackEnabled) {
                        if (sta == States.ONE) {
                            ok(null, ea);
                        }
                    } else {
                        ok(null, ea);
                    }
                }
            }
        }.runTaskLater(st, config.getFilecon().getInt("creature.stack.delay"));
    }


    private boolean checks(Entity i, Entity ea) {
        boolean tamedCheck = config.getFilecon().getBoolean("creature.tamed.check");
        boolean leashCheck = config.getFilecon().getBoolean("creature.leashed.check");
        boolean sheepSameColor = config.getFilecon().getBoolean("creature.sheep.stacksamecolor");
        boolean slimeSameSize = config.getFilecon().getBoolean("creature.slime.stacksamesize");
        boolean sheepSheared = config.getFilecon().getBoolean("creature.sheep.stacksheared");
        boolean separateAges = config.getFilecon().getBoolean("creature.separateages");
        boolean horseSameType = config.getFilecon().getBoolean("creature.horses.stacksametype");
        boolean horseSameColor = config.getFilecon().getBoolean("creature.horses.stacksamecolor");
        boolean villagerStackSameProfession = config.getFilecon().getBoolean("creature.villager.stacksameprofession");
        boolean zombieIsVillager = config.getFilecon().getBoolean("creature.zombie.stacksamevillager");
        boolean skeletonType = config.getFilecon().getBoolean("creature.skeleton.stacksametype");
        boolean canBreed = config.getFilecon().getBoolean("creature.breeding");
        boolean lamaSame = config.getFilecon().getBoolean("creature.stacksamellama");
        if (tamedCheck) {
            if (i instanceof Tameable) {
                Tameable t = (Tameable) i;
                if (t.isTamed()) {
                    return true;
                }
            }
        }
        if (leashCheck) {
            LivingEntity le = (LivingEntity) i;
            if (le.isLeashed()) {
                return true;
            }
        }
        if (sheepSameColor && i instanceof Sheep && ea instanceof Sheep) {
            if (!(((Sheep) i).getColor() == ((Sheep) ea).getColor())) {
                return true;
            }
        }
        if (separateAges && (ea instanceof Ageable || ea instanceof Zombie)) {
            if(ea instanceof Zombie){
                if(((Zombie)ea).isBaby() != ((Zombie) i).isBaby()){
                    return true;
                }
            }else{
                Ageable aea = (Ageable) ea;
                Ageable ie = (Ageable) i;
                if (aea.isAdult() != ie.isAdult()) {
                    return true;
                }
            }
        }
        if (sheepSheared && ea instanceof Sheep && i instanceof Sheep) {
            if ((((Sheep) ea).isSheared() != ((Sheep) i).isSheared())) {
                return true;
            }
        }
        if (slimeSameSize && ea instanceof Slime) {
            if (((Slime) ea).getSize() != ((Slime) i).getSize()) {
                return true;
            }
        }
        if(horseSameColor && ea instanceof Horse && i instanceof Horse){
            if(((Horse)ea).getColor() != ((Horse)i).getColor()){
                return true;
            }
        }
        if(!Bukkit.getVersion().contains("1.11")){
            if(zombieIsVillager && ea instanceof Zombie && i instanceof Zombie){
                if(!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.7")) {
                    if (((Zombie) ea).getVillagerProfession() != ((Zombie) i).getVillagerProfession()) {
                        return true;
                    }
                }else{
                    if(((Zombie)ea).isVillager() != ((Zombie) i).isVillager()){
                        return true;
                    }
                }
            }
            if(skeletonType && ea instanceof Skeleton){
                if(((Skeleton)ea).getSkeletonType() != ((Skeleton)i).getSkeletonType()){
                    return true;
                }
            }
        }else{
            if(horseSameType && ea instanceof AbstractHorse && i instanceof AbstractHorse){
                if(((AbstractHorse)ea).getDomestication() != ((AbstractHorse)i).getDomestication()){
                    return true;
                }
            }
            if(lamaSame && ea instanceof Llama){
                if(((Llama)ea).getColor() != ((Llama) i).getColor()){
                    return true;
                }
            }
        }
        if(villagerStackSameProfession && ea instanceof Villager && i instanceof Villager){
            if(((Villager)ea).getProfession() != ((Villager)i).getProfession()){
                return true;
            }
        }
        if(canBreed && ea instanceof Animals){
            if(((Animals)ea).canBreed() != ((Animals)i).canBreed()){
                return true;
            }
        }
        if(Bukkit.getServer().getPluginManager().getPlugin("MythicMobs") != null){
            if(config.getFilecon().getBoolean("mythicmobs.enabled")) {
                if (mm.getMythicMobs().isMythicMob(i) && mm.getMythicMobs().isMythicMob(ea)) {
                    ActiveMob am = mm.getMythicMobs().getMythicMobInstance(ea);
                    ActiveMob am2 = mm.getMythicMobs().getMythicMobInstance(i);
                    if (am.getType() != am2.getType()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    private void ok(Entity p, Entity ea) {
        if (p != null) {
            st.amountMap.put(ea.getUniqueId(), 1);
            EntityStackEvent ese = new EntityStackEvent(p, ea, st, MergeType.SPAWN_NEAR);
            Bukkit.getServer().getPluginManager().callEvent(ese);
            if(!ese.isCancelled()){
                ea.remove();
                st.amountMap.remove(ea.getUniqueId());
                st.amountMap.put(p.getUniqueId(), (st.amountMap.get(p.getUniqueId()) + 1));
            }
        } else {
            EntityStackEvent ese = new EntityStackEvent(null, ea, st, MergeType.NEW_STACK);
            Bukkit.getServer().getPluginManager().callEvent(ese);
            if(ese.isCancelled()){
                return;
            }
            st.amountMap.put(ea.getUniqueId(), 1);
            if(!Bukkit.getVersion().contains("1.8") && !Bukkit.getVersion().contains("1.7")) {
                ((LivingEntity) ea).setAI(!config.getFilecon().getBoolean("creature.disablemobai"));
            }else if((Bukkit.getVersion().contains("1.8.8")  || Bukkit.getVersion().contains("1.8.9")) && config.getFilecon().getBoolean("creature.disablemobai")){
                new EntityUtils(st).setAI(ea);
            }
            if(config.getFilecon().getBoolean("mcmmo.disable-xp") && st.isLegacy()){
                if(config.getFilecon().getBoolean("mcmmo.use-whitelist")){
                    if(!config.getFilecon().getStringList("mcmmo.whitelist").contains(ea.getType().toString())){
                        return;
                    }
                }
                ea.setMetadata("mcMMO: Spawned Entity", new FixedMetadataValue(st.getServer().getPluginManager().getPlugin("mcMMO"), false));
            }
        }
    }

}
