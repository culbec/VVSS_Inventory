package inventory.test;

import inventory.model.Part;
import inventory.repository.InventoryRepository;
import inventory.service.InventoryService;
import inventory.validator.ValidatorException;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InventoryServiceIntegrationSRETest {

    private InventoryService service;

    @BeforeEach
    void setUp() {
        InventoryRepository repo = new InventoryRepository(true); // repo real fără fișiere
        service = new InventoryService(repo);
    }

    @Test
    void testAddInhousePart_ValidFlow_Integration() throws ValidatorException {
        service.addInhousePart("Piston", 200.0, 10, 1, 20, 888);

        ObservableList<Part> parts = service.getAllParts();
        assertEquals(1, parts.size());

        Part added = parts.get(0);
        assertEquals("Piston", added.getName());
        assertEquals(200.0, added.getPrice());
        assertEquals(888, ((inventory.model.InhousePart) added).getMachineId());
    }

    @Test
    void testAddInhousePart_Invalid_Integration() {
        assertThrows(ValidatorException.class, () -> {
            service.addInhousePart("", -100.0, -1, 10, 5, 999);
        });
    }
}
