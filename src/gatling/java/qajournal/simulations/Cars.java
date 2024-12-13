package qajournal.simulations;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import qajournal.client.CarsClient;
import static io.gatling.javaapi.core.CoreDsl.*;

public class Cars extends BaseSimulation {

    FeederBuilder<String> feeder = csv("testdata.csv").circular();

    // Json feeder
    // FeederBuilder.FileBased<Object> jsonFeeder = jsonFile("testData.json").random();

    private CarsClient carsClient = new CarsClient();

    ScenarioBuilder scn = scenario("Get user")
            .feed(feeder)
            .exec(carsClient.createCar())
            .exec(carsClient.updateCard())
            .exec(carsClient.getCar())
            .exec(carsClient.getCarByBrand())
            .exec(carsClient.deleteCar());

    {
        setUp(
                scn.injectOpen(constantUsersPerSec(1 * multiplier).during(loadDuration)
                ).protocols(httpProtocol)
        );
    }

}