package inventory.test;

import inventory.model.Part;
import inventory.repository.InventoryRepository;
import inventory.service.InventoryService;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InventoryServiceIntegrationTest {

    private InventoryService service;

    @BeforeEach
    void setUp() {
        InventoryRepository repo = new InventoryRepository(true); // repo real, fără fișiere
        service = new InventoryService(repo);
    }

    @Test
    void testAddAndLookupPart_ByName() {
        Part mockPart = mock(Part.class);
        when(mockPart.getName()).thenReturn("Rulment");
        when(mockPart.getPartId()).thenReturn(5);

        service.getAllParts().add(mockPart); // forțăm adăugarea în repo

        Part found = service.lookupPart("Rulment");
        assertNotNull(found);
        assertEquals("Rulment", found.getName());
    }

    @Test
    void testAddAndLookupPart_ById() {
        Part mockPart = mock(Part.class);
        when(mockPart.getName()).thenReturn("Ventilator");
        when(mockPart.getPartId()).thenReturn(6);

        service.getAllParts().add(mockPart);

        Part found = service.lookupPart("6");
        assertNotNull(found);
        assertEquals("Ventilator", found.getName());
    }
}