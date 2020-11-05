package jsonReader;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JsonReader {
  private String filePath;
  List<JSONObject> data = new ArrayList<>();

  public JsonReader(String filePath) {
    this.filePath = filePath;
    readData();
  }

  private void readData() {
    ObjectMapper mapper = new ObjectMapper();
    try {
      data = mapper.readValue(new File(filePath), new TypeReference<List<JSONObject>>() {
      });
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public List<JSONObject> getContents() {
    return data;
  }
}
