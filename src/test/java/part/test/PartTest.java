package part.test;

import inventory.model.Part;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PartTest {

    @Test
    void testPartValidInput() {
        String result = Part.isValidPart("Motor", 50.0, 10, 5, 20);
        assertEquals("", result); // fără erori
    }

    @Test
    void testPartInvalidNameEmpty() {
        String result = Part.isValidPart("", 50.0, 10, 5, 20);
        assertTrue(result.contains("A name has not been entered."));
    }

    @Test
    void testPartInvalidPriceTooLow() {
        String result = Part.isValidPart("Ax", 0.0, 10, 5, 20);
        assertTrue(result.contains("The price must be greater than 0."));
    }

    @Test
    void testPartInventoryBelowMin() {
        String result = Part.isValidPart("Ax", 30.0, 2, 5, 20);
        assertTrue(result.contains("Inventory level is lower than minimum value."));
    }

    @Test
    void testPartInventoryAboveMax() {
        String result = Part.isValidPart("Ax", 30.0, 25, 5, 20);
        assertTrue(result.contains("Inventory level is higher than the maximum value."));
    }

    @Test
    void testPartMinGreaterThanMax() {
        String result = Part.isValidPart("Ax", 30.0, 10, 30, 20);
        assertTrue(result.contains("The Min value must be less than the Max value."));
    }

    @Test
    void testPartInventoryLessThanOne() {
        String result = Part.isValidPart("Ax", 30.0, 0, 0, 20);
        assertTrue(result.contains("Inventory level must be greater than 0."));
    }
}
