package inventory.controller;

import inventory.model.InhousePart;
import inventory.model.OutsourcedPart;
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

public class ModifyPartController implements Initializable, Controller {
    private final Logger logger = LogManager.getLogger(ModifyPartController.class);
    // Declare fields
    private boolean isOutsourced;
    private int partIndex;
    private int partId;
    private InventoryService service;

    @FXML
    private RadioButton inhouseRBtn;

    @FXML
    private RadioButton outsourcedRBtn;

    @FXML
    private Label modifyPartDynamicLbl;

    @FXML
    private TextField modifyPartDynamicTxt;

    @FXML
    private TextField partIdTxt;

    @FXML
    private TextField nameTxt;

    @FXML
    private TextField inventoryTxt;

    @FXML
    private TextField priceTxt;

    @FXML
    private TextField maxTxt;

    @FXML
    private TextField minTxt;

    // Declare methods
    public ModifyPartController(int partId) {
        this.partId = partId;
    }

    public void setService(InventoryService service) {
        this.service = service;
        fillWithData();
    }

    public void setPartIndex(int partIndex) {
        this.partIndex = partIndex;
    }

    private void fillWithData() {
        Part part = service.getAllParts().get(partIndex);

        partId = service.getAllParts().get(partIndex).getPartId();
        partIdTxt.setText(Integer.toString(part.getPartId()));
        nameTxt.setText(part.getName());
        inventoryTxt.setText(Integer.toString(part.getInStock()));
        priceTxt.setText(Double.toString(part.getPrice()));
        maxTxt.setText(Integer.toString(part.getMax()));
        minTxt.setText(Integer.toString(part.getMin()));

        if (part instanceof InhousePart) {
            modifyPartDynamicTxt.setText(Integer.toString(((InhousePart) service.getAllParts().get(partIndex)).getMachineId()));
            modifyPartDynamicLbl.setText("Machine ID");
            inhouseRBtn.setSelected(true);
            isOutsourced = false;
        } else {
            modifyPartDynamicTxt.setText(((OutsourcedPart) service.getAllParts().get(partIndex)).getCompanyName());
            modifyPartDynamicLbl.setText("Company Name");
            outsourcedRBtn.setSelected(true);
            isOutsourced = true;
        }
    }


    /**
     * Initializes the controller class.
     *
     * @param url The location used to resolve relative paths for the root object, or null if the location is not known.
     * @param rb  The resources used to localize the root object, or null if the root object was not localized.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.fillWithData();
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
     * If in-house radio button is selected set isOutsourced boolean
     * to false and modify dynamic label to Machine ID
     *
     */
    @FXML
    void handleInhouseRBtn() {
        isOutsourced = false;
        modifyPartDynamicLbl.setText("Machine ID");
    }

    /**
     * If outsourced radio button is selected set isOutsourced boolean
     * to true and modify dynamic label to Company Name
     *
     */
    @FXML
    void handleOutsourcedRBtn() {
        isOutsourced = true;
        modifyPartDynamicLbl.setText("Company Name");
    }

    /**
     * Seek user confirmation before canceling modifications and
     * switching scene to MainScreen
     *
     * @param event The event that happened
     * @throws IOException If there is an error loading the FXML file
     */
    @FXML
    void handleModifyPartCancel(ActionEvent event) throws IOException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.NONE);
        alert.setTitle("Confirmation Needed");
        alert.setHeaderText("Confirm Cancellation");
        alert.setContentText("Are you sure you want to cancel modifying part " + nameTxt.getText() + "?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            logger.info("OK clicked, continuing to main screen.");
            displayScene(event, "/fxml/MainScreen.fxml");
        } else {
            logger.info("Cancel clicked, returning to modify part screen.");
        }
    }

    /**
     * Validate part attributes and save modifications to chosen
     * Part object then switch scene to MainScreen
     *
     * @param event The event that happened
     * @throws IOException If there is an error loading the FXML file
     */
    @FXML
    void handleModifyPartSave(ActionEvent event) throws IOException {
        String name = nameTxt.getText();
        String price = priceTxt.getText();
        String inStock = inventoryTxt.getText();
        String min = minTxt.getText();
        String max = maxTxt.getText();
        String partDynamicValue = modifyPartDynamicTxt.getText();

        try {
            this.partId = Integer.parseInt(partIdTxt.getText());
            String errorMessage = Part.isValidPart(name, Double.parseDouble(price), Integer.parseInt(inStock), Integer.parseInt(min), Integer.parseInt(max));
            if (!errorMessage.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Error Adding Part!");
                alert.setHeaderText("Error!");
                alert.setContentText(errorMessage);
                alert.showAndWait();
            } else {
                if (isOutsourced) {
                    OutsourcedPart outsourcedPart = new OutsourcedPart(name, Double.parseDouble(price), Integer.parseInt(inStock), Integer.parseInt(min), Integer.parseInt(max), partDynamicValue);
                    outsourcedPart.setPartId(partId);
                    service.updateOutsourcedPart(partIndex, outsourcedPart);
                } else {
                    InhousePart inhousePart = new InhousePart(name, Double.parseDouble(price), Integer.parseInt(inStock), Integer.parseInt(min), Integer.parseInt(max), Integer.parseInt(partDynamicValue));
                    inhousePart.setPartId(partId);
                    service.updateInhousePart(partIndex, inhousePart);
                }
                displayScene(event, "/fxml/MainScreen.fxml");
            }

        } catch (NumberFormatException e) {

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error Adding Part!");
            alert.setHeaderText("Error");
            alert.setContentText("Form contains blank field.");
            alert.showAndWait();
        }

    }

}