package model;


/**
 * This class represents a body
 */
public class Body {

    private static final double REPULSIVE_CONST = 0.01;
    private static final double FRICTION_CONST = 1;

    private final P2d pos;
    private final V2d vel;
    private final double mass;
    private final int id;


    public Body(int id, P2d pos, V2d vel, double mass) {
        this.id = id;
        this.pos = pos;
        this.vel = vel;
        this.mass = mass;
    }

    public Body(Body b) {
        this.id = b.getId();
        this.pos = new P2d(b.getPos());
        this.vel = new V2d(b.getVel());
        this.mass = b.getMass();
    }

    public double getMass() {
        return this.mass;
    }

    public P2d getPos() {
        return this.pos;
    }

    public V2d getVel() {
        return this.vel;
    }

    public int getId() {
        return this.id;
    }

    public boolean equals(Object b) {
        return ((Body) b).id == id;
    }


    /**
     * Update the position, according to current velocity
     *
     * @param dt time elapsed
     */
    public void updatePos(double dt) {
        pos.sum(new V2d(vel).scalarMul(dt));
    }

    /**
     * Update the velocity, given the instant acceleration
     *
     * @param acc instant acceleration
     * @param dt  time elapsed
     */
    public void updateVelocity(V2d acc, double dt) {
        vel.sum(new V2d(acc).scalarMul(dt));
    }

    /**
     * Change the velocity
     *
     * @param vx v
     * @param vy vy
     */
    public void changeVel(double vx, double vy) {
        vel.change(vx, vy);
    }

    /**
     * Computes the distance from the specified body
     *
     * @param b body
     * @return distance
     */
    public double getDistanceFrom(Body b) {
        double dx = pos.getX() - b.getPos().getX();
        double dy = pos.getY() - b.getPos().getY();
        return Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * Compute the repulsive force exerted by another body
     *
     * @param b body
     * @return V2d
     * @throws InfiniteForceException infiniteForce
     */
    public V2d computeRepulsiveForceBy(Body b) throws InfiniteForceException {
        double dist = getDistanceFrom(b);
        if (dist > 0) {
            try {
                return new V2d(b.getPos(), pos)
                        .normalize()
                        .scalarMul(b.getMass() * REPULSIVE_CONST / (dist * dist));
            } catch (Exception ex) {
                throw new InfiniteForceException();
            }
        } else {
            throw new InfiniteForceException();
        }
    }

    /**
     * Compute current friction force, given the current velocity
     */
    public V2d getCurrentFrictionForce() {
        return new V2d(vel).scalarMul(-FRICTION_CONST);
    }

    /**
     * Check if there are collisions with the boundary and update the
     * position and velocity accordingly
     *
     * @param bounds boundaries
     */
    public void checkAndSolveBoundaryCollision(Boundary bounds) {
        double x = pos.getX();
        double y = pos.getY();
        if (x > bounds.getX1()) {
            pos.change(bounds.getX1(), pos.getY());
            vel.change(-vel.getX(), vel.getY());
        } else if (x < bounds.getX0()) {
            pos.change(bounds.getX0(), pos.getY());
            vel.change(-vel.getX(), vel.getY());
        } else if (y > bounds.getY1()) {
            pos.change(pos.getX(), bounds.getY1());
            vel.change(vel.getX(), -vel.getY());
        } else if (y < bounds.getY0()) {
            pos.change(pos.getX(), bounds.getY0());
            vel.change(vel.getX(), -vel.getY());
        }
    }

    @Override
    public String toString() {
        return "Body ID: " + this.id +
                " {" +
                "pos=" + pos +
                ", vel=" + vel +
                ", mass=" + mass +
                ", id=" + id +
                '}';
    }

}
