package routing;

import entities.Coordinate;
import entities.Location;

import java.util.*;

public class TwoOptRouter extends Router{

  private List<Location> route;
  int nearestDist = Integer.MAX_VALUE;

  public TwoOptRouter(Coordinate startingPoint, List<Location> clusterLocations) {
    super(startingPoint);
    GreedyRouter greedyRouter = new GreedyRouter(startingPoint, clusterLocations);
    List<Location> greedyRoute = greedyRouter.getRoute();
    int greedyDistance = greedyRouter.getTotalDist();
    route = swapLocations(greedyRoute, greedyDistance);
  }

  private List<Location> swapLocations (List<Location> route, int minDistance) {
    List<Location> shortestRoute = new ArrayList<>(route);
    Random random = new Random();

    int num = route.size();
    for (int i = 0; i < num * num; i++) {
      List<Location> temp = new ArrayList<>(shortestRoute);
      int swap1 = random.nextInt(num);
      int swap2 = random.nextInt(num);
      Collections.swap(temp, swap1, swap2);
      int newDist = calDist(temp);
      if (newDist <= minDistance) {
        shortestRoute = temp;
        minDistance = newDist;
      }
    }
    nearestDist = minDistance;
    return shortestRoute;
  }

  public List<Location> getRoute() {
    return route;
  }

  public int getTotalDist () {
    return nearestDist;
  }
}
