package qajournal.simulations;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import qajournal.client.CarsClient;

import java.util.List;
import java.util.Map;

import static io.gatling.javaapi.core.CoreDsl.*;

public class ListFeederExample extends BaseSimulation {
    private CarsClient carsClient = new CarsClient();

    FeederBuilder<Object> listFeeder = listFeeder(List.of(
            Map.of("EMAIL", "user1@test.com", "MOBILE", "1234567890"),
            Map.of("EMAIL", "user2@test.com", "MOBILE", "1234567891"),
            Map.of("EMAIL", "user3@test.com", "MOBILE", "1234567892")
    )).circular();

//    FeederBuilder arrayFeeder = arrayFeeder(new Map[] {
//            Map.of("EMAIL", "user1@test.com", "MOBILE", "1234567890"),
//            Map.of("EMAIL", "user2@test.com", "MOBILE", "1234567891"),
//            Map.of("EMAIL", "user3@test.com", "MOBILE", "1234567892")
//    }).circular();

    ScenarioBuilder scn = scenario("Add email and mobile number")
            .feed(listFeeder)
            .exec(carsClient.addEmailAndMobileNumber());


    {
        setUp(
                scn.injectOpen(constantUsersPerSec(1 * multiplier).during(loadDuration)
                ).protocols(httpProtocol)
        );
    }
}
