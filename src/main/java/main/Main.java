package main;

import entities.Location;
import kmeans.Kmeans;
import location_selector.GreedyLocationSelector;
import location_selector.KnapsackLocationSelector;
import location_selector.LocationSelector;
import scoring.ScoringCalculator;

import java.util.HashMap;
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

        System.out.println(getGreedyLocation(clusters, 9));
        System.out.println(getKnapsackLocation(clusters, 9));
    }

    public static Map<Integer, List<Location>> getGreedyLocation(Map<Integer, List<Location>> clusters, int timeConstraint) {
        Map<Integer, List<Location>> selectedLocations = new HashMap<>();
        double score = 0.0;
        for (Integer i : clusters.keySet()) {
            LocationSelector selector = new GreedyLocationSelector(clusters.get(i), 9);
            selectedLocations.put(i, selector.selectLocationsToVisit());
            score += getTotalScore(selector.selectLocationsToVisit());
        }

        System.out.println(String.format("TOTAL GREEDY SCORE : %.2f", score));
        return selectedLocations;
    }

    public static Map<Integer, List<Location>> getKnapsackLocation(Map<Integer, List<Location>> clusters, int timeConstraint) {
        Map<Integer, List<Location>> selectedLocations = new HashMap<>();
        double score = 0.0;
        for (Integer i : clusters.keySet()) {
            LocationSelector selector = new KnapsackLocationSelector(clusters.get(i), 9);
            selectedLocations.put(i, selector.selectLocationsToVisit());
            score += getTotalScore(selector.selectLocationsToVisit());
        }
        System.out.println(String.format("TOTAL KNAP SCORE : %.2f", score));
        return selectedLocations;
    }

    public static double getTotalScore(List<Location> locations) {
        double score = 0;
        for (Location l : locations) {
            score += l.getScore();
        }

        return score;
    }

}
