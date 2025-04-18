import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Represents a collection of businesses that can be managed, stored, and retrieved.
 * It supports functionality to add, remove, sort, and persist business records,
 * while also allowing users to define and use a reference location.
 */
public class Directory {

    private static final List<Business> list = new ArrayList<>();
    private static final int EARTH_RADIUS_METERS = 6371008;
    private static final String DATABASE_PATH = "data.txt";

    // user's location
    private static double userLatitude;
    private static double userLongitude;

    // comparisons for the sorting function
    public static final Comparator<Business> NAME_COMPARATOR = Comparator.comparing(Business::getName);
    public static final Comparator<Business> DIST_COMPARATOR = Comparator.comparingDouble(a -> distance_km(a, getUserLocation()));
    public static final Comparator<Business> RATING_COMPARATOR = Comparator.comparingDouble(Business::getStarRating).reversed();

    public static Business getUserLocation() {
        return new Business(userLatitude, userLongitude);
    }
    public static void setUserLocation(double x, double y) {
        Directory.userLatitude = x;
        Directory.userLongitude = y;
    }

    public static void addBusiness(Business b) {
        list.add(b);
    }
    public static void removeBusiness(Business b){
        list.remove(b);
    }

    public static List<Business> getBusinesses() {
        return list;
    }

    // return the list sorted by the given comparator as an arraylist
    public static List<Business> getBusinesses(Comparator<Business> comp) {
        return list.stream()
                .sorted(comp)
                .collect(Collectors.toCollection(ArrayList::new));
    }


    // https://en.wikipedia.org/wiki/Great-circle_distance#Formulae
    public static double distance_km(Business a, Business b) {
        double ax = Math.toRadians(a.getCoordsX());
        double bx = Math.toRadians(b.getCoordsX());
        double dy = Math.toRadians(b.getCoordsY() - a.getCoordsY());

        double d = Math.acos(Math.sin(ax) * Math.sin(bx) + Math.cos(ax) * Math.cos(bx) * Math.cos(dy));
        return  (d * EARTH_RADIUS_METERS) / 1e3;

    }


    // write list to databse file
    public static void saveList() {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(DATABASE_PATH)))
        {
            for (Business businessToWrite : Directory.list) {
                writeBusinessToFile(businessToWrite, bufferedWriter);
            }

        } catch (Exception e) {
            System.err.println("error occurred:" + e.getMessage());
        }
    }

    private static void writeBusinessToFile(Business b, BufferedWriter bw) throws IOException {
        bw.write(b.getName() + "\n");
        bw.write(b.getCoordsX() + "\n");
        bw.write(b.getCoordsY() + "\n");
        bw.write(b.getStarRating() + "\n");
        bw.write(b.getReviewCount() + "\n");
        ArrayList<String> reviews = b.getReviews();
        for (String s : reviews)
            bw.write(s + "\n");
    }

    //read business from file
    public static void readList(){
        try (BufferedReader br = new BufferedReader(new FileReader(DATABASE_PATH))){
            while(readBusiness(br));
        } catch (Exception e) {
            System.err.println("error occurred:" + e.getMessage());
        }
    }
    private static boolean readBusiness(BufferedReader br) throws IOException {
        String name = br.readLine();
        if(name == null){
            return false;
        }
        double x = Double.parseDouble(br.readLine());
        double y = Double.parseDouble(br.readLine());
        double rating = Double.parseDouble(br.readLine());
        int reviewCount = Integer.parseInt(br.readLine());
        ArrayList<String> reviews = new ArrayList<>();
        for (int i = 0; i < reviewCount; i++) {
            reviews.add(br.readLine());
        }
        Business b = new Business(name, rating, reviews, reviewCount, x, y);
        Directory.addBusiness(b);
        return true;
    }
}
