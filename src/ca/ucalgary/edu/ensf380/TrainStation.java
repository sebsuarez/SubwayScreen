package ca.ucalgary.edu.ensf380;

/**
 * Represents a train station in the subway system.
 */
public class TrainStation {
    private final String stationCode;
    private final String stationName;
    private final double x;
    private final double y;

    /**
     * Constructs a TrainStation object.
     *
     * @param stationCode  The code representing the station.
     * @param stationName  The name of the station.
     * @param x            The x-coordinate of the station's location.
     * @param y            The y-coordinate of the station's location.
     */
    public TrainStation(String stationCode, String stationName, double x, double y) {
        this.stationCode = stationCode;
        this.stationName = stationName;
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the code representing the station.
     *
     * @return The station code.
     */
    public String getStationCode() {
        return stationCode;
    }

    /**
     * Returns the name of the station.
     *
     * @return The station name.
     */
    public String getStationName() {
        return stationName;
    }

    /**
     * Returns the x-coordinate of the station's location.
     *
     * @return The x-coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the station's location.
     *
     * @return The y-coordinate.
     */
    public double getY() {
        return y;
    }
}
