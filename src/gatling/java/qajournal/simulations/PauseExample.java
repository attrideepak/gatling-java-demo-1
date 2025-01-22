package qajournal.simulations;

import io.gatling.javaapi.core.PauseType;
import io.gatling.javaapi.core.ScenarioBuilder;
import qajournal.client.CarsClient;
import static io.gatling.javaapi.core.CoreDsl.*;


public class PauseExample extends BaseSimulation {

    CarsClient carsClient = new CarsClient();

    ScenarioBuilder fixedPause = scenario("Fixed pause")
            .exec(carsClient.getCarByBrand())
            .pause(3, PauseType.Constant)
            .exec(carsClient.getCarById("1"));

    ScenarioBuilder randomPause = scenario("Random pause")
            .exec(carsClient.getCarByBrand())
            .pause(3, 10)
            .exec(carsClient.getCarById("1"));

    ScenarioBuilder pace = scenario("pace")
            .exec(repeat(3)
                    .on(pace(7)
                            .exec(carsClient.getCarByBrand("tata"))
                            .pause(2)
                            .exec(carsClient.getCarById("1"))));

    ScenarioBuilder renedezVous = scenario("renedezVous")
            .exec(carsClient.getCarByBrand())
            .rendezVous(10)
            .exec(carsClient.getCarById("1"));

    {
        setUp(
                pace.injectOpen(constantUsersPerSec(1 * multiplier).during(1)
                ).protocols(httpProtocol).disablePauses()
        );
    }
}
