package qajournal.simulations;

import io.gatling.javaapi.core.ScenarioBuilder;
import qajournal.client.CarsClient;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;

public class NewCars extends BaseSimulation {
    private CarsClient carsClient = new CarsClient();

    ScenarioBuilder scn = scenario("Get user")
            .exec(carsClient.getCarByBrand());


    {
        setUp(
                scn.injectOpen(constantUsersPerSec(1 * multiplier).during(loadDuration)
                ).protocols(httpProtocol)
        );
    }
}
