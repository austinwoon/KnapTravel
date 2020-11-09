package main;

import entities.Coordinate;
import entities.Location;
import kmeans.Kmeans;
import location_selector.GreedyLocationSelector;
import location_selector.KnapsackLocationSelector;
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

        HashSet<String> pref = new HashSet<>();
        pref.add("Museums");
        ScoringCalculator scorer = new ScoringCalculator(pref, "src/main/resources/data/tokyo-processed.json");

        List<Location> locations = scorer.getLocations();
        Map<Integer, List<Location>> clusters = Kmeans.fit(locations, 5, 100000);

        getClusterRoutes(getKnapsackLocation(clusters, 12), scorer.getCenter());
//        LocationSelector knapper = new KnapsackLocationSelector(clusters.get(2), 9);
////        LocationSelector greedy = new GreedyLocationSelector(clusters.get(2), 9);

//        System.out.println(getGreedyLocation(clusters, 9));
//        System.out.println(getKnapsackLocation(clusters, 9));
    }

    public static Map<Integer, List<Location>> getClusterRoutes(Map<Integer, List<Location>> knappedClusters, Coordinate startPoint) {
        Map<Integer, List<Location>> routedClusters = new HashMap<>();
        for (Integer i : knappedClusters.keySet()) {
            List<Location> topClusterLocations = knappedClusters.get(i);
            System.out.println("for cluster " + i + ": ");

            long startTime = System.nanoTime();
            GreedyRouter greedyRouter = new GreedyRouter(startPoint, topClusterLocations);
            System.out.println("greedy distance: " + greedyRouter.getTotalDist());
            long endTime = System.nanoTime();
            System.out.println("Time elapse for GREEDY: " + (endTime-startTime)+ " ms");

            startTime = System.nanoTime();
            TwoOptRouter twoOptRouter = new TwoOptRouter(startPoint, topClusterLocations);
            System.out.println("2-opt distance: " +twoOptRouter.getTotalDist());
            endTime = System.nanoTime();
            System.out.println("Time elapse for 2-opt: " + (endTime-startTime)+ " ms");

            startTime = System.nanoTime();
            PermutationsRouter perRouter = new PermutationsRouter(startPoint, topClusterLocations);
            System.out.println("permu distance: " +perRouter.getTotalDist());
            endTime = System.nanoTime();
            System.out.println("Time elapse for all permu: " + (endTime-startTime) + " ms");

            System.out.println("##################");
            List<Location> routedLocations = greedyRouter.getRoute();
            routedClusters.put(i, routedLocations);
        }
        return routedClusters;
    }

    public static Map<Integer, List<Location>> getGreedyLocation(Map<Integer, List<Location>> clusters, int timeConstraint) {
        Map<Integer, List<Location>> selectedLocations = new HashMap<>();
        double score = 0.0;
        for (Integer i : clusters.keySet()) {
            LocationSelector selector = new GreedyLocationSelector(clusters.get(i), timeConstraint);
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
            LocationSelector selector = new KnapsackLocationSelector(clusters.get(i), timeConstraint);
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
