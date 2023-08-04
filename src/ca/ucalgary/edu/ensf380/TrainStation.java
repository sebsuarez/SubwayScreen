package ca.ucalgary.edu.ensf380;


public class TrainStation {
    private final String stationCode;
    private final String stationName;
    private final double x;
    private final double y;

    public TrainStation(String stationCode, String stationName, double x, double y) {
        this.stationCode = stationCode;
        this.stationName = stationName;
        this.x = x;
        this.y = y;
    }


    public String getStationCode() {
        return stationCode;
    }

    public String getStationName() {
        return stationName;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
