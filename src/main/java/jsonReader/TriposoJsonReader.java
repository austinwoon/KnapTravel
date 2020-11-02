package jsonReader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TriposoJsonReader {
  private String dataDir = "src/main/resources/data/";

  List<Map<String, Object>> data = new ArrayList<>();
  Map<String, JSONObject> map = new HashMap<>();

  public TriposoJsonReader(String city) {
    ObjectMapper mapper = new ObjectMapper();
    try {
      for (int i = 1; i <= 5; i++) {
        String fileDir = String.format("%s%s_%d.json", dataDir, city.toLowerCase(), i);
        Map<String, Object> map = mapper.readValue(new File(fileDir), new TypeReference<>() {});
        var temp = (List) map.get("results");
        data.addAll(temp);
//        map.putAll(mapper.readValue();
      }
//      data = mapper.readValue(new File(dataPath), new TypeReference<List<JSONObject>>() {
//      });
    } catch (FileNotFoundException e) {
      System.out.println("Iterations completed, stopping...");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Map<String, JSONObject> getMap() {
    return map;
  }

  public List<Map<String, Object>> getContents() {
    return data;
  }
}
