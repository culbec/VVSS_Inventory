package inventory.test;

import inventory.model.InhousePart;
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
        Part part = new InhousePart("Ax", 50.0, 10, 1, 20, 1001);
        part.setPartId(1);

        InventoryRepository spyRepo = Mockito.spy(repo);

        // avoid actual file writing
        doNothing().when(spyRepo).writeAll();

        spyRepo.addPart(part);

        ObservableList<Part> parts = spyRepo.getAllParts();
        assertTrue(parts.contains(part));
        verify(spyRepo, times(1)).writeAll(); // verify writeAll() is called
    }

    @Test
    void testLookupPartByName() {
        Part part = new InhousePart("Rotor", 100.0, 5, 1, 10, 123);
        part.setPartId(2);
        repo.getAllParts().add(part);

        Part found = repo.lookupPart("Rotor");
        assertNotNull(found);
        assertEquals("Rotor", found.getName());
    }

    @Test
    void testLookupPartById() {
        Part part = new InhousePart("Blade", 80.0, 3, 1, 10, 321);
        part.setPartId(5);
        repo.getAllParts().add(part);

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
