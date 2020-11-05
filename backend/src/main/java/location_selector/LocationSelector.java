package location_selector;

import entities.Location;
import java.util.List;

public interface LocationSelector {
    /**
     * Returns the selected locations to visit from a list of locations
     * @return List of locations selected to meet within time constraint
     */
    List<Location> selectLocationsToVisit();
}
