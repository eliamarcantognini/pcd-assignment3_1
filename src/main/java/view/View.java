package view;

import controller.ViewListener;
import model.Body;
import model.Boundary;

import java.util.List;

/**
 * Interface to be implemented by the classes that are designed to be a View for the system.
 */
public interface View {
    /**
     * Display the system status in some way.
     *
     * @param bodies - the list of bodies to show
     * @param vt     - the virtual time passed since the beginning of the simulation
     * @param iter   - the current iterazione to be displayed
     * @param bounds - the {@link Boundary} that indicate the 2D plan where the bodies can move
     */
    void display(final List<Body> bodies, final double vt, final long iter, final Boundary bounds);

    /**
     * Method called by the controller to say if it's possible to stop the simulation.
     *
     * @param enabled - is possible to stop the simulation
     */
    void setStopEnabled(final Boolean enabled);

    /**
     * Method called by the controller to say if it's possible to start the simulation.
     *
     * @param enabled - is possible to start the simulation
     */
    void setStartEnabled(final Boolean enabled);

    /**
     * Method called to set a {@link ViewListener} to respond to certain event.
     *
     * @param listener - the listener to notify when the event happen
     */
    void addListener(final ViewListener listener);

    /**
     * Show message passed as parameter.
     *
     * @param message - the message to show
     */
    void showMessage(final String message);
}
