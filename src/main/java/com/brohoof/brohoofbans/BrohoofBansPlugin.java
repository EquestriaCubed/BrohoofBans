package com.brohoof.brohoofbans;

import org.bukkit.plugin.java.JavaPlugin;
import net.shonx.lib.SweetieLib;
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
    private Scheduler scheduler;
    private static API api;

    public static API getAPI() {
        return api;
    }

    public ExpireConverter getConverter() {
        return converter;
    }

    public Data getData() {
        return data;
    }

    public Settings getSettings() {
        return settings;
    }

    @Override
    public void onDisable() {
        scheduler.shutdown();
    }

    @Override
    public void onEnable() {
        scheduler = new Scheduler(this);
        settings = new Settings(this);
        try {
            data = new Data(this, settings, SweetieLib.getPlugin().getConnection());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        api = new API(data);
        getCommand("ban").setExecutor(new BanCommandHandler(this, api, settings));
        getCommand("kick").setExecutor(new KickCommandHandler(this, api, settings));
        getCommand("baninfo").setExecutor(new BanInfoCommandHandler(this, api, settings));
        getCommand("isbanned").setExecutor(new IsBannedCommandHandler(this, api, settings));
        getCommand("unban").setExecutor(new UnbanCommandHandler(this, api, settings));
        getCommand("suspend").setExecutor(new SuspendCommandHandler(this, api, settings));
        getCommand("brohoofbans").setExecutor(new CoreCommandHandler(this, api, settings));
        getServer().getPluginManager().registerEvents(new EventManager(api, settings), this);
    }
}
