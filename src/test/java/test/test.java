package test;

import util.Driver;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Test;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


public class test {

    // to get the list of the NEO
    @Test
    public void getNEOList() {
        Response response = Driver.setup().get();
        response.then().assertThat().statusCode(200);
        // get the whole response body
        String resString = response.getBody().asString();
        //System.out.println(resString); // print out the response
        response.prettyPrint(); // print out the response (prettyPrint)
        JsonPath jsonPathEvaluator = response.jsonPath();
        List<String> neoIDs = jsonPathEvaluator.get("near_earth_objects.name");
        // Below is to print of the list of names of the NEOs
        System.out.println("Here is the list of NEOs (names only): " + neoIDs);
        //Assert.assertTrue(resString.contains("near_earth_objects"));
        //System.out.println(response.getHeaders().toString());
    }

    // get the most current and future NEO ( based on close approach date )
    @Test
    public void getCurrentFutureNEO() {
        Response response = Driver.setup().get();
        response.then().assertThat().statusCode(200);
        JsonPath jsonPathEvaluator = response.jsonPath();
        List<String> currentFutureNEO = jsonPathEvaluator.get("near_earth_objects.close_approach_data.close_approach_date");
        // all close approach date in the API response
        System.out.println(currentFutureNEO);
        // find the most current and future
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime now = LocalDateTime.now();

        List<String> validNEO = null;
        for (Object date : currentFutureNEO) {
            String dateCheck = date.toString().replace("[", "");
            String dateCheck2 = dateCheck.replace("]", "");
            if (dateCheck2 != null && dateCheck2.compareTo(now.toString()) > 0) {
                validNEO.add(date.toString());
            }
            System.out.println(date);
        }
        System.out.println(validNEO);
        // this will not return the correct answer which is 2072-09-29 because somehow it is tie together
        // with another date
    }

    // Check the smallest neo that orbits Jupiter and the largest neo that orbits Mercury
    @Test
    public void checkSmallestAndLargest() {
        Response response = Driver.setup().get();
        response.then().assertThat().statusCode(200);
        JsonPath jsonPathEvaluator = response.jsonPath();
        // first check if any of the neo orbiting Jupiter or Mercury
        List<String> neoOrbiting = jsonPathEvaluator.get("near_earth_objects.close_approach_data.orbiting_body");
        if (!(neoOrbiting.contains("Jupiter") || neoOrbiting.contains("Mercury"))) {
            System.out.println("None of the neo is orbiting either Jupiter or Mercury");
            // if there is no neo orbiting jupiter or mercury, there is no need to find it
        }
        System.out.println(neoOrbiting);
        String orbitingBodies = "";
        for (Object body : neoOrbiting) {
            String body1 = body.toString().replace("[", "");
            String body2 = body1.replace("]", "");
            if (!body2.equals("")) {
                if (!orbitingBodies.contains(body2)) {
                    orbitingBodies += body2 + " ";
                }
            }
        }
        System.out.println("List of bodies that our neo are orbiting: " + orbitingBodies);
        // some of the neo are orbiting multiple bodies

    }
}




