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
        String[] cities = { "tokyo", "new-york-city", "taipei", "paris", "california", "osaka" };
        int daysToVisit = 4;
        int timeConstraint = 8;

        for (String city : cities ) {
            System.out.println("======================================NEW CITY===================================================");
            System.out.println(String.format("Generating Itinerary for %s, Visiting %s Days, with %s visiting hours", city, daysToVisit, timeConstraint));
            generateItinerary(city, daysToVisit, timeConstraint);
            System.out.println("=======================================END OF CITY==============================================\n");
        }
    }

    public static void generateItinerary(String city, int days, int timeConstraint) {
        HashSet<String> pref = new HashSet<>();
        pref.add("Museums");

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
