import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Directory {

    private static final List<Business> list = new ArrayList<>();
    private static double userLatitude;
    private static double userLongitude;
    private static final int EARTH_RADIUS_METERS = 6371008;

    public static final Comparator<Business> NAME_COMPARATOR = Comparator.comparing(Business::getName);
    public static final Comparator<Business> DIST_COMPARATOR = (a, b) -> (int) (distance(a, getUserLocation()) - distance(b, getUserLocation()));
    public static final Comparator<Business> RATING_COMPARATOR = Comparator.comparingDouble(Business::getStarRating).reversed();

    public static Business getUserLocation() {
        return new Business(userLatitude, userLongitude);
    }

    public static void setUserLocation(double userLat, double userLong) {
        Directory.userLatitude = userLat;
        Directory.userLongitude = userLong;
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


    public static double distance(Business a, Business b) { // kilos
        // https://en.wikipedia.org/wiki/Great-circle_distance#Formulae
        double ax = Math.toRadians(a.getCoordsX());
        double bx = Math.toRadians(b.getCoordsX());
        double dy = Math.toRadians(b.getCoordsY() - a.getCoordsY());

        double d = Math.acos(Math.sin(ax) * Math.sin(bx) + Math.cos(ax) * Math.cos(bx) * Math.cos(dy));
        return ((int) (d * EARTH_RADIUS_METERS)) / 1e3;

    }

    public static void saveList() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("data.txt"));
            for (Business b : Directory.list) {
                bw.write(b.getName() + "\n");
                bw.write(b.getCoordsX() + "\n");
                bw.write(b.getCoordsY() + "\n");
                bw.write(b.getStarRating() + "\n");
                bw.write(b.getReviewCount() + "\n");
                ArrayList<String> reviews = b.getReviews();
                for (String s : reviews)
                    bw.write(s + "\n");

            }
            bw.close();

        } catch (Exception e) {
            System.err.println("error occurred:" + e.getMessage());
        }
    }

    public static void readList(){
        try {
            BufferedReader br = new BufferedReader(new FileReader("data.txt"));
            while(true){
                String name = br.readLine();
                if(name == null){break;}
                double x = Double.parseDouble(br.readLine());
                double y = Double.parseDouble(br.readLine());
                double rating = Double.parseDouble(br.readLine());
                int revCount = Integer.parseInt(br.readLine());
                ArrayList<String> revs = new ArrayList<>();
                for (int i = 0; i < revCount; i++) {
                    revs.add(br.readLine());
                }
                Business b = new Business(name, rating, revs, revCount, x, y);
                Directory.addBusiness(b);
            }
            br.close();
        } catch (Exception e) {
            System.err.println("error occurred:" + e.getMessage());
        }
    }
}
