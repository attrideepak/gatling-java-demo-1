package qajournal.simulations;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import qajournal.client.CarsClient;

import static io.gatling.javaapi.core.CoreDsl.*;

public class SessionAPIExample extends BaseSimulation{

    private CarsClient carsClient = new CarsClient();
    FeederBuilder<String> feeder = csv("contactdata.csv").circular();

    ScenarioBuilder scn = scenario("Add contact details")
            .exec(carsClient.sessionAPIExample());

    ScenarioBuilder scn1 = scenario("Add contact details via session")
            .exec(session -> carsClient.setAttributesUsingSessionApi(session))
            .exec(carsClient.sessionAPIExample1());

    ScenarioBuilder scn2 = scenario("Add contact details via session")
            .exec(session -> carsClient.setAttributesUsingSessionApi(session))
            .exec(carsClient.sessionAPIExample2("#{email}", "#{mobile}"));

    ScenarioBuilder scn3 = scenario("Add contact details via session")
            .feed(feeder)
            .exec(carsClient.addEmailAndMobileNumber());

    ScenarioBuilder scn4 = scenario("Add contact details via session")
            .feed(feeder)
            .exec(carsClient.exampleCheckIfWithBiFunction());

    {
        setUp(
                scn4.injectOpen(constantUsersPerSec(1 * multiplier).during(loadDuration)
                ).protocols(httpProtocol)
        );
    }
}