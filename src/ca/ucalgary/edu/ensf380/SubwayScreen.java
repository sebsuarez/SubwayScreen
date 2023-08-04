package ca.ucalgary.edu.ensf380;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SubwayScreen {
    private static List<NewsItem> news;
    private static WeatherInstance currentWeather;
    private static List<Train> trains;
    private static Train selectedTrain;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());

        String cityCode = (String)args[0];
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> updateData(args[1], cityCode), 0, 15, TimeUnit.SECONDS);

        ScheduledExecutorService newsScheduler = Executors.newScheduledThreadPool(1);
        newsScheduler.scheduleAtFixedRate(SubwayScreen::updateNews, 0, 7500, TimeUnit.MILLISECONDS);
    }

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

    private static void updateNews() {
        if (news != null && news.size() > 1) {
            NewsItem firstNewsItem = news.remove(0);
            news.add(firstNewsItem);
        }
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Subway Screen");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        JPanel contentPane = new JPanel();
        contentPane.setLayout(new GridLayout(3, 1));

        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        JLabel blankLabel = new JLabel(" ");
        topPanel.add(blankLabel);

        JLabel dateTimeWeatherLabel = new JLabel("Date, Time, and Weather", SwingConstants.RIGHT);
        topPanel.add(dateTimeWeatherLabel);
        contentPane.add(topPanel);

        JTextArea newsTextArea = new JTextArea(10, 40);
        contentPane.add(new JScrollPane(newsTextArea));

        JPanel trainInfoPanel = new JPanel();
        trainInfoPanel.setLayout(new BoxLayout(trainInfoPanel, BoxLayout.Y_AXIS));
        contentPane.add(trainInfoPanel);

        frame.setContentPane(contentPane);
        frame.setVisible(true);

        ScheduledExecutorService guiUpdater = Executors.newScheduledThreadPool(1);
        guiUpdater.scheduleAtFixedRate(() -> updateGUI(dateTimeWeatherLabel, newsTextArea, trainInfoPanel), 0, 1, TimeUnit.SECONDS);
    }

    private static void updateGUI(JLabel dateTimeWeatherLabel, JTextArea newsTextArea, JPanel trainInfoPanel) {
        if (currentWeather != null) {
            String weatherText = "Temperature: " + currentWeather.getTemperature() + " °C, Date and Time: " + currentWeather.getFormattedDateTime();
            dateTimeWeatherLabel.setText(weatherText);
        }

        if (news != null) {
            StringBuilder newsText = new StringBuilder();
            for (NewsItem newsItem : news) {
                newsText.append("Title: ").append(newsItem.getTitle()).append("\n");
                newsText.append("Source: ").append(newsItem.getSourceName()).append("\n\n");
            }
            newsTextArea.setText(newsText.toString());
        }

        if (trains != null && selectedTrain != null) {
            StringBuilder trainInfoText = new StringBuilder();
            TrainStation currentStation = selectedTrain.getCurrentStation();
            int currentIndex = selectedTrain.getCurrentLine().getStations().indexOf(currentStation);

            trainInfoText.append("Past Station: ");
            if (currentIndex > 0) {
                trainInfoText.append(selectedTrain.getCurrentLine().getStations().get(currentIndex - 1).getStationName()).append("\n");
            } else {
                trainInfoText.append("N/A\n");
            }

            trainInfoText.append("Current Station: ").append(currentStation.getStationName()).append("\n");

            trainInfoText.append("Next Stations:\n");
            for (int i = currentIndex + 1; i < currentIndex + 4 && i < selectedTrain.getCurrentLine().getStations().size(); i++) {
                trainInfoText.append(selectedTrain.getCurrentLine().getStations().get(i).getStationName()).append("\n");
            }

            trainInfoPanel.removeAll();
            JLabel trainInfoLabel = new JLabel(trainInfoText.toString());
            trainInfoPanel.add(trainInfoLabel);
            trainInfoPanel.revalidate();
            trainInfoPanel.repaint();
        }
    }
}
