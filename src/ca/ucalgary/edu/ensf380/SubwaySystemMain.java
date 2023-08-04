package ca.ucalgary.edu.ensf380;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class SubwaySystemMain {
    public static void main(String[] args) {
        List<TrainStation> stations = new ArrayList<>();
        List<SubwayLine> lines = new ArrayList<>();
        List<Train> trains = new ArrayList<>();

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

        SubwayLine redLine = new SubwayLine("R");
        SubwayLine blueLine = new SubwayLine("B");
        SubwayLine greenLine = new SubwayLine("G");

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
        WatchService watchService;
        try {
            watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get(folderPath);
            path.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        while (true) {
            WatchKey key;
            try {
                key = watchService.poll(15, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
                break;
            }

            if (key != null) {
                for (WatchEvent<?> event : key.pollEvents()) {
                    Path fileName = (Path) event.context();
                    if (fileName.toString().endsWith(".csv")) {
                        String csvFilePath = folderPath + "\\" + fileName;
                        updateTrainsFromCSV(csvFilePath, trains);
                        
                        System.out.println("Updated train information:");
                        for (Train train : trains) {
                            System.out.println("Train: " + train.getTrainNumber());
                            System.out.println("  Line: " + train.getCurrentLine().getLineName());
                            System.out.println("  Station: " + train.getCurrentStation().getStationName());
                            System.out.println("  Direction: " + train.getDirection());
                            System.out.println("  Destination: " + train.getDestination());
                        }
                        System.out.println("=================================");
                    }
                }
                key.reset();
            }
        }
    }

    public static void updateTrainsFromCSV(String csvFilePath, List<Train> trains) {
        try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
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
    }


}
  
