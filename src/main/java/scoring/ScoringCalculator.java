package scoring;

import jsonReader.JsonReader;
import kmeans.Location;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ScoringCalculator {

  private static List<Location> locations = new ArrayList<>();

  private String dataPath = "src/main/resources/aggregated_data.json";

  public ScoringCalculator(HashSet<String> preferences) {
    JsonReader jr = new JsonReader();
    List<JSONObject> data = jr.getContents();

    for (JSONObject location : data) {
      System.out.println(location.get("name"));
    }

  }

  public static void main(String[] args) {
    HashSet<String> pref = new HashSet<>();
    new ScoringCalculator(pref);
  }

}