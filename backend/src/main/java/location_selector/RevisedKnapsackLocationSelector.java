package location_selector;

import entities.Coordinate;
import entities.Location;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class RevisedKnapsackLocationSelector implements LocationSelector {
  public final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;
  public final static int OUTSIDE_CLUSTER_DISTANCE = 20;

  private int timeConstraint;
  private List<Location> locations;
  Coordinate center;


  public RevisedKnapsackLocationSelector(List<Location> locations, int timeConstraint, Coordinate startingPoint) {
    // multiply timeConstraint by two because our hours are in floats of 0.5, for ILP, we want whole numbers
    // so that you can access the scores by index in the ILP matrix
    this.locations = locations;
    this.center = getClusterCentre(locations);
    int distanceFromHotel = calculateDistanceInKilometer(center, startingPoint);
    int speedOnCar = 90; // 90km/hr
    int timeTakenToCluster = (int) Math.ceil(distanceFromHotel / speedOnCar);
    this.timeConstraint = (timeConstraint - timeTakenToCluster) * 2;
  }

  private Coordinate getClusterCentre (List<Location> locations) {
    int locationNum = 0;
    double totalLat = 0;
    double totalLong = 0;

    for (Location location : locations) {
      totalLat += location.getCoordinate().getCoordinates().get("lat");
      totalLong += location.getCoordinate().getCoordinates().get("lng");
      locationNum ++;
    }

    double avgLat = totalLat / locationNum;
    double avgLong = totalLong / locationNum;

    return new Coordinate(avgLat, avgLong);
  }

  @Override
  public List<Location> selectLocationsToVisit() {
    List<Location> results = new ArrayList<>();
    int num = locations.size();

//    System.out.println(timeConstraint);
    double[][] table = new double[num + 1][timeConstraint + 1];
    // create knapsack table
    for (int row = 0; row <= num; row++) {
      for (int col = 0; col <= timeConstraint; col++) {
//        System.out.println(getTotalTimeTaken(locations.get(row)) + ", ");
        if (row == 0 || col == 0) {
          table[row][col] = 0;
        } else if (getTotalTimeTaken(locations.get(row - 1)) <= col) {
          if (col - getTotalTimeTaken(locations.get(row - 1)) < 0) {
            table[row][col] = table[row - 1][col];
          } else {
            table[row][col] = Double.max(table[row - 1][col],
                locations.get(row - 1).getScore() + table[row - 1][col - getTotalTimeTaken(locations.get(row - 1))]);
          }
        } else {
          table[row][col] = table[row - 1][col];
        }
      }
    }

//    for (double row[] : table) {
//      for (double col : row) {
//        System.out.print(col + " ,");
//      }
//      System.out.println("new row");
//    }
//  get items that contributed to the highest score

    int traverseRow = num;
    int traverseCol = timeConstraint;

    while (traverseRow >= 0 && traverseCol >= 0) {

      double biggest = table[traverseRow][traverseCol];

      while (biggest == table[traverseRow - 1][traverseCol]) {
        traverseRow--;
      }
      Location shortlisted = locations.get(traverseRow - 1);
      results.add(shortlisted);
      biggest -= shortlisted.getScore();
      if (biggest == 0) {
        break;
      }
      traverseRow--;
      DecimalFormat df = new DecimalFormat("#.########");
      while (traverseCol >= 0 && !df.format(biggest).equals(df.format(table[traverseRow][traverseCol]))) {
        traverseCol--;
      }
    }

    return results;
  }

  private int getTotalTimeTaken (Location location) {
    double timeSpent = location.getHours();
    int distanceFromCenter = calculateDistanceInKilometer(center, location.getCoordinate());
    int clusterSpeed = 50; // 50km/hr
    double travelTime = distanceFromCenter / clusterSpeed;

    int totalTime = (int) Math.ceil((timeSpent + travelTime) * 2);
    return totalTime;
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

//  public static void main(String[] args) {
//    Coordinate co = new Coordinate(4.0,3.0);
//    Location loc1 = new Location("hi", co, 5.0, 3);
//    Location loc2 = new Location("hi", co, 6.0, 3);
//    Location loc3 = new Location("hi", co, 7.0, 3);
//    Location loc4 = new Location("hi", co, 5.0, 3);
//    Location loc5 = new Location("hi", co, 5.0, 3);
//
//    List<Location> locations = new ArrayList<>();
//    locations.add(loc1);
//    locations.add(loc2);
//    locations.add(loc3);
//    locations.add(loc4);
//    locations.add(loc5);
//
//    for (Location loc: selectLocationsToVisit(locations)) {
//      System.out.println(loc.getScore());
//    }
//
//  }
}
