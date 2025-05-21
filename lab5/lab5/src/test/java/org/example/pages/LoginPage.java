package org.example.pages;

import net.serenitybdd.core.pages.PageObject;
import net.thucydides.core.annotations.DefaultUrl;
import org.openqa.selenium.By;

import static junit.framework.TestCase.assertTrue;

@DefaultUrl("https://www.saucedemo.com/")
public class LoginPage extends PageObject {

    private final By usernameField = By.id( "user-name" );
    private final By passwordField = By.id( "password" );
    private final By loginButton = By.id( "login-button" );
    private final By inventoryTitle = By.className( "title" );
    private final By errorMessage = By.cssSelector( "h3[data-test='error']" );

    public void loginAs( String username, String password ) {
        $( usernameField ).type( username );
        $( passwordField ).type( password );
        $( loginButton ).click( );
    }

    public void shouldSeeInventoryPage( ) {
        $( inventoryTitle ).shouldBeVisible( );
    }

    public void shouldSeeLoginErrorContaining( String expectedText ) {
        $( errorMessage ).shouldBeVisible( );
        $( errorMessage ).shouldContainText( expectedText );
    }

    public void shouldBeOnLoginPage() {
        boolean isVisible = $(loginButton).isVisible();
        assertTrue("Login button should be visible on login page", isVisible);
    }
}
