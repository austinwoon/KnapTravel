package main;

import entities.Coordinate;
import entities.Location;
import kmeans.Kmeans;
import location_selector.GreedyLocationSelector;
import location_selector.KnapsackLocationSelector;
import location_selector.LocationSelector;
import location_selector.RevisedKnapsackLocationSelector;
import routing.GreedyRouter;
import routing.PermutationsRouter;
import routing.TwoOptRouter;
import scoring.ScoringCalculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ExperimentOne {
    // containing data to write to experiments.csv file
    private static StringBuilder selectorSb = new StringBuilder();
    private static StringBuilder routeSb = new StringBuilder();
    private static String currentCity = "";




    public static void main(String[] args) throws IOException {
        selectorSb.append("country,cluster,timeConstraint,knapScore,knapTime,greedyScore,greedyTime\n");
        routeSb.append("country,greedyDistance,greedyTimeTaken,twoOptDistance,twoOptTimeTaken\n");

        HashSet<String> pref = new HashSet<>();
        pref.add("Museums");
        String[] cities = {"tokyo", "california", "london", "new-york-city", "osaka", "paris", "taipei" };
//        String[] cities = {"tokyo" };

        for (String city : cities ) {
            currentCity = city;
            System.out.println(String.format("========== CONDUCTING EXPERIMENT FOR CITY %s ===========", city));
            String fileName = String.format("/data/%s.json", city);
            conductExperiment(pref, fileName);
        }

        writeToCSV("scorer_experiments_test.csv", selectorSb);
        writeToCSV("router_experiments_test.csv", routeSb);
    }

    public static void writeToCSV(String fileName, StringBuilder linesToAdd) {
        try (PrintWriter writer = new PrintWriter(new File(fileName))) {

            writer.write(linesToAdd.toString());

            System.out.println("done!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void conductExperiment(HashSet<String> pref, String fileName) {
        ScoringCalculator scorer = new ScoringCalculator(pref, fileName);

        // Map where key is clusters, value is timeConstraint for visiting hours
        List<Map<Integer, Integer>> experimentValues = new ArrayList<>();

        for (int i=2; i < 5; i++) {
            Map<Integer, Integer> map = new HashMap<>();
            Map<Integer, Integer> map2 = new HashMap<>();
            map.put(i, 20);
            map2.put(i, 300);

            experimentValues.add(map);
            experimentValues.add(map2);
        }


        for (Map<Integer, Integer> experimentMap : experimentValues ) {
            for (int clusterNumber : experimentMap.keySet()) {
                int timeConstraint = experimentMap.get(clusterNumber);
                System.out.println(String.format(">>>> CLUSTER NUMBER %s \n>>>> Time Constraint: %s", clusterNumber, timeConstraint));

                List<Location> locations = scorer.getLocations();
                System.out.println(" ================== KNAPSACK SELECTIONS =============== ");
                Map<Integer, List<Location>> clusters = Kmeans.fit(locations, clusterNumber, 100000);
                Map<Integer, List<Location>> knapsackLocations = getKnapsackLocation(clusters, timeConstraint);

//                System.out.println(String.format("TOTAL SCORE FOR KNAPSACK: %.2f", getTotalScoreOfSelectedLocations(knapsackLocations)));
                getClusterRoutes(knapsackLocations, scorer.getCenter());

                System.out.println(" ================== GREEDY SELECTIONS =============== ");
                Map<Integer, List<Location>> greedyLocations = getGreedyLocation(clusters, timeConstraint);

//                System.out.println(String.format("TOTAL SCORE FOR KNAPSACK: %.2f", getTotalScoreOfSelectedLocations(greedyLocations)));
                getClusterRoutes(greedyLocations, scorer.getCenter());
            }
        }
    }

    public static Map<Integer, List<Location>> getClusterRoutes(Map<Integer, List<Location>> clusters, Coordinate startPoint) {
        Map<Integer, List<Location>> routedClusters = new HashMap<>();
        for (Integer i : clusters.keySet()) {
            List<Location> topClusterLocations = clusters.get(i);
            System.out.println("for cluster " + i + ": ");

            long startTime = System.nanoTime();
            GreedyRouter greedyRouter = new GreedyRouter(startPoint, topClusterLocations);
            System.out.println("greedy distance: " + greedyRouter.getTotalDist());
            long endTime = System.nanoTime();
            System.out.println("Time elapse for GREEDY: " + (endTime-startTime)+ " ms");
            long greedyTime = endTime-startTime;

            startTime = System.nanoTime();
            TwoOptRouter twoOptRouter = new TwoOptRouter(startPoint, topClusterLocations);
            System.out.println("2-opt distance: " +twoOptRouter.getTotalDist());
            endTime = System.nanoTime();
            System.out.println("Time elapse for 2-opt: " + (endTime-startTime)+ " ms");
            long twoOptTime = endTime-startTime;

            System.out.println("##################");

            routeSb.append(String.format("%s,%s,%s,%s,%s\n", currentCity, greedyRouter.getTotalDist(), greedyTime, twoOptRouter.getTotalDist(), twoOptTime));

            List<Location> routedLocations = greedyRouter.getRoute();
            routedClusters.put(i, routedLocations);
        }
        return routedClusters;
    }

    public static Map<Integer, List<Location>> getGreedyLocation(Map<Integer, List<Location>> clusters, int timeConstraint) {
        Map<Integer, List<Location>> selectedLocations = new HashMap<>();
        double score = 0.0;
        long startTime = System.nanoTime();
        for (Integer i : clusters.keySet()) {
            LocationSelector selector = new GreedyLocationSelector(clusters.get(i), timeConstraint);
            List<Location> locationsSelected = selector.selectLocationsToVisit();
            selectedLocations.put(i, locationsSelected);
            score += getTotalScore(locationsSelected);
        }
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;

        System.out.println(String.format("TOTAL GREEDY SCORE : %.2f", score));

        selectorSb.append(String.format("%s,", score));
        selectorSb.append(String.format("%s,", elapsedTime));
        selectorSb.append("\n");

        return selectedLocations;
    }

    public static Map<Integer, List<Location>> getKnapsackLocation(Map<Integer, List<Location>> clusters, int timeConstraint) {
        Map<Integer, List<Location>> selectedLocations = new HashMap<>();


        double score = 0.0;
        long startTime = System.nanoTime();
        for (Integer i : clusters.keySet()) {
            LocationSelector selector = new KnapsackLocationSelector(clusters.get(i), timeConstraint);
            List<Location> locationsSelected = selector.selectLocationsToVisit();
            selectedLocations.put(i, locationsSelected);
            score += getTotalScore(locationsSelected);
        }
        long endTime = System.nanoTime();
        long elapsedTime = endTime - startTime;
        selectorSb.append(String.format("%s,", currentCity));
        selectorSb.append(String.format("%s,", clusters.size()));
        selectorSb.append(String.format("%s,", timeConstraint));
        selectorSb.append(String.format("%s,", score));
        selectorSb.append(String.format("%s,", elapsedTime));

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
