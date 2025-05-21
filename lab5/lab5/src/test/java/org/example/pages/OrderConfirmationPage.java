package org.example.pages;

import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.By;

import static junit.framework.TestCase.assertEquals;


public class OrderConfirmationPage extends PageObject {
    private final By completeHeader = By.className( "complete-header" );

    public void shouldSeeThankYouMessage( ) {
        String actualMessage = $( completeHeader ).getText( );
        assertEquals( "Thank you for your order!", actualMessage );
    }

    public void logout( ) {
        $( "#react-burger-menu-btn" ).click( );
        $( "#logout_sidebar_link" ).click( );
    }
}
