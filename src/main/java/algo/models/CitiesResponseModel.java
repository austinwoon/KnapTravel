package algo.models;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class CitiesResponseModel {
    private List<String> availableCities;

    public CitiesResponseModel() {
        // have to do this way instead of static due to Springboot constraints
        this.availableCities = new ArrayList<String>(Arrays.asList("New York City", "California", "London", "Tokyo", "Osaka", "Paris", "Taipei"));
    }
}
