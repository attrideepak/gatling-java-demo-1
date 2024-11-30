package qajournal.simulations;

import api.StandardHeader;
import ch.qos.logback.classic.Logger;
import helpers.ConfigProvider;
import helpers.Properties;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import models.Config;
import org.slf4j.LoggerFactory;

import static io.gatling.javaapi.http.HttpDsl.http;

public class BaseSimulation extends Simulation {
    protected Config config;
    protected int multiplier = Integer.parseInt(System.getProperty("multiplier"));

    protected static final Logger LOGGER = (Logger) LoggerFactory.getLogger(BaseSimulation.class);
    int loadDuration = Integer.parseInt(System.getProperty("testDuration"));


    {
        LOGGER.info("Loading env config: " + Properties.getEnvironment());
        LOGGER.info("Load test duration: " +  loadDuration + " seconds");
        this.config = ConfigProvider.getConfig();
    }

    protected HttpProtocolBuilder httpProtocol = http
            .baseUrl(config.getBaseUri())
            .disableAutoReferer()
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .userAgentHeader("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 " +
                    "(KHTML, like Gecko) Chrome/111.0.0.0 Mobile Safari/537.36")
            .header(StandardHeader.CONTENT_TYPE, "application/json");

}
