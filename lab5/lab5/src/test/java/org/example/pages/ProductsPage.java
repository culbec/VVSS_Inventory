package org.example.pages;

import net.thucydides.core.pages.PageObject;

import java.time.Duration;

import static junit.framework.TestCase.assertEquals;

public class ProductsPage extends PageObject {

    public void addFirstProductToCart( ) {

        $( ".inventory_item button" ).click( ); // adaugă primul produs
        $(".shopping_cart_badge").waitUntilVisible().withTimeoutOf(Duration.ofSeconds(5));
    }

    public void goToCart( ) {
        $( ".shopping_cart_link" ).click( );
    }

    public void shouldBeOnProductsPage( ) {
        String actualTitle = $( ".title" ).getText( );
        assertEquals( "Page title should be 'Products'", actualTitle, "Products" );
    }

    public void cartBadgeShouldShow( String expectedCount ) {
//        String actualCount = $( ".shopping_cart_badge" ).getText( );
//        assertEquals(  "Cart badge count mismatch", expectedCount, actualCount );
        try {
            // Așteaptă maxim 5 secunde apariția badge-ului
            $(".shopping_cart_badge").waitUntilVisible().withTimeoutOf(Duration.ofSeconds(5));
            String actualCount = $(".shopping_cart_badge").getText();
            assertEquals("Cart badge count mismatch", expectedCount, actualCount);
        } catch (Exception e) {
            throw new AssertionError("Cart badge not found or not visible. Possible pop-up interference.", e);
        }
    }
}
