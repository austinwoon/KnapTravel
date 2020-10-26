package algo.recommender;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AlgoController {

  @GetMapping("/test")
  public String testingMethod() {
    return "yesss";
  }
}
