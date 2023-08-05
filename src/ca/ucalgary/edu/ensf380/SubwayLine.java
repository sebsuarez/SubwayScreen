package ca.ucalgary.edu.ensf380;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a subway line with its stations and trains.
 */
public class SubwayLine {
    private final String lineName;
    private final List<TrainStation> stations;
    private final List<Train> trains;

    /**
     * Constructs a SubwayLine object with the given line name, stations, and trains.
     *
     * @param lineName The name of the subway line.
     * @param stations The list of stations on the subway line.
     * @param trains The list of trains on the subway line.
     */
    public SubwayLine(String lineName, List<TrainStation> stations, List<Train> trains) {
        this.lineName = lineName;
        this.stations = Collections.unmodifiableList(stations);
        this.trains = Collections.unmodifiableList(trains);
    }

    /**
     * Constructs a SubwayLine object with the given line name and stations.
     *
     * @param lineName The name of the subway line.
     * @param stations The list of stations on the subway line.
     */
    public SubwayLine(String lineName, List<TrainStation> stations) {
        this.lineName = lineName;
        this.stations = Collections.unmodifiableList(stations);
        this.trains = null;
    }

    /**
     * Constructs a SubwayLine object with the given line name and no stations or trains.
     *
     * @param lineName The name of the subway line.
     */
    public SubwayLine(String lineName) {
        this.lineName = lineName;
        this.stations = new ArrayList<>();
        this.trains = null;
    }

    /**
     * Retrieves the name of the subway line.
     *
     * @return The name of the subway line.
     */
    public String getLineName() {
        return lineName;
    }

    /**
     * Retrieves the list of stations on the subway line.
     *
     * @return The list of stations on the subway line.
     */
    public List<TrainStation> getStations() {
        return stations;
    }

    /**
     * Retrieves the list of trains on the subway line.
     *
     * @return The list of trains on the subway line.
     */
    public List<Train> getTrains() {
        return trains;
    }

    /**
     * Adds a station to the list of stations on the subway line.
     *
     * @param station The TrainStation object representing the station to be added.
     */
    public void addStation(TrainStation station) {
        this.stations.add(station);
    }

    /**
     * Retrieves a TrainStation object by its station code.
     *
     * @param stationCode The station code to search for.
     * @return The matching TrainStation object, or null if not found.
     */
    public TrainStation getStationByCode(String stationCode) {
        TrainStation matchingTrainStation = null;
        for (TrainStation station : stations) {
            if (station.getStationCode().equals(stationCode)) {
                matchingTrainStation = station;
                break;
            }
        }
        return matchingTrainStation;
    }
}

	