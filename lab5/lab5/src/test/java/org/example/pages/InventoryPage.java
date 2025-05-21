package org.example.pages;

import net.serenitybdd.core.pages.PageObject;
import net.serenitybdd.core.pages.WebElementFacade;
import net.thucydides.core.annotations.DefaultUrl;

import java.util.List;

@DefaultUrl("https://www.saucedemo.com/")
public class InventoryPage extends PageObject {

    public void login_as_standard_user() {
        $("#user-name").type("standard_user");
        $("#password").type("secret_sauce");
        $("#login-button").click();
    }

    public boolean any_description_contains(String keyword) {
        List<WebElementFacade> descriptions = findAll(".inventory_item_description");

        return descriptions.stream()
                .map(WebElementFacade::getText)
                .anyMatch(text -> text.toLowerCase().contains(keyword.toLowerCase()));
    }
}
