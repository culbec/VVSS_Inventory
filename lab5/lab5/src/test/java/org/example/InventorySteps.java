package org.example;

import net.thucydides.core.annotations.Step;
import org.example.pages.InventoryPage;

import static org.junit.Assert.assertTrue;

public class InventorySteps {

    InventoryPage inventoryPage;

    @Step
    public void open_inventory_page() {
        inventoryPage.open();
        inventoryPage.login_as_standard_user();
    }

    @Step
    public void description_should_contain(String keyword) {
        boolean found = inventoryPage.any_description_contains(keyword);
        assertTrue("Expected at least one product to contain: " + keyword, found);
    }
}
