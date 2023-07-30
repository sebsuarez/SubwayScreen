package ca.ucalgary.edu.ensf380;

public class NewsItem {
    private String title;
    private String sourceName;

    public NewsItem(String title, String sourceName) {
        this.title = title;
        this.sourceName = sourceName;
    }

    public String getTitle() {
        return title;
    }

    public String getSourceName() {
        return sourceName;
    }
}


