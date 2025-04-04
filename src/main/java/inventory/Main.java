package inventory;

import inventory.controller.MainScreenController;
import inventory.repository.InventoryRepository;
import inventory.service.InventoryService;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main extends Application {
    private final Logger logger = LogManager.getLogger(Main.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        InventoryRepository repo = new InventoryRepository(false);
        InventoryService service = new InventoryService(repo);

        logger.info("Starting application");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MainScreen.fxml"));

        Parent root = loader.load();
        MainScreenController ctrl = loader.getController();
        ctrl.setService(service);

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

}