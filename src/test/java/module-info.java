module Inventory {
    requires inventory;
    requires javafx.base;
    requires org.junit.jupiter.api;
    requires org.mockito;

    opens part.test to org.junit.platform.commons;
    opens product.test to org.junit.platform.commons;
    opens inventory.test to org.junit.platform.commons;
}