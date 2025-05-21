package org.example.pages;

import net.thucydides.core.pages.PageObject;
import org.openqa.selenium.By;

import static junit.framework.TestCase.assertEquals;

public class CheckoutPage extends PageObject {
    private final By pageTitle = By.className( "title" );

    public void enterCheckoutInfo( String firstName, String lastName, String postalCode ) {
        $( "#first-name" ).type( firstName );
        $( "#last-name" ).type( lastName );
        $( "#postal-code" ).type( postalCode );
        $( "#continue" ).click( );
    }

    public void finishOrder( ) {
        $( "#finish" ).click( );
    }

    public void shouldBeOnCheckoutPage( ) {
        String actualTitle = $( pageTitle ).getText( );
        assertEquals( "Page title should indicate checkout step", "Checkout: Your Information", actualTitle );
    }
}
