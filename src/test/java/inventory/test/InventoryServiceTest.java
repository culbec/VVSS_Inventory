package inventory.test;

import inventory.model.InhousePart;
import inventory.repository.InventoryRepository;
import inventory.service.InventoryService;
import inventory.validator.ValidatorException;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Tests for F01a - Adding Parts with Validation")
class InventoryServiceTest {

    private InventoryService service;

    @BeforeEach
    void setUp() {
        InventoryRepository repo = new InventoryRepository(true);
        repo.getAllParts().clear();
        service = new InventoryService(repo);
    }

    @AfterEach
    void tearDown() {
        service = null;
    }

    // ✅ ECP valid
    @Test
    @DisplayName("Add valid inhouse part")
    @Tag("ECP")
    void testAddValidInhousePart() throws ValidatorException {
        int initialSize = service.getAllParts().size();
        service.addInhousePart("Motor", 100.0, 5, 1, 10, 123);
        ObservableList<?> parts = service.getAllParts();

        assertEquals(initialSize + 1, parts.size());
        assertTrue(parts.get(parts.size() - 1) instanceof InhousePart);
    }

    // ❌ ECP - stoc negativ
    @Test
    @DisplayName("Invalid inhouse part with negative stock")
    @Tag("ECP")
    void testAddInhousePartNegativeStock() {
        assertThrows(ValidatorException.class, () ->
                service.addInhousePart("Rotor", 50.0, -1, 1, 5, 123));
    }

    // ❌ ECP - min > max
    @Test
    @DisplayName("Invalid inhouse part with min > max")
    @Tag("ECP")
    void testAddInhousePartMinGreaterThanMax() {
        assertThrows(ValidatorException.class, () ->
                service.addInhousePart("Axle", 60.0, 5, 10, 5, 111));
    }

    // ❌ ECP - nume lipsă
    @Test
    @DisplayName("Invalid part with empty name")
    @Tag("ECP")
    void testAddInhousePartEmptyName() {
        assertThrows(ValidatorException.class, () ->
                service.addInhousePart("   ", 80.0, 3, 1, 5, 100));
    }

    // ✅ BVA - stoc = min
    @Test
    @DisplayName("Valid part with stock = min")
    @Tag("BVA")
    void testAddInhousePartStockEqualsMin() throws ValidatorException {
        service.addInhousePart("Frame", 20.0, 1, 1, 5, 200);
        assertNotNull(service.lookupPart("Frame"));
    }

    // ✅ BVA - stoc = max
    @Test
    @DisplayName("Valid part with stock = max")
    @Tag("BVA")
    void testAddInhousePartStockEqualsMax() throws ValidatorException {
        service.addInhousePart("Cover", 15.0, 10, 1, 10, 201);
        assertNotNull(service.lookupPart("Cover"));
    }

    // ❌ BVA - stoc = -1
    @Test
    @DisplayName("Invalid part with stock = -1")
    @Tag("BVA")
    void testAddInhousePartStockNegativeBoundary() {
        assertThrows(ValidatorException.class, () ->
                service.addInhousePart("Pipe", 25.0, -1, 0, 5, 202));
    }

    // ❌ BVA - min = max + 1
    @Test
    @DisplayName("Invalid part with min = max + 1")
    @Tag("BVA")
    void testAddInhousePartInvalidMinMaxBoundary() {
        assertThrows(ValidatorException.class, () ->
                service.addInhousePart("Spring", 22.0, 5, 6, 5, 203));
    }

    // ✅ ParametrizedTest – valori valide
    @ParameterizedTest
    @ValueSource(strings = {"Rotor", "Bolt", "Wheel"})
    @DisplayName("Add parts with different valid names")
    @Tag("ECP")
    void testAddPartsWithValidNames(String name) throws ValidatorException {
        service.addInhousePart(name, 10.0, 1, 1, 5, 100);
        assertNotNull(service.lookupPart(name));
    }

    // ❌ Test inactiv temporar
    @Test
    @Disabled("Testul pentru ID duplicat nu se aplică deoarece ID-ul e generat automat.")
    void testDuplicateIdShouldFail() {
        // Placeholder
    }
}
