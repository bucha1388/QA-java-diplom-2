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

public class ChangeUserDataTest {

    WorkWithUser work = new WorkWithUser();
    NewUser newUser = new NewUser(EMAIL, PASSWORD, NAME);



    @Before
    public void setUp() {
        RestAssured.baseURI = BASE;
        work.createUser(newUser);
    }

    @Test
    @DisplayName("Check change user login with autorization")
    @Description("Проверка, что можно изминить электронную почту пользователя передав токен")
    public void patchNewUserEmailWithTokenTest(){

        String token = work.bearerToken(newUser);
        newUser.setEmail(NEW_EMAIL);
        work.patchUser(newUser, token)
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Check change user email with autorization to this email")
    @Description("Проверка, что нельзя изминить электронную почту пользователя передав токен на ту же самую")
    public void patchNewUserEmailToThisWithTokenTest(){

        String token = work.bearerToken(newUser);
                work.patchUser(newUser, token)
                .then().assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("User with such email already exists"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Check change user login with autorization")
    @Description("Проверка, что можно изминить электронную почту пользователя передав токен")
    public void patchNewUserPasswordWithTokenTest(){

        String token = work.bearerToken(newUser);
        newUser.setPassword(NEW_PASSWORD);
        work.patchUser(newUser, token)
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Check change user name with autorization")
    @Description("Проверка, что можно изминить имя пользователя передав токен")
    public void patchNewUserNameWithTokenTest(){

        String token = work.bearerToken(newUser);
        newUser.setName(NEW_NAME);
        work.patchUser(newUser, token)
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);
    }

    @Test
    @DisplayName("Check change user login without autorization, 401 and message")
    @Description("Проверка, что нельзя изминить электронную почту пользователя без токен")
    public void patchNewUserEmailWithoutTokenTest(){

        newUser.setEmail(NEW_EMAIL);
        work.patchUser(newUser, "")
                .then().assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
        newUser.setEmail(EMAIL);
    }

    @Test
    @DisplayName("Check change user password without autorization, 401 and message")
    @Description("Проверка, что нельзя изминить пароль пользователя без токен")
    public void patchNewUserPasswordWithoutTokenTest(){

        newUser.setPassword(NEW_PASSWORD);
        work.patchUser(newUser, "")
                .then().assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
        newUser.setPassword(PASSWORD);
    }

    @Test
    @DisplayName("Check change user name without autorization, 401 and message")
    @Description("Проверка, что нельзя изминить имяь пользователя без токен")
    public void patchNewUserNameWithoutTokenTest(){

        newUser.setName(NEW_NAME);
        work.patchUser(newUser, "")
                .then().assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(401);
        newUser.setName(NAME);
    }

    @After
    public void deleteNewUser() {
        work.deleteUser(work.bearerToken(newUser));
    }
}
