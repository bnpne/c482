package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Outsourced;
import model.inHouse;

import java.io.IOException;

/**
 *
 *
 * This controller adds a part to the list of parts in the main controller. This covers error checking and adds to the inventory.
 * Adds a part to the main parts list
 * @author Ben Paine
 */
public class AddPartController {
  Stage stage;
  Parent scene;
  Inventory inv;

  @FXML
  private RadioButton inHouseToggle;
  @FXML
  private RadioButton outsourceToggle;
  @FXML
  private ToggleGroup property;
  @FXML
  private TextField partName;
  @FXML
  private TextField partStock;
  @FXML
  private TextField partPrice;
  @FXML
  private TextField partMax;
  @FXML
  private TextField labelProperty;
  @FXML
  private TextField partMin;
  @FXML
  private Button savePartButton;
  @FXML
  private Button cancelPartButton;
  @FXML
  private Label labelToChange;

  /**
   * Constructor. Adds the inventory that was initialized from the main function. So that you can easily add parts
   * @param inv
   */
  public AddPartController(Inventory inv) {
    this.inv = inv;
  }

  /**
   * This is triggered on the mouse click. The change property is in charge of chaging whether a part is Outsourced or In house
   * This allows a program to determine whether the part is in house and add a machine ID or outsourced and add a company name.
   * @param event this is the radio button. it is toggled from "In House" and "Outsourced"
   */
  @FXML
  void changeProperty(MouseEvent event) {
    RadioButton a = (RadioButton) property.getSelectedToggle();
    String b = a.getText();

    if (b.equals("In-House")) {
      labelToChange.setText("Machine ID");
    }
    else {
      labelToChange.setText("Company");
    }
  }

  /**
   * Cancels part and returns to main menu on button press.
   * @param event This is the cancel part button. Returns to main menu.
   * @throws IOException This exception is from JavaFX. If the main screen fails to load, this is called.
   */
  @FXML
  void cancelPart(MouseEvent event) throws IOException {
    partName.clear();
    partStock.clear();
    partPrice.clear();
    partMax.clear();
    labelProperty.clear();
    partMin.clear();

    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/main.fxml"));
    controllers.MainController controller = new controllers.MainController(inv);
    loader.setController(controller);
    scene = loader.load();
    stage.setScene(new Scene(scene, 1000, 500));
    stage.show();
  }

  /**
   * Saves part when button is pressed and returns the user to the main menu. It adds the part into the inventory and can be seen in the main table.
   * @param event this is the Save part button that initiates creating a part and returning to the main menu.
   * @throws IOException throws an exception if part not saved
   */
  @FXML
  void savePart(MouseEvent event) throws IOException {

    try {
      RadioButton a = (RadioButton) property.getSelectedToggle();
      String b = a.getText();

      if (b.equals("In-House")) {
        inHouse newPart = new inHouse(Inventory.getPartGeneratedId(), partName.getText(), Double.parseDouble(partPrice.getText()), Integer.parseInt(partStock.getText()), Integer.parseInt(partMin.getText()), Integer.parseInt(partMax.getText()), Integer.parseInt(labelProperty.getText()));
        Inventory.addPart(newPart);
      } else {
        Outsourced newPart = new Outsourced(Inventory.getPartGeneratedId(), partName.getText(), Double.parseDouble(partPrice.getText()), Integer.parseInt(partStock.getText()), Integer.parseInt(partMin.getText()), Integer.parseInt(partMax.getText()), labelProperty.getText());
        Inventory.addPart(newPart);
      }

      stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/main.fxml"));
      controllers.MainController controller = new controllers.MainController(inv);
      loader.setController(controller);
      scene = loader.load();
      stage.setScene(new Scene(scene, 1000, 500));
      stage.show();
    } catch (NumberFormatException e) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("Please use numbers in the correct field. Fields cannot be blank");
      alert.showAndWait();
    } catch (Exception e) {
      System.out.println("Try Again");
    }
  }

}
