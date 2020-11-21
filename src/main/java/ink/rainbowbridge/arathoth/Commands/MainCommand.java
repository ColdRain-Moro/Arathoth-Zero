package ink.rainbowbridge.arathoth.Commands;

import ink.rainbowbridge.arathoth.Commands.SubCommands.*;
import ink.rainbowbridge.arathoth.Utils.SendUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!sender.hasPermission("Arathoth.admin")){
            sender.sendMessage("&f&lArathoth &7- &7&lby.&8&l寒雨".replaceAll("&","§"));
            sender.sendMessage("&7Version: &f0.0.1.2020.11.14".replaceAll("&","§"));
            return true;
        }
        else{
            if(args.length == 0){
                sender.sendMessage(" ".replaceAll("&","§"));
                sender.sendMessage("&f&lArathoth Zero   &70.0.1-SNAPSHOT".replaceAll("&","§"));
                sender.sendMessage(" ".replaceAll("&","§"));
                sender.sendMessage("&7MainCommands:".replaceAll("&","§"));
                sender.sendMessage("&8- &fArathoth &7/ &fAra".replaceAll("&","§"));
                sender.sendMessage(" ".replaceAll("&","§"));
                sender.sendMessage("&7SubCommands: ".replaceAll("&","§"));
                sender.sendMessage("&8- &flistattr ".replaceAll("&","§"));
                sender.sendMessage("    &8- &7List registered attributes".replaceAll("&","§"));
                sender.sendMessage("&8- &freload ".replaceAll("&","§"));
                sender.sendMessage("    &8- &7Reload Configuration ".replaceAll("&","§"));
                sender.sendMessage("&8- &fstatus ".replaceAll("&","§"));
                sender.sendMessage("    &8- &7Status Information ".replaceAll("&","§"));
                sender.sendMessage("&8- &fdebug ".replaceAll("&","§"));
                sender.sendMessage("    &8- &7Debug-Mode Switcher ".replaceAll("&","§"));
                sender.sendMessage("&8- &fAbout ".replaceAll("&","§"));
                sender.sendMessage("    &8- &7About Author ".replaceAll("&","§"));
                sender.sendMessage(" ".replaceAll("&","§"));
            }
            else {
                switch (parse(args[0].toUpperCase())) {
                    case LISTATTR: {
                        return new Listattr().command(sender, args);
                    }
                    case RELOAD: {
                        return new Reload().command(sender,args);
                    }
                    case STATUS:{
                        return new Status().command(sender,args);
                    }
                    case ABOUT:{
                        return new About().command(sender,args);
                    }
                    case DEBUG:{
                        return new Debug().command(sender,args);
                    }
                    default: {
                        sender.sendMessage("&7&l[&f&lArathoth&7&l] &7Invalid SubCommand".replaceAll("&","§"));
                        return true;
                    }
                }
            }
        }
        return true;
    }

    private enum command {

        LISTATTR, STATUS, RELOAD,

        DEBUG, ABOUT,ERROR
    }

    private command parse(String s) {
        try {
            return command.valueOf(s);
        }
        catch (Exception e) {
            return command.ERROR;
        }
    }
}