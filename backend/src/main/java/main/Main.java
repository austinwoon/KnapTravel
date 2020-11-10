package main;

import entities.Coordinate;
import entities.Location;
import kmeans.Kmeans;
import location_selector.GreedyLocationSelector;
import location_selector.RevisedKnapsackLocationSelector;
import location_selector.LocationSelector;
import routing.PermutationsRouter;
import routing.GreedyRouter;
import routing.TwoOptRouter;
import scoring.ScoringCalculator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        String city = "tokyo";
        HashSet<String> preferences = new HashSet<>();
        // please edit days to visit and time constraint to change inputs accordingly
        // to all available prefences to add for each city, call viewPreferences(cityName) function
        // e.g viewPreferences("tokyo");
        viewPreferences("tokyo");
        preferences.add("Museums");
        int daysToVisit = 4;
        int timeConstraint = 8;

        System.out.println("======================================GENERATING ITINERARY===================================================");
        System.out.println(String.format("Generating Itinerary for %s, Visiting %s Days, with %s visiting hours", city, daysToVisit, timeConstraint));
        System.out.println(String.format("PREFERENCES STATED: %s", preferences.toString()));
        generateItinerary(city, daysToVisit, timeConstraint, preferences);
        System.out.println("=======================================END==============================================\n");

    }

    public static void viewPreferences(String city) {
        String dataSource = String.format("/data/%s.json", city);
        HashSet<String> pref = new HashSet<>();

        ScoringCalculator scorer = new ScoringCalculator(pref, dataSource);
        List<String> tags = scorer.getFilteredTags(100);
        System.out.println(String.format("AVAILABLE TAGS: %s", tags.toString()));
    }

    public static void generateItinerary(String city, int days, int timeConstraint, HashSet<String> pref) {
        String dataSource = String.format("/data/%s.json", city);

        // get scores for each location in "database" according to interests of user
        ScoringCalculator scorer = new ScoringCalculator(pref, dataSource);

        // get list of locations with scores
        List<Location> locations = scorer.getLocations();

        // get clusters according to number of days to visit
        Map<Integer, List<Location>> clusters = Kmeans.fit(locations, days, 100000);

        // select locations within cluster
        Map<Integer, List<Location>> selectedLocations = getKnapsackLocation(clusters, timeConstraint, scorer.getCenter());

        // select ordering of locations to visit within cluster
        Map<Integer, List<Location>> itinerary = getClusterRoutes(selectedLocations, scorer.getCenter());
        printItinerary(itinerary);
    }

    public static void printItinerary(Map<Integer, List<Location>> itinerary) {
        int i = 0;
        for (List<Location> locations : itinerary.values()) {
            System.out.println(String.format("Day %s itinerary: ", ++i));
            for (int j = 0; j < locations.size(); j++) {
                String name = locations.get(j).getName();
                System.out.println(String.format("Visit Sequence %s: %s", j + 1, name));
            }
        }
    }

    public static Map<Integer, List<Location>> getKnapsackLocation(Map<Integer, List<Location>> clusters, int timeConstraint,
                                                                   Coordinate center) {
        Map<Integer, List<Location>> selectedLocations = new HashMap<>();
        for (Integer i : clusters.keySet()) {
            LocationSelector selector = new RevisedKnapsackLocationSelector(clusters.get(i), timeConstraint, center);
            selectedLocations.put(i, selector.selectLocationsToVisit());
        }
        return selectedLocations;
    }

    public static Map<Integer, List<Location>> getClusterRoutes(Map<Integer, List<Location>> selectedLocations, Coordinate startPoint) {
        Map<Integer, List<Location>> routedClusters = new HashMap<>();
        for (Integer i : selectedLocations.keySet()) {
            List<Location> topClusterLocations = selectedLocations.get(i);
            GreedyRouter router = new GreedyRouter(startPoint, topClusterLocations);
            List<Location> routedLocations = router.getRoute();
            routedClusters.put(i, routedLocations);
        }

        return routedClusters;
    }

}
