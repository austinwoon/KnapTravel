package routing;

import entities.Coordinate;
import entities.Location;

import java.util.ArrayList;
import java.util.List;

public class GreedyRouter extends Router {

  private List<Location> route;
  private int totalDist = 0;

  public GreedyRouter (Coordinate startingPoint, List<Location> clusterLocations) {
    super(startingPoint);
    route = new ArrayList<>();
    Coordinate start = startingPoint;
    List<Location> temp = new ArrayList<>(clusterLocations);

    Location nearest = null;
    while (route.size() < clusterLocations.size()) {
      int nearestDist = Integer.MAX_VALUE;
      nearest = null;
      for (Location location : temp) {
        int dist = calculateDistanceInKilometer(start, location.getCoordinate());
        if (dist < nearestDist) {
          nearest = location;
          nearestDist = dist;
        }
      }
      route.add(nearest);
      start = nearest.getCoordinate();
      temp.remove(nearest);
      totalDist += nearestDist;
    }

    totalDist += calculateDistanceInKilometer(nearest.getCoordinate(), startingPoint);
  }

  public int getTotalDist() { return calDist(route); }

  public List<Location> getRoute() {
    return route;
  }
}
