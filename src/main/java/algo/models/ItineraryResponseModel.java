package algo.models;
import entities.Location;
import lombok.Data;

import java.util.List;
import java.util.Map;

public @Data class ItineraryResponseModel {
    private Map<Integer, List<Location>> locationMap;

    public ItineraryResponseModel(Map<Integer, List<Location>> locationMap) {
        this.locationMap = locationMap;
    }
}
