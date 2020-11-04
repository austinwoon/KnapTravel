package knapsack;

import entities.Coordinate;
import entities.Location;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class KnapsackLocations {
  private static final int TIME_LIMIT = 8 * 2;

  public static List<Location> getTopLocations(List<Location> locations) {
    List<Location> results = new ArrayList<>();
    int num = locations.size();

    double table[][] = new double[num + 1][TIME_LIMIT + 1];
// create knapsack table
    for (int row = 0; row <= num; row++) {
      for (int col = 0; col <= TIME_LIMIT; col++) {
        if (row == 0 || col == 0) {
          table[row][col] = 0;
        } else if (locations.get(row - 1).getHours() <= col) {
          if (col - locations.get(row - 1).getHours() < 0) {
            table[row][col] = table[row - 1][col];
          } else {
            table[row][col] = Double.max(table[row - 1][col],
                locations.get(row - 1).getScore() + table[row - 1][col - locations.get(row - 1).getHours()]);
          }
        } else {
          table[row][col] = table[row - 1][col];
        }
      }
    }

//    for (double[] rows : table) {
//      for (double nums: rows) {
//        System.out.print(nums + " , ");
//      }
//      System.out.println();
//    }
//  get items that contributed to the highest score
    int traverseRow = num;
    int traverseCol = TIME_LIMIT;

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

  public static void main(String[] args) {
    Coordinate co = new Coordinate(4.0,3.0);
    Location loc1 = new Location("hi", co, 5.0, 3);
    Location loc2 = new Location("hi", co, 6.0, 3);
    Location loc3 = new Location("hi", co, 7.0, 3);
    Location loc4 = new Location("hi", co, 5.0, 3);
    Location loc5 = new Location("hi", co, 5.0, 3);

    List<Location> locations = new ArrayList<>();
    locations.add(loc1);
    locations.add(loc2);
    locations.add(loc3);
    locations.add(loc4);
    locations.add(loc5);

    for (Location loc: getTopLocations(locations)) {
      System.out.println(loc.getScore());
    }

  }
}
