package ca.ucalgary.edu.ensf380;

/**
 * Represents a train in the subway system.
 */
public class Train {
    private final String trainNumber;
    private final SubwayLine currentLine;
    private String direction;
    private String destination;
    private TrainStation currentStation;

    /**
     * Constructs a Train object.
     *
     * @param trainNumber  The train's identification number.
     * @param currentLine  The subway line the train belongs to.
     * @param direction    The direction the train is traveling.
     * @param destination  The train's destination.
     */
    public Train(String trainNumber, SubwayLine currentLine, String direction, String destination) {
        this.trainNumber = trainNumber;
        this.currentLine = currentLine;
        this.direction = direction;
        this.destination = destination;
    }

    /**
     * Returns the train's identification number.
     *
     * @return The train number.
     */
    public String getTrainNumber() {
        return trainNumber;
    }

    /**
     * Returns the subway line the train belongs to.
     *
     * @return The current subway line.
     */
    public SubwayLine getCurrentLine() {
        return currentLine;
    }

    /**
     * Returns the direction the train is traveling.
     *
     * @return The direction of the train.
     */
    public String getDirection() {
        return direction;
    }

    /**
     * Returns the train's destination.
     *
     * @return The destination of the train.
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Returns the current station where the train is located.
     *
     * @return The current station of the train.
     */
    public TrainStation getCurrentStation() {
        return currentStation;
    }

    /**
     * Sets the direction the train is traveling.
     *
     * @param direction The new direction of the train.
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    /**
     * Sets the destination of the train.
     *
     * @param destination The new destination of the train.
     */
    public void setDestination(String destination) {
        this.destination = destination;
    }

    /**
     * Sets the current station where the train is located.
     *
     * @param currentStation The new current station of the train.
     */
    public void setCurrentStation(TrainStation currentStation) {
        this.currentStation = currentStation;
    }

    /**
     * Returns the station code of the current station.
     *
     * @return The station code of the current station.
     */
    public String getCurrentStationCode() {
        String statCode = this.currentStation.getStationCode();
        return statCode;
    }
}
