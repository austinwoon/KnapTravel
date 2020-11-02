package scoring;

import entities.Coordinate;
import entities.Location;
import jsonReader.JsonReader;
import knapsack.KnapsackLocations;
import org.json.simple.JSONObject;

import java.util.*;

public class ScoringCalculator {

  private static List<Location> locations = new ArrayList<>();

  private static final double PREFERENCE_WEIGHT = 1.5;
  private static final double POPULAR_WEIGHT = 1.2;

  public ScoringCalculator(HashSet<String> preferences) {
    JsonReader jr = new JsonReader();
    List<JSONObject> data = jr.getContents();

    System.out.println(data.get(0).keySet());
    System.out.println(data.get(0).get("tags"));
    for (JSONObject location : data) {

      double score = (double) location.get("score");
      if ((int) location.get("popular") == 1) {
        score *= POPULAR_WEIGHT;
      }

      if (preferences.size() != 0) {
        List<LinkedHashMap> tags = (List<LinkedHashMap>) location.get("tags");
        for (LinkedHashMap tag : tags) {
          LinkedHashMap innerTag = (LinkedHashMap) tag.get("tag");
          String tagName = (String) innerTag.get("name");
          if (preferences.contains(tagName)) {
            score *= PREFERENCE_WEIGHT;
          }
        }
      }

      String name = (String) location.get("name");
      double hours = (Double) location.get("hours_spent") * 2;

      Map<String, Double> cm = (Map<String, Double>) location.get("coordinates");
      try {
        Coordinate coordinates = new Coordinate(cm.get("latitude"), cm.get("longitude"));
        locations.add(new Location(name, coordinates, score, (int) hours));
      } catch (ClassCastException e) {
        System.out.println(location.get("name"));
      }
    }

  }

  public static List<Location> getLocations() {
    return locations;
  }

  public static void main(String[] args) {
    HashSet<String> pref = new HashSet<>();
//    pref.add("Museums");
    new ScoringCalculator(pref);
    KnapsackLocations knapper =new KnapsackLocations();
    for (Location location : knapper.getTopLocations(getLocations())) {
      System.out.println(location.getName() + ": " + location.getScore());
    }
  }



}