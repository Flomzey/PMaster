package Flomezy.pMaster.TimedEvents.Events;

import Flomezy.pMaster.TimedEvents.TimedEvent;

public class TimedUpdate implements TimedEvent {

    @Override
    public void eventTask() {

    }

    @Override
    public int getDurationPeriodCount() {
        return 0;
    }

    @Override
    public int getPeriodsUntilNextEvent() {
        return 0;
    }

    @Override
    public void subtractOnePeriod() {

    }

    @Override
    public void resetPeriods() {

    }

    @Override
    public void printToConsole(String msg) {

    }
}
