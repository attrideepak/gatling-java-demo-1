package qajournal.simulations;

import io.gatling.javaapi.core.ScenarioBuilder;
import qajournal.client.CarsClient;

import java.time.Duration;
import java.util.List;

import static io.gatling.javaapi.core.CoreDsl.*;

public class LoopsExample extends BaseSimulation {

    CarsClient carsClient = new CarsClient();

    ScenarioBuilder repeat = scenario("Repeat loop")
            .exec(repeat(5).on(exec(carsClient.getCarByBrand("tata"))));

    ScenarioBuilder forEach = scenario("For Each loop")
            .exec(foreach(List.of("tata", "ford", "maruti"), "brand")
                    .on(carsClient.getCarByBrand("#{brand}")));

    ScenarioBuilder during = scenario("During loop")
            .exec(during(Duration.ofSeconds(2))
                    .on(exec(carsClient.getCarByBrand("tata"))));

    ScenarioBuilder duringExitAsap = scenario("During loop exit asap")
            .exec(during(Duration.ofSeconds(2), false)
                    .on(exec(carsClient.getCarByBrand("tata"))));

    ScenarioBuilder asLongAs = scenario("As long as loop")
            .exec(carsClient.getCarById("1"))
            .exec(session -> carsClient.setCondition(session))
            .exec(asLongAs("#{condition}", "counter")
                    .on(exec(carsClient.getCarByBrand("tata")
                            .exec(session -> {
                                if (session.getInt("counter") == 5) {
                                    return session.set("condition", false);
                                }
                                return session;
                            })
                    )));

    ScenarioBuilder doWhile = scenario("do while loop")
            .exec(session -> session.set("condition", false))
            .exec(doWhile("#{condition}", "counter")
                    .on(exec(carsClient.getCarByBrand("tata")
                            .exec(session -> {
                                if (session.getInt("counter") == 5) {
                                    return session.set("condition", false);
                                }
                                return session;
                            })
                    )));

    ScenarioBuilder asLongAsDuring = scenario("as long as during")
            .exec(session -> session.set("condition", true))
            .exec(asLongAsDuring("#{condition}", 3)
                    .on(exec(carsClient.getCarByBrand("tata")
                    )));

    ScenarioBuilder doWhileDuring = scenario("do while during")
            .exec(session -> session.set("condition", true))
            .exec(doWhileDuring("#{condition}", 3)
                    .on(exec(carsClient.getCarByBrand("tata")
                    )));

    ScenarioBuilder forever = scenario("forever")
            .exec(forever().on(exec(carsClient.getCarByBrand("tata")
            )));

    {
        setUp(
                asLongAsDuring.injectOpen(constantUsersPerSec(1).during(1)
                ).protocols(httpProtocol)
        );
    }
}

