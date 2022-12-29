package testsdata;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static testsdata.Endpoints.*;

public class WorkWithUser {

    public Response createUser(NewUser newUser) {

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .auth().oauth2("")
                        .and()
                        .body(newUser)
                        .when()
                        .post(API_REGISTER);


        System.out.println(response.asString());

        return response;

    }

    public Response loginUser(NewUser newUser) {

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .auth().oauth2("")
                        .and()
                        .body(newUser)
                        .when()
                        .post(API_LOGIN);


        System.out.println(response.asString());

        return response;

    }


    public Response deleteUser(String bearerToken) {

        Response response =
                given()
                        .headers("Content-type", "application/json", "Authorization", bearerToken)
                        .when()
                        .delete(API_USER);

        System.out.println(response.asString());

        return response;

    }

    public String bearerToken(NewUser newUser) {

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .auth().oauth2("")
                        .and()
                        .body(newUser)
                        .when()
                        .post(API_LOGIN);

        String bearerToken = response.then().extract().path("accessToken");

        System.out.println(bearerToken);

        return bearerToken;

    }

}
