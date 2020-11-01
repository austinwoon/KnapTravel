package kmeans;

import entities.Coordinate;
import entities.Location;
import jsonReader.JsonReader;
import jsonReader.TriposoJsonReader;
import org.apache.commons.text.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TriposoMain {
    public static void main(String[] args) {
        TriposoJsonReader js = new TriposoJsonReader("Tokyo");
        List<Location> locations = new ArrayList<>();

        for(Map<String, Object> datapoint : js.getContents()) {

            String name = datapoint.get("name").toString().strip();
//            JSONObject coordinatesJSON = new JSONObject((Map) datapoint.get("location"));

//            Double lat = Double.parseDouble(coordinatesJSON.get("latitude").toString());
//            Double lng = Double.parseDouble(coordinatesJSON.get("longitude").toString());
            Map<String, Double> coordinates = (Map<String, Double>) datapoint.get("coordinates");
            Double lat = coordinates.get("latitude");
            Double lng = coordinates.get("longitude");

            locations.add(new Location(name, new Coordinate(lat, lng)));
        }

        Map<Centroid, List<Location>> clusters = Kmeans.fit(locations, 5, 100000);
//        for (Centroid c : clusters.keySet()) {
//            System.out.println(c);
//        }

        int i = 0;
        System.out.println("Name,Lat,Lng,Cluster");
        for (List<Location> locationCluster : clusters.values()) {
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
