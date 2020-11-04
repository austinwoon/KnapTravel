package kmeans;

import entities.Location;
import location_picker.KnapsackLocationSelector;
import location_picker.LocationSelector;
import scoring.ScoringCalculator;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class TriposoMain {
    public static void main(String[] args) {

        HashSet<String> pref = new HashSet<>();
        pref.add("Museums");
        ScoringCalculator scorer = new ScoringCalculator(pref, "src/main/resources/data/tokyo-processed.json");

        List<Location> locations = scorer.getLocations();
        Map<Integer, List<Location>> clusters = Kmeans.fit(locations, 5, 100000);

        LocationSelector knapper = new KnapsackLocationSelector(clusters.get(2), 9);

        for (Location location : knapper.selectLocationsToVisit()) {
            System.out.println(location.getName() + ": " + location.getScore() + ", " + (location.getHours() / 2.0));
        }

    }

}
