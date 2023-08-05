package ca.ucalgary.edu.ensf380;

/**
 * Represents a news item with a title and source name.
 */
public class NewsItem {
    private String title;
    private String sourceName;

    /**
     * Constructs a NewsItem with the provided title and source name.
     *
     * @param title      The title of the news item.
     * @param sourceName The name of the news source.
     */
    public NewsItem(String title, String sourceName) {
        this.title = title;
        this.sourceName = sourceName;
    }

    /**
     * Returns the title of the news item.
     *
     * @return The title of the news item.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the name of the news source.
     *
     * @return The name of the news source.
     */
    public String getSourceName() {
        return sourceName;
    }
}


