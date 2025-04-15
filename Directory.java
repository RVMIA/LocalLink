import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Directory {

    private static final List<Business> list = new ArrayList<>();
    private static final int EARTH_RADIUS_METERS = 6371008;
    private static final String DATABASE_PATH = "data.txt";

    private static double userLatitude;
    private static double userLongitude;

    public static final Comparator<Business> NAME_COMPARATOR = Comparator.comparing(Business::getName);
    public static final Comparator<Business> DIST_COMPARATOR = (a, b) -> (int) (distance_km(a, getUserLocation()) - distance_km(b, getUserLocation()));
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
        return ((int) (d * EARTH_RADIUS_METERS)) / 1e3;

    }

    public static void saveList() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(DATABASE_PATH));
            for (Business businessToWrite : Directory.list) {
                writeBusinessToFile(businessToWrite, bufferedWriter);
            }
            bufferedWriter.close();

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


    public static void readList(){
        try {
            BufferedReader br = new BufferedReader(new FileReader(DATABASE_PATH));
            while(readBusiness(br));
            br.close();
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
