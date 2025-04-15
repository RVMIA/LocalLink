import java.util.*;

// TODO: comments
public class Main {
    public static void main(String[] args) {
        System.out.println("WELCOME TO LOCALLINK");
        Scanner sc = new Scanner(System.in); // gets input
        setLocation(sc); // sets home location on startup

        Directory.readList(); // read list from database file
        while (!menu(sc)); // until quit, keep prompting from menu

        sc.close();
        System.out.println("\033[H\033[2J THANK YOU FOR USING LOCALLINK");
    }

    public static boolean menu(Scanner sc) {
        Directory.saveList(); // saves the current list to the database file
        System.out.println("Type a command; type \"help\" for more info; type \"quit\" to quit");
        String input = sc.nextLine();
        switch (input) {
            case "quit":
                Directory.saveList();
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
            case "remove":
                removeBusiness(sc);
                break;
            case "review":
                leaveReview(sc);
                break;
            case "maps":
                maps(sc);
                break;
            case "view":
                view(sc);
                break;
            case "relocate":
                setLocation(sc);
                break;
            default:
                System.out.println("not a valid command");
                break;
        }
        return false;

    }

    public static void printHelp() {
        System.out.println("Commands:");
        System.out.println("\t\"help\": displays this menu");
        System.out.println("\t\"quit\": quits the program");
        System.out.println("\t\"list\": list businesses");
        System.out.println("\t\"add\": add a business to directory");
        System.out.println("\t\"remove\": remove a business from directory");
        System.out.println("\t\"review\": leave a review on a business");
        System.out.println("\t\"view\": search and view a specific business");
        System.out.println("\t\"relocate\": change where the distance measurements are referenced from");
        System.out.println("\t\"maps\": opens the business in google maps in your browser");
    }

    public static void maps(Scanner sc) {
        System.out.println("name of business");
        String n = sc.nextLine();
        Business business = search(n);
        if (business == null) {
            System.out.println("unable to find business with that name");
            return;
        }
        System.out.println("Opening maps");
        business.openMaps();

    }

    public static void list(Scanner sc) {
        Comparator<Business> comp = null;
        while (comp == null) {
            System.out.println("sort by (name, distance, rating)");
            String mode = sc.nextLine();
            switch (mode) { // picks which way to sort the list of businesses
                case "name":
                    comp=Directory.NAME_COMPARATOR;
                    break;
                case "distance":
                    comp=Directory.DIST_COMPARATOR;
                    break;
                case "rating":
                    comp=Directory.RATING_COMPARATOR;
                    break;
                default:
                    System.out.println("try again");

            }
        }
        for (Business b : Directory.getBusinesses(comp)) {
            System.out.println(b);
        }
    }

    public static void view(Scanner sc) {
        System.out.println("name of business");
        String n = sc.nextLine();
        Business business = search(n);
        if (business == null) {
            System.out.println("unable to find business with that name");
            return;
        }
        ArrayList<String> review = business.getReviews();
        String a = (!review.isEmpty()) ? "|\t" + review.get(0) + "\n" : "";
        String b = (review.size() > 1) ? "|\t" + review.get(1) + "\n" : "";
        String c = (review.size() > 2) ? "|\t" + review.get(2) + "\n" : "";
        System.out.printf("%s\n%.1f stars - %d reviews\n%s%s%s", business.getName(), business.getStarRating(),
                business.getReviewCount(), a, b, c);

    }

    public static void removeBusiness(Scanner sc) {
        System.out.println("name of business");
        String n = sc.nextLine();
        Business b = search(n);
        if (b == null) {
            System.out.println("unable to find business with that name");
            return;
        }
        System.out.printf("removing business \"%s\"\n", n);
        Directory.removeBusiness(b);
    }

    public static void leaveReview(Scanner sc) {
        System.out.println("name of business"); // searches for business
        String n = sc.nextLine();
        Business b = search(n);
        if (b == null) {
            System.out.println("unable to find business with that name");
            return;
        }


        int stars; // leaving a star rating
        while (true) {
            try {
                System.out.println("how many stars?");
                stars = sc.nextInt();
                sc.nextLine();
                break;
            } catch (Exception e) {
                System.out.println("invalid number try again");
                sc.nextLine();
            }
        }

        System.out.println("leave note here:"); // leave a comment
        String review = sc.nextLine();
        b.addReview(stars, stars + " stars - " + review);

    }

    // returns the business where the names match
    public static Business search(String n) {
        for (Business b : Directory.getBusinesses()) {
            if (b.getName().equals(n)) {
                return b;
            }
        }
        return null;
    }
    // asks for coordinates dealing with error handling, returning a temporary business that only stores coordinates.
    public static Business getCoords(Scanner sc){
        while (true) {
            try {
                System.out.println("coordinates as signed doubles, space separated");
                System.out.println("(North and East are positive, South and West are negative)");
                double lat = sc.nextDouble();
                double lon = sc.nextDouble();
                sc.nextLine();
                return new Business(lat, lon);

            } catch (Exception e) {
                System.err.println("invalid input");
                sc.nextLine();
            }
        }

    }
    // adds a new business to the list, asking for a name and coords
    public static void addBusiness(Scanner sc) {
        System.out.println("name: ");
        String name = sc.nextLine();
        Business b = getCoords(sc);
        b.setName(name);

        Directory.addBusiness(b);
        System.out.println("adding business to list: " + b);

    }

    // sets the "home" location in the program
    public static void setLocation(Scanner sc) throws InputMismatchException {
        Business temp = getCoords(sc); // get coords
        Directory.setUserLocation(temp.getCoordsX(), temp.getCoordsY());
        System.out.printf("Setting coordinates to (%.4f, %.4f)\n", temp.getCoordsX(), temp.getCoordsY());

    }

}