package Flomezy.pMaster.timedEvents;

import org.bukkit.Bukkit;
import java.time.Duration;


/**
 * @author flomzey
 */
public class TimedBackup implements TimedEvent{

    //TODO: add properties file to edit variables

    private Duration sleepTime;
    private String eventName = this.getClass().getSimpleName();

    public TimedBackup(Duration sleepTime){
        this.sleepTime = sleepTime;

    }


    @Override
    public boolean eventTask() {
        return false;
    }

    @Override
    public Duration getDurationMs() {
        return sleepTime;
    }

    @Override
    public int getTimeUntilEventS() {
        return 0;
    }

    @Override
    public void printToConsole(String msg) {
        Bukkit.getLogger().info("[PMaster]["+eventName+"] "+msg);
    }
}
