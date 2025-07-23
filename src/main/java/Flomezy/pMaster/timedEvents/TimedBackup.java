package Flomezy.pMaster.timedEvents;

import org.bukkit.Bukkit;

import java.time.Duration;
import java.util.logging.Logger;


/**
 * @author flomzey
 */
public class TimedBackup implements TimedEvent{

    //TODO: add properties file to edit variables

    private final int sleepTimeS;
    private final Logger logger;
    private int timeUntilEventTask;
    private String eventName = this.getClass().getSimpleName();

    public TimedBackup(Duration sleepTimeS, Logger logger){
        this.sleepTimeS = sleepTimeS.toSecondsPart();
        timeUntilEventTask = sleepTimeS.toSecondsPart();
        this.logger = logger;
    }

    @Override
    public boolean eventTask() {
        printToConsole("Backing up files");

        resetTimeUnitlEventTaskS();
        return true;
    }

    @Override
    public int getDurationS() {
        return sleepTimeS;
    }

    @Override
    public int getTimeUntilEventTaskS() {
        return timeUntilEventTask;
    }

    @Override
    public void subtractTimeUntilEventTaskS() {
        timeUntilEventTask--;
    }

    @Override
    public void printToConsole(String msg) {
        logger.info("["+eventName+"] " + msg);
    }

    @Override
    public void resetTimeUnitlEventTaskS() {
        timeUntilEventTask = sleepTimeS;
    }
}
