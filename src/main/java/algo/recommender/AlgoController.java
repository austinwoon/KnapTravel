package algo.recommender;

import algo.models.ItineraryFormInputModel;
import algo.models.ItineraryResponseModel;
import algo.models.TagsResponseModel;
import entities.Location;
import kmeans.Kmeans;
import location_selector.KnapsackLocationSelector;
import location_selector.LocationSelector;
import org.springframework.web.bind.annotation.*;
import scoring.ScoringCalculator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RestController
public class AlgoController {

  @GetMapping("/getTags/{city}")
  public TagsResponseModel getTags(@PathVariable String city) {
    // TODO: Either put data source in db, or make JSONReader return a List of Locations, getting List of Locations too tightly coupled with Scorer class
    String dataSource = String.format("src/main/resources/data/%s-processed.json", city.toLowerCase());
    HashSet<String> pref = new HashSet<>();

    ScoringCalculator scorer = new ScoringCalculator(pref, dataSource);
    List<String> tags = scorer.getFilteredTags(50);

    return new TagsResponseModel(tags);
  }

  @PostMapping("/getItinerary")
  public ItineraryResponseModel getItinerary(@RequestBody ItineraryFormInputModel request) throws Exception {

    // for now data source is read from data file
    String dataSource = String.format("src/main/resources/data/%s-processed.json", request.getCity().toLowerCase());

    // get scores for each location in "database" according to interests of user
    HashSet<String> pref = new HashSet<>(request.getInterests());
    ScoringCalculator scorer = new ScoringCalculator(pref, dataSource);

    // get list of locations with scores
    List<Location> locations = scorer.getLocations();

    // get clusters according to number of days to visit
    Map<Integer, List<Location>> clusters = Kmeans.fit(locations, request.getDays(), 100000);

    Map<Integer, List<Location>> itinerary = getKnapsackLocation(clusters, request.getTimeConstraint());

    return new ItineraryResponseModel(itinerary);
  }

  public static Map<Integer, List<Location>> getKnapsackLocation(Map<Integer, List<Location>> clusters, int timeConstraint) {
    Map<Integer, List<Location>> selectedLocations = new HashMap<>();
    for (Integer i : clusters.keySet()) {
      LocationSelector selector = new KnapsackLocationSelector(clusters.get(i), timeConstraint);
      selectedLocations.put(i, selector.selectLocationsToVisit());
    }
    return selectedLocations;
  }
}
