package com.brohoof.brohoofbans;

import java.net.InetSocketAddress;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

class CommandHandler implements CommandExecutor {
    private final static UUID consoleUUID = UUID.fromString("3c879ef9-95c2-44d1-98f9-2824610477c8");
    private final static String noPermission = "§cYou do not have permission.";
    private final ExpireConverter c;
    private final Data d;
    private final BrohoofBansPlugin p;
    private final Settings s;

    CommandHandler(final Data d, final Settings s, final BrohoofBansPlugin p, final ExpireConverter c) {
        this.d = d;
        this.s = s;
        this.p = p;
        this.c = c;
    }

    /**
     * May or may not be null
     *
     * @param uuidorName
     * @return
     */
    private Ban fromString(final String uuidorName) {
        try {
            return d.getBan(UUID.fromString(uuidorName));
        } catch (final IllegalArgumentException e) {
            d.error(e);
            Player p;
            try {
                p = (Player) getPlayer(uuidorName);
                return d.getBan(p.getUniqueId());
            } catch (final ClassCastException f) {
                d.error(f);
                return d.getBan(uuidorName);
            }
        }
    }

    private String getIP(final InetSocketAddress address) {
        // return (address.getAddress().toString().replace("/", "").split(":"))[0];
        return address.getAddress().getHostAddress();
    }

    @SuppressWarnings("deprecation")
    private OfflinePlayer getPlayer(final String playerORuuid) {
        UUID uuid = null;
        try {
            uuid = UUID.fromString(playerORuuid);
            final Player p = Bukkit.getPlayer(uuid);
            if (p == null)
                return Bukkit.getOfflinePlayer(uuid);
            return p;
        } catch (final IllegalArgumentException e) {
            // Not a UUID
            for (final Player p : Bukkit.getOnlinePlayers())
                if (p.getName().equalsIgnoreCase(playerORuuid))
                    return p;
            return Bukkit.getServer().getOfflinePlayer(playerORuuid);
        }
    }

    private String getReason(final String[] args, final boolean isTimed) {
        String reason = "";
        int j = 1;
        if (isTimed)
            j = 3;
        for (int i = j; i < args.length; i++) {
            if (i != 1)
                reason += " ";
            reason += args[i];
        }
        if (reason.startsWith(" "))
            return reason.substring(1, reason.length());
        return reason;
    }

