package kmeans;

import entities.Location;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Kmeans {
    private static final Random random = new Random();

    public static Map<Centroid, List<Location>> fit(List<Location> locations, int k, EuclideanDistance euclideanDistance, int maxIterations) {
        applyPreconditions(locations, k, euclideanDistance, maxIterations);

        List<Centroid> centroids = randomCentroids(locations, k);
        Map<Centroid, List<Location>> clusters = new HashMap<>();
        Map<Centroid, List<Location>> lastState = new HashMap<>();

        for (int i = 0; i < maxIterations; i++) {
            boolean isLastIteration = i == maxIterations - 1;

            for (Location location : locations) {
                Centroid centroid = nearestCentroid(location, centroids, euclideanDistance);
                assignToCluster(clusters, location, centroid);
            }
            // termination condition
            boolean shouldTerminate = isLastIteration || clusters.equals(lastState);
            lastState = clusters;
            if (shouldTerminate) {
                break;
            }

            centroids = relocateCentroids(clusters);
            clusters = new HashMap<>();
        }

        return lastState;
    }

    private static List<Centroid> relocateCentroids(Map<Centroid, List<Location>> clusters) {
        return clusters.entrySet().stream().map(e -> average(e.getKey(), e.getValue())).collect(toList());
    }

    private static Centroid average(Centroid centroid, List<Location> locations) {
        if (locations == null || locations.isEmpty()) {
            return centroid;
        }

        Map<String, Double> average = centroid.getLatLng();

        locations.stream().flatMap(e -> e.getLatLng().keySet().stream()).forEach(k -> average.put(k, 0.0));

        for (Location l : locations) {
            l.getLatLng().forEach((k, v) -> average.compute(k, (k1, currentValue) -> v + currentValue));
        }

        average.forEach((k, v) -> average.put(k, v / locations.size()));

        return new Centroid(new Coordinate(average));
    }

    private static void assignToCluster(Map<Centroid, List<Location>> clusters, Location location, Centroid centroid) {
        clusters.compute(centroid, (key, list) -> {
            if (list == null) {
                list = new ArrayList<>();
            }

            list.add(location);
            return list;
        });
    }

    private static Centroid nearestCentroid(Location location, List<Centroid> centroids, EuclideanDistance distance) {
        double minimumDistance = Double.MAX_VALUE;
        Centroid nearest = null;

        for (Centroid centroid : centroids) {
            double currentDistance = distance.calculate(location.getLatLng(), centroid.getLatLng());

            if (currentDistance < minimumDistance) {
                minimumDistance = currentDistance;
                nearest = centroid;
            }
        }

        return nearest;
    }

    private static List<Centroid> randomCentroids(List<Location> locations, int k) {
        List<Centroid> centroids = new ArrayList<>();
        Map<String, Double> maxs = new HashMap<>();
        Map<String, Double> mins = new HashMap<>();

        for (Location location : locations) {
            location
                    .getLatLng()
                    .forEach((key, value) -> {
                        // compares the value with the current max and choose the bigger value between them
                        maxs.compute(key, (k1, max) -> max == null || value > max ? value : max);

                        // compare the value with the current min and choose the smaller value between them
                        mins.compute(key, (k1, min) -> min == null || value < min ? value : min);
                    });
        }

        Set<String> attributes = locations
                .stream()
                .flatMap(e -> e
                        .getLatLng()
                        .keySet()
                        .stream())
                .collect(toSet());
        for (int i = 0; i < k; i++) {
            Map<String, Double> coordinates = new HashMap<>();
            for (String attribute : attributes) {
                double max = maxs.get(attribute);
                double min = mins.get(attribute);
                coordinates.put(attribute, random.nextDouble() * (max - min) + min);
            }

            centroids.add(new Centroid(new Coordinate(coordinates)));
        }

        return centroids;
    }

    private static void applyPreconditions(List<Location> locations, int k, EuclideanDistance distance, int maxIterations) {
        if (locations == null || locations.isEmpty()) {
            throw new IllegalArgumentException("The dataset can't be empty");
        }

        if (k <= 1) {
            throw new IllegalArgumentException("It doesn't make sense to have less than or equal to 1 cluster");
        }

        if (distance == null) {
            throw new IllegalArgumentException("The distance calculator is required");
        }

        if (maxIterations <= 0) {
            throw new IllegalArgumentException("Max iterations should be a positive number");
        }
    }
}
