package kmeans;
import entities.Coordinate;

import java.util.Objects;

public class Centroid {
    private final Coordinate coordinate;

    public Centroid(Coordinate coordinate) {
        this.coordinate = coordinate;
    }

    public Coordinate getCoordinate() {
        return coordinate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Centroid centroid = (Centroid) o;
        return Objects.equals(coordinate, centroid.coordinate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordinate);
    }

    @Override
    public String toString() {
        return "Centroid{" +
                "coordinate=" + coordinate +
                '}';
    }
}
