package model;

/**
 * Context class represents context of the simulation. It contains information about time and limits of two-dimensional
 * world. It also allows to know if simulation is ended or not. It owns two {@link BodiesSharedList} to use
 * one to read and the other to write.
 *
 * @see BodiesSharedList
 * @see Boundary
 */

public class Context {

    private final double dt;
    private final Boundary boundary;
    private BodiesSharedList writeSharedList;
    private BodiesSharedList readSharedList;

    /**
     * Create the context with boundaries and deltaT passed
     *
     * @param boundary - boundaries of two-dimensional world
     * @param dt       - the deltaT, that is quantity time added to virtual time each iteration
     */
    public Context(final Boundary boundary, final double dt) {
        this.boundary = boundary;
        this.dt = dt;
        this.restartContext();
    }

    /**
     * Restart context reinitializing lists and keepWorking flag
     */
    public void restartContext() {
        this.writeSharedList = new BodiesSharedList();
        this.readSharedList = new BodiesSharedList();
    }

    /**
     * Get boundaries of two-dimensional world
     *
     * @return - boundaries of two-dimensional world
     */
    public Boundary getBoundary() {
        return boundary;
    }

    /**
     * Get the list used to write new bodies
     *
     * @return the list used to write new bodies
     */
    public BodiesSharedList getWriteSharedList() {
        return writeSharedList;
    }

    /**
     * Get the list used to read bodies
     *
     * @return the list used to read bodies
     */
    public BodiesSharedList getReadSharedList() {
        return readSharedList;
    }

    /**
     * Get the deltaT of the simulation
     *
     * @return the deltaT, that is quantity time added to virtual time each iteration
     */
    public double getDT() {
        return this.dt;
    }
}
