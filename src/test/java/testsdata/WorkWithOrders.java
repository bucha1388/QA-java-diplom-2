package testsdata;

import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static testsdata.Endpoints.API_ORDERS;
import static testsdata.Endpoints.API_REGISTER;

public class WorkWithOrders {

    public Response createOrder(NewOrder order, String bearerToken) {

        Response response =
                given()
                        .headers("Content-type", "application/json", "Authorization", bearerToken)
                        .body(order)
                        .when()
                        .post(API_ORDERS);


        System.out.println("создаем заказ... " + response.asString());

        return response;

    }

    public Response getOrderList(String bearerToken) {

        Response response =
                given()
                        .headers("Content-type", "application/json", "Authorization", bearerToken)
                        .when()
                        .get(API_ORDERS);

        System.out.println("список заказов... " + response.asString());

        return response;

    }

}
