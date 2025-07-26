package ac.id.binus.labux.model;

public class Anime {
    private String title;
    private String genre;
    private String imageResource;
    private String wideImageResource;
    private float rating;
    private int reviewsCount;
    private int watchingCount;
    private int episodesCount;

    public Anime(String title, String genre, String imageResource, String wideImageResource) {
        this.title = title;
        this.genre = genre;
        this.imageResource = imageResource;
        this.wideImageResource = wideImageResource;
        // Default values
        this.rating = 4.5f;
        this.reviewsCount = 1200;
        this.watchingCount = 3200;
        this.episodesCount = 25;
    }

    public Anime(String title, String genre, String imageResource, String wideImageResource,
                 float rating, int reviewsCount, int watchingCount, int episodesCount) {
        this.title = title;
        this.genre = genre;
        this.imageResource = imageResource;
        this.wideImageResource = wideImageResource;
        this.rating = rating;
        this.reviewsCount = reviewsCount;
        this.watchingCount = watchingCount;
        this.episodesCount = episodesCount;
    }

    // Getters
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getImageResource() { return imageResource; }
    public String getWideImageResource() { return wideImageResource; }
    public float getRating() { return rating; }
    public int getReviewsCount() { return reviewsCount; }
    public int getWatchingCount() { return watchingCount; }
    public int getEpisodesCount() { return episodesCount; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setImageResource(String imageResource) { this.imageResource = imageResource; }
    public void setWideImageResource(String wideImageResource) { this.wideImageResource = wideImageResource; }
    public void setRating(float rating) { this.rating = rating; }
    public void setReviewsCount(int reviewsCount) { this.reviewsCount = reviewsCount; }
    public void setWatchingCount(int watchingCount) { this.watchingCount = watchingCount; }
    public void setEpisodesCount(int episodesCount) { this.episodesCount = episodesCount; }
}
