package controller;

import model.*;
import view.View;
import model.*;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that represents the main controller of the system. Its main responsibility is to manage the
 * Tasks of the system, which are represented by the class {@link UpdateTask}.
 */
public class Simulator {

    private final static int BODIES_INIT_WITHOUT_FILE = 500;
    private final static int STEPS_INIT_WITHOUT_FILE = 5000;
    private final static double DT_INIT_WITHOUT_FILE = 0.001;
    private final static Boundary BOUNDARY_INIT_WITHOUT_FILE = new Boundary(-6, -6, 6, 6);
    private final static String CONFIGURATION_FILE_NAME = "config.properties";

    private final View viewer;
    private Context context;
    private long nSteps;
    /* bodies in the field */
    private BodiesSharedList readSharedList;
    private BodiesSharedList writeSharedList;
    private double vt;
    private long iter;
    private int nBodies;
    private Configuration configuration;
    private final ExecutorService executor;
    private final SyncMonitor syncMonitor;

    /**
     * Create the controller and the shared elements in the system, which are the @Context and the CyclicBarrier used
     * for synchronization.
     *
     * @param viewer - the view to be used to display the evolution of the simulation
     */
    public Simulator(View viewer) {

        this.viewer = viewer;
        this.readConfiguration();
        this.executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() + 1);
        this.syncMonitor = new SyncMonitor();
        this.initSimulation();
    }

    private void exec() {
        TaskSyncMonitor taskSyncMonitor = new TaskSyncMonitor(readSharedList.getBodies().size());
        for (int iter = 0; iter < nSteps && syncMonitor.isSimulationRunning(); iter++) {
            for (Body b : readSharedList.getBodies()) {
                executor.execute(new UpdateTask(b, this.context, taskSyncMonitor));
            }
            try {
                taskSyncMonitor.awaitCompletion();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            taskSyncMonitor.reset();
            this.updateSimulation();
        }
        this.initSimulation();
    }

    private void initSimulation() {
        this.context.restartContext();
        this.readSharedList = this.context.getReadSharedList();
        this.writeSharedList = this.context.getWriteSharedList();
        createBodies(this.nBodies);
        this.vt = 0;
        this.iter = 0;
        viewer.setStopEnabled(false);
        viewer.setStartEnabled(true);
    }

    private void updateSimulation() {
        readSharedList.reset();
        readSharedList.addBodies(writeSharedList.getBodies());
        /* update virtual time */
        vt = vt + this.context.getDT();
        iter++;
        /* display current stage */
        this.viewer.display(readSharedList.getBodies(), vt, iter, context.getBoundary());
    }

    /**
     * Method to call when the simulation has to restart, for example when the button Start from the GUI is
     * pressed. It also waits for the termination of the BodyAgents of the previous simulation, if any.
     */
    public void startSimulation() {
        this.syncMonitor.startSimulation();
        new Thread(this::exec).start();
        viewer.setStartEnabled(false);
        viewer.setStopEnabled(true);
    }

    /**
     * Method to call when the simulation has to stop, for example when the button Stop from the GUI is pressed.
     */
    public void stopSimulation() {
        syncMonitor.stopSimulation();
        viewer.setStopEnabled(false);
    }

    protected void readConfiguration() {
        try {
            this.configuration = new Configuration(Simulator.CONFIGURATION_FILE_NAME);
            this.initConfigurationWithFile();
        } catch (FileNotFoundException e) {
            this.initConfigurationWithoutFile();
        }
    }

    protected void initConfigurationWithoutFile() {
        this.viewer.showMessage("Configuration file not found. Simulation will be initialized with prefixed data: " + Simulator.BODIES_INIT_WITHOUT_FILE + " bodies and " + Simulator.STEPS_INIT_WITHOUT_FILE + " steps.");
        this.context = new Context(Simulator.BOUNDARY_INIT_WITHOUT_FILE, Simulator.DT_INIT_WITHOUT_FILE);
        this.nBodies = Simulator.BODIES_INIT_WITHOUT_FILE;
        this.nSteps = Simulator.STEPS_INIT_WITHOUT_FILE;
    }

    private void initConfigurationWithFile() {
        Boundary boundary = new Boundary(this.configuration.getLefterBoundary(), this.configuration.getUpperBoundary(), this.configuration.getRighterBoundary(), this.configuration.getLowerBoundary());
        this.context = new Context(boundary, this.configuration.getDT());
        this.nBodies = this.configuration.getBodiesQuantity();
        this.nSteps = this.configuration.getIterationsQuantity();
    }

    private void createBodies(final int nBodies) {
        List<Body> bodies = new ArrayList<>();
        Random rand = new Random(System.currentTimeMillis());
        for (int i = 0; i < nBodies; i++) {
            double x = context.getBoundary().getX0() * 0.25 + rand.nextDouble() * (context.getBoundary().getX1() - context.getBoundary().getX0()) * 0.25;
            double y = context.getBoundary().getY0() * 0.25 + rand.nextDouble() * (context.getBoundary().getY1() - context.getBoundary().getY0()) * 0.25;
            Body b = new Body(i, new P2d(x, y), new V2d(0, 0), 10);
            bodies.add(b);
        }
        readSharedList.addBodies(bodies);
        writeSharedList.addBodies(readSharedList.getBodies());
    }

}
