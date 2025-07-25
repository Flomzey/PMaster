package Flomezy.pMaster.TimedEvents.Events;

import Flomezy.pMaster.TimedEvents.TimedEvent;
import Flomezy.pMaster.TimedEvents.Util.ZipUtil;
import org.bukkit.Bukkit;
import org.bukkit.World;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;


/**
 * @author flomzey
 */
public class TimedBackup implements TimedEvent {

    //TODO: add properties file to edit variables

    private final int sleepPeriodCount;
    private final Logger logger;
    public final ZipUtil zipUtil = new ZipUtil();
    private int periodsUntilEventTask;
    private final String eventName = this.getClass().getSimpleName();
    private final String[] worldNames;
    private final File pathToBackup, destOfBackups;

    public TimedBackup(int sleepTimeS, Logger logger, String[] worldNames, File pathToBackup, File saveBackupPath){
        this.logger = logger;
        this.worldNames = worldNames;
        this.sleepPeriodCount = sleepTimeS;
        periodsUntilEventTask = sleepTimeS;
        this.pathToBackup = pathToBackup;
        this.destOfBackups = saveBackupPath;
    }

    /*TODO: rewrite the event task to the following, create World List in which the worlds are
            saved after one another, then call every copy process to a subdirectory of backup
            after that zip that subdirectory.
     */
    @Override
    public void eventTask() {
        File worldContainer = Bukkit.getWorldContainer();

        for(String name : worldNames){
            World currentWorld = Bukkit.getWorld(name);
            if(currentWorld == null) {
                printToConsole( name + " doesn't exist or is already unloaded | not updating");
                continue;
            }
            currentWorld.save();

            printToConsole("Backing up files of: " + name);

            new Thread(() -> { //to not impact server performance -> Thread
                try {
                    File copyPath = new File(worldContainer + "/" +name);
                    File destOfCopy = new File( "backups/" + name +"_copy");
                    File destOfBackup = new File(destOfBackups.getAbsolutePath()+"/"+name+".zip");

                    zipUtil.copy(copyPath, destOfCopy);
                    zipUtil.zip(destOfCopy, destOfBackup);
                    zipUtil.del(destOfCopy);

                    printToConsole("Backup of " + name + " successful!");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }).start();
        }
        resetPeriods();
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
        logger.info("["+eventName+"] " + msg);
    }

    @Override
    public void resetPeriods() {
        periodsUntilEventTask = sleepPeriodCount;
    }
}
