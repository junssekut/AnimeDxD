package ac.id.binus.labux.model;

public class Review {
    private String title;
    private String subtitle;
    private String description;
    private int rating;
    private String imageResource;

    public Review(String title, String subtitle, String description, int rating, String imageResource) {
        this.title = title;
        this.subtitle = subtitle;
        this.description = description;
        this.rating = rating;
        this.imageResource = imageResource;
    }

    // Getters
    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
    public String getDescription() { return description; }
    public int getRating() { return rating; }
    public String getImageResource() { return imageResource; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setSubtitle(String subtitle) { this.subtitle = subtitle; }
    public void setDescription(String description) { this.description = description; }
    public void setRating(int rating) { this.rating = rating; }
    public void setImageResource(String imageResource) { this.imageResource = imageResource; }
}
