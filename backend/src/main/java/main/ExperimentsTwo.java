package main;

import entities.Location;
import kmeans.Kmeans;
import location_selector.KnapsackLocationSelector;
import location_selector.LocationSelector;
import location_selector.RevisedKnapsackLocationSelector;
import routing.GreedyRouter;
import routing.TwoOptRouter;
import scoring.ScoringCalculator;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.*;

public class ExperimentsTwo {
    static StringBuilder sb = new StringBuilder();
    static String currentCity = "";
    public static void main(String[] args) {
        HashSet<String> pref = new HashSet<>();
        pref.add("Museums");
        String[] cities = {"tokyo", "california", "london", "new-york-city", "osaka", "paris", "taipei" };

        sb.append("city,timeConstraint,knapTimeTaken,heuristicTimeTaken,knapScore,heuristicScore\n");
        for (String city : cities ) {
            currentCity = city;
            System.out.println(String.format("========== CONDUCTING EXPERIMENT FOR CITY %s ===========", city));
            String fileName = String.format("/data/%s.json", city);
            conductExperiment(pref, fileName);
        }
        writeToCSV("knap_heurstic_experiment.csv", sb);
    }

    public static void conductExperiment(HashSet<String> pref, String fileName) {
        ScoringCalculator scorer = new ScoringCalculator(pref, fileName);

        List<Map<Integer, Integer>> experimentValues = new ArrayList<>();

        for (int i=2; i < 5; i++) {
            Map<Integer, Integer> map = new HashMap<>();
            Map<Integer, Integer> map2 = new HashMap<>();
            map.put(i, 12);
            map2.put(i, 10);

            experimentValues.add(map);
            experimentValues.add(map2);
        }

        for (Map<Integer, Integer> experimentMap : experimentValues ) {
            for (int clusterNumber : experimentMap.keySet()) {
                int timeConstraint = experimentMap.get(clusterNumber);
                System.out.println(String.format(">>>> CLUSTER NUMBER %s \n>>>> Time Constraint: %s", clusterNumber, timeConstraint));
                List<Location> locations = scorer.getLocations();
                Map<Integer, List<Location>> clusters = Kmeans.fit(locations, clusterNumber, 100000);

                double knapDistance = 0.0;
                double knapHeuristicDistance = 0.0;

                double totalKnapScore = 0.0;
                double totalHeursiticScore = 0.0;

                for (int clusterKey : clusters.keySet()) {
                    List<Location> clusterLocations = clusters.get(clusterKey);

                    LocationSelector knapSelector = new KnapsackLocationSelector(clusterLocations, timeConstraint);

                    LocationSelector heuristicSelector = new RevisedKnapsackLocationSelector(clusterLocations, timeConstraint, scorer.getCenter());
                    List<Location> knapLocations = knapSelector.selectLocationsToVisit();
                    List<Location> heursticLocations = heuristicSelector.selectLocationsToVisit();

                    totalKnapScore += getTotalScore(knapLocations);
                    totalHeursiticScore += getTotalScore(heursticLocations);

                    TwoOptRouter knapRouter = new TwoOptRouter(scorer.getCenter(), knapLocations);
                    TwoOptRouter heuristicRouter = new TwoOptRouter(scorer.getCenter(), heursticLocations);
                    knapDistance += knapRouter.getTotalDist();
                    knapHeuristicDistance += heuristicRouter.getTotalDist();
                }

                double knapTimeTaken = knapDistance / 75;
                double heursticTimeTaken = knapHeuristicDistance / 75;


                sb.append(String.format("%s,%s,%.2f,%.2f,%.2f,%.2f\n", currentCity, timeConstraint, knapTimeTaken,heursticTimeTaken, totalKnapScore, totalHeursiticScore));
            }
        }
    }

    public static double getTotalScore(List<Location> locations) {
        double score = 0;
        for (Location l : locations) {
            score += l.getScore();
        }

        return score;
    }

    public static void writeToCSV(String fileName, StringBuilder linesToAdd) {
        try (PrintWriter writer = new PrintWriter(new File(fileName))) {

            writer.write(linesToAdd.toString());

            System.out.println("done!");

        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }


}
