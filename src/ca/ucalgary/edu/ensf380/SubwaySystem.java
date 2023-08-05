package ca.ucalgary.edu.ensf380;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represents the subway system with lines, stations, and trains.
 */
public class SubwaySystem {
    private static SubwayLine redLine;
    private static SubwayLine blueLine;
    private static SubwayLine greenLine;
    private static List<Train> trains = new ArrayList<>();

    /**
     * Returns the red subway line.
     *
     * @return The red subway line.
     */
    public static SubwayLine getRedLine() {
        return redLine;
    }

    /**
     * Returns the blue subway line.
     *
     * @return The blue subway line.
     */
    public static SubwayLine getBlueLine() {
        return blueLine;
    }

    /**
     * Returns the green subway line.
     *
     * @return The green subway line.
     */
    public static SubwayLine getGreenLine() {
        return greenLine;
    }

    /**
     * Returns the list of trains in the subway system.
     *
     * @return The list of trains.
     */
    public static List<Train> getTrains() {
        return trains;
    }

    /**
     * Initializes the subway system with stations, lines, and trains.
     */
    public static void getSubwaySystem() {
        List<TrainStation> stations = new ArrayList<>();
        List<SubwayLine> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader("C:\\Users\\charl\\eclipse-workspace\\SubwayScreen\\data\\subway.csv"))) {
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String stationCode = parts[3];
                String stationName = parts[4];
                double x = Double.parseDouble(parts[5]);
                double y = Double.parseDouble(parts[6]);
                TrainStation station = new TrainStation(stationCode, stationName, x, y);
                stations.add(station);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        redLine = new SubwayLine("R");
        blueLine = new SubwayLine("B");
        greenLine = new SubwayLine("G");

        for (TrainStation station : stations) {
            String lineName = station.getStationCode().substring(0, 1);
            if (lineName.equals("R")) {
                redLine.addStation(station);
            } else if (lineName.equals("B")) {
                blueLine.addStation(station);
            } else if (lineName.equals("G")) {
                greenLine.addStation(station);
            }
        }

        for (int i = 1; i <= 12; i++) {
            SubwayLine line;
            if (i <= 4) {
                line = redLine;
            } else if (i <= 8) {
                line = blueLine;
            } else {
                line = greenLine;
            }
            Train train = new Train("T" + i, line, null, null);
            trains.add(train);
        }

        lines.add(redLine);
        lines.add(blueLine);
        lines.add(greenLine);

        String folderPath = "C:\\Users\\charl\\eclipse-workspace\\SubwayScreen\\out";
        updateTrainsFromCSV(folderPath, trains);
    }

    /**
     * Updates train information from CSV files.
     *
     * @param csvFilePath Path to the folder containing CSV files.
     * @param trains List of trains to update.
     */
    private static void updateTrainsFromCSV(String csvFilePath, List<Train> trains) {
        try {
            Path folder = Paths.get(csvFilePath);
            DirectoryStream<Path> directoryStream = Files.newDirectoryStream(folder, "*.csv");
            List<Path> csvFiles = new ArrayList<>();
            for (Path file : directoryStream) {
                csvFiles.add(file);
            }

            Path mostRecentCSV = csvFiles.stream()
                .max(Comparator.comparingLong(file -> {
                    try {
                        return Files.getLastModifiedTime(file).toMillis();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return 0L;
                    }
                }))
                .orElse(null);
            
            if (mostRecentCSV != null) {
                try (BufferedReader reader = new BufferedReader(new FileReader(mostRecentCSV.toFile()))) {
                    String line;
                    reader.readLine();
                    int lineNumber = 1;
                    while ((line = reader.readLine()) != null) {
                        lineNumber++;

                        String[] parts = line.split(",");
                        if (parts.length < 5) {
                            System.err.println("Invalid CSV format on line " + lineNumber + ": " + line);
                            continue;
                        }

                        String trainNum = parts[1];
                        String stationCode = parts[2];
                        String direction = parts[3];
                        String destination = parts[4];
                        String trainNumber = "T" + trainNum;

                        Train matchingTrain = null;
                        for (Train train : trains) {
                            if (train.getTrainNumber().equals(trainNumber)) {
                                matchingTrain = train;
                                break;
                            }
                        }

                        if (matchingTrain != null) {
                            matchingTrain.setDirection(direction);
                            matchingTrain.setDestination(destination);

                            SubwayLine line1 = matchingTrain.getCurrentLine();
                            TrainStation currentStation = line1.getStationByCode(stationCode);

                            matchingTrain.setCurrentStation(currentStation);
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("No CSV files found in the folder.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

  
