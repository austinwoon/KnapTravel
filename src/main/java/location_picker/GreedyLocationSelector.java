package location_picker;

import entities.Location;

import java.util.List;

public class GreedyLocationSelector implements LocationSelector {
    private int timeConstraint;
    private List<Location> locations;

    public GreedyLocationSelector(List<Location> locations, int timeConstraint) {
        this.locations = locations;
        this.timeConstraint = timeConstraint;
    }

    private List<Location> heapify(List<Location> locations) {
        return locations;
    }

    @Override
    public List<Location> selectLocationsToVisit() {
        return locations;
    }
}
