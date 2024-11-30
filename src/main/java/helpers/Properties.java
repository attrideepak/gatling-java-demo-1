package helpers;

import constants.Constants;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class Properties {
    private static String environment;

    /**
     * Private constructor to hide public one.
     */
    private Properties() {
    }

    /**
     * Gets the value of specified property and also checks if its in the list of valid values.
     *
     * @param propertyName whose value needs to be returned
     * @param isMandatory  for execution ?
     * @param validValues  list to validate the property value is one of the valid values
     * @return String value of the property
     */
    private static String getProperty(String propertyName, boolean isMandatory, List<String> validValues) {
        Optional<String> optPropValue = Optional.ofNullable(System.getProperty(propertyName));
        if (!optPropValue.isPresent()) {
            if (isMandatory) {
                throw new RuntimeException(propertyName + " value is not present");
            } else {
                return "";
            }
        }

        String propertyValue = optPropValue.get();
        if (!validValues.isEmpty() && !validValues.contains(propertyValue.toUpperCase())) {
            throw new RuntimeException(propertyName + " is invalid, can only be one of " + validValues);
        }

        return propertyValue;
    }

    /**
     * Gets the environment property.
     *
     * @return String value of environment
     */
    public static String getEnvironment() {
        if (environment == null || environment.isEmpty()) {
            environment = getProperty(Constants.ENV_PROPERTY, true, Arrays.asList("PERF-A", "PERF-B"));
        }

        return environment;
    }


}
