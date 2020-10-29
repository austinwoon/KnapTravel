package scoring;

import entities.Location;
import jsonReader.JsonReader;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ScoringCalculator {

  private static List<Location> locations = new ArrayList<>();

  private static final int PREFERENCE_POINTS = 1;

  public ScoringCalculator(HashSet<String> preferences) {
    JsonReader jr = new JsonReader();
    List<JSONObject> data = jr.getContents();

    for (JSONObject location : data) {
      System.out.println(location.get("tags"));
    }

  }

  public static void main(String[] args) {
    HashSet<String> pref = new HashSet<>();
    new ScoringCalculator(pref);
  }

}