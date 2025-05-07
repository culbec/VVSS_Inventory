package inventory.test;

import inventory.model.InhousePart;
import inventory.model.Part;
import inventory.repository.InventoryRepository;
import inventory.service.InventoryService;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryServiceIntegrationTest {

    private InventoryService service;

    @BeforeEach
    void setUp() {
        InventoryRepository repo = new InventoryRepository(true); // repo real, fără fișiere
        service = new InventoryService(repo);
    }

    @Test
    void testAddAndLookupPart_ByName() {
        Part part = new InhousePart("Rulment", 80.0, 15, 5, 20, 999);
        part.setPartId(5);

        service.getAllParts().add(part); // forțăm adăugarea în repo

        Part found = service.lookupPart("Rulment");
        assertNotNull(found);
        assertEquals("Rulment", found.getName());
    }

    @Test
    void testAddAndLookupPart_ById() {
        Part part = new InhousePart("Ventilator", 120.0, 8, 3, 15, 77);
        part.setPartId(6);
        service.getAllParts().add(part);

        Part found = service.lookupPart("6");
        assertNotNull(found);
        assertEquals("Ventilator", found.getName());
    }
}
