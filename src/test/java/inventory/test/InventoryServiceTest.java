package inventory.test;

import inventory.repository.InventoryRepository;
import inventory.service.InventoryService;
import inventory.validator.ValidatorException;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for F01a - Name and Price Validations Only")
public class InventoryServiceTest {

    private InventoryService service;

    @BeforeEach
    void setUp() {
        InventoryRepository repo = new InventoryRepository(true); // skip file reading
        service = new InventoryService(repo);
    }

    @AfterEach
    void tearDown() {
        service = null;
    }

    // ✅ nume valid
    @Test
    @DisplayName("Add part with valid name")
    void testAddPartWithValidName() throws ValidatorException {
        service.addInhousePart("Axle", 10.0, 5, 1, 10, 1);
        assertNotNull(service.lookupPart("Axle"));
    }

    // ❌ nume invalid
    @Test
    @DisplayName("Fail to add part with empty name")
    void testAddPartWithInvalidName() {
        assertThrows(ValidatorException.class, () ->
                service.addInhousePart("   ", 10.0, 5, 1, 10, 1));
    }

    // ✅ preț valid
    @Test
    @DisplayName("Add part with valid price")
    void testAddPartWithValidPrice() throws ValidatorException {
        service.addInhousePart("Bolt", 0.01, 5, 1, 10, 1);
        assertNotNull(service.lookupPart("Bolt"));
    }

    // ❌ preț invalid
    @Test
    @DisplayName("Fail to add part with negative price")
    void testAddPartWithInvalidPrice() {
        assertThrows(ValidatorException.class, () ->
                service.addInhousePart("Bolt", -5.0, 5, 1, 10, 1));
    }

    // ✅ nume + preț valide
    @Test
    @DisplayName("Add part with valid name and price")
    void testAddPartWithValidNameAndPrice() throws ValidatorException {
        service.addInhousePart("Gear", 5.5, 5, 1, 10, 1);
        assertNotNull(service.lookupPart("Gear"));
    }

    // ❌ nume + preț invalide
    @Test
    @DisplayName("Fail to add part with empty name and negative price")
    void testAddPartWithInvalidNameAndPrice() {
        assertThrows(ValidatorException.class, () ->
                service.addInhousePart("", -1.0, 5, 1, 10, 1));
    }
}
