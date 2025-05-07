package product.test;

import inventory.model.Part;
import inventory.model.Product;
import inventory.repository.InventoryRepository;
import inventory.service.InventoryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class SearchProductTest {

    private InventoryService service;

    @BeforeEach
    void setUp() {
        InventoryRepository repo = new InventoryRepository(true);
        service = new InventoryService(repo);

        ObservableList<Part> parts = FXCollections.observableArrayList();

        Product ceas = new Product("ceas", 100.0, 5, 1, 10, parts);
        ceas.setProductId(1);
        Product lampa = new Product("lampă", 80.0, 3, 1, 10, parts);
        lampa.setProductId(2);
        Product laptop = new Product("laptop", 2000.0, 10, 1, 20, parts);
        laptop.setProductId(3);
        Product mouse = new Product("mouse", 2010.0, 30, 1, 20, parts);
        mouse.setProductId(4);

        repo.addProduct(ceas);
        repo.addProduct(lampa);
        repo.addProduct(laptop);
        repo.addProduct(mouse);
    }

    // F02_TC01: input null
    @Test
    void test_TC01() {
        Product result = service.lookupProduct(null);
        assertNull(result);
    }

    // F02_TC02: căutare fără rezultat
    @Test
    void test_TC02() {
        Product result = service.lookupProduct("telefon");
        assertNull(result);
    }

    // F02_TC03: căutare parțială "ceas" (match parțial, dar returnează primul)
    @Test
    void test_TC03() {
        Product result = service.lookupProduct("cea");
        assertNotNull(result);
        assertEquals("ceas", result.getName());
    }

    // F02_TC04: căutare după id = "1"
    @Test
    void test_TC04() {
        Product result = service.lookupProduct("1");
        assertNotNull(result);
        assertEquals("ceas", result.getName());
    }

    // F02_TC05: căutare după nume "lampă"
    @Test
    void test_TC05_cautareExactaLampa() {
        Product result = service.lookupProduct("lampă");
        assertNotNull(result);
        assertEquals("lampă", result.getName());
    }

    // F02_TC06: căutare după id = "2"
    @Test
    void test_TC06_cautareDupaId2() {
        Product result = service.lookupProduct("2");
        assertNotNull(result);
        assertEquals("lampă", result.getName());
    }

    // F02_TC07: căutare după id = "3"
    @Test
    void test_TC07_cautareDupaId3() {
        Product result = service.lookupProduct("3");
        assertNotNull(result);
        assertEquals("laptop", result.getName());
    }

    // F02_TC08: căutare după id = "4"
    @Test
    void test_TC08_cautareDupaId4() {
        Product result = service.lookupProduct("4");
        assertNotNull(result);
        assertEquals("mouse", result.getName());
    }
    // F02_TC09: căutare după id = "5" NULL
    @Test
    void test_TC09_cautareDupaId5() {
        Product result = service.lookupProduct("5");
        assertNull(result);
    }

}
