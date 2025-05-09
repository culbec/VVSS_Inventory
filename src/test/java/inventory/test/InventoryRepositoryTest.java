package inventory.test;

import inventory.model.Part;
import inventory.repository.InventoryRepository;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InventoryRepositoryTest {

    private InventoryRepository repo;

    @BeforeEach
    void setUp() {
        repo = new InventoryRepository(true); // fără citire din fișiere
    }

    @Test
    void testAddPartAddsToList() {
        Part mockPart = mock(Part.class);
        when(mockPart.getPartId()).thenReturn(1);

        InventoryRepository spyRepo = Mockito.spy(repo);

        // avoid actual file writing
        doNothing().when(spyRepo).writeAll();

        spyRepo.addPart(mockPart);

        ObservableList<Part> parts = spyRepo.getAllParts();
        assertTrue(parts.contains(mockPart));
        verify(spyRepo, times(1)).writeAll(); // verify writeAll() is called
    }

    @Test
    void testLookupPartByName() {
        Part mockPart = mock(Part.class);
        when(mockPart.getName()).thenReturn("Rotor");
        when(mockPart.getPartId()).thenReturn(2);
        repo.getAllParts().add(mockPart);

        Part found = repo.lookupPart("Rotor");
        assertNotNull(found);
        assertEquals("Rotor", found.getName());
    }

    @Test
    void testLookupPartById() {
        Part mockPart = mock(Part.class);
        when(mockPart.getName()).thenReturn("Blade");
        when(mockPart.getPartId()).thenReturn(5);
        repo.getAllParts().add(mockPart);

        Part found = repo.lookupPart("5");
        assertNotNull(found);
        assertEquals("Blade", found.getName());
    }

    @Test
    void testLookupPartNotFound() {
        Part result = repo.lookupPart("SomethingMissing");
        assertNull(result);
    }
}