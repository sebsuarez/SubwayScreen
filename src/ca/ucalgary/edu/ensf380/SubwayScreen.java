package ca.ucalgary.edu.ensf380;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
        SwingUtilities.invokeLater(() -> createAndShowGUI());

        String cityCode = (String)args[0];
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> updateData(args[1], cityCode), 0, 15, TimeUnit.SECONDS);

        ScheduledExecutorService newsScheduler = Executors.newScheduledThreadPool(1);
        newsScheduler.scheduleAtFixedRate(SubwayScreen::updateNews, 0, 7500, TimeUnit.MILLISECONDS);
    }

    /**
     * Updates the data including news, weather, and subway information.
     *
     * @param keyword  Keyword for news retrieval.
     * @param cityCode City code for weather retrieval.
     */
    private static void updateData(String keyword, String cityCode) {
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
            if (train.getTrainNumber().equals("T6")) {
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
        mapPanel.setBackground(Color.GRAY);
        mapPanel.setPreferredSize(new Dimension((int)(frame.getWidth() * 0.7), frame.getHeight()));
        topPanel.add(mapPanel, BorderLayout.WEST);

        JPanel weatherPanel = new JPanel(new BorderLayout());
        weatherPanel.setBackground(Color.WHITE);
        JLabel dateTimeWeatherLabel = new JLabel("Date, Time, and Weather", SwingConstants.RIGHT);
        dateTimeWeatherLabel.setFont(new Font("Arial", Font.BOLD, 16));
        weatherPanel.add(dateTimeWeatherLabel, BorderLayout.CENTER);
        weatherPanel.setPreferredSize(new Dimension((int)(frame.getWidth() * 0.3), frame.getHeight()));
        topPanel.add(weatherPanel, BorderLayout.CENTER);

        contentPane.add(topPanel, BorderLayout.NORTH);

        JPanel bottomPanel = new JPanel(new GridLayout(2, 1));

        JPanel newsPanel = new JPanel(new BorderLayout());
        newsPanel.setBackground(Color.LIGHT_GRAY);
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

        contentPane.add(bottomPanel, BorderLayout.SOUTH); // Placing it at the bottom

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

            // Bold formatting for the current station
            String currStat = currentStation.getStationName();
            currStat = currStat.toUpperCase();
            trainInfoText.append(currStat);

            // Arrow symbol for indicating direction
            trainInfoText.append("➔ ");

            // Display the next stations
            for (int i = currentIndex + 1; i < currentIndex + 4 && i < selectedTrain.getCurrentLine().getStations().size(); i++) {
                trainInfoText.append(selectedTrain.getCurrentLine().getStations().get(i).getStationName()).append(" ");
            }

            trainInfoTextArea.setText(trainInfoText.toString());
        }
    }

}

