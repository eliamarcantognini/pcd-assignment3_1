package model;

/**
 * Class that implements a monitor used to synchronize tasks.
 */
public class TaskSyncMonitor {

    private final long bodyToWait;
    private long counter = 0;

    /**
     * Constructor of monitor that allows to specify number of call of {@link #countMeIn()}
     * necessary before unlock monitor.
     *
     * @param bodyToWait number of call of {@link #countMeIn()} necessary before unlock monitor
     */
    public TaskSyncMonitor(long bodyToWait) {
        this.bodyToWait = bodyToWait;
    }

    /**
     * Increase by one monitor's counter
     */
    public synchronized void countMeIn() {
        counter++;
        if (counter >= bodyToWait) {
            notifyAll();
        }
    }

    /**
     * Wait monitor's counter reach value specified in constructor.
     *
     * @throws InterruptedException as in {@link Object#wait()}
     *
     * @see Object#wait()
     */
    public synchronized void awaitCompletion() throws InterruptedException {
        while (counter < bodyToWait) {
            wait();
        }
    }

    /**
     * Reset monitor's counter.
     */
    public synchronized void reset() {
        counter = 0;
    }
}
