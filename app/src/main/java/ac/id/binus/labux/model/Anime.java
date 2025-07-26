package ac.id.binus.labux.model;

public class Anime {
    private String title;
    private String genre;
    private String imageResource;
    private String wideImageResource;

    public Anime(String title, String genre, String imageResource, String wideImageResource) {
        this.title = title;
        this.genre = genre;
        this.imageResource = imageResource;
        this.wideImageResource = wideImageResource;
    }

    // Getters
    public String getTitle() { return title; }
    public String getGenre() { return genre; }
    public String getImageResource() { return imageResource; }
    public String getWideImageResource() { return wideImageResource; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setGenre(String genre) { this.genre = genre; }
    public void setImageResource(String imageResource) { this.imageResource = imageResource; }
    public void setWideImageResource(String wideImageResource) { this.wideImageResource = wideImageResource; }
}
