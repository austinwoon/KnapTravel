package clustering.circle;

import entities.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CircleCluster {
    /**
     * This function gives a split to the data points based on the a percentile calculation of the angle of the points from origin.
     *
     * @param locations List of locations
     * @param n         Number of splits
     * @return List of list of coordinates in each split
     */
    public static List<List<Location>> getNSplits(List<Location> locations, int n) {
        List<List<Location>> resultArr = new ArrayList<>();
        List<Double> xCoordinates = new ArrayList<>();
        List<Double> yCoordinates = new ArrayList<>();
        for (Location location : locations) {
            Map<String, Double> coordinatesMap = location.getLatLng();
            xCoordinates.add(coordinatesMap.get("lng"));
            yCoordinates.add(coordinatesMap.get("lat"));
        }
        // Create number of regions needed based on the number of trucks, and transform each of the data points to
        // its own angle from the origin, in terms of radians.
        List<Double> percentilesToCheck = genFractions(n);
        List<Double> aTanGrads = new ArrayList<>();
        for (int i = 0; i < xCoordinates.size(); i++) {
            double xTemp = xCoordinates.get(i);
            double yTemp = yCoordinates.get(i);
            aTanGrads.add(Math.atan2(yTemp, xTemp));
        }
        List<Double> checkGradients = new ArrayList<>();
        // Insert lower bound as negative pi radians.
        checkGradients.add(-Math.PI);
        for (double percentile : percentilesToCheck) {
            double currAtanGradient = percentile(aTanGrads, percentile);
            checkGradients.add(currAtanGradient);
        }
        // Insert upper bound as positive pi radians.
        checkGradients.add(Math.PI);
        // Sort the gradients (angles) from the origin so we know which region to insert which point.
        checkGradients.sort(Double::compareTo);

        for (int i = 0; i < checkGradients.size(); i++) {
            Double currAtanGradient = checkGradients.get(i);
            List<Location> currArray = new ArrayList<>();
            for (int j = 0; j < aTanGrads.size(); j++) {
                Double aTanGrad = aTanGrads.get(j);
                Double currAtanGradAhead = checkGradients.get((i + 1) % checkGradients.size());
                // Data is separated here comparing 2 gradients. The gradients here represent regression lines passing through the origin.
                if (aTanGrad >= currAtanGradient && aTanGrad < currAtanGradAhead) {
                    Location location = locations.get(j);
                    currArray.add(location);
                }
            }
            resultArr.add(currArray);
        }

        resultArr.removeIf(result -> result.size() == 0);
        return resultArr;
    }

    public static List<Double> genFractions(int n) {
        List<Double> resultArr = new ArrayList<>();
        double smallestFraction = 1.0d / (double) n;
        double smallestRatio = smallestFraction;
        while (smallestFraction < 0.995) {
            resultArr.add(smallestFraction * 100);
            smallestFraction += smallestRatio;
        }
        return resultArr;
    }

    public static double percentile(List<Double> aTanGrads, double percentile) {
        int index = (int) (percentile / 100 * aTanGrads.size());
        return aTanGrads.get(index);
    }
}
