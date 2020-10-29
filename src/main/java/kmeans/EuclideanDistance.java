package kmeans;

import entities.Location;

import java.util.Map;

public class EuclideanDistance {
    public double calculate(Location location1, Location location2) {
        double sum = 0;

        Map<String, Double> c1 = location1.getCoordinate().getCoordinates();
        Map<String, Double> c2 = location2.getCoordinate().getCoordinates();

        Double s1 = Math.pow(c1.get("lat") - c2.get("lat"),2);
        Double s2 = Math.pow(c1.get("lng") - c2.get("lng"), 2);

        return Math.sqrt(s1 + s2);
    }
}
