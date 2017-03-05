package com.brohoof.brohoofbans;

import org.bukkit.plugin.java.JavaPlugin;


public class BrohoofBansPlugin extends JavaPlugin {
    private ExpireConverter c;
    private CommandHandler ch;
    private Data d;
    private Settings s;

    public Data getData() {
        return d;
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        c = new ExpireConverter();
        s = new Settings(this);
        d = new Data(this, s);
        ch = new CommandHandler(d, s, this, c);
        getCommand("ban").setExecutor(ch);
        getCommand("kick").setExecutor(ch);
        getCommand("baninfo").setExecutor(ch);
        getCommand("isbanned").setExecutor(ch);
        getCommand("unban").setExecutor(ch);
        getCommand("suspend").setExecutor(ch);
        getCommand("brohoofbans").setExecutor(ch);
        getServer().getPluginManager().registerEvents(new EventManager(d, c, s, this), this);
    }
}
