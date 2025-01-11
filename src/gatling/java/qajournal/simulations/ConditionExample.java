package qajournal.simulations;

import io.gatling.javaapi.core.ScenarioBuilder;
import qajournal.client.CarsClient;

import static io.gatling.javaapi.core.CoreDsl.*;

public class ConditionExample extends BaseSimulation {

    CarsClient carsClient = new CarsClient();

    ScenarioBuilder doIf = scenario("do if condition")
            .exec(session -> session.set("condition", false))
            .exec(doIf("#{condition}")
                    .then(exec(carsClient.getCarById("1"))))
            .exec(session -> session.set("condition", true))
            .exec(doIf("#{condition}")
                    .then(exec(carsClient.getCarById("1"))));

    ScenarioBuilder doIfEquals = scenario("do if equals condition")
            .exec(session -> session.set("brand", "tata"))
            .exec(doIfEquals("#{brand}", "tata")
                    .then(exec(carsClient.getCarById("1"))))
            .exec(doIfEquals("#{brand}", "ford")
                    .then(exec(carsClient.getCarById("1"))));

    ScenarioBuilder doIfOrElse = scenario("do if or else condition")
            .exec(session -> session.set("condition", true))
            .exec(doIfOrElse("#{condition}")
                    .then(exec(carsClient.getCarById("1")))
                    .orElse(exec(carsClient.getCarById("2"))));

    ScenarioBuilder doIfEqualsOrElse = scenario("do if equals or else condition")
            .exec(session -> session.set("brand", "tata"))
            .exec(doIfEqualsOrElse("#{brand}", "tata")
                    .then(exec(carsClient.getCarById("1")))
                    .orElse(exec(carsClient.getCarById("2"))));

    ScenarioBuilder doSwitch = scenario("do switch condition")
            .exec(session -> session.set("brand", "#{brand}"))
            .exec(doSwitch("#{brand}").on(
                    onCase("tata").then(exec(carsClient.getCarByBrand("tata"))),
                    onCase("maruti").then(exec(carsClient.getCarByBrand("maruti"))),
                    onCase("ford").then(exec(carsClient.getCarByBrand("ford")))
            ));

    ScenarioBuilder doSwitchOrElse = scenario("do switch or else condition")
            .exec(session -> session.set("brand", "#{brand}"))
            .exec(doSwitchOrElse("#{brand}").on(
                    onCase("tata").then(exec(carsClient.getCarByBrand("tata"))),
                    onCase("maruti").then(exec(carsClient.getCarByBrand("maruti"))),
                    onCase("ford").then(exec(carsClient.getCarByBrand("ford")))
            ).orElse(
                    exec(carsClient.getCarByBrand("tata"))
            ));

    ScenarioBuilder randomSwitch = scenario("random switch condition")
            .exec(randomSwitch().on(
                    percent(60.0).then(exec(carsClient.getCarByBrand("tata"))),
                    percent(20.0).then(exec(carsClient.getCarByBrand("maruti"))),
                    percent(20.0).then(exec(carsClient.getCarByBrand("ford")))
            ));

    ScenarioBuilder randomSwitchOrElse = scenario("random switch or Else condition")
            .exec(randomSwitchOrElse().on(
                    percent(60.0).then(exec(carsClient.getCarByBrand("tata"))),
                    percent(20.0).then(exec(carsClient.getCarByBrand("maruti"))),
                    percent(20.0).then(exec(carsClient.getCarByBrand("ford")))
            ).orElse(
                    exec(carsClient.getCarByBrand("tata"))
            ));

    ScenarioBuilder uniformRandomSwitch = scenario("uniform random switch")
            .exec(uniformRandomSwitch().on(
                    exec(carsClient.getCarByBrand("tata")),
                    exec(carsClient.getCarByBrand("maruti")),
                    exec(carsClient.getCarByBrand("ford"))
            ));

    ScenarioBuilder roundRobinSwitch = scenario("round robin switch")
            .exec(roundRobinSwitch().on(
                    exec(carsClient.getCarByBrand("tata")),
                    exec(carsClient.getCarByBrand("maruti")),
                    exec(carsClient.getCarByBrand("ford"))
            ));

    ScenarioBuilder doSwitchAndRepeat = scenario("Do switch and Repeat loop")
            .exec(doSwitch("#{brand}").on(
                    onCase("tata").then(repeat(2).on(exec(carsClient.getCarByBrand("tata")))),
                    onCase("maruti").then(repeat(3).on(exec(carsClient.getCarByBrand("maruti")))),
                    onCase("ford").then(repeat(4).on(exec(carsClient.getCarByBrand("ford")))
            )));

    {
        setUp(
                roundRobinSwitch.injectOpen(constantUsersPerSec(1).during(10)
                ).protocols(httpProtocol)
        );
    }

}
