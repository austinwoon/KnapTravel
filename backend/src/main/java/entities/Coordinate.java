package entities;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Coordinate {
    private Map<String, Double> coordinates = new HashMap<>();

    public Coordinate(Map<String, Double> coordinates) {
        this.coordinates = coordinates;
    }

    public Coordinate(Double lat, Double lng) {
        Map<String, Double> coordinates = new HashMap<>();
        coordinates.put("lat", lat);
        coordinates.put("lng", lng);
        this.coordinates = coordinates;
    }

    public Map<String, Double> getCoordinates() {
        return this.coordinates;
    }

    @Override
    public String toString() {
        return "Coordinate{" +
                "coordinates=" + coordinates +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coordinate that = (Coordinate) o;
        return Objects.equals(coordinates, that.coordinates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinates);
    }
}
