package ink.rainbowbridge.arathoth.Attributes;

import ink.rainbowbridge.arathoth.Arathoth;
import ink.rainbowbridge.arathoth.Utils.ItemUtils;
import ink.rainbowbridge.arathoth.Utils.SendUtils;
import org.apache.commons.lang.ObjectUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttributeManager {
    public static Plugin plugin = Bukkit.getPluginManager().getPlugin("Arathoth");
    public static void register(String Name,Plugin plugin,String Pattern,Integer Priority){
        if(AttributesData.RegisteredAttr != null && AttributesData.RegisteredAttr.containsKey(Name)) {
            AttributesData.RegisteredAttr.put(Name, plugin.getName());
            AttributesData.Patterns.put(Name,Pattern);
            AttributesData.Priority.put(Name,Priority);
            SendUtils.info("&fAttribute &8" + Name + "&fhas been existed,cover it......");
        }
        else{
            AttributesData.RegisteredAttr.put(Name, plugin.getName());
            AttributesData.Patterns.put(Name,Pattern);
            AttributesData.Priority.put(Name,Priority);
        }
        SendUtils.info("&fAttribute Registered: &8"+Name+" &fFrom: &8"+plugin.getName() + " &fPriority: &8"+Priority);
    }

    public static void DisableAttr(String Name){
        if(AttributesData.RegisteredAttr != null && !AttributesData.Disabled.contains(Name)){
            AttributesData.Disabled.add(Name);
            SendUtils.info("Attribute &8"+Name+" &7has been Disabled!");
        }
    }

    public static void EnableAttr(String Name){
            AttributesData.Disabled.remove(Name);
            SendUtils.info("Attribute &8"+Name+" &7has been Enabled!");
    }

    public static Double[] ParseNumber(String Attribute,List<String> uncoloredlores){
        Double[] value = {0D,0D,0D};
        if(AttributesData.RegisteredAttr.containsKey(Attribute)) {
            Pattern Pattern = java.util.regex.Pattern.compile(AttributesData.Patterns.get(Attribute).replace("[VALUE]","((\\-|\\+)?\\d+(\\.\\d+)?)"));
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (String str : uncoloredlores) {
                        Matcher m = Pattern.matcher(str);
                        if(m.find()){
                            value[1] = value[1] + Double.valueOf(m.group(1));
                        }
                    }
                }
            }.runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("Arathoth"));
        }
        return value;
    }

    public static void StatusUpdate(LivingEntity e){
        if(!(e instanceof Player)){
            new BukkitRunnable(){
                @Override
                public void run() {
                    long time = System.currentTimeMillis();
                    HashMap<String,Double[]> data = new HashMap<>();
                    List<ItemStack> items = new ArrayList<>();
                    List<String> uncoloredlores = new ArrayList<>();
                    items.add(e.getEquipment().getBoots());
                    items.add(e.getEquipment().getLeggings());
                    items.add(e.getEquipment().getChestplate());
                    items.add(e.getEquipment().getHelmet());
                    items.add(e.getEquipment().getItemInMainHand());
                    items.add(e.getEquipment().getItemInOffHand());
                    for(ItemStack item : items){
                        if(ItemUtils.hasLore(item)){
                            List<String> coloredlores = item.getItemMeta().getLore();
                            for(String lore : coloredlores){
                               uncoloredlores.add(ChatColor.stripColor(lore));
                            }
                        }
                        for(String attr : AttributesData.RegisteredAttr.keySet()){
                            data.put(attr,ParseNumber(attr,uncoloredlores));
                        }
                        AttributesData.AttrData.put(e.getUniqueId().toString(),data);
                    }
                    Arathoth.Debug("Status Update: &f"+(System.currentTimeMillis() - time)+"ms &8("+e.getType()+")");
                }
            }.runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("Arathoth"));

        }
        else{
            new BukkitRunnable(){

                @Override
                public void run() {
                    long time = System.currentTimeMillis();
                    HashMap<String,Double[]> data = new HashMap<>();
                    List<ItemStack> items = new ArrayList<>();
                    Player p = (Player)e;
                    List<String> uncoloredlores = new ArrayList<>();
                    HashMap<Integer,String> Slots = SlotsManager.getSlots();
                    for(Integer i : Slots.keySet()){
                        if(p.getInventory().getItem(i)!= null && p.getInventory().getItem(i).getItemMeta().hasLore() && ChatColor.stripColor(p.getInventory().getItem(i).getItemMeta().getLore().get(1)).contains(Slots.get(i))){
                            items.add(p.getInventory().getItem(i));
                        }
                    }
                    if(p.getInventory().getItemInMainHand() != null && p.getInventory().getItemInMainHand().getItemMeta().hasLore() && ChatColor.stripColor(p.getInventory().getItemInMainHand().getItemMeta().getLore().get(1)).contains(plugin.getConfig().getString("Slots.MainHand"))){
                        items.add(p.getInventory().getItemInMainHand());
                    }
                    for(ItemStack item : items){
                        if(ItemUtils.hasLore(item)){
                            List<String> coloredlores = item.getItemMeta().getLore();
                            for(String lore : coloredlores){
                                uncoloredlores.add(ChatColor.stripColor(lore));
                            }
                        }
                        for(String attr : AttributesData.RegisteredAttr.keySet()){
                            data.put(attr,ParseNumber(attr,uncoloredlores));
                        }
                        AttributesData.AttrData.put(e.getUniqueId().toString(),data);
                    }
                    Arathoth.Debug("Status Update: &f"+(System.currentTimeMillis() - time)+"ms &8("+p.getName()+")");
                }
            }.runTaskAsynchronously(Bukkit.getPluginManager().getPlugin("Arathoth"));

        }
    }
}