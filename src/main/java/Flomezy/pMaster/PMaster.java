package Flomezy.pMaster;

import Flomezy.pMaster.timedEvents.TimedBackup;
import Flomezy.pMaster.timedEvents.TimedEvent;
import Flomezy.pMaster.timedEvents.TimedEventHandler;
import org.bukkit.Bukkit;

import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

public final class PMaster extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getLogger().info("setting up running Threads . . .");

        TimedEvent event = new TimedBackup(Duration.ofSeconds(10));
        TimedEventHandler eventHandler = new TimedEventHandler(event);
        Thread thread = new Thread(eventHandler);
        thread.start();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.getLogger().info("This is a test..................");
    }
}
