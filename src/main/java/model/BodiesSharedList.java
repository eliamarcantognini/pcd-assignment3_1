package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Class used to share list of bodies
 */
public class BodiesSharedList {

    private List<Body> updatedBodies;

    /**
     * Create an empty list of bodies
     */
    public BodiesSharedList() {
        updatedBodies = new ArrayList<>();
    }

    /**
     * Get the list of bodies of this shared list
     *
     * @return list of bodies of this shared list
     */
    public List<Body> getBodies() {
        return new ArrayList<>(updatedBodies);
    }

    /**
     * Update the body inside list, using body id to find it
     *
     * @param body the body to update inside the list
     */
    public void updateBody(Body body) {
        updatedBodies.set(body.getId(), body);
    }

    /**
     * Insert all bodies in the list contained in this BodiesSharedList
     *
     * @param collection a collection of bodies inserted inside the list
     */
    public void addBodies(Collection<Body> collection) {
        updatedBodies.addAll(collection);
    }

    /**
     * Reset the list of bodies
     */
    public void reset() {
        this.updatedBodies = new ArrayList<>();
    }
}
