package routing;

import entities.Coordinate;
import entities.Location;

import java.util.ArrayList;
import java.util.List;

public class PermutationsRouter {
  public final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

  private List<Location> route;
  int nearestDist = Integer.MAX_VALUE;
  private final Coordinate STARTING_POINT;
  int counter = 0;

  public PermutationsRouter(Coordinate startingPoint, List<Location> clusterLocations) {
    this.STARTING_POINT = startingPoint;
    List<Location> temp = new ArrayList<>(clusterLocations);
    heapPermutation(temp, clusterLocations.size(), clusterLocations.size());
  }

  private void heapPermutation(List<Location> tempList, int size, int n) {
    List<Location> clusterLocations = new ArrayList<>(tempList);
      if (size == 1) {
        int dist = calDist(clusterLocations);
        counter ++;
        if (dist < nearestDist) {
          route = clusterLocations;
          nearestDist = dist;
        }
        return;
    }

    for (int i = 0; i < size; i++) {
      heapPermutation(clusterLocations, size - 1, n);

      if (i % 2 == 1) {
        Location temp = clusterLocations.get(0);
        clusterLocations.set(0, clusterLocations.get(size - 1));
        clusterLocations.set(size - 1, temp);
      } else {
        Location temp = clusterLocations.get(i);
        clusterLocations.set(i, clusterLocations.get(size - 1));
        clusterLocations.set(size - 1, temp);
      }
    }
  }

  private int calDist (List<Location> clusterLocations) {
    int results = calculateDistanceInKilometer(STARTING_POINT, clusterLocations.get(0).getCoordinate());
    for (int i = 0; i < clusterLocations.size() - 1; i++) {
      results += calculateDistanceInKilometer(clusterLocations.get(i).getCoordinate(), clusterLocations.get(i + 1).getCoordinate());
    }
    results += calculateDistanceInKilometer(clusterLocations.get(clusterLocations.size() - 1).getCoordinate(), STARTING_POINT);
    return  results;
  }

  public List<Location> getRoute() {
    return route;
  }

  public int getTotalDist () {
    return nearestDist;
  }

  public int calculateDistanceInKilometer(Coordinate start, Coordinate destination) {
    double venueLat = destination.getCoordinates().get("lat");
    double venueLng = destination.getCoordinates().get("lng");

    double latDistance = Math.toRadians(start.getCoordinates().get("lat") - venueLat);
    double lngDistance = Math.toRadians(start.getCoordinates().get("lng") - venueLng);

    double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
        + Math.cos(Math.toRadians(start.getCoordinates().get("lat"))) * Math.cos(Math.toRadians(venueLat))
        * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH_KM * c));
  }
}
