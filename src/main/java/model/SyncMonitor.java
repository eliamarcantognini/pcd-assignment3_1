package model;

/**
 * Class that implements monitor for simulation's synchronization.
 */
public class SyncMonitor {

    private boolean running = false;

    /**
     * Specify that simulation can start.
     */
    public synchronized void startSimulation() {
        running = true;
    }

    /**
     * Specify that simulation can stop.
     */
    public synchronized void stopSimulation() {
        running = false;
    }

    /**
     * Method to know if simulation is running.
     *
     * @return true if simulation is running
     */
    public synchronized boolean isSimulationRunning() {
        return running;
    }
}
