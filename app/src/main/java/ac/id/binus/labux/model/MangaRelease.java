package ac.id.binus.labux.model;

public class MangaRelease {
    private String title;
    private String description;
    private String chapter;
    private String imageResource;

    public MangaRelease(String title, String description, String chapter, String imageResource) {
        this.title = title;
        this.description = description;
        this.chapter = chapter;
        this.imageResource = imageResource;
    }

    // Getters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getChapter() { return chapter; }
    public String getImageResource() { return imageResource; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setChapter(String chapter) { this.chapter = chapter; }
    public void setImageResource(String imageResource) { this.imageResource = imageResource; }
}
