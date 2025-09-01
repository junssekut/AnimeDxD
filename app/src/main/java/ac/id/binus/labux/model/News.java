package ac.id.binus.labux.model;

public class News {
    private String title;
    private String description;
    private String date;
    private String imageResource;
    private String synopsis;
    private String genre;
    private Float rating;

    public News(String title, String description, String date, String imageResource, String synopsis, String genre, Float rating) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.imageResource = imageResource;
        this.synopsis = synopsis;
        this.genre = genre;
        this.rating = rating;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getImageResource() { return imageResource; }
    public String getSynopsis() { return synopsis; }
    public String getGenre() { return genre; }
    public Float getRating() {return rating;}

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(String date) { this.date = date; }
    public void setImageResource(String imageResource) { this.imageResource = imageResource; }
    public void setSynopsis(String synopsis) { this.synopsis = synopsis; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setRating(Float rating) { this.rating = rating; }
}
