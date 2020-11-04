package entities;

import lombok.Data;

import java.util.Objects;
import java.util.Map;

public @Data class Location implements Comparable<Location> {
    private String name;
    private Coordinate coordinate;
    private double score;
    private double hours;
    private int hoursMultipliedByTwo; // whole hours needed for ILP in knapsack

    public Location(String name, Coordinate coordinate, double score, double hours) {
        this.name = name;
        this.coordinate = coordinate;
        this.score = score;
        this.hours = hours;
        this.hoursMultipliedByTwo = (int)(hours * 2);
    }

    public Location(String name, Coordinate coordinate) {
        this.name = name;
        this.coordinate = coordinate;
    }

    public Map<String, Double> getLatLng() {
        return coordinate.getCoordinates();
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", coordinates=" + coordinate + '\'' +
                ", score=" + score +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(name, location.name) &&
                Objects.equals(coordinate, location.coordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, coordinate);
    }

    public String getName() {
        return name;
    }

    @Override
    public int compareTo(Location o) {
        return Double.compare(this.score, o.getScore());
    }
}
