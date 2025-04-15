import java.util.*;

// TODO: Screenshots of the workings
// TODO: comments
public class Main {
    public static void main(String[] args) {
        System.out.println("WELCOME TO LOCALLINK");
        Scanner scanner = new Scanner(System.in); // gets input
        setLocation(scanner); // sets home location on startup

        Directory.readList(); // read list from database file
        while (!processUserCommand(scanner)) ; // until quit, keep prompting from menu

        scanner.close();
        System.out.println("\033[H\033[2J THANK YOU FOR USING LOCALLINK");
    }

    public static boolean processUserCommand(Scanner scanner) {
        Directory.saveList(); // saves the current list to the database file
        System.out.println("Type a command; type \"help\" for more info; type \"quit\" to quit");
        String input = scanner.nextLine();
        switch (input) {
            case "quit":
                Directory.saveList();
                return true;
            case "help":
                printHelp();
                break;
            case "list":
                list(scanner);
                break;
            case "add":
                addBusiness(scanner);
                break;
            case "remove":
                removeBusiness(scanner);
                break;
            case "review":
                leaveReview(scanner);
                break;
            case "maps":
                maps(scanner);
                break;
            case "view":
                view(scanner);
                break;
            case "relocate":
                setLocation(scanner);
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

    public static Business search(String name) {
        return Directory.getBusinesses()
                .stream()
                .filter(b -> b.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public static Business getBusinessFromInput(Scanner scanner){
        System.out.println("name of business");
        String name = scanner.nextLine();
        return search(name);
    }

    public static Business getCoords(Scanner scanner) {
        while (true) {
            try {
                System.out.println("coordinates as signed doubles, space separated");
                System.out.println("(North and East are positive, South and West are negative)");
                double latitude = scanner.nextDouble();
                double longitude = scanner.nextDouble();
                scanner.nextLine();
                return new Business(latitude, longitude);

            } catch (Exception e) {
                System.err.println("invalid input");
                scanner.nextLine();
            }
        }

    }

    public static void addBusiness(Scanner scanner) {
        System.out.println("name: ");
        String name = scanner.nextLine();
        Business newBusiness = getCoords(scanner);
        newBusiness.setName(name);

        Directory.addBusiness(newBusiness);
        System.out.println("adding business to list: " + newBusiness);

    }

    public static void removeBusiness(Scanner scanner) {
        Business businessToRemove = getBusinessFromInput(scanner);
        if (businessToRemove == null) {
            System.out.println("unable to find business with that name");
            return;
        }
        System.out.printf("removing business \"%s\"\n", businessToRemove.getName());
        Directory.removeBusiness(businessToRemove);
    }

    public static void leaveReview(Scanner sc) {
        Business businessToReview = getBusinessFromInput(sc);
        if (businessToReview == null) {
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
        businessToReview.addReview(stars, stars + " stars - " + review);

    }

    public static void setLocation(Scanner sc) {
        try {
            Business temp = getCoords(sc); // get coords
            Directory.setUserLocation(temp.getCoordsX(), temp.getCoordsY());
            System.out.printf("Setting coordinates to (%.4f, %.4f)\n", temp.getCoordsX(), temp.getCoordsY());
        } catch (Exception e) {
            System.err.println("invalid input");
            sc.nextLine();
        }

    }

    public static void maps(Scanner sc) {
        Business businessToView = getBusinessFromInput(sc);
        if (businessToView == null) {
            System.out.println("unable to find business with that name");
            return;
        }
        System.out.println("Opening maps");
        businessToView.openMaps();

    }

    public static void list(Scanner sc) {
        Comparator<Business> businessComparator = null;
        while (businessComparator == null) {
            System.out.println("sort by (name, distance, rating)");
            String mode = sc.nextLine();
            switch (mode) { // picks which way to sort the list of businesses
                case "name":
                    businessComparator = Directory.NAME_COMPARATOR;
                    break;
                case "distance":
                    businessComparator = Directory.DIST_COMPARATOR;
                    break;
                case "rating":
                    businessComparator = Directory.RATING_COMPARATOR;
                    break;
                default:
                    System.out.println("try again");

            }
        }
        for (Business b : Directory.getBusinesses(businessComparator)) {
            System.out.println(b);
        }
    }

    public static void view(Scanner sc) {
        Business business = getBusinessFromInput(sc);
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
}