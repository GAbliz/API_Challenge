package util;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class Driver {

    public static RequestSpecification setup(){

        RestAssured.baseURI ="http://www.neowsapp.com/rest/v1/neo/browse";
        RequestSpecification request = RestAssured.given();

        return request;
    }
}
