package org.example.saucedemo;

import net.serenitybdd.junit.runners.SerenityParameterizedRunner;
import net.thucydides.core.annotations.Managed;
import net.thucydides.core.annotations.Steps;
import net.thucydides.junit.annotations.UseTestDataFrom;
import org.example.InventorySteps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;

@RunWith(SerenityParameterizedRunner.class)
@UseTestDataFrom("src/test/resources/SearchData.csv")
public class SearchByKeywordStoryCSV {

    public String keyword;

    @Managed
    WebDriver driver;

    @Steps
    InventorySteps inventorySteps;

    @Test
    public void description_should_contain_the_keyword() {
        inventorySteps.open_inventory_page();
        inventorySteps.description_should_contain(keyword);
    }
}
