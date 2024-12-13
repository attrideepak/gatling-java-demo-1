package qajournal.simulations;

import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import qajournal.client.CarsClient;
import static io.gatling.javaapi.core.CoreDsl.constantUsersPerSec;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.jdbc.JdbcDsl.*;

public class JDBCFeederExample extends BaseSimulation {
    // JDBC connection configuration
    private static final String JDBC_URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String JDBC_USER = "test-metrics";
    private static final String JDBC_PASSWORD = "secret";
    private CarsClient carsClient = new CarsClient();


    FeederBuilder<Object> jdbcFeeder =
            jdbcFeeder(JDBC_URL, JDBC_USER, JDBC_PASSWORD, "SELECT * FROM contact_details").circular();


    ScenarioBuilder scn = scenario("Add email and mobile number")
            .feed(jdbcFeeder)
            .exec(carsClient.addEmailAndMobileNumber());


    {
        setUp(
                scn.injectOpen(constantUsersPerSec(1 * multiplier).during(loadDuration)
                ).protocols(httpProtocol)
        );
    }
}
