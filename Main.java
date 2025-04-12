import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
// TODO: IO to a file for saving
public class Main {
    public static void main(String[] args) {
        System.out.println("WELCOME TO LOCALLINK");
        Scanner sc = new Scanner(System.in);
        setLocation(sc);

        while (!menu(sc)) { }

        System.out.println("\033[H\033[2J THANK YOU FOR USING LOCALLINK");
        sc.close();
        // Directory.setUserLocation(33,-96.65);
        // Business b = (new Business("White House", 38.898, -77.037));
        // Business a = (new Business("Eiffel tower", 48.858, 2.294));
        // Directory.addBusiness(b);
        // Directory.addBusiness(a);
        // System.out.println(Directory.orderedByDist());
    }

    public static boolean menu(Scanner sc) {
        System.out.println("Type a command; type \"help\" for more info; type \"quit\" to quit");
        String input = sc.nextLine();
        switch (input) {
            case "quit":
                return true;
            case "help":
                printHelp();
                break;
            case "list":
                list(sc);
                break;
            case "add":
                addBusiness(sc);
                break;
            case "review":
                leaveReview(sc);
                break;
            case "view":
                view(sc);
                break;
            default:
                System.out.println("not a valid command");
                break;
        }
        return false;

    }

    public static void list(Scanner sc) {
        while (true) {
            System.out.println("sort by (name, distance)");
            String mode = sc.nextLine();
            switch (mode) {
                case "name":
                    for (Business b : Directory.orderedByName()) {
                        System.out.println(b);
                    }
                    return;
                case "distance":
                    for (Business b : Directory.orderedByDist()) {
                        System.out.println(b);

                    }
                    return;
                case "rating":
                    for (Business b : Directory.orderedByRating()){
                        System.out.println(b);
                    }
                    return;
                default:
                    System.out.println("try again");

            }
        }
    }
    public static void view(Scanner sc){
        System.out.println("name of business");
        String n = sc.nextLine();
        Business business = search(n);
        if (business == null){
            System.out.println("unable to find business with that name");
            return;
        }
        ArrayList<String> review = business.getReviews();
        String a = (review.size()>0) ? "|\t"+review.get(0)+"\n" : "";
        String b = (review.size()>1) ? "|\t"+review.get(1)+"\n" : "";
        String c = (review.size()>2) ? "|\t"+review.get(2)+"\n" : "";
        System.out.printf("%s by %s\n%.1f stars - %d reviews\n%s%s%s", business.getName(),
                business.getOwner(), business.getStarRating(),business.getReviewCount(),a,b,c);

    }
    public static void leaveReview(Scanner sc){
        System.out.println("name of business");
        String n = sc.nextLine();
        Business b = search(n);
        if (b == null){
            System.out.println("unable to find business with that name");
            return;
        }
        System.out.println("how many stars?");
        int stars = sc.nextInt();
        sc.nextLine();
        System.out.println("leave note here:");
        String review = sc.nextLine();
        b.addReview(stars, ""+stars+" stars - "+review);


    }
    public static Business search(String n){
        for (Business b : Directory.orderedByName()){
            if (b.getName().equals(n)){
                return b;
            }
        }
        return null;
    }

    public static Business getCoords(Scanner sc) throws InputMismatchException {
        while (true) {
            try {
                System.out.println("coordinates as signed doubles, space separated");
                System.out.println("(North and East are positive, South and West are negative)");
                double lat = sc.nextDouble();
                double lon = sc.nextDouble();
                sc.nextLine();
                Business temp = new Business(lat, lon);
                return temp;

            } catch (Exception e) {
                System.err.println("invalid input");
                sc.nextLine();
                continue; 
            }
        }

    }
    public static void addBusiness(Scanner sc) {
        System.out.println("name: ");
        String name = sc.nextLine();
        Business b = getCoords(sc);
        b.setName(name);

        Directory.addBusiness(b);
        System.out.println("adding business to list: " + b);

    }

    public static void setLocation(Scanner sc) throws InputMismatchException {
        Business temp = getCoords(sc);
        Directory.setUserLocation(temp.getCoordsX(), temp.getCoordsY());
        System.out.printf("Setting coordinates to (%.4f, %.4f)\n", temp.getCoordsX(), temp.getCoordsY());

    }

    public static void printHelp() {
        System.out.println("Commands:");
        System.out.println("\t\"help\": displays this menu");
        System.out.println("\t\"quit\": quits the program");
        System.out.println("\t\"list\": list businesses");
        System.out.println("\t\"add\": add a business to directory");
        System.out.println("\t\"review\": leave a review on a business");
        System.out.println("\t\"view\": search and view a specific business");
        System.out.println("\t\"relocate\": change where the distance measurements are referenced from");
    }
}