package ac.id.binus.labux.model;

public class News {
    private String title;
    private String description;
    private String date;
    private String imageResource;

    public News(String title, String description, String date, String imageResource) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.imageResource = imageResource;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getDate() { return date; }
    public String getImageResource() { return imageResource; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setDate(String date) { this.date = date; }
    public void setImageResource(String imageResource) { this.imageResource = imageResource; }
}
