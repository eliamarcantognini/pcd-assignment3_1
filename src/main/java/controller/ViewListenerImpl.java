package controller;

import model.Commands;

public class ViewListenerImpl implements ViewListener {

    private final Simulator simulator;

    public ViewListenerImpl(final Simulator simulator) {
        this.simulator = simulator;
    }

    @Override
    public void eventPerformed(Commands code) {
        switch (code) {
            case START:
                simulator.startSimulation();
                break;
            case STOP:
                simulator.stopSimulation();
                break;
            default:
                break;
        }
    }
}
