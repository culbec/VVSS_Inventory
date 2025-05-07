package inventory.controller;

import inventory.model.Part;
import inventory.model.Product;
import inventory.service.InventoryService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainScreenController implements Initializable, Controller {
    // Declare fields
    private final Logger logger = LogManager.getLogger(MainScreenController.class);

    private int modifyPartIndex;
    private int modifyProductIndex;

    private InventoryService service;

    @FXML
    private TableView<Part> partsTableView;

    @FXML
    private TableColumn<Part, Integer> partsIdCol;

    @FXML
    private TableColumn<Part, String> partsNameCol;

    @FXML
    private TableColumn<Part, Integer> partsInventoryCol;

    @FXML
    private TableColumn<Part, Double> partsPriceCol;


    @FXML
    private TableView<Product> productsTableView;

    @FXML
    private TableColumn<Product, Integer> productsIdCol;

    @FXML
    private TableColumn<Product, String> productsNameCol;

    @FXML
    private TableColumn<Product, Integer> productsInventoryCol;

    @FXML
    private TableColumn<Product, Double> productsPriceCol;

    @FXML
    private TextField partsSearchTxt;

    @FXML
    private TextField productsSearchTxt;

    // Declare methods
    public void setService(InventoryService service) {
        this.service = service;
        partsTableView.setItems(service.getAllParts());
        productsTableView.setItems(service.getAllProducts());
    }

    /**
     * Initializes the controller class and populate table views.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Populate parts table view
        partsIdCol.setCellValueFactory(new PropertyValueFactory<>("partId"));
        partsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        partsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Populate products table view
        productsIdCol.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productsNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsInventoryCol.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        productsPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Method to add to button handler to switch to scene passed as source
     *
     * @param event  The event that happened
     * @param source The source of the scene
     * @throws IOException If there is an error in displaying the scene
     */
    private void displayScene(ActionEvent event, String source) throws IOException {
        // Declare fields
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(source));
        Parent scene = loader.load();

        Controller ctrl = loader.getController();
        ctrl.setService(service);

        if (ctrl instanceof ModifyProductController) {
            ModifyProductController modifyProductController = (ModifyProductController) ctrl;
            modifyProductController.setProductIndex(modifyProductIndex);
        } else if (ctrl instanceof ModifyPartController) {
            ModifyPartController modifyPartController = (ModifyPartController) ctrl;
            modifyPartController.setPartIndex(modifyPartIndex);
        }

        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Ask user for confirmation before deleting selected part from list of parts.
     */
    @FXML
    void handleDeletePart() {
        Part part = partsTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Part Deletion?");
        alert.setContentText("Are you sure you want to delete part " + part.getName() + " from parts?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            logger.info("Part deleted.");
            service.deletePart(part);
        } else {
            logger.info("Canceled part deletion.");
        }
    }

    /**
     * Ask user for confirmation before deleting selected product from list of products.
     */
    @FXML
    void handleDeleteProduct() {
        Product product = productsTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Product Deletion?");
        alert.setContentText("Are you sure you want to delete product " + product.getName() + " from products?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            service.deleteProduct(product);
            logger.info("Product " + product.getName() + " was removed.");
        } else {
            logger.info("Product " + product.getName() + " was not removed.");
        }
    }

    /**
     * Switch scene to Add Part
     *
     * @param event The event that happened
     * @throws IOException If there is an error loading the FXML file
     */
    @FXML
    void handleAddPart(ActionEvent event) throws IOException {
        displayScene(event, "/fxml/AddPart.fxml");
    }

    /**
     * Switch scene to Add Product
     *
     * @param event The event that happened
     * @throws IOException If there is an error loading the FXML file
     */
    @FXML
    void handleAddProduct(ActionEvent event) throws IOException {
        displayScene(event, "/fxml/AddProduct.fxml");
    }

    /**
     * Changes scene to Modify Part screen and passes values of selected part
     * and its index
     *
     * @param event The event that happened
     * @throws IOException If there is an error loading the FXML file
     */
    @FXML
    void handleModifyPart(ActionEvent event) throws IOException {
        Part modifyPart = partsTableView.getSelectionModel().getSelectedItem();
        modifyPartIndex = service.getAllParts().indexOf(modifyPart);

        displayScene(event, "/fxml/ModifyPart.fxml");
    }

    /**
     * Switch scene to Modify Product
     *
     * @param event The event that happened
     * @throws IOException If there is an error loading the FXML file
     */
    @FXML
    void handleModifyProduct(ActionEvent event) throws IOException {
        Product modifyProduct = productsTableView.getSelectionModel().getSelectedItem();
        modifyProductIndex = service.getAllProducts().indexOf(modifyProduct);

        displayScene(event, "/fxml/ModifyProduct.fxml");
    }

    /**
     * Ask user for confirmation before exiting
     */
    @FXML
    void handleExit() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation Needed");
        alert.setHeaderText("Confirm Exit");
        alert.setContentText("Are you sure you want to exit?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            logger.info("OK clicked, exiting program.");
            System.exit(0);
        } else {
            logger.info("Cancel clicked, returning to main screen.");
        }
    }

    /**
     * Gets search text and inputs into lookupPart method to highlight desired part
     */
    @FXML
    void handlePartsSearchBtn() {
        String x = partsSearchTxt.getText();
        partsTableView.getSelectionModel().select(service.lookupPart(x));
    }

    /**
     * Gets search text and inputs into lookupProduct method to highlight desired product
     */
    @FXML
    void handleProductsSearchBtn() {
        String x = productsSearchTxt.getText();
        productsTableView.getSelectionModel().select(service.lookupProduct(x));
    }

}


