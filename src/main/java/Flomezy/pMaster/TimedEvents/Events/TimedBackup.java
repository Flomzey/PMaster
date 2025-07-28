package Flomezy.pMaster.TimedEvents.Events;

import Flomezy.pMaster.TimedEvents.TimedEvent;
import Flomezy.pMaster.TimedEvents.Util.ZipUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


/**
 * @author flomzey
 */
public class TimedBackup implements TimedEvent {

    /*TODO: add properties file to edit variables maybe do this in the constructor, you will need the Plugin for that
            figure out wether you can maybe use datatypes, in the worst case just use strings to get the information
            also figure out what type of timing you want to use using the variable that counts down makes sence, since
            you maybe want to add more TimedEvents in the future that run on a diffrent time pattern.

            Auto delete function, deleting updates after a specific amount of stored updates.
            */


    private final String CONFIG_NAME = "backup";
    private final File CONFIG_PATH = new File("./plugins/PMaster/" + CONFIG_NAME + "/backup.yml");
    private final int sleepPeriodCount;
    private final Logger logger;
    public final ZipUtil zipUtil = new ZipUtil();
    private int periodsUntilEventTask;
    private final String eventName = this.getClass().getSimpleName();
    private final List<World> worlds;
    private final List<String> worldNames = new ArrayList<>();
    private final File backupDest, backupZipName;
    private boolean running = false;
    private final Plugin plugin;
    private final FileConfiguration config;

    public TimedBackup(Plugin plugin) throws IOException {
        this.plugin = plugin;
        this.logger = plugin.getLogger();
        config = plugin.getConfig();
        worlds = Bukkit.getWorlds();

        for(World world : worlds){
            worldNames.add(world.getName());
        }

        config.set("worlds", worldNames);
        config.save(CONFIG_PATH);
        this.sleepPeriodCount = 30;//FIXED CHANGE
        periodsUntilEventTask = sleepPeriodCount;
        printToConsole(config.getString("save-dir"));
        this.backupDest = new File(config.getString("save-dir"));
        this.backupZipName = new File(backupDest + "/current_backup.zip");
    }

    private void saveDefaultConfig() throws IOException {
        config.save(CONFIG_PATH);
    }

    @Override
    public void eventTask() {
        if(running){
            printToConsole("Backup already running, skipping this one...");
            return;
        }
        running = true;
        File worldContainer = Bukkit.getWorldContainer();
        File toBeZipped = new File(backupDest + "/backup");

        printToConsole("Saving worlds...");

        worlds.forEach(World::save);

        new Thread(() -> {
            running = true;
            worlds.forEach(world -> {
                File copyPath = new File(worldContainer + "/" + world.getName());
                File destOfCopy = new File(toBeZipped + "/" + world.getName());
                printToConsole("Copying "+world.getName()+"...");
                try {
                    zipUtil.copy(copyPath, destOfCopy);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            printToConsole("Zipping backup...");
            try{
                zipUtil.zip(toBeZipped, backupZipName);
                printToConsole("Deleting temporary files...");
                zipUtil.del(toBeZipped);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            printToConsole("Backup finished successfully!");
            running = false;
        }).start();
    }

    @Override
    public int getDurationPeriodCount() {
        return sleepPeriodCount;
    }

    @Override
    public int getPeriodsUntilNextEvent() {
        return periodsUntilEventTask;
    }

    @Override
    public void subtractOnePeriod() {
        periodsUntilEventTask--;
    }


    @Override
    public void printToConsole(String msg) {
        logger.info("[" + eventName + "] " + msg);
    }

    @Override
    public void resetPeriods() {
        periodsUntilEventTask = sleepPeriodCount;
    }

    public String toString(){
        return CONFIG_NAME;
    }
}
