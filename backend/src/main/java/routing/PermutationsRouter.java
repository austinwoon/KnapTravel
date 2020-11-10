package routing;

import entities.Coordinate;
import entities.Location;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PermutationsRouter extends Router{

  private List<Location> route;
  int nearestDist = Integer.MAX_VALUE;
  int counter = 0;

  public PermutationsRouter(Coordinate startingPoint, List<Location> clusterLocations) {
    super (startingPoint);
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
//        Location temp = clusterLocations.get(0);
//        clusterLocations.set(0, clusterLocations.get(size - 1));
//        clusterLocations.set(size - 1, temp);
        Collections.swap(clusterLocations, 0, size - 1);
      } else {
//        Location temp = clusterLocations.get(i);
//        clusterLocations.set(i, clusterLocations.get(size - 1));
//        clusterLocations.set(size - 1, temp);
        Collections.swap(clusterLocations, i, size - 1);
      }
    }
  }

  public int getTotalDist () {
    return nearestDist;
  }
}
