package com.salo.voteclient;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.Set;

/**
 * Created by user on 24.11.2016.
 */
public class Main extends JavaPlugin {


    public List<String> playersQueue;

    public String serverId;
    private String prefix;

    public Integer rate;

    public String[] whatToTell={" Ваш баланс: ",
                         " У вас недостаточно денег: "};

    public TextComponent getBalance;
    public TextComponent balancePrefix;

    @Override
    public void onEnable() {
        if(!getDataFolder().exists()){
            getConfig().options().copyDefaults(true);
            saveConfig();
        }
        prefix = ChatColor.translateAlternateColorCodes('&', "&0&l[&4&lVFC&0&l] &r&l");

        serverId=getConfig().getString("serverId");
        rate = getConfig().getInt("rate");

        balancePrefix = new TextComponent("Нажмите, чтобы узнать состояние вашего счета: ");
        balancePrefix.setColor(ChatColor.GREEN);

        getBalance = new TextComponent("Баланс");
        getBalance.setColor(ChatColor.RED);
        getBalance.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/votereward balance"));
        getBalance.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Нажмите, чтобы узнать баланс").create()));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length>0){
            switch (label){
                case "coreanswer":
                    if(sender instanceof ConsoleCommandSender) {
                        switch (args[0]) {
                            case "tell":
                                if (getServer().getPlayer(args[1]).isOnline()) {
                                    getServer().getPlayer(args[1]).sendMessage(prefix +  whatToTell[Integer.valueOf(args[2])] + Integer.valueOf(args[3]) * rate);
                                    //getServer().dispatchCommand(getServer().getConsoleSender(), "tell " + args[1] + whatToTell[Integer.valueOf(args[2])] + Integer.valueOf(args[3]) * rate);
                                }
                                return true;

                            case "pay":
                                getServer().dispatchCommand(getServer().getConsoleSender(), "eco give " + args[1] + " " + Integer.valueOf(args[2]) * rate);
                                return true;

                            case "balance":
                                balancePrefix.addExtra(getBalance);
                                getServer().getPlayer(args[1]).spigot().sendMessage(balancePrefix);
                        }
                    }
                    return true;

                case "votereward":
                    switch(args[0]){
                        case "balance":
                            sender.sendMessage(prefix + "Ваш запрос обрабатывается...");
                            getServer().dispatchCommand(getServer().getConsoleSender(), "sync console Build votecore balance " + sender.getName() + " " + serverId);
                            return true;

                        case "receive":
                            if(NumberUtils.isNumber(args[1])) {
                                sender.sendMessage(prefix + "Ваш запрос обрабатывается...");
                                getServer().dispatchCommand(getServer().getConsoleSender(), "sync console Build votecore receive " + sender.getName() + " " + serverId + " " + Integer.valueOf(args[1]) / rate);
                            } else {
                                sender.sendMessage(prefix + "Вы должны ввести число!");
                            }
                            return true;

                    }
                    return true;
            }
        }

        return true;
    }
}
