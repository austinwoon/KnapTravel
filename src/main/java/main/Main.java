package main;

import entities.Location;
import kmeans.Kmeans;
import location_selector.GreedyLocationSelector;
import location_selector.KnapsackLocationSelector;
import location_selector.LocationSelector;
import scoring.ScoringCalculator;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        HashSet<String> pref = new HashSet<>();
        pref.add("Museums");
        ScoringCalculator scorer = new ScoringCalculator(pref, "src/main/resources/data/tokyo-processed.json");

        List<Location> locations = scorer.getLocations();
        Map<Integer, List<Location>> clusters = Kmeans.fit(locations, 5, 100000);

        LocationSelector knapper = new KnapsackLocationSelector(clusters.get(2), 9);
        LocationSelector greedy = new GreedyLocationSelector(clusters.get(2), 9);

        System.out.println("======================= KNAPPPPP ====================================");
        double knapScore = 0.0;
        for (Location location : knapper.selectLocationsToVisit()) {
            System.out.println(location.getName() + ": " + location.getScore() + ", " + (location.getHours()));
            knapScore += location.getScore();
        }


        System.out.println("======================= GREEEEEEEEEEEEDY ====================================");
        double greedyScore = 0.0;
        for (Location location : greedy.selectLocationsToVisit()) {
            greedyScore += location.getScore();
            System.out.println(location.getName() + ": " + location.getScore() + ", " + (location.getHours()));
        }

        System.out.println("\n========================== FINAL SCORES =================");

        System.out.println(String.format("KNAPSCORE: %.2f, GREEDYSCORE: %.2f", knapScore, greedyScore));
    }

}
