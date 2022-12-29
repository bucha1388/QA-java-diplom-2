import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import testsdata.NewUser;
import testsdata.WorkWithUser;

import static org.hamcrest.Matchers.equalTo;
import static testsdata.Endpoints.BASE;
import static testsdata.TestsData.*;

public class LoginUserTests {
    WorkWithUser work = new WorkWithUser();
    NewUser newUser = new NewUser(EMAIL, PASSWORD, NAME);



    @Before
    public void setUp() {
        RestAssured.baseURI = BASE;
        work.createUser(newUser);
    }

    @Test
    @DisplayName("Check login wih all right data")
    @Description("Проверка, что можно залогинится под данными созданного пользователя")
    public void loginNewUserPositiveTest(){
        work.loginUser(newUser)
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Check login wih wrong email")
    @Description("Проверка, что нельзя залогинится с екверным логином")
    public void loginNewUserWithWrongEmail(){
         work.loginUser(new NewUser(WRONG_LOGIN, newUser.getPassword(), newUser.getName()))
                .then().assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }

    @Test
    @DisplayName("Check login wih wrong password")
    @Description("Проверка, что нельзя залогинится с неверным паролем")
    public void loginNewUserWithWrongPassword(){
        work.loginUser(new NewUser(newUser.getLogin(), WRONG_PASSWORD, newUser.getName()))
                .then().assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(401);
    }



    @After
    public void deleteNewUser() {
        work.deleteUser(work.bearerToken(newUser));
    }

}
