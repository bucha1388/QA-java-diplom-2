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

public class CreateUserTests {

    WorkWithUser work = new WorkWithUser();
    NewUser newUser = new NewUser(EMAIL, PASSWORD, NAME);

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE;
    }

    @Test
    @DisplayName("Check status code of /api/auth/register and JSON contains true")
    @Description("Проверка, что со всеми параметрами пользователь создается")
    public void createNewUserPositiveTest(){
        work.createUser(newUser)
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);


           }

    @Test
    @DisplayName("Check create duplicate user, 403 and  User already exists")
    @Description("Проверка, что нельзя создать дубль юзера")
    public void createNewUserAlreadyCreateTest(){
        work.createUser(newUser)
                .then().assertThat().body("success", equalTo(true))
                .and()
                .statusCode(200);

        work.createUser(newUser)
                .then().assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("User already exists"))
                .and()
                .statusCode(403);
    }

    @Test
    @DisplayName("Check registration without email, 403 and message")
    @Description("Проверкарегистрации без указания электроной почты")
    public void createNewUserWithoutEmail(){
        work.createUser(new NewUser(null, PASSWORD, NAME))
                .then().assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);


    }

    @Test
    @DisplayName("Check registration without password, 403 and message")
    @Description("Проверкарегистрации без указания пароля ")
    public void createNewUserWithoutPassword(){
        work.createUser(new NewUser(EMAIL, null, NAME))
                .then().assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);

    }

    @Test
    @DisplayName("Check registration without name, 403 and message")
    @Description("Проверкарегистрации без указания имени пользователя ")
    public void createNewUserWithoutName(){
        work.createUser(new NewUser(EMAIL, PASSWORD, null))
                .then().assertThat().body("success", equalTo(false))
                .assertThat().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(403);

    }


    @After
    public void deleteNewUser() {
        work.deleteUser(work.bearerToken(newUser));
    }
}
