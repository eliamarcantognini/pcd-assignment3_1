package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * Class to manage configuration properties of the simulation. It can read from file data for application and allows to
 * use getters for obtain data.
 */
public class Configuration {

    private static final String ITERATIONS_QUANTITY_FIELD_NAME = "iterations_initial_quantity";
    private static final String BODIES_QUANTITY_FIELD_NAME = "bodies_initial_quantity";
    private static final String DT_FIELD_NAME = "delta_incremental_time";
    private static final String UPPER_BOUNDARY_FIELD_NAME = "upper_boundary";
    private static final String LOWER_BOUNDARY_FIELD_NAME = "lower_boundary";
    private static final String RIGHTER_BOUNDARY_FIELD_NAME = "righter_boundary";
    private static final String LEFTER_BOUNDARY_FIELD_NAME = "lefter_boundary";

    private final Properties properties;

    /**
     * Create a new Configuration instance that contains information about simulation. Particularly, deltaT, bodies quantity,
     * iterations quantity and four boundaries of two-dimensional world. Information are searched in file with filename passed.
     * The file can be in two location: <pre> src/main/resources </pre>
     * or the same folder where the simulation is launched (suggested if
     * .jar file is used).
     * If file is not found in both path, it throws a {@link FileNotFoundException}
     *
     * @param fileName - name of file that contains information for simulation
     * @throws FileNotFoundException - if file is not found
     */
    public Configuration(final String fileName) throws FileNotFoundException {
        this.properties = new Properties();
        String pathForProject = new File("src/main/resources/" + fileName)
                .getAbsolutePath();
        File configurationFile = new File(pathForProject);
        File configurationFileJar = new File(fileName);
        FileInputStream inputStream;
        if (configurationFile.exists()) {
            inputStream = new FileInputStream(configurationFile);
        } else {
            inputStream = new FileInputStream(configurationFileJar);
        }

        try {
            this.properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the deltaT of the simulation
     *
     * @return deltaT, that is quantity time added to virtual time each iteration
     */
    public double getDT() {
        return Double.parseDouble(this.properties.getProperty(Configuration.DT_FIELD_NAME));
    }

    /**
     * Get number of bodies for the simulation
     *
     * @return the number of bodies for the simulation
     */
    public int getBodiesQuantity() {
        return Integer.parseInt(this.properties.getProperty(Configuration.BODIES_QUANTITY_FIELD_NAME));
    }


    /**
     * Get the number of iterations for simulation
     *
     * @return the number of total iteration that simulation should do
     */
    public int getIterationsQuantity() {
        return Integer.parseInt(this.properties.getProperty(Configuration.ITERATIONS_QUANTITY_FIELD_NAME));
    }

    /**
     * Get the upper boundary of two-dimensional world of simulation
     *
     * @return the upper boundary of two-dimensional world of simulation
     */
    public int getUpperBoundary() {
        return Integer.parseInt(this.properties.getProperty(Configuration.UPPER_BOUNDARY_FIELD_NAME));
    }

    /**
     * Get the lower boundary of two-dimensional world of simulation
     *
     * @return the lower boundary of two-dimensional world of simulation
     */
    public int getLowerBoundary() {
        return Integer.parseInt(this.properties.getProperty(Configuration.LOWER_BOUNDARY_FIELD_NAME));
    }

    /**
     * Get the righter boundary of two-dimensional world of simulation
     *
     * @return the righter boundary of two-dimensional world of simulation
     */
    public int getRighterBoundary() {
        return Integer.parseInt(this.properties.getProperty(Configuration.RIGHTER_BOUNDARY_FIELD_NAME));
    }

    /**
     * Get the lefter boundary of two-dimensional world of simulation
     *
     * @return the lefter boundary of two-dimensional world of simulation
     */
    public int getLefterBoundary() {
        return Integer.parseInt(this.properties.getProperty(Configuration.LEFTER_BOUNDARY_FIELD_NAME));
    }

}
