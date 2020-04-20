package com.brohoof.brohoofbans;

import org.bukkit.plugin.java.JavaPlugin;

import com.brohoof.brohoofbans.command.handlers.BanCommandHandler;
import com.brohoof.brohoofbans.command.handlers.BanInfoCommandHandler;
import com.brohoof.brohoofbans.command.handlers.CoreCommandHandler;
import com.brohoof.brohoofbans.command.handlers.IsBannedCommandHandler;
import com.brohoof.brohoofbans.command.handlers.KickCommandHandler;
import com.brohoof.brohoofbans.command.handlers.SuspendCommandHandler;
import com.brohoof.brohoofbans.command.handlers.UnbanCommandHandler;

public class BrohoofBansPlugin extends JavaPlugin {

    private ExpireConverter converter;
    private Data data;
    private Settings settings;

    public Data getData() {
        return data;
    }

    public Settings getSettings() {
        return settings;
    }

    public ExpireConverter getConverter() {
        return converter;
    }

    @Override
    public void onDisable() {
    }

    @Override
    public void onEnable() {
        converter = new ExpireConverter();
        settings = new Settings(this);
        data = new Data(this, settings);
        getCommand("ban").setExecutor(new BanCommandHandler(this, data, settings, converter));
        getCommand("kick").setExecutor(new KickCommandHandler(this, data, settings));
        getCommand("baninfo").setExecutor(new BanInfoCommandHandler(this, data, settings, converter));
        getCommand("isbanned").setExecutor(new IsBannedCommandHandler(this, data, settings));
        getCommand("unban").setExecutor(new UnbanCommandHandler(this, data, settings));
        getCommand("suspend").setExecutor(new SuspendCommandHandler(this, data, settings));
        getCommand("brohoofbans").setExecutor(new CoreCommandHandler(this, data, settings));
        getServer().getPluginManager().registerEvents(new EventManager(data, converter, settings), this);
    }
}
