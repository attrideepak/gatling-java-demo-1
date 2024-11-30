package helpers;

import constants.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import models.Config;
import models.EnvConfig;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Optional;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;

public class ConfigProvider {
    private static final Gson gson = new Gson();
    protected static final Logger LOGGER = (Logger) LoggerFactory.getLogger(ConfigProvider.class);
    private static Config config;

    /**
     * Private constructor to hide public one.
     */
    private ConfigProvider() {
    }

    /**
     * Loads the config and static data from json files.
     */
    public static synchronized void loadDataConfig() {
        if (config == null) {
            config = getDataConfig("config", Constants.ENV_CONF_FILE).getConfig();
        }
    }

    /**
     * Gets the config object.
     *
     * @return Config object
     */
    public static synchronized Config getConfig() {
        LOGGER.info("Getting config object");
        if (config == null) {
            loadDataConfig();
        }
        return config;
    }

    /**
     * Gets the EnvConfig object.
     *
     * @param dirName  of the file
     * @param fileName to parse
     * @return EnvConfig object
     */
    private static EnvConfig getDataConfig(String dirName, String fileName) {
        JsonArray json = getJsonArray(dirName, fileName);
        String env = Properties.getEnvironment();

        EnvConfig[] dataSet = gson.fromJson(json, EnvConfig[].class);
        Optional<EnvConfig> optData = Arrays.stream(dataSet).
                filter(item -> item.getEnv().equalsIgnoreCase(env)).
                findFirst();

        if (!optData.isPresent()) {
            throw new RuntimeException("unable to get EnvConfig for environment " + env);
        }
        return optData.get();
    }

    /**
     * Get Json Array extracted from file.
     *
     * @param dirName  of the file
     * @param fileName to parse
     * @return extracted json array
     */
    public static JsonArray getJsonArray(String dirName, String fileName) {
        String filePath = File.separator + dirName + File.separator + fileName;
        InputStream is = ConfigProvider.class.getResourceAsStream(filePath);
        return getJsonArray(new InputStreamReader(is));
    }

    /**
     * De-serializes the given file reader object into JsonArray.
     *
     * @param reader for the file
     * @return JsonArray object
     */
    private static JsonArray getJsonArray(InputStreamReader reader) {
        Optional<JsonArray> optJsonArray = Optional.ofNullable((JsonArray) new JsonParser().parse(reader));
        if (optJsonArray.isPresent()) {
            return optJsonArray.get();
        }

        throw new RuntimeException("JsonArray is not present");
    }
}
