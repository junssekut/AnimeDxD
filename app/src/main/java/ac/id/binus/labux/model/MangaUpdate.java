package ac.id.binus.labux.model;

public class MangaUpdate {
    private String title;
    private String updateDate;
    private String imageResource;
    private String status; // e.g., "Ch.159", "NEW", etc.

    public MangaUpdate(String title, String updateDate, String imageResource, String status) {
        this.title = title;
        this.updateDate = updateDate;
        this.imageResource = imageResource;
        this.status = status;
    }

    // Getters
    public String getTitle() { return title; }
    public String getUpdateDate() { return updateDate; }
    public String getImageResource() { return imageResource; }
    public String getStatus() { return status; }

    // Setters
    public void setTitle(String title) { this.title = title; }
    public void setUpdateDate(String updateDate) { this.updateDate = updateDate; }
    public void setImageResource(String imageResource) { this.imageResource = imageResource; }
    public void setStatus(String status) { this.status = status; }
}
