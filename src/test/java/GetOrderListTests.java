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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static testsdata.Endpoints.BASE;
import static testsdata.TestsData.*;

public class GetOrderListTests {

    WorkWithUser work = new WorkWithUser();
    WorkWithOrders orderWork = new WorkWithOrders();
    NewUser newUser = new NewUser(EMAIL, PASSWORD, NAME);

    NewOrder order = new NewOrder(new String[]{"61c0c5a71d1f82001bdaaa6c", "61c0c5a71d1f82001bdaaa77", "61c0c5a71d1f82001bdaaa6f" });



    @Before
    public void setUp() {
        RestAssured.baseURI = BASE;
        work.createUser(newUser);
    }

    @Test
    @DisplayName("Check list of order authorised user")
    @Description("Проверка, что для авторизированного пользователя можно получить список его заказов")
    public void OrderListWithAuthorisationTest(){
        String token = work.bearerToken(newUser);
        orderWork.createOrder(order, token);
        orderWork.createOrder(order, token);
        orderWork.getOrderList(token)
                .then().assertThat().body("total", equalTo(2))
                .assertThat().body("orders", notNullValue())
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Check list of order NOT authorised user")
    @Description("Проверка, что для неавторизированного пользователя нельзя получить список заказов")
    public void OrderListWitouthAuthorisationTest(){
        String token = work.bearerToken(newUser);
        orderWork.getOrderList("")
                .then().assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
    }



    @After
    public void deleteNewUser() {
        work.deleteUser(work.bearerToken(newUser));
    }


}
