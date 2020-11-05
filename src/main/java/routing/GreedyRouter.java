package routing;

import entities.Coordinate;
import entities.Location;

import java.util.ArrayList;
import java.util.List;

public class GreedyRouter {
  public final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;

  private List<Location> route;

  public GreedyRouter (Coordinate startingPoint, List<Location> clusterLocations) {
    route = new ArrayList<>();
    Coordinate start = startingPoint;
    List<Location> temp = new ArrayList<>(clusterLocations);

    while (route.size() < clusterLocations.size()) {
      int nearestDist = Integer.MAX_VALUE;
      Location nearest = null;
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
    }
  }

  public List<Location> getRoute() {
    return route;
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
