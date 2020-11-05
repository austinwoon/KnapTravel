package algo.models;
import lombok.Data;
import java.util.List;

public @Data class ItineraryFormInputModel {
    private int days;
    private List<String> interests;
    private String city;
    private int timeConstraint;

    public ItineraryFormInputModel(int days, List<String> interests, String city, int timeConstraint) {
        this.city = city;
        this.days = days;
        this.interests = interests;
        this.timeConstraint = timeConstraint;
    }
}
