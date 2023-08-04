package ca.ucalgary.edu.ensf380;

import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class SubwayLine {
    private final String lineName;
    private final List<TrainStation> stations;
    private final List<Train> trains;

    public SubwayLine(String lineName, List<TrainStation> stations, List<Train> trains) {
        this.lineName = lineName;
        this.stations = Collections.unmodifiableList(stations);
        this.trains = Collections.unmodifiableList(trains);
    }

    public SubwayLine(String lineName, List<TrainStation> stations) {
    	this.lineName = lineName;
        this.stations = Collections.unmodifiableList(stations);
		this.trains = null;
        
	}

	public SubwayLine(String lineName) {
    	this.lineName = lineName;
		this.stations = new ArrayList<>();
		this.trains = null;	
	}

	public String getLineName() {
        return lineName;
    }

    public List<TrainStation> getStations() {
        return stations;
    }

    public List<Train> getTrains() {
        return trains;
    }

	public void addStation(TrainStation station) {
		this.stations.add(station);
		
	}

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
	