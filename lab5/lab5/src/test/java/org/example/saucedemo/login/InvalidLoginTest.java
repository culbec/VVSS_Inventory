package org.example.saucedemo.login;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.junit.annotations.TestData;
import org.example.InvalidLoginData;
import org.example.pages.LoginPage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RunWith(SerenityParameterizedRunner.class)
public class InvalidLoginTest {

    @Managed
    WebDriver driver;

    LoginPage loginPage;

    private final InvalidLoginData data;

    public InvalidLoginTest(InvalidLoginData data) {
        this.data = data;
    }

    @TestData
    public static Collection<Object[]> invalidData() {
        Gson gson = new Gson();
        Type listType = new TypeToken<List<InvalidLoginData>>() {}.getType();
        InputStreamReader reader = new InputStreamReader(
                InvalidLoginTest.class.getClassLoader().getResourceAsStream("data/invalid_login_data.json")
        );
        List<InvalidLoginData> testData = gson.fromJson(reader, listType);
        return testData.stream()
                .map(d -> new Object[]{d})
                .collect(Collectors.toList());
    }

    @Test
    public void shouldShowCorrectErrorMessageForInvalidLogin() {
        loginPage.open();
        loginPage.loginAs(data.username, data.password);
        loginPage.shouldSeeLoginErrorContaining(data.error);
    }
}
