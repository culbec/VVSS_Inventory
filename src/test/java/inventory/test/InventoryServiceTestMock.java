package inventory.test;

import inventory.model.InhousePart;
import inventory.model.Part;
import inventory.repository.InventoryRepository;
import inventory.service.InventoryService;
import inventory.validator.ValidatorException;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class InventoryServiceTestMock {

    private InventoryRepository mockRepo;
    private InventoryService service;

    @BeforeEach
    void setUp() {
        mockRepo = mock(InventoryRepository.class);
        service = new InventoryService(mockRepo);
    }

    @Test
    void testAddInhousePart_ValidInput_AddsToRepo() throws ValidatorException {
        when(mockRepo.getAutoPartId()).thenReturn(10);

        service.addInhousePart("Motor", 150.0, 5, 1, 10, 123);

        ArgumentCaptor<Part> partCaptor = ArgumentCaptor.forClass(Part.class);
        verify(mockRepo, times(1)).addPart(partCaptor.capture());

        Part addedPart = partCaptor.getValue();
        assertEquals("Motor", addedPart.getName());
        assertEquals(150.0, addedPart.getPrice());
        assertEquals(10, addedPart.getPartId());
    }

    @Test
    void testLookupPart_DelegatesToRepo() {
        Part dummy = new InhousePart("Ax", 50.0, 10, 1, 20, 99);
        when(mockRepo.lookupPart("Ax")).thenReturn(dummy);

        Part result = service.lookupPart("Ax");

        assertEquals(dummy, result);
        verify(mockRepo, times(1)).lookupPart("Ax");
    }

    @Test
    void testAddInhousePart_Invalid_ThrowsValidatorException() {
        assertThrows(ValidatorException.class, () ->
                service.addInhousePart("", -10.0, -2, 5, 2, 100));
    }
}
