import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import testsdata.NewOrder;
import testsdata.NewUser;
import testsdata.WorkWithOrders;
import testsdata.WorkWithUser;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static testsdata.Endpoints.BASE;
import static testsdata.TestsData.*;

public class CreateOrdersTest {

    WorkWithUser work = new WorkWithUser();
    WorkWithOrders orderWork = new WorkWithOrders();
    NewUser newUser = new NewUser(EMAIL, PASSWORD, NAME);

    NewOrder order = new NewOrder(new String[]{"61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa77", "61c0c5a71d1f82001bdaaa6f" });

    NewOrder orderWithWrongIngredients = new NewOrder(new String[]{"61cdsfga433aaa63", "61c0c5awwaeaa77", "61c0c5awaeasd001bdaaa63" });

    NewOrder orderWithoutIngredients = new NewOrder(new String[]{});


    @Before
    public void setUp() {
        RestAssured.baseURI = BASE;
        work.createUser(newUser);
    }

    @Test
    @DisplayName("Check status code of /api/orders and JSON contains true and name")
    @Description("Проверка, что при токене и всех правильных данных заказ создается")
    public void createNewOrderPositiveTest(){
        String token = work.bearerToken(newUser);
        orderWork.createOrder(order, token)
                .then().assertThat().body("success", equalTo(true))
                .assertThat().body("name", notNullValue())
                .and()
                .statusCode(200);

    }

    @Test
    @DisplayName("Check status code of /api/orders without token")
    @Description("Проверка, что без токене со всеми правильных данных заказ создается(НА МОЙ ВЗГЛЯД НЕ ДОЛЖЕН)")
    public void createNewOrderWithoutTokenTest(){
        orderWork.createOrder(order, "")
                .then().assertThat().body("success", equalTo(true))
                .assertThat().body("name", notNullValue())
                .and()
                .statusCode(200);


    }

    @Test
    @DisplayName("Check order without ingredients with token")
    @Description("Проверка, что с токеном, без игридиентов заказ не создается")
    public void createNewOrderWithoutIngredientsWithTokenTest(){
        String token = work.bearerToken(newUser);
        orderWork.createOrder(orderWithoutIngredients, token)
                .then().assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(400);

    }

    @Test
    @DisplayName("Check order without ingredients without token")
    @Description("Проверка, что без токеном, без игридиентов заказ не создается")
    public void createNewOrderWithoutIngredientsWithoutTokenTest(){
        String token = work.bearerToken(newUser);
        orderWork.createOrder(orderWithoutIngredients, "")
                .then().assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(400);

    }


    @Test
    @DisplayName("Check order with wrong hash ingredients with token")
    @Description("Проверка, что при токене и не правильных данных заказ НЕ создается")
    public void createNewOrderWithWrongIngredientsWithTokenTest(){
        String token = work.bearerToken(newUser);
        orderWork.createOrder(orderWithWrongIngredients, token)
                .then().assertThat().statusCode(500);
    }


    @Test
    @DisplayName("Check order with wrong hash ingredients without token")
    @Description("Проверка, что без токене и не правильных данных заказ НЕ создается")
    public void createNewOrderWithWrongIngredientsWithoutTokenTest(){
        String token = work.bearerToken(newUser);
        orderWork.createOrder(orderWithWrongIngredients, "")
                .then().assertThat().statusCode(500);
    }

    @After
    public void deleteNewUser() {
       work.deleteUser(work.bearerToken(newUser));
    }

}