    private boolean hasPermission(final CommandSender sender, final String command) {
        return sender.hasPermission("brohoofbans." + command);
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label, final String[] args) {
        if (command.getName().equalsIgnoreCase("brohoofbans")) {
            // Format = /brohoofbans <subcommand> <...> subcommand is args[0]
            if (args.length == 0)
                return false;
            if (args[0].equalsIgnoreCase("reload")) {
                if (!hasPermission(sender, "reload")) {
                    sender.sendMessage(noPermission);
                    return true;
                }
                d.forceConnectionRefresh();
                s.reloadSettings();
                sender.sendMessage("§e[BrohoofBans] Config reloaded.");
                return true;
            }
            return false;
        }
        if (command.getName().equalsIgnoreCase("ban")) {
            // Format = /ban [-t] [time] playerName | UUID reason... args[0 OR 1] is the name or UUID, other args are reason
            if (args.length < 2)
                return false;
            if (!hasPermission(sender, "ban")) {
                sender.sendMessage(noPermission);
                return true;
            }
            Boolean isTemporary = false;
            String expires = "NEVER";
            if (args[0].equalsIgnoreCase("-t"))
                try {
                    expires = String.valueOf(c.getExpires(args[1]));
                    isTemporary = true;
                } catch (final CommandException e) {
                    d.error(e);
                    sender.sendMessage("§cThat is not a valid time format. §f1h-5m §cis a valid format.");
                    return true;
                }
            if (isTemporary && args.length <= 3)
                return false;
            // The player is online.
            if (getPlayer(args[isTemporary ? 2 : 0]) instanceof Player) {
                final Player victim = (Player) getPlayer(args[isTemporary ? 2 : 0]);
                final String reason = getReason(args, isTemporary);
                if (sender instanceof Player) {
                    // Sender is a Player
                    final Player executor = (Player) sender;
                    final Ban b = new Ban(victim.getUniqueId(), executor.getUniqueId(), victim.getName(), executor.getName(), getIP(victim.getAddress()), getIP(executor.getAddress()), expires, reason, false);
                    victim.kickPlayer(reason);
                    p.getLogger().info(b.toString());
                    Bukkit.broadcastMessage("§eBanned " + b.getVictimName() + " for " + b.getReason());
                    d.saveBan(b);
                    return true;
                }
                // It's console
                final Ban b = new Ban(victim.getUniqueId(), consoleUUID, victim.getName(), "CONSOLE", getIP(victim.getAddress()), "::1", expires, reason, false);
                p.getLogger().info(b.toString());
                victim.kickPlayer(reason);
                Bukkit.broadcastMessage("§eBanned " + b.getVictimName() + " for " + b.getReason());
                d.saveBan(b);
                return true;
            }
            // Offline player
            final OfflinePlayer victim = getPlayer(args[isTemporary ? 2 : 0]);
            final String reason = getReason(args, isTemporary);
            if (sender instanceof Player) {
                // Sender is a Player
                final Player executor = (Player) sender;
                final Ban b = new Ban(victim.getUniqueId(), executor.getUniqueId(), victim.getName(), executor.getName(), "NULL", getIP(executor.getAddress()), expires, reason, false);
                p.getLogger().info(b.toString());
                d.saveBan(b);
                Bukkit.broadcastMessage("§eBanned " + b.getVictimName() + " for " + b.getReason());
                return true;
            }
            // It's console
            final Ban b = new Ban(victim.getUniqueId(), consoleUUID, victim.getName(), "CONSOLE", "NULL", "::1", expires, reason, false);
            p.getLogger().info(b.toString());
            d.saveBan(b);
            Bukkit.broadcastMessage("§eBanned " + b.getVictimName() + " for " + b.getReason());
            return true;
        }
        if (command.getName().equalsIgnoreCase("kick")) {
            // Format = /kick playerName | UUID reason... args[0 OR 1] is the name or UUID, other args are reason
            if (args.length < 2)
                return false;
            if (!hasPermission(sender, "kick")) {
                sender.sendMessage(noPermission);
                return true;
            }
            Player victim;
            try {
                victim = (Player) getPlayer(args[0]);
            } catch (final ClassCastException e) {
                d.error(e);
                sender.sendMessage("§cThat player is not online.");
                return true;
            }
            final String reason = getReason(args, false);
            victim.kickPlayer(reason);
            Bukkit.broadcastMessage("§eKicked " + victim.getName() + " for " + reason);
            if (sender instanceof Player)
                p.getLogger().info("Player " + ((Player) sender).getName() + " kicked " + victim.getName() + " for " + reason + ".");
            else
                p.getLogger().info("CONSOLE kicked " + victim.getName() + " for " + reason + ".");
            return true;
        }
        if (command.getName().equalsIgnoreCase("baninfo")) {
            // Format = /baninfo [-i] <playerName | UUID> args[0] is either -i or player. args[1] is player if -1
            if (args.length < 1)
                return false;
            if (!hasPermission(sender, "baninfo")) {
                sender.sendMessage(noPermission);
                return true;
            }
            if (args[0].equalsIgnoreCase("-i")) {
                if (!hasPermission(sender, "baninfo.admin")) {
                    sender.sendMessage(noPermission);
                    return true;
                }
                final Ban b = fromString(args[1]);
                if (b == null) {
                    // No ban
                    sender.sendMessage("§e" + getPlayer(args[1]).getName() + " is §cnot §e banned.");
                    return true;
                }
                if (b.isSuspension()) {
                    sender.sendMessage("§e" + b.getVictimName() + " §cis §esuspended.");
                    sender.sendMessage("§eVictim UUID is " + b.getVictim().toString() + ". Their name is " + b.getVictimName());
                    sender.sendMessage("§eIssuer UUID is " + b.getExecutor().toString() + ". Their name is " + b.getExecutorName());
                    sender.sendMessage("§eThe Victim is suspended for §f" + b.getReason());
                    sender.sendMessage("§eThe Issuer's IP is " + b.getExecutorIP() + ". The Victim's IP is " + b.getVictimIP());
                    return true;
                }
                sender.sendMessage("§e" + b.getVictimName() + " §cis §ebanned.");
                sender.sendMessage("§eVictim UUID is " + b.getVictim().toString() + ". Their name is " + b.getVictimName());
                sender.sendMessage("§eIssuer UUID is " + b.getExecutor().toString() + ". Their name is " + b.getExecutorName());
                sender.sendMessage("§eThe Issuer's IP is " + b.getExecutorIP() + ". The Victim's IP is " + b.getVictimIP());
                if (b.getExpires().equalsIgnoreCase("NEVER"))
                    sender.sendMessage("§eThe Victim is banned for §f" + b.getReason() + "§e. This ban does §cnot §eexpire.");
                else
                    sender.sendMessage("§eThe Victim is banned for §f" + b.getReason() + "§e. This ban expires at §f" + c.getFriendlyTime(Long.parseLong(b.getExpires())));
                return true;
            }
            // Not a -i
            final Ban b = fromString(args[0]);
            if (b == null) {
                // No ban
                sender.sendMessage("§e" + getPlayer(args[0]).getName() + " is §cnot §ebanned.");
                return true;
            }
            if (b.isSuspension()) {
                sender.sendMessage("§e" + b.getVictimName() + " §cis §esuspended.");
                sender.sendMessage("§eVictim UUID is " + b.getVictim().toString() + ". Their name is " + b.getVictimName());
                sender.sendMessage("§eThe Victim is suspended for §f" + b.getReason());
                return true;
            }
            sender.sendMessage("§e" + b.getVictimName() + " §cis §ebanned.");
            sender.sendMessage("§eVictim UUID is " + b.getVictim().toString() + ". Their name is " + b.getVictimName());
            if (b.getExpires().equalsIgnoreCase("NEVER"))
                sender.sendMessage("§eThe Victim is banned for §f" + b.getReason() + "§e. This ban does §cnot §eexpire.");
            else
                sender.sendMessage("§eThe Victim is banned for §f" + b.getReason() + "§e. This ban expires at §f" + c.getFriendlyTime(Long.parseLong(b.getExpires())));
            return true;
        }
        if (command.getName().equalsIgnoreCase("isbanned")) {
            // Format = /isbanned <playerName | UUID> args[0] is player.
            if (args.length < 1)
                return false;
            if (!hasPermission(sender, "isbanned")) {
                sender.sendMessage(noPermission);
                return true;
            }
            final Ban b = fromString(args[0]);
            if (b == null) {
                sender.sendMessage("§e" + getPlayer(args[0]).getName() + " is §cnot §e banned.");
                return true;
            }
            if (b.isSuspension()) {
                sender.sendMessage("§e" + b.getVictimName() + " §cis §esuspended.");
                return true;
            }
            sender.sendMessage("§e" + b.getVictimName() + " §cis §ebanned.");
            return true;
        }
        if (command.getName().equalsIgnoreCase("unban")) {
            // Format = /unban <playerName | UUID> args[0] is player.
            if (args.length < 1)
                return false;
            if (!hasPermission(sender, "unban")) {
                sender.sendMessage(noPermission);
                return true;
            }
            final Ban b = fromString(args[0]);
            if (b == null) {
                sender.sendMessage("§c" + getPlayer(args[0]).getName() + " is not banned.");
                return true;
            }
            d.unban(b);
            sender.sendMessage("§e" + b.getVictimName() + " unbanned.");
            return true;
        }
        if (command.getName().equalsIgnoreCase("suspend")) {
            // Format = /suspend playerName | UUID reason... args[0] is the name or UUID, other args are reason
            if (args.length < 2)
                return false;
            if (!hasPermission(sender, "ban")) {
                sender.sendMessage(noPermission);
                return true;
            }
            // The player is online.
            if (getPlayer(args[0]) instanceof Player) {
                final Player victim = (Player) getPlayer(args[0]);
                final String reason = getReason(args, false);
                if (sender instanceof Player) {
                    // Sender is a Player
                    final Player executor = (Player) sender;
                    final Ban b = new Ban(victim.getUniqueId(), executor.getUniqueId(), victim.getName(), executor.getName(), getIP(victim.getAddress()), getIP(executor.getAddress()), "NEVER", reason, true);
                    victim.kickPlayer(s.suspendReason);
                    p.getLogger().info(b.toString());
                    d.saveBan(b);
                    Bukkit.broadcastMessage("§eSuspended " + b.getVictimName() + " for " + b.getReason());
                    return true;
                }
                // It's console
                final Ban b = new Ban(victim.getUniqueId(), consoleUUID, victim.getName(), "CONSOLE", getIP(victim.getAddress()), "::1", "NEVER", reason, true);
                p.getLogger().info(b.toString());
                victim.kickPlayer(s.suspendReason);
                d.saveBan(b);
                Bukkit.broadcastMessage("§eSuspended " + b.getVictimName() + " for " + b.getReason());
                return true;
            }
            // Offline player
            final OfflinePlayer victim = getPlayer(args[0]);
            final String reason = getReason(args, false);
            if (sender instanceof Player) {
                // Sender is a Player
                final Player executor = (Player) sender;
                final Ban b = new Ban(victim.getUniqueId(), executor.getUniqueId(), victim.getName(), executor.getName(), "NULL", getIP(executor.getAddress()), "NEVER", reason, true);
                p.getLogger().info(b.toString());
                d.saveBan(b);
                Bukkit.broadcastMessage("§eSuspended " + b.getVictimName() + " for " + b.getReason());
                return true;
            }
            // It's console
            final Ban b = new Ban(victim.getUniqueId(), consoleUUID, victim.getName(), "CONSOLE", "NULL", "::1", "NEVER", reason, true);
            p.getLogger().info(b.toString());
            d.saveBan(b);
            Bukkit.broadcastMessage("§eSuspended " + b.getVictimName() + " for " + b.getReason());
            return true;
        }
        return false;
    }
}
