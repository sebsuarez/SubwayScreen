package ca.ucalgary.edu.ensf380;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Timer;


/**
 * Represents a graphical user interface for displaying subway information.
 */
public class SubwayScreen {
    private static List<NewsItem> news;
    private static WeatherInstance currentWeather;
    private static List<Train> trains;
    private static Train selectedTrain;
    private static int currentNewsIndex = 0;

    /**
     * The main entry point of the program.
     *
     * @param args Command-line arguments containing city code and keyword.
     */
    public static void main(String[] args) {
    	
    	startSimulator(args[0], args[1], args[2], args[3], args[4], args[5], args[6]);
    	
        SwingUtilities.invokeLater(() -> createAndShowGUI());

        String cityCode = (String)args[7];
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> updateData(args[8], cityCode, args[9]), 0, 15, TimeUnit.SECONDS);

        ScheduledExecutorService newsScheduler = Executors.newScheduledThreadPool(1);
        newsScheduler.scheduleAtFixedRate(SubwayScreen::updateNews, 0, 7500, TimeUnit.MILLISECONDS);
    }

    /**
     * Updates the data including news, weather, and subway information.
     *
     * @param keyword  Keyword for news retrieval.
     * @param cityCode City code for weather retrieval.
     */
    private static void updateData(String keyword, String cityCode, String trainNum) {
        try {
            news = NewsService.getNews(keyword);
        } catch (Exception e) {
            e.printStackTrace();
            news = null;
        }

        try {
            currentWeather = WeatherService.getCurrentWeather(cityCode);
        } catch (Exception e) {
            e.printStackTrace();
            currentWeather = null;
        }

        SubwaySystem.getSubwaySystem();
        trains = SubwaySystem.getTrains();

        Train matchingTrain = null;
        for (Train train : trains) {
            if (train.getTrainNumber().equals(trainNum)) {
                matchingTrain = train;
                break;
            }
        }
        selectedTrain = matchingTrain;
    }

    /**
     * Updates the currently displayed news.
     */
    private static void updateNews() {
        if (news != null && news.size() > 1) {
            currentNewsIndex = (currentNewsIndex + 1) % news.size();
        }
    }

    /**
     * Creates and shows the graphical user interface (GUI) for the subway screen.
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Subway Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());

        JPanel mapPanel = new JPanel();
        mapPanel.setBackground(Color.WHITE);

        BufferedImage trainSystem = null;
		try {
			trainSystem = ImageIO.read(new File("C:\\Users\\charl\\eclipse-workspace\\SubwayScreen\\src\\ca\\ucalgary\\edu\\ensf380\\Trains.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
        JLabel picLabel = new JLabel(new ImageIcon(trainSystem));
        mapPanel.add(picLabel);
        topPanel.add(mapPanel, BorderLayout.WEST);

        JPanel weatherPanel = new JPanel(new BorderLayout());
        weatherPanel.setBackground(Color.WHITE);
        JLabel dateTimeWeatherLabel = new JLabel("Date, Time, and Weather", SwingConstants.RIGHT);
        dateTimeWeatherLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        weatherPanel.add(dateTimeWeatherLabel, BorderLayout.CENTER);
        weatherPanel.setPreferredSize(new Dimension((int)(frame.getWidth() * 0.3), frame.getHeight()));
        topPanel.add(weatherPanel, BorderLayout.CENTER);

        contentPane.add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));

        JPanel newsPanel = new JPanel(new BorderLayout());
        newsPanel.setBackground(Color.WHITE);
        JTextArea newsTextArea = new JTextArea(4, 40);
        newsTextArea.setFont(new Font("Helvetica", Font.PLAIN, 14));
        newsPanel.add(new JScrollPane(newsTextArea), BorderLayout.CENTER);
        bottomPanel.add(newsPanel);

        JPanel trainStationsPanel = new JPanel(new BorderLayout());
        trainStationsPanel.setBackground(Color.WHITE);
        JTextArea trainStationsTextArea = new JTextArea(2, 40);
        trainStationsTextArea.setFont(new Font("Helvetica", Font.PLAIN, 14));
        trainStationsPanel.add(new JScrollPane(trainStationsTextArea), BorderLayout.CENTER);
        bottomPanel.add(trainStationsPanel);

        contentPane.add(bottomPanel, BorderLayout.SOUTH);

        frame.setContentPane(contentPane);
        frame.setVisible(true);

        ScheduledExecutorService guiUpdater = Executors.newScheduledThreadPool(1);
        guiUpdater.scheduleAtFixedRate(() -> updateGUI(dateTimeWeatherLabel, newsTextArea, trainStationsTextArea), 0, 1, TimeUnit.SECONDS);
    }




    /**
     * Updates the GUI components with the latest data.
     *
     * @param dateTimeWeatherLabel Label for date, time, and weather.
     * @param newsTextArea         TextArea for news display.
     * @param trainInfoTextArea    TextArea for train information display.
     */
    private static void updateGUI(JLabel dateTimeWeatherLabel, JTextArea newsTextArea, JTextArea trainInfoTextArea) {
        if (currentWeather != null) {
            String weatherText = "<html>" +
                    "Temperature: " + currentWeather.getTemperature() + " °C<br>" +
                    "Date and Time: " + currentWeather.getFormattedDateTime() + "<br>" +
                    "Conditions: " + currentWeather.getWeatherCondition() + "<br>" +
                    "Wind Speed: " + String.format("%.2f", currentWeather.getWindSpeed()) + " km/h<br>" +
                    "Humidity: " + currentWeather.getHumidity() + "%" +
                    "</html>";
            dateTimeWeatherLabel.setText(weatherText);
        }

        if (news != null) {
            NewsItem currentNewsItem = news.get(currentNewsIndex);
            String newsText = "Title: " + currentNewsItem.getTitle() + "\nSource: " + currentNewsItem.getSourceName();
            newsTextArea.setText(newsText);
        }

        if (trains != null && selectedTrain != null) {
            StringBuilder trainInfoText = new StringBuilder();
            TrainStation currentStation = selectedTrain.getCurrentStation();
            int currentIndex = selectedTrain.getCurrentLine().getStations().indexOf(currentStation);

            if (currentIndex > 0) {
                trainInfoText.append(selectedTrain.getCurrentLine().getStations().get(currentIndex - 1).getStationName());
            } else {
                trainInfoText.append("Past Station: N/A\n");
            }
            
            trainInfoText.append("➔ ");

            String currStat = currentStation.getStationName();
            currStat = currStat.toUpperCase();
            trainInfoText.append(currStat);

            trainInfoText.append("➔ ");

            // Display the next stations
            for (int i = currentIndex + 1; i < currentIndex + 4 && i < selectedTrain.getCurrentLine().getStations().size(); i++) {
                trainInfoText.append(selectedTrain.getCurrentLine().getStations().get(i).getStationName()).append(" ");
            }

            trainInfoTextArea.setText(trainInfoText.toString());
        }
    }
    
    
    /**
     * Starts the subway simulator process and manages its lifecycle.
     * The simulator process is terminated either when the program exits
     * or after a specified time duration.
     */
    private static void startSimulator(String arg0, String arg1, String arg2, String arg3, String arg4, String arg5, String arg6) {
        Process process = null;
        try {
            String[] command = {arg0, arg1, arg2, arg3, arg4, arg5, arg6};
            process = new ProcessBuilder(command).start();
        } catch (IOException e) {
            System.err.println(e);
            e.printStackTrace();
        }

        final Process finalProcess = process;
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if (finalProcess != null) {
                finalProcess.destroy();
            }
        }));

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (finalProcess != null) {
                    finalProcess.destroy();
                }
                timer.cancel();
            }
        }, 5 * 60 * 1000);
    }
}

