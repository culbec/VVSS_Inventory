module inventory {
    requires javafx.graphics;
    requires javafx.fxml;
    requires javafx.controls;
    requires org.apache.logging.log4j;
    requires org.apache.logging.log4j.core;

    opens inventory.model to javafx.base;
    exports inventory.model;

    opens inventory.validator to javafx.base;
    exports inventory.validator;

    opens inventory.repository to javafx.fxml;
    exports inventory.repository;

    opens inventory to javafx.fxml;
    exports inventory;

    opens inventory.service to javafx.fxml;
    exports inventory.service;

    opens inventory.controller to javafx.fxml;
    exports inventory.controller;
}