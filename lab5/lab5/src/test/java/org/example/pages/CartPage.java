package org.example.pages;

import net.thucydides.core.pages.PageObject;

import java.time.Duration;

import static junit.framework.TestCase.assertTrue;

public class CartPage extends PageObject {
    public void proceedToCheckout( ) {
        $( "#checkout" ).click( );
        $(".title").waitUntilVisible().withTimeoutOf(Duration.ofSeconds(5));
    }

   /* public void shouldContainOneProduct( ) {
        try {
            Thread.sleep( 3000 ); // Wait 3 seconds so you can manually click "OK"
        } catch ( InterruptedException e ) {
            e.printStackTrace( );
        }
        boolean isProductVisible = $( ".cart_item" ).isVisible( );
        assertTrue( isProductVisible );
    }*/
   public void shouldContainOneProduct() {
       try {
           $(".cart_item").waitUntilVisible().withTimeoutOf(Duration.ofSeconds(5));
           assertTrue("Product should be visible in cart", $(".cart_item").isVisible());
       } catch (Exception e) {
           throw new AssertionError("Product not visible in cart. Possible click failure or popup issue.", e);
       }
   }

}
