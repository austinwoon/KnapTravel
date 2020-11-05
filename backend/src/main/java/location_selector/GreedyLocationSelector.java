package location_selector;

import datastructures.AbstractPriorityQueue;
import datastructures.HeapPriorityQueue;
import datastructures.MaxHeapPriorityQueue;
import entities.Location;

import java.util.ArrayList;
import java.util.List;

public class GreedyLocationSelector implements LocationSelector {
    private double timeConstraint;
    private AbstractPriorityQueue<Location, Double> locationHeap;

    public GreedyLocationSelector(List<Location> locations, int timeConstraint) {
        this.locationHeap = getHeap(locations);
        this.timeConstraint = timeConstraint + 0.0;
    }

    /**
     * Heapify the List of locations given in constructor and get max heap sorted according to score
     * @param locations
     * @return Max Heap of Locations sorted according to the score
     */
    private AbstractPriorityQueue<Location, Double> getHeap(List<Location> locations) {
        List<Double> scores = new ArrayList<>();

        for (Location l : locations) {
            scores.add(l.getScore());
        }

        Location[] locationArr = new Location[locations.size()];
        locationArr = locations.toArray(locationArr);

        Double[] scoreArr = new Double[scores.size()];
        scoreArr = scores.toArray(scoreArr);

        return new MaxHeapPriorityQueue<>(locationArr, scoreArr);
    }

    @Override
    public List<Location> selectLocationsToVisit() {
        double timeConstraint = this.timeConstraint;
        List<Location> selectedLocations = new ArrayList<>();

        while (timeConstraint > 0.0 && this.locationHeap.size() != 0) {
            // pick topLocation according to the Max score and see if it fits time constraint
            Location topLocation = this.locationHeap.removeMin().getKey();
            if (timeConstraint - topLocation.getHours() >= 0.0) {
                selectedLocations.add(topLocation);
                timeConstraint -= topLocation.getHours();
            }
        }
        return selectedLocations;
    }

    public AbstractPriorityQueue<Location, Double> getLocationHeap() {
        return locationHeap;
    }
}
