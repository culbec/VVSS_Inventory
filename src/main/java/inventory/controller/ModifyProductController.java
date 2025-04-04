package inventory.controller;

import inventory.model.Part;
import inventory.model.Product;
import inventory.service.InventoryService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class ModifyProductController implements Initializable, Controller {
    private final Logger logger = LogManager.getLogger(ModifyProductController.class);
    // Declare fields
    private ObservableList<Part> addParts = FXCollections.observableArrayList();
    private int productId;
    private int productIndex;
    private InventoryService service;

    @FXML
    private TextField minTxt;

    @FXML
    private TextField maxTxt;

    @FXML
    private TextField productIdTxt;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField inventoryTxt;

    @FXML
    private TextField priceTxt;

    @FXML
    private TextField productSearchTxt;

    @FXML
    private TableView<Part> addProductTableView;

    @FXML
    private TableColumn<Part, Integer> addProductIdCol;

    @FXML
    private TableColumn<Part, String> addProductNameCol;

    @FXML
    private TableColumn<Part, Integer> addProductInventoryCol;

    @FXML
    private TableColumn<Part, Double> addProductPriceCol;

    @FXML
    private TableView<Part> deleteProductTableView;

    @FXML
    private TableColumn<Part, Integer> deleteProductIdCol;

    @FXML
    private TableColumn<Part, String> deleteProductNameCol;

    @FXML
    private TableColumn<Part, Integer> deleteProductInventoryCol;

    @FXML
    private TableColumn<Part, Double> deleteProductPriceCol;

    public void setService(InventoryService service) {
        this.service = service;
        fillWithData();
    }

    public void setProductIndex(int productIndex) {
        this.productIndex = productIndex;
    }


    private void fillWithData() {
        // Populate add product table view
        addProductTableView.setItems(service.getAllParts());

        addProductIdCol.setCellValueFactory(new PropertyValueFactory<>("partId"));
        addProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        addProductInventoryCol.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        addProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        // Populate modify product form
        Product product = service.getAllProducts().get(productIndex);

        productId = service.getAllProducts().get(productIndex).getProductId();
        productIdTxt.setText(Integer.toString(product.getProductId()));
        nameTxt.setText(product.getName());
        inventoryTxt.setText(Integer.toString(product.getInStock()));
        priceTxt.setText(Double.toString(product.getPrice()));
        maxTxt.setText(Integer.toString(product.getMax()));
        minTxt.setText(Integer.toString(product.getMin()));

        // Populate delete product table view
        addParts = product.getAssociatedParts();
        updateDeleteProductTableView();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fillWithData();
    }


    /**
     * Method to add to button handler to switch to scene passed as source
     *
     * @param event  The event that happened
     * @param source The source of the event
     * @throws IOException If an error occurs in displaying the scene
     */
    @FXML
    private void displayScene(ActionEvent event, String source) throws IOException {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(source));
        Parent scene = loader.load();

        Controller ctrl = loader.getController();
        ctrl.setService(service);
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Method to add values of addParts to the bottom table view of the scene.
     */
    public void updateDeleteProductTableView() {
        deleteProductTableView.setItems(addParts);

        deleteProductIdCol.setCellValueFactory(new PropertyValueFactory<>("partId"));
        deleteProductNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        deleteProductInventoryCol.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        deleteProductPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Ask user for confirmation before deleting selected part from current product.
     *
     */
    @FXML
    void handleDeleteProduct() {
        Part part = deleteProductTableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation");
        alert.setHeaderText("Confirm Part Deletion!");
        alert.setContentText("Are you sure you want to delete part " + part.getName() + " from parts?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            logger.info("Part " + part.getName() + " deleted from product.");
            addParts.remove(part);
        } else {
            logger.info("Canceled part deletion.");
        }
    }

    /**
     * Add selected part from top table view to bottom table view in order to create
     * new product
     *
     */
    @FXML
    void handleAddProduct() {
        Part part = addProductTableView.getSelectionModel().getSelectedItem();
        addParts.add(part);
        updateDeleteProductTableView();
    }

    /**
     * Ask user for confirmation before canceling product modification
     * and switching scene to Main Screen
     *
     * @param event The event that happened
     * @throws IOException If there is an error cancelling the product modification
     */
    @FXML
    void handleCancelProduct(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation Needed");
        alert.setHeaderText("Confirm Cancelation");
        alert.setContentText("Are you sure you want to cancel modifying product?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            logger.info("Ok selected. Product modification canceled.");
            displayScene(event, "/fxml/MainScreen.fxml");
        } else {
            logger.info("Cancel clicked.");
        }
    }

    /**
     * Validate given product parameters.  If valid, update product in inventory,
     * else give user an error message explaining why the product is invalid.
     *
     * @param event The event that happened
     * @throws IOException If there was an error saving the product
     */
    @FXML
    void handleSaveProduct(ActionEvent event) throws IOException {
        String name = nameTxt.getText();
        String price = priceTxt.getText();
        String inStock = inventoryTxt.getText();
        String min = minTxt.getText();
        String max = maxTxt.getText();

        try {
            Product product = new Product(name, Double.parseDouble(price), Integer.parseInt(inStock), Integer.parseInt(min), Integer.parseInt(max), addParts);
            product.setProductId(productId);

            String errorMessage = Product.isValidProduct(product);
            if (!errorMessage.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Adding Part!");
                alert.setHeaderText("Error!");
                alert.setContentText(errorMessage);
                alert.showAndWait();
            } else {
                service.updateProduct(productIndex, product);
                displayScene(event, "/fxml/MainScreen.fxml");
            }
        } catch (NumberFormatException e) {
            logger.error("Form contains blank field.");
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Adding Product!");
            alert.setHeaderText("Error!");
            alert.setContentText("Form contains blank field.");
            alert.showAndWait();
        }
    }

    /**
     * Gets search text and inputs into lookupAssociatedPart method to highlight desired part
     *
     */
    @FXML
    void handleSearchProduct() {
        String x = productSearchTxt.getText();
        addProductTableView.getSelectionModel().select(service.lookupPart(x));
    }

}
