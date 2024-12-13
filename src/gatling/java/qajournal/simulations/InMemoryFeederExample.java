package qajournal.simulations;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import org.apache.commons.lang3.RandomStringUtils;
import qajournal.client.CarsClient;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Stream;

import static io.gatling.javaapi.core.CoreDsl.*;

public class InMemoryFeederExample extends BaseSimulation {
    private CarsClient carsClient = new CarsClient();

    Iterator<Map<String, Object>> inMemoryfeeder =
            Stream.generate((Supplier<Map<String, Object>>) () -> {
                        String email = RandomStringUtils.randomAlphanumeric(20) + "@test.com";
                        String mobileNumber = RandomStringUtils.randomNumeric(10);
                        return Map.of(
                                "EMAIL", email,
                                "MOBILE", mobileNumber
                        );
                    }
            ).iterator();

    FeederBuilder<Object> listFeeder = listFeeder(List.of(
            Map.of("EMAIL", "user1@test.com", "MOBILE", "1234567890"),
            Map.of("EMAIL", "user2@test.com", "MOBILE", "1234567891"),
            Map.of("EMAIL", "user3@test.com", "MOBILE", "1234567892")
    )).random();

    ScenarioBuilder scn = scenario("Add email and mobile number")
            .feed(inMemoryfeeder)
            .exec(carsClient.addEmailAndMobileNumber());


    {
        setUp(
                scn.injectOpen(constantUsersPerSec(1 * multiplier).during(loadDuration)
                ).protocols(httpProtocol)
        );
    }
}
