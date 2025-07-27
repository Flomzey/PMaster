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

            Auto delete function, deleting updates after a specific length.
            */

    private final String CONFIG_NAME = "backup";
    private final int sleepPeriodCount;
    private final Logger logger;
    public final ZipUtil zipUtil = new ZipUtil();
    private int periodsUntilEventTask;
    private final String eventName = this.getClass().getSimpleName();
    private final List<World> worldsToSave = new ArrayList<>();
    private final File backupDest, backupZipName;
    private boolean running = false;

    public TimedBackup(Plugin plugin) {
        this.logger = plugin.getLogger();
        FileConfiguration config = plugin.getConfig();
        List<String> worldNames = config.getStringList(CONFIG_NAME+".worlds");
        for (String name : worldNames) worldsToSave.add(Bukkit.getWorld(name));
        this.sleepPeriodCount = 30;
        periodsUntilEventTask = sleepPeriodCount;
        this.backupDest = new File(config.getString(CONFIG_NAME+".save-dir"));
        this.backupZipName = new File(backupDest + "/current_backup.zip");
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

        worldsToSave.forEach(World::save);

        new Thread(() -> {
            worldsToSave.forEach(world -> {
                running = true;
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
