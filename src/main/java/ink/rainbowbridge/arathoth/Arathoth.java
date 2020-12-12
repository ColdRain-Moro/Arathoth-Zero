package ink.rainbowbridge.arathoth;

import ink.rainbowbridge.arathoth.Attributes.AttributeLoader;
import ink.rainbowbridge.arathoth.Attributes.SubAttributes.*;
import ink.rainbowbridge.arathoth.Commands.MainCommand;
import ink.rainbowbridge.arathoth.Listener.AttributeListener;
import ink.rainbowbridge.arathoth.Listener.StatusUpdateListeners;
import ink.rainbowbridge.arathoth.Rules.RulesManager;
import ink.rainbowbridge.arathoth.Rules.sub.LevelRequired;
import ink.rainbowbridge.arathoth.Rules.sub.OwnderRequest;
import ink.rainbowbridge.arathoth.Rules.sub.PAPIRequest;
import ink.rainbowbridge.arathoth.Rules.sub.PermRequest;
import ink.rainbowbridge.arathoth.Utils.DrawFucker;
import ink.rainbowbridge.arathoth.Utils.SendUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.text.DecimalFormat;
import java.util.Random;


public final class Arathoth extends JavaPlugin {
   FileConfiguration config = YamlConfiguration.loadConfiguration(new File(getDataFolder(),"config.yml"));
   public static boolean isDebug = false;
   private static Arathoth instance;
   public static Arathoth getInstance() {return instance;}
   public static Random random = new Random();
   public static DecimalFormat DecimalFormat = new DecimalFormat("0.0");


    @Override
    public void onEnable() {
       instance = this;
        if (!getDataFolder().exists())
            getDataFolder().mkdir();
        File file = new File(getDataFolder(), "config.yml");
        if (!file.exists())
            saveDefaultConfig();
        reloadConfig();

        File attributeFile = new File(getDataFolder(), "Attributes");
        if (!attributeFile.exists()) {
            attributeFile.mkdirs();
        }

        File RulesFile = new File(getDataFolder(), "Rules");
        if (!RulesFile.exists()) {
            RulesFile.mkdirs();
        }

        isDebug = config.getBoolean("Debug");
        SendUtils.info("&fPlugin has been enabled");
        if(hasPAPI()){
            SendUtils.info("&fPlaceholderAPI &8Hook!");
        }
        else{
            SendUtils.warn("PlaceHolderAPI &4not found!");
        }
        DrawFucker.fuck();
        boolean success = new PlaceHolderAPIHook(this).hook();
        if(success){
            SendUtils.info("PlaceHolderHook Successfully");
        }
        else{
            SendUtils.warn("Failed to PlaceHolderHook");
        }
        registerDefault();
        if (config.get("DecimalFormat") != null){
            DecimalFormat = new DecimalFormat(config.getString("DecimalFormat"));
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean hasPAPI(){
        if (getServer().getPluginManager().getPlugin("PlaceHolderAPI") == null)
            return false;
        else
            return true;
    }

    public static boolean isDebug(){
        return isDebug;
    }

    public static void Debug(String Debugmsg){
        String prefix = "§8§l[§3§lArathoth§8§l] §3[§bDeBug§3] §8";
        if(isDebug()){
            Bukkit.getConsoleSender().sendMessage(prefix + Debugmsg.replaceAll("&","§"));
        }
    }


    public void registerDefault(){
        //TODO 监听器注册
        Bukkit.getPluginManager().registerEvents(new StatusUpdateListeners(),this);
        Bukkit.getPluginManager().registerEvents(new AttributeListener(),this);
        //TODO 命令&TabCompleter注册
        Bukkit.getPluginCommand("Arathoth").setTabCompleter(new MainCommand());
        Bukkit.getPluginCommand("Arathoth").setExecutor(new MainCommand());
        //TODO 本体属性注册
        AttributeLoader.Register(new AdditionalHealth(),this);
        AttributeLoader.Register(new AttackSpeedRank(),this);
        AttributeLoader.Register(new CriticalArmor(),this);
        AttributeLoader.Register(new CriticalChance(),this);
        AttributeLoader.Register(new CriticalDamage(),this);
        AttributeLoader.Register(new CriticalDodge(),this);
        AttributeLoader.Register(new DodgeRate(),this);
        AttributeLoader.Register(new HitRate(),this);
        AttributeLoader.Register(new LifeSteal(),this);
        AttributeLoader.Register(new LifeStealResist(),this);
        AttributeLoader.Register(new MagicArmor(),this);
        AttributeLoader.Register(new MagicDamage(),this);
        AttributeLoader.Register(new MonsterDamage(),this);
        AttributeLoader.Register(new MonsterArmor(),this);
        AttributeLoader.Register(new PhysicalArmor(),this);
        AttributeLoader.Register(new PhysicalDamage(),this);
        AttributeLoader.Register(new PlayerArmor(),this);
        AttributeLoader.Register(new PlayerDamage(),this);
        AttributeLoader.Register(new PlayerTrueDamage(),this);
        AttributeLoader.Register(new TrueDamage(),this);
        AttributeLoader.Register(new Regen(),this);
        //TODO 本体规则注册
        RulesManager.register(this,new LevelRequired());
        RulesManager.register(this,new OwnderRequest());
        RulesManager.register(this,new PermRequest());
        RulesManager.register(this,new PAPIRequest());
    }

}
