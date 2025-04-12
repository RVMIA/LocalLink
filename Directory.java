import java.util.ArrayList;
import java.util.stream.Collectors;

public class Directory {

    private static ArrayList<Business> list = new ArrayList<Business>();
    private static double userLat;
    private static double userLong;
    public static Business getUserLocation(){
        Business tmp = new Business(userLat, userLong);
        return tmp;
    }
    public static void setUserLocation(double userLat, double userLong) {
        Directory.userLat = userLat;
        Directory.userLong = userLong;
    }

    public static void addBusiness(Business b) {
        list.add(b);
    }

    public static ArrayList<Business> orderedByName() {
        return list.stream().sorted((a, b) -> a.getName().compareTo(b.getName()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static ArrayList<Business> orderedByDist() {
        return list.stream().sorted((a, b) -> (int) (distance(a, getUserLocation()) - distance(b, getUserLocation())))
                .collect(Collectors.toCollection(ArrayList::new));
    }
    
    public static ArrayList<Business> orderedByRating() {
        return list.stream().sorted((a,b)-> (int) (a.getStarRating() - b.getStarRating()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    public static double distance(Business a, Business b) { // kilos
        // https://en.wikipedia.org/wiki/Great-circle_distance#Formulae
        int EARTH_RADIUS = 6371008; // meters
        double ax = Math.toRadians(a.getCoordsX());
        double bx = Math.toRadians(b.getCoordsX());
        double dy = Math.toRadians(b.getCoordsY() - a.getCoordsY());

        double d = Math.acos(Math.sin(ax) * Math.sin(bx) + Math.cos(ax) * Math.cos(bx) * Math.cos(dy));
        return ((int) (d * EARTH_RADIUS)) / 1e3;

    }
}
