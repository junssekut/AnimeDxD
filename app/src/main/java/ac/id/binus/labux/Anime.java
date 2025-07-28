package ac.id.binus.labux;

public class Anime {
    public String title;
    public String author;
    public String description;
    public double rating;
    public String genre;
    public int imageResId;

    public Anime(String title, String author, String description, double rating, String genre, int imageResId) {
        this.title = title;
        this.author = author;
        this.description = description;
        this.rating = rating;
        this.genre = genre;
        this.imageResId = imageResId;
    }
}
