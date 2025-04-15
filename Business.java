import java.awt.Desktop;
import java.util.*;
import java.net.URI;

public class Business {
    private String name;
    private double coordsX;
    private double coordsY;
    private double starRating;
    private int reviewCount;
    private ArrayList<String> reviews = new ArrayList<>();

    public Business(String name, double starRating, ArrayList<String> reviews, int reviewCount,
            double coordsX, double coordsY) {
        this.name = name;
        this.starRating = starRating;
        this.reviews = reviews;
        this.reviewCount = reviewCount;
        this.coordsX = coordsX;
        this.coordsY = coordsY;
    }


    public Business(double coordsX, double coordsY) {
        this.coordsX = coordsX;
        this.coordsY = coordsY;
    }

    public Business(String name, double coordsX, double coordsY) {
        this.name = name;
        this.coordsX = coordsX;
        this.coordsY = coordsY;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public double getCoordsX() { return coordsX; }
    public double getCoordsY() { return coordsY; }

    public double getStarRating() { return starRating; }
    public ArrayList<String> getReviews() { return reviews; }
    public void addReview(int stars, String reviewText) {
        starRating *= reviewCount;
        starRating += stars;
        starRating /= ++reviewCount;
        reviews.add(reviewText);
    }

    public String mapsURL() {
        return String.format("https://www.google.com/maps/@?api=1&map_action=map&center=%.5f,%.5f", coordsX, coordsY);
    }

    public void openMaps() {
        try {
            URI url = new URI(mapsURL());
            Desktop.getDesktop().browse(url);
        } catch (Exception e) {
            System.err.println("Could not open maps!");
        }
    }

    @Override
    public String toString() {
        return String.format("%.1fkm : %s - %.1f Stars - %d reviews",
                Directory.distance_km(this, Directory.getUserLocation()), this.name, this.starRating, this.reviewCount);
    }

    public int getReviewCount() {
        return reviewCount;
    }

}
