package scoring;

import entities.Coordinate;
import entities.Location;
import jsonReader.JsonReader;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ScoringCalculator {

  private static List<Location> locations = new ArrayList<>();

  private static final int PREFERENCE_POINTS = 1;

  public ScoringCalculator(HashSet<String> preferences) {
    JsonReader jr = new JsonReader();
    List<JSONObject> data = jr.getContents();

    for (JSONObject location : data) {
      double rating;
      try {
        rating = (Double) location.get("rating");
      } catch (ClassCastException e){
        rating = (int)location.get("rating");
      }

      double score = rating;
      if (preferences.size() != 0) {
        List<String> tags = (List<String>) location.get("tags");
        for (String tag : tags) {
          if (preferences.contains(tag)) {
            score += PREFERENCE_POINTS;
          }
        }
      }

      String name = (String) location.get("name");
      Map<String, Double> cm = (Map<String, Double>) location.get("location");
      try {
        Coordinate coordinates = new Coordinate(cm.get("latitude"), cm.get("longitude"));
        locations.add(new Location(name, coordinates, score));
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
    pref.add("Museums");
    new ScoringCalculator(pref);
    for (Location location : getLocations()) {
      System.out.println(location.getName() + ": " + location.getScore());
    }
  }



}