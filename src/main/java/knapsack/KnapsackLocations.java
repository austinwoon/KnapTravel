package knapsack;

import entities.Location;

import java.util.ArrayList;
import java.util.List;

public class KnapsackLocations {
  private static final int TIME_LIMIT = 8;

  public static List<Loc> getTopLocations(List<Loc> locations) {
    List<Loc> results = new ArrayList<>();
    int num = locations.size();

    double table[][] = new double[num + 1][TIME_LIMIT + 1];
// create knapsack table
    for (int row = 0; row <= num; row++) {
      for (int col = 0; col <= TIME_LIMIT; col++) {
        if (row == 0 || col == 0) {
          table[row][col] = 0;
        } else if (locations.get(row - 1).getWeight() <= col) {
          if (col - locations.get(row - 1).getWeight() < 0) {
            table[row][col] = table[row - 1][col];
          } else {
            table[row][col] = Double.max(table[row - 1][col],
                locations.get(row - 1).getScore() + table[row - 1][col - locations.get(row - 1).getWeight()]);
          }
        } else {
          table[row][col] = table[row - 1][col];
        }
      }
    }
//  get items that contributed to the highest score
    int traverseRow = num;
    int traverseCol = TIME_LIMIT;

    while (traverseRow != 0) {
      double biggest = table[traverseRow][traverseCol];

      while (biggest == table[traverseRow - 1][traverseCol]) {
        traverseRow--;
      }
      Loc shortlisted = locations.get(traverseRow - 1);
      results.add(shortlisted);
      biggest -= shortlisted.getScore();
      if (biggest == 0) {
        break;
      }
      traverseRow--;
      while (biggest != table[traverseRow][traverseCol]) {
        traverseCol--;
      }
    }
    return results;
  }

  public static void main(String[] args) {
    Loc loc1 = new Loc(1, 1);
    Loc loc2 = new Loc(3, 4);
    Loc loc3 = new Loc(4, 15);
    Loc loc4 = new Loc(2, 10);
    Loc loc5 = new Loc(5, 11);

    List<Loc> locations = new ArrayList<>();
    locations.add(loc1);
    locations.add(loc2);
    locations.add(loc3);
    locations.add(loc4);
    locations.add(loc5);

    for (Loc loc: getTopLocations(locations)) {
      System.out.println(loc.getScore());
    }

  }
}
