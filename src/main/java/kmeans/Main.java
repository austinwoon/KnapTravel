package kmeans;

import entities.Location;
import entities.Coordinate;
import jsonReader.JsonReader;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        JsonReader js = new JsonReader();
        List<Location> locations = new ArrayList<>();

        for(JSONObject datapoint : js.getContents()) {
            String name = datapoint.get("name").toString().strip();
            JSONObject coordinatesJSON = new JSONObject((Map) datapoint.get("location"));

            Double lat = Double.parseDouble(coordinatesJSON.get("latitude").toString());
            Double lng = Double.parseDouble(coordinatesJSON.get("longitude").toString());

            locations.add(new Location(name, new Coordinate(lat, lng)));
        }

        Map<Centroid, List<Location>> clusters = Kmeans.fit(locations, 5, 1000);
        for (Centroid c : clusters.keySet()) {
            System.out.println(c);
        }

    }

}
