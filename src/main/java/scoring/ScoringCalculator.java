package scoring;

import entities.Coordinate;
import entities.Location;
import jsonReader.JsonReader;
import org.json.simple.JSONObject;

import java.util.*;

public class ScoringCalculator {

    private List<Location> locations;
    private HashSet<String> preferences;
    private String filePath;

    private static final double PREFERENCE_WEIGHT = 1.5;
    private static final double POPULAR_WEIGHT = 1.2;
    private static Map<String, Integer> tagCount = new HashMap<>();

    /**
     * @param preferences
     * @param filePath
     */
    public ScoringCalculator(HashSet<String> preferences, String filePath) {
        this.preferences = preferences;
        this.filePath = filePath;
        this.locations = new ArrayList<Location>();
        this.generateLocationsWithScores();
    }

    /**
     * Generate static locations attribute from given preferences and filePath inputs
     */
    private void generateLocationsWithScores() {
        JsonReader jr = new JsonReader(filePath);
        List<JSONObject> data = jr.getContents();

        for (JSONObject location : data) {

            double score = (double) location.get("score");
            List<LinkedHashMap> properties = (List<LinkedHashMap>) location.get("properties");
            String address = "";
            String website = "";
            String price = "";
            String openingHours = "";

            for (LinkedHashMap property : properties) {
                switch ((String) property.get("key")) {
                    case "address":
                        address = property.get("value") + "";
                        break;

                    case "website":
                        website = property.get("value") + "";
                        break;

                    case "price":
                        price = property.get("value") + "";
                        break;

                    case "hours":
                        openingHours = property.get("value") + "";
                }
            }
            String description = (String) location.get("intro");

            if ((int) location.get("popular") == 1) {
                score *= POPULAR_WEIGHT;
            }


            List<LinkedHashMap> tags = (List<LinkedHashMap>) location.get("tags");
            for (LinkedHashMap tag : tags) {
                LinkedHashMap innerTag = (LinkedHashMap) tag.get("tag");
                String tagName = (String) innerTag.get("name");

                if (tagCount.containsKey(tagName)) {
                    int count = tagCount.get(tagName);
                    tagCount.put(tagName, ++count);
                } else {
                    tagCount.put(tagName, 1);
                }

                if (preferences.size() != 0) {
                    if (preferences.contains(tagName)) {
                        score *= PREFERENCE_WEIGHT;
                    }
                }
            }

            String name = (String) location.get("name");
            double hours = (Double) location.get("hours_spent");

            Map<String, Double> cm = (Map<String, Double>) location.get("coordinates");
            try {
                Coordinate coordinates = new Coordinate(cm.get("latitude"), cm.get("longitude"));
                locations.add(new Location(name, coordinates, score, hours, description, website, price, openingHours, address));
            } catch (ClassCastException e) {
                System.out.println(location.get("name"));
            }
        }
    }

    public List<Location> getLocations() {
        return locations;
    }

    public static Map<String, Integer> getTagCount() {
        return tagCount;
    }

    /**
     * Gets a number of tags that can be selected according to a lower limit
     *
     * @param countLowerLimit threshold that tags must meet to be returned
     * @return list of tags that met threshold
     */
    public List<String> getFilteredTags(int countLowerLimit) {
        List<String> tags = new ArrayList<>();

        for (String tagName : tagCount.keySet()) {
            if (tagCount.get(tagName) >= countLowerLimit) {
                tags.add(tagName);
            }
        }
        return tags;
    }
}