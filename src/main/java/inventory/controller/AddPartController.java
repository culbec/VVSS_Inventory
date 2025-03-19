package inventory.controller;

import inventory.model.Part;
import inventory.service.InventoryService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class AddPartController implements Initializable, Controller {
    // Declare fields
    private final Logger logger = LogManager.getLogger(AddPartController.class);
    private boolean isOutsourced = true;
    private InventoryService service;

    @FXML
    private RadioButton inhouseRBtn;

    @FXML
    private RadioButton outsourcedRBtn;

    @FXML
    private Label addPartDynamicLbl;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField inventoryTxt;

    @FXML
    private TextField priceTxt;

    @FXML
    private TextField addPartDynamicTxt;

    @FXML
    private TextField maxTxt;

    @FXML
    private TextField minTxt;

    @Override
    public void setService(InventoryService service) {
        this.service = service;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        outsourcedRBtn.setSelected(true);
    }

    /**
     * Method to add to button handler to switch to scene passed as source
     *
     * @param event The event that happened
     * @param source The source of the FXML file
     * @throws IOException If there is an error loading the FXML file
     */
    @FXML
    private void displayScene(ActionEvent event, String source) throws IOException {
        Parent scene;
        Stage stage;
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource(source));
        scene = loader.load();
        Controller ctrl = loader.getController();
        ctrl.setService(service);
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * Ask user for confirmation before canceling part addition
     * and switching scene to Main Screen
     *
     * @param event The event that happened
     * @throws IOException If there is an error loading the FXML file
     */
    @FXML
    void handleAddPartCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation Needed");
        alert.setHeaderText("Confirm Cancelation");
        alert.setContentText("Are you sure you want to cancel adding part?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            logger.info("OK clicked, continuing to main screen.");
            displayScene(event, "/fxml/MainScreen.fxml");
        } else {
            logger.info("Cancel clicked, returning to add part screen.");
        }
    }

    /**
     * If in-house radio button is selected set isOutsourced boolean
     * to false and modify dynamic label to Machine ID
     *
     */
    @FXML
    void handleInhouseRBtn() {
        isOutsourced = false;
        inhouseRBtn.setSelected(true);
        addPartDynamicLbl.setText("Machine ID");
    }

    /**
     * If outsourced radio button is selected set isOutsourced boolean
     * to true and modify dynamic label to Company Name
     *
     */
    @FXML
    void handleOutsourcedRBtn() {
        isOutsourced = true;
        addPartDynamicLbl.setText("Company Name");
    }

    /**
     * Validate given part parameters.  If valid, add part to inventory,
     * else give user an error message explaining why the part is invalid.
     *
     * @param event The event that happened
     * @throws IOException If there is an error adding the part
     */
    @FXML
    void handleAddPartSave(ActionEvent event) throws IOException {
        String name = nameTxt.getText();
        String price = priceTxt.getText();
        String inStock = inventoryTxt.getText();
        String min = minTxt.getText();
        String max = maxTxt.getText();
        String partDynamicValue = addPartDynamicTxt.getText();

        try {
            String errorMessage = Part.isValidPart(name, Double.parseDouble(price), Integer.parseInt(inStock), Integer.parseInt(min), Integer.parseInt(max));
            if (!errorMessage.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Adding Part!");
                alert.setHeaderText("Error!");
                alert.setContentText(errorMessage);
                alert.showAndWait();
            } else {
                if (isOutsourced) {
                    service.addOutsourcePart(name, Double.parseDouble(price), Integer.parseInt(inStock), Integer.parseInt(min), Integer.parseInt(max), partDynamicValue);
                } else {
                    service.addInhousePart(name, Double.parseDouble(price), Integer.parseInt(inStock), Integer.parseInt(min), Integer.parseInt(max), Integer.parseInt(partDynamicValue));
                }
                displayScene(event, "/fxml/MainScreen.fxml");
            }

        } catch (NumberFormatException e) {
            logger.error("Error adding part: " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Adding Part!");
            alert.setHeaderText("Error!");
            alert.setContentText("Form contains blank field.");
            alert.showAndWait();
        }
    }

}
