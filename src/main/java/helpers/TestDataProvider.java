package helpers;

import com.github.javafaker.Faker;

import java.util.UUID;

public class TestDataProvider {
    private static final Faker faker = new Faker();

    /**
     * Gets a random name.
     *
     * @return a string of random name
     */
    public static String getRandomName() {
        return faker.name().name();
    }

    /**
     * Gets a random nick-name.
     *
     * @return a string of random nick-name
     */
    public static String getRandomNickName() {
        return faker.name().firstName();
    }


    public static String getRandomMobileNumber() {
            return faker.phoneNumber().phoneNumber();
    }

    /**
     * Gets a random alphanumeric string of lenght n.
     * Can be used for apiKey, any random alphanumeric string
     *
     * @param length of alphanumeric string
     * @return string of specified length
     */
    public static String getRandomAlphanumericString(int length) {
        return getRandomStringMatchingPattern("[a-zA-Z0-9]{" + length + "}");
    }
    /**
     * Gets a random email.
     *
     * @return email address with pattern *.digibank@test.grab.com
     */
    public static String getRandomEmail() {
        return getRandomStringMatchingPattern("random_\\d{8}") + "@gmail.com";
    }

    /**
     * Gets a random string by pattern.
     *
     * @param pattern string
     * @return a random string generated using pattern
     */
    public static String getRandomStringMatchingPattern(String pattern) {
        return faker.regexify(pattern);
    }

    /**
     * Gets a random street address.
     *
     * @return a random street address
     */
    public static String getRandomStreetAddress() {
        return faker.address().streetAddress();
    }

    /**
     * Gets a random full address.
     *
     * @return a random full address
     */
    public static String getFullAddress() {
        return faker.address().fullAddress();
    }

    /**
     * Generates and returns a random UUID.
     *
     * @return random uuid
     */
    public static String getRandomUuid() {
        return UUID.randomUUID().toString();
    }

    public static String getRandomNumber() {
        return String.valueOf(faker.number().randomNumber());
    }

}
