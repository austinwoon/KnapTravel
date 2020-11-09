package routing;

import entities.Coordinate;
import entities.Location;

import java.util.List;

public class Router {
  public final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;
  private final Coordinate STARTING_POINT;

  public Router (Coordinate startingPoint) {
    this.STARTING_POINT = startingPoint;
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

  public int calDist (List<Location> clusterLocations) {
    int results = calculateDistanceInKilometer(STARTING_POINT, clusterLocations.get(0).getCoordinate());
    for (int i = 0; i < clusterLocations.size() - 1; i++) {
      results += calculateDistanceInKilometer(clusterLocations.get(i).getCoordinate(), clusterLocations.get(i + 1).getCoordinate());
    }
    results += calculateDistanceInKilometer(clusterLocations.get(clusterLocations.size() - 1).getCoordinate(), STARTING_POINT);
    return  results;
  }
}
