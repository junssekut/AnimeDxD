package ac.id.binus.labux.model;

public class MangaUpdate {
    private String title;
    private String author;
    private String description;
    private String genres;
    private float rating;
    private String chapter;
    private String updateDate;
    private String imageResource;

    public MangaUpdate(String title, String author, String description, String genres,
                      float rating, String chapter, String updateDate, String imageResource) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.genres = genres;
        this.rating = rating;
        this.chapter = chapter;
        this.updateDate = updateDate;
        this.imageResource = imageResource;
    }

    // Simple constructor for backward compatibility
    public MangaUpdate(String title, String updateDate, String imageResource, String chapter) {
        this.title = title;
        this.updateDate = updateDate;
        this.imageResource = imageResource;
        this.chapter = chapter;
        // Set default values for other attributes
        this.author = "";
        this.description = "";
        this.genres = "";
        this.rating = 5.3f;
    }

    // Getters
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getDescription() { return description; }
    public String getGenres() { return genres; }
    public float getRating() { return rating; }
    public String getChapter() { return chapter; }
    public String getUpdateDate() { return updateDate; }
    public String getImageResource() { return imageResource; }

    // Alias method for backward compatibility
    public String getStatus() { return chapter; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setDescription(String description) { this.description = description; }
    public void setGenres(String genres) { this.genres = genres; }
    public void setRating(float rating) { this.rating = rating; }
    public void setChapter(String chapter) { this.chapter = chapter; }
    public void setUpdateDate(String updateDate) { this.updateDate = updateDate; }
    public void setImageResource(String imageResource) { this.imageResource = imageResource; }
}
