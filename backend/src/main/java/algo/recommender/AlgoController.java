package algo.recommender;

import algo.models.CitiesResponseModel;
import algo.models.ItineraryFormInputModel;
import algo.models.ItineraryResponseModel;
import algo.models.TagsResponseModel;
import entities.Coordinate;
import entities.Location;
import kmeans.Kmeans;
import location_selector.RevisedKnapsackLocationSelector;
import location_selector.LocationSelector;
import org.springframework.web.bind.annotation.*;
import routing.GreedyRouter;
import scoring.ScoringCalculator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@RestController
public class AlgoController {

  @CrossOrigin
  @GetMapping("/getTags/{city}")
  public TagsResponseModel getTags(@PathVariable String city) {
    String cityFormatted = city.toLowerCase().replace(' ', '-'); // get city in correct format
    // TODO: Either put data source in db, or make JSONReader return a List of Locations, getting List of Locations too tightly coupled with Scorer class
    String dataSource = String.format("/data/%s.json", cityFormatted);
    HashSet<String> pref = new HashSet<>();

    ScoringCalculator scorer = new ScoringCalculator(pref, dataSource);
    List<String> tags = scorer.getFilteredTags(100);

    return new TagsResponseModel(tags);
  }

  @CrossOrigin
  @GetMapping("/getCities")
  public CitiesResponseModel getTags() {
    return new CitiesResponseModel();
  }

  @CrossOrigin
  @PostMapping("/getItinerary")
  public ItineraryResponseModel getItinerary(@RequestBody ItineraryFormInputModel request) throws Exception {
    System.out.println(request);
    // for now data source is read from data file
    String city = request.getCity().toLowerCase().replace(' ', '-'); // get city in correct format
    String dataSource = String.format("/data/%s.json", city);

    // get scores for each location in "database" according to interests of user
    HashSet<String> pref = new HashSet<>(request.getInterests());
    ScoringCalculator scorer = new ScoringCalculator(pref, dataSource);

    // get list of locations with scores
    List<Location> locations = scorer.getLocations();

    // get clusters according to number of days to visit
    Map<Integer, List<Location>> clusters = Kmeans.fit(locations, request.getDays(), 100000);

    // select locations within cluster
    Map<Integer, List<Location>> selectedLocations = getKnapsackLocation(clusters, request.getTimeConstraint(), scorer.getCenter());

    // select ordering of locations to visit within cluster

    Map<Integer, List<Location>> itinerary = getClusterRoutes(selectedLocations, scorer.getCenter());

    return new ItineraryResponseModel(itinerary);
  }

  public static Map<Integer, List<Location>> getKnapsackLocation(Map<Integer, List<Location>> clusters, int timeConstraint,
                                                                 Coordinate center) {
    Map<Integer, List<Location>> selectedLocations = new HashMap<>();
    for (Integer i : clusters.keySet()) {
      LocationSelector selector = new RevisedKnapsackLocationSelector(clusters.get(i), timeConstraint, center);
      selectedLocations.put(i, selector.selectLocationsToVisit());
    }
    return selectedLocations;
  }

  public static Map<Integer, List<Location>> getClusterRoutes(Map<Integer, List<Location>> selectedLocations, Coordinate startPoint) {
    Map<Integer, List<Location>> routedClusters = new HashMap<>();
    for (Integer i : selectedLocations.keySet()) {
      List<Location> topClusterLocations = selectedLocations.get(i);
      GreedyRouter router = new GreedyRouter(startPoint, topClusterLocations);
      List<Location> routedLocations = router.getRoute();
      routedClusters.put(i, routedLocations);
    }

    return routedClusters;
  }
}
