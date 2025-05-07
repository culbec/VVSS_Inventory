module inventoryTest {
    requires inventory;
    requires javafx.base;
    requires org.junit.jupiter.api;
    requires org.mockito;

    opens inventory to org.junit.platform.commons;
    opens inventory.model to org.junit.platform.commons;
    opens inventory.repository to org.junit.platform.commons;
    opens inventory.validator to org.junit.platform.commons;
    opens inventory.service to org.junit.platform.commons;

    opens part.test to org.junit.platform.commons;
    opens product.test to org.junit.platform.commons;
    opens inventory.test to org.junit.platform.commons;
}