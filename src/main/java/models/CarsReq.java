package models;

import helpers.TestDataProvider;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CarsReq {

 /*    sample request
    {
        "brand": "tata",
        "name": "hexa",
        "variant": "petrol",
        "id": "7",
        "email": xyz@gmail.com
    }
  */

    private String brand;
    private String name;
    private String variant;
    private String id;
    private String email;

    public CarsReq(String brand, String name, String variant) {
        this.brand = brand;
        this.name = name;
        this.variant = variant;
        this.id = TestDataProvider.getRandomNumber();
        this.email = TestDataProvider.getRandomEmail();
    }

}
