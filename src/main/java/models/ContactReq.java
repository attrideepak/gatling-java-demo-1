package models;

import helpers.TestDataProvider;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ContactReq {
    private String email;
    private String mobile;

    public ContactReq() {
        this.email = TestDataProvider.getRandomEmail();
        this.mobile = TestDataProvider.getRandomMobileNumber();
    }
}