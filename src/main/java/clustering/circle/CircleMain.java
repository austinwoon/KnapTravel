package clustering.circle;

import entities.Coordinate;
import entities.Location;
import jsonReader.JsonReader;
import kmeans.Centroid;
import kmeans.Kmeans;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.tomcat.util.buf.StringUtils;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CircleMain {
    public static void main(String[] args) {
        JsonReader js = new JsonReader("/src/main/resources/data/tokyo-processed.json");
        List<Location> locations = new ArrayList<>();

        for (JSONObject datapoint : js.getContents()) {
            String name = datapoint.get("name").toString().strip();
            JSONObject coordinatesJSON = new JSONObject((Map) datapoint.get("location"));

            Double lat = Double.parseDouble(coordinatesJSON.get("latitude").toString());
            Double lng = Double.parseDouble(coordinatesJSON.get("longitude").toString());

            locations.add(new Location(name, new Coordinate(lat, lng)));
        }

        List<List<Location>> locationClusters = CircleCluster.getNSplits(locations, 5);
        int i = 0;
        System.out.println("Name,Lat,Lng,Cluster");
        for (List<Location> locationCluster : locationClusters) {
            StringBuilder outputString = new StringBuilder();
            for (Location loc : locationCluster) {
                Map<String, Double> latLng = loc.getLatLng();
                Double lat = latLng.get("lat");
                Double lng = latLng.get("lng");
                String escaped = StringEscapeUtils.escapeCsv(loc.getName());
                outputString.append(String.format("%s,%f,%f,%d\n", escaped, lat, lng, i));
            }
            System.out.println(outputString.substring(0, outputString.length() - 1));
//            System.out.println("-----");
            i++;
        }

    }

}
