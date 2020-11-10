import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.*;

/**
 * Main program. This starts the main menu up.
 * @author Ben Paine
 * @version 1.0.0
 */
public class Main extends Application {

  /**
   * Main function
   * @param args the args of the main function
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Starts Program
   * @param primaryStage main stage
   * @throws Exception whenever the stage cannot be loaded
   */
  @Override
  public void start(Stage primaryStage) throws Exception {
    Inventory inv = new Inventory();

    addTestData(inv);
    primaryStage.setTitle("Inventory Management System");
    FXMLLoader loader = new FXMLLoader(getClass().getResource("/res/main.fxml"));
    controllers.MainController controller = new controllers.MainController(inv);
    loader.setController(controller);
    Parent root = loader.load();
    Scene scene = new Scene(root);
    primaryStage.setScene(scene);
    primaryStage.show();
  }

  /**
   * Dummy Data for a couple of products and parts
   * @param inv the inventory class
   * @throws Exception whenever the inventory cannot add the part or product
   */
  void addTestData(Inventory inv) throws Exception {
    Part a1 = new inHouse(1, "PartA1", 2.99, 10,5,100,101);
    Part a2 = new Outsourced(2, "PartA2", 2.99, 10,5,100,"Tesla");
    Inventory.addPart(a1);
    Inventory.addPart(a2);
    Product prod1 = new Product(101, "Product 1", 3.99, 5, 1, 50);
    Product prod2 = new Product(102, "Product 2", 3.99, 5, 1, 50);
    prod1.addAssociatedPart(a1);
    prod2.addAssociatedPart(a2);
    Inventory.addProduct(prod1);
    Inventory.addProduct(prod2);
  }

  @FXML
  private void exitProgram(ActionEvent event) {
    Platform.exit();
  }

}
