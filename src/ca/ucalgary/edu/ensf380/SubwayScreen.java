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
     * Creates and displays the graphical user interface.
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Subway Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(4, 1));

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        JLabel dateTimeWeatherLabel = new JLabel("Date, Time, and Weather", SwingConstants.RIGHT);
        dateTimeWeatherLabel.setFont(new Font("Arial", Font.BOLD, 16));
        topPanel.add(dateTimeWeatherLabel, BorderLayout.CENTER);
        contentPane.add(topPanel);

        JPanel blankPanel = new JPanel();
        blankPanel.setBackground(Color.LIGHT_GRAY);
        contentPane.add(blankPanel);

        JPanel newsPanel = new JPanel(new BorderLayout());
        newsPanel.setBackground(Color.WHITE);
        JTextArea newsTextArea = new JTextArea(4, 40);
        newsTextArea.setFont(new Font("Helvetica", Font.PLAIN, 14));
        newsPanel.add(new JScrollPane(newsTextArea), BorderLayout.CENTER);
        contentPane.add(newsPanel);

        JPanel trainInfoPanel = new JPanel(new BorderLayout());
        trainInfoPanel.setBackground(Color.WHITE);
        JTextArea trainInfoTextArea = new JTextArea(2, 40);
        trainInfoTextArea.setFont(new Font("Helvetica", Font.PLAIN, 14));
        trainInfoPanel.add(new JScrollPane(trainInfoTextArea), BorderLayout.CENTER);
        contentPane.add(trainInfoPanel);

        frame.setContentPane(contentPane);
        frame.setVisible(true);

        ScheduledExecutorService guiUpdater = Executors.newScheduledThreadPool(1);
        guiUpdater.scheduleAtFixedRate(() -> updateGUI(dateTimeWeatherLabel, newsTextArea, trainInfoTextArea), 0, 1, TimeUnit.SECONDS);
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
            String weatherText = "Temperature: " + currentWeather.getTemperature() + " °C, Date and Time: " + currentWeather.getFormattedDateTime();
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
                trainInfoText.append("Past Station: ").append(selectedTrain.getCurrentLine().getStations().get(currentIndex - 1).getStationName()).append("\n");
            } else {
                trainInfoText.append("Past Station: N/A\n");
            }

            trainInfoText.append("Current Station: ").append(currentStation.getStationName()).append("\n");

            trainInfoText.append("Next Stations:\n");
            for (int i = currentIndex + 1; i < currentIndex + 4 && i < selectedTrain.getCurrentLine().getStations().size(); i++) {
                trainInfoText.append(selectedTrain.getCurrentLine().getStations().get(i).getStationName()).append("\n");
            }

            trainInfoTextArea.setText(trainInfoText.toString());
        }
    }
}

