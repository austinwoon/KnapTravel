package entities;

import lombok.Data;

import java.util.Objects;

public @Data class Location {
    private String name;
    private Coordinate coordinate;
    private double score;

    public Location(String name, Coordinate coordinate) {
        this.name = name;
        this.coordinate = coordinate;
    }

    @Override
    public String toString() {
        return "Location{" +
                "name='" + name + '\'' +
                ", coordinates=" + coordinate +
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
}
