package qajournal.simulations;

import io.gatling.javaapi.core.PauseType;
import io.gatling.javaapi.core.ScenarioBuilder;
import qajournal.client.CarsClient;

import static io.gatling.javaapi.core.CoreDsl.*;

public class ErrorHandlingExample extends BaseSimulation {
    CarsClient carsClient = new CarsClient();

    ScenarioBuilder tryMax = scenario("try max")
            .tryMax(5).on(exec(carsClient.getCarById("1"))
                    .exec(carsClient.getCarByBrand("maruti")))
            .exec(carsClient.getCarByBrand("tata"));

    ScenarioBuilder exitBlockOnFail = scenario("Exit block on fail")
            .exitBlockOnFail().on(exec(carsClient.getCarById("1"))
                    .exec(carsClient.getCarByBrand("maruti")))
            .exec(carsClient.getCarByBrand("tata"));

    ScenarioBuilder exitHere = scenario("exit here")
            .exec(carsClient.getCarById("1"))
            .exec(carsClient.getCarByBrand("maruti"))
            .exitHere()
            .exec(carsClient.getCarByBrand("tata"));

    ScenarioBuilder exitHereIf = scenario("exit here if")
            .exec(carsClient.getCarById("1"))
            .exitHereIf(session -> session.getString("brand").equals("Tata"))
            .exec(carsClient.getCarByBrand("tata"));

    ScenarioBuilder exitHereIfFailed = scenario("exit here if failed")
            .exec(carsClient.getCarById("1"))
            .exec(carsClient.getCarByBrand("tata"))
            .exitHereIfFailed()
            .exec(carsClient.getCarById("1"));

    ScenarioBuilder stopLoadGenerator = scenario("stop load generator")
            .exec(carsClient.getCarById("1"))
            .stopLoadGenerator("stop the load generator on demand")
            .exec(carsClient.getCarByBrand("tata"));

    ScenarioBuilder crashLoadGenerator = scenario("crash load generator")
            .exec(carsClient.getCarById("1"))
            .crashLoadGenerator("crash the load generator on demand")
            .exec(carsClient.getCarByBrand("tata"));

    ScenarioBuilder stopLoadGeneratorIf = scenario("stop load generator if")
            .exec(carsClient.getCarById("1"))
            .stopLoadGeneratorIf("stop the load generator forcefully",
                    session -> session.getString("brand").equals("Tata"))
            .exec(carsClient.getCarByBrand("tata"));

    ScenarioBuilder crashLoadGeneratorIf = scenario("crash load generator if")
            .exec(carsClient.getCarById("1"))
            .crashLoadGeneratorIf("crash the load generator forcefully",
                    session -> session.getString("brand").equals("Tata") )
            .exec(carsClient.getCarByBrand("tata"));

    {
        setUp(
                stopLoadGeneratorIf.injectOpen(constantUsersPerSec(1 * multiplier).during(1)
                ).protocols(httpProtocol)
        );
    }

}
