package algo.models;
import entities.Location;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public @Data class ItineraryResponseModel {
    private List<Map<Integer, Location>> data;

    public ItineraryResponseModel(Map<Integer, List<Location>> locationMap) {
        //TODO(Austin): Remove this fake sequencing logic with HL's inter-day routing algo
        List<Map<Integer, Location>> sequencedLocations = new ArrayList<>();

        for(List<Location> ll : locationMap.values()) {
            Map<Integer, Location> interDayVisitSequence = new HashMap<>();
            int i = 0;

            for (Location l : ll) {
                interDayVisitSequence.put(i++, l);
            }

            sequencedLocations.add(interDayVisitSequence);
        }

        this.data = sequencedLocations;
    }
}
