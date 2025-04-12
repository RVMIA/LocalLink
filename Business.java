import java.awt.Desktop;
import java.net.URI;
import java.util.ArrayList;

public class Business {
    private String name;
    private String owner;
    private double starRating;
    private ArrayList<String> reviews = new ArrayList<String>();
    private int reviewCount;
    private double coordsX;
    private double coordsY;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public Business(String name) {
        this.name = name;
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

    public String getOwner() {
        return owner;
    }

    public double getStarRating() {
        return starRating;
    }

    public ArrayList<String> getReviews() {
        return reviews;
    }

    public double getCoordsX() {
        return coordsX;
    }

    public double getCoordsY() {
        return coordsY;
    }

    public void addReview(int stars, String reviewText) {
        starRating = ((starRating * reviewCount) + stars) / (reviewCount + 1);
        reviews.add(reviewText);
        reviewCount++;
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
        return String.format("%.1fkm : %s - %.1f Stars - %d reviews", Directory.distance(this, Directory.getUserLocation()), this.name, this.starRating, this.reviewCount);
    }

    public int getReviewCount() {
        return reviewCount;
    }

}
