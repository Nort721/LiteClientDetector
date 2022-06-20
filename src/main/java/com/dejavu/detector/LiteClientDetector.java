package com.dejavu.detector;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.util.ArrayList;
import java.util.List;

public class LiteClientDetector extends JavaPlugin implements Listener, PluginMessageListener {

    private List<String> commands = new ArrayList<>();

    @Override
    public void onEnable() {
        if (!verifyPluginYaml()) return;

        saveDefaultConfig();

        commands = getConfig().getStringList("commands");

        getServer().getMessenger().registerIncomingPluginChannel(this, "218c69d8875f", this);
        getServer().getPluginManager().registerEvents(this, this);

        sendConsoleMessage("has been enabled.");
    }

    @Override
    public void onDisable() {
        sendConsoleMessage("has been disabled.");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        player.sendMessage("§r");
    }

    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equals("218c69d8875f")) return;

        String msg = new String(message);

        if (msg.contains("LiteClient")) {
            for (String cmd : commands) {
                cmd = cmd.replaceAll("%player%", player.getName());
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), cmd);
            }
        }
    }

    public void sendConsoleMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "[" + getDescription().getName() + "] " + ChatColor.RESET + msg);
    }

    public boolean verifyPluginYaml() {
        // verify plugin name
        if (!getDescription().getName().equals("LiteClientDetector"))
            return false;

        // verify authors
        for (String author : getDescription().getAuthors())
            if (!author.equals("Nort721") && !author.equals("Dejavu"))
                return false;

        return true;
    }

}
