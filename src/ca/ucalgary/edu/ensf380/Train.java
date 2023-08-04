package ca.ucalgary.edu.ensf380;

public class Train {
    private final String trainNumber;
    private final SubwayLine currentLine;
    private String direction;
    private String destination;
    private TrainStation currentStation;

    public Train(String trainNumber, SubwayLine currentLine, String direction, String destination) {
        this.trainNumber = trainNumber;
        this.currentLine = currentLine;
        this.direction = direction;
        this.destination = destination;
    }


    public String getTrainNumber() {
        return trainNumber;
    }

    public SubwayLine getCurrentLine() {
        return currentLine;
    }

    public String getDirection() {
        return direction;
    }

    public String getDestination() {
        return destination;
    }

    public TrainStation getCurrentStation() {
        return currentStation;
    }

    public void setDirection(String direction) {
		this.direction = direction;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public void setCurrentStation(TrainStation currentStation) {
		this.currentStation = currentStation;
	}

	public String getCurrentStationCode() {
		String statCode = this.currentStation.getStationCode();
		return statCode;
	}
}