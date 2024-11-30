package qajournal.client;

import api.StandardHeader;
import api.URLs;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.core.CoreDsl;
import io.netty.handler.codec.http.HttpResponseStatus;
import models.CarsReq;

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

    public ChainBuilder getCarByBrand() {
        return exec(http("Get car by brand")
                .get(cars)
                .queryParam("brand", "tata")
                .check(status().is(HttpResponseStatus.OK.code())));
    }

   public ChainBuilder deleteCar() {
        return exec(http("Delete car")
                .delete(cars + "/#{carId}")
                .check(status().is(HttpResponseStatus.OK.code())));
    }
}
