package kmeans;

import java.util.Map;

public class EuclideanDistance {
    public double calculate(Map<String, Double> location1, Map<String, Double> location2) {
        double sum = 0;

        Double s1 = Math.pow(location1.get("lat") - location2.get("lat"),2);
        Double s2 = Math.pow(location1.get("lng") - location2.get("lng"), 2);

        return Math.sqrt(s1 + s2);
    }
    public double calculate(Location location1, Location location2) {
        double sum = 0;

        Map<String, Double> c1 = location1.getCoordinate().getCoordinates();
        Map<String, Double> c2 = location2.getCoordinate().getCoordinates();

        Double s1 = Math.pow(c1.get("lat") - c2.get("lat"),2);
        Double s2 = Math.pow(c1.get("lng") - c2.get("lng"), 2);

        return Math.sqrt(s1 + s2);
    }
}
