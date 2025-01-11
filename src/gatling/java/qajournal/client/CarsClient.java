package qajournal.client;

import api.StandardHeader;
import api.URLs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import helpers.TestDataProvider;
import io.gatling.http.response.Response;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CoreDsl;
import io.gatling.javaapi.core.Session;
import io.netty.handler.codec.http.HttpResponseStatus;
import models.CarsReq;
import models.ContactReq;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class CarsClient {

    private ObjectMapper mapper = new ObjectMapper();
    private String cars = URLs.CARS_URL;

    public ChainBuilder createCar() {
        return exec(http("Create car")
                .post(cars)
                .body(CoreDsl.StringBody(session -> {
                    CarsReq carsReq =
                            new CarsReq(session.getString("BRAND"), session.getString("NAME"),
                                    session.getString("VARIANT"));
                    String body;
                    try {
                        body = mapper.writeValueAsString(carsReq);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    return body;
                }))
                .check(status().is(HttpResponseStatus.CREATED.code())).check(jmesPath("id").saveAs("carId")));
    }


    public ChainBuilder updateCard() {
        ChainBuilder chainBuilder;
        CarsReq carsReq =
                new CarsReq("#{BRAND}", "#{NAME}", "#{VARIANT}");
        try {
             chainBuilder =  exec(http("Update car")
                    .put(cars + "/#{carId}")
                    .body(StringBody(mapper.writeValueAsString(carsReq)))
                    .check(status().is(HttpResponseStatus.OK.code())));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return chainBuilder;
    }

    public ChainBuilder getCar() {
        return exec(http("Get car")
                .get(cars + "/#{carId}")
                .check(status().is(HttpResponseStatus.OK.code())));
    }

    public ChainBuilder getCarById(String id) {
        return exec(http("Get car")
                .get(cars + "/" + id)
                .check(status().is(HttpResponseStatus.OK.code()))
                .check(jmesPath("brand").saveAs("brand")));
    }

    public Session setCondition(Session session) {
        if(session.getString("brand").equalsIgnoreCase("tata")) {
            return session.set("condition", "true");
        } else {
            return session.set("condition", "false");
        }
    }

    public ChainBuilder getCarByBrand() {
        return exec(http("Get car by brand")
                .get(cars)
                .queryParam("brand", "tata")
                .check(status().is(HttpResponseStatus.OK.code())));
    }

    public ChainBuilder getCarByBrand(String brandName) {
        return exec(http("Get car by brand:" +brandName)
                .get(cars)
                .queryParam("brand", brandName)
                .check(status().is(HttpResponseStatus.OK.code())));
    }

   public ChainBuilder deleteCar() {
        return exec(http("Delete car")
                .delete(cars + "/#{carId}")
                .check(status().is(HttpResponseStatus.OK.code())));
    }

    public ChainBuilder addEmailAndMobileNumber() {
        Map<String, String> data = new HashMap<>();
        data.put("email", "#{EMAIL}");
        data.put("mobile", "#{MOBILE}");
        try {
            return exec(http("Add email and mobile number")
                    .post("/contactDetails")
                    .body(StringBody(mapper.writeValueAsString(data)))
                    .check(status().is(HttpResponseStatus.CREATED.code()))
                    .check(jmesPath("email").saveAs("emailAddress"))
                    .checkIf(session -> session.getString("emailAddress").contains("random"))
                    .then(jmesPath("mobile").saveAs("mobileNumber")));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public ChainBuilder exampleCheckIfWithBiFunction() {
        Map<String, String> data = new HashMap<>();
        data.put("email", "#{EMAIL}");
        data.put("mobile", "#{MOBILE}");
        try {
            return exec(http("Add email and mobile number")
                    .post("/contactDetails")
                    .body(StringBody(mapper.writeValueAsString(data)))
                    .check(jmesPath("email").saveAs("emailAddress"))
                    .checkIf((response, session) ->
                            session.getString("emailAddress").contains("random")
                                    && response.status().code() == 201)
                    .then(jmesPath("mobile").saveAs("mobileNumber")));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public ChainBuilder sessionAPIExample() {
        return exec(http("Add email and mobile number")
                .post("/contactDetails")
                .body(CoreDsl.StringBody(session -> {
                    ContactReq contactReq = new ContactReq();
                    String body;
                    try {
                        body = mapper.writeValueAsString(contactReq);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    return body;
                }))
                .check(status().is(HttpResponseStatus.CREATED.code())));
    }

    public Session setAttributesUsingSessionApi(Session session) {
        Map<String, Object> sessionMap = new HashMap<>();
        sessionMap.put("email", TestDataProvider.getRandomEmail());
        sessionMap.put("mobile", TestDataProvider.getRandomMobileNumber());
        return session.setAll(sessionMap);
    }

    public ChainBuilder sessionAPIExample1() {
        return exec(http("Add email and mobile number")
                .post("/contactDetails")
                .body(CoreDsl.StringBody(session -> {
                    ContactReq contactReq = new ContactReq(session.getString("email"),
                            session.getString("mobile"));
                    String body;
                    try {
                        body = mapper.writeValueAsString(contactReq);
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                    return body;
                }))
                .check(status().is(HttpResponseStatus.CREATED.code())));
    }

    public ChainBuilder sessionAPIExample2(String email, String mobile) {
        ContactReq contactReq = new ContactReq(email, mobile);
        try {
            return exec(http("Add email and mobile number")
                    .post("/contactDetails")
                    .body(StringBody(mapper.writeValueAsString(contactReq)))
                    .check(status().is(HttpResponseStatus.CREATED.code())));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
