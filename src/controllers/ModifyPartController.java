package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Outsourced;
import model.Part;
import model.inHouse;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Modifies selected Product
 * @author Ben Paine
 */
public class ModifyPartController implements Initializable {
  Stage stage;
  Parent scene;
  /**
   * main inventory
   */
  Inventory inv;
  Part partToModify;

  @FXML
  private RadioButton inHouseToggle;

  @FXML
  private ToggleGroup property;

  @FXML
  private RadioButton outsourceToggle;

  @FXML
  private TextField id;

  @FXML
  private TextField partName;

  @FXML
  private TextField partStock;

  @FXML
  private TextField partPrice;

  @FXML
  private TextField partMax;

  @FXML
  private Label labelToChange;

  @FXML
  private TextField labelProperty;

  @FXML
  private TextField partMin;

  @FXML
  private Button saveModifyPart;

  @FXML
  private Button cancelModifyPart;

  public ModifyPartController(Inventory inv, Part partToModify) {
    this.inv = inv;
    this.partToModify = partToModify;
  }

  /**
   * Initializes scene with text fields. The text fields are filled with the information of the selected part.
   * @param url This is the url of the fxml file
   * @param resourceBundle resource
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {

    id.setText(Integer.toString(partToModify.getId()));
    partName.setText(partToModify.getName());
    partStock.setText(Integer.toString(partToModify.getStock()));
    partPrice.setText(Double.toString(partToModify.getPrice()));
    partMax.setText(Integer.toString(partToModify.getMax()));
    partMin.setText(Integer.toString(partToModify.getMin()));
    if (partToModify instanceof inHouse) {
      inHouseToggle.fire();
      labelProperty.setText(Integer.toString(((inHouse) partToModify).getMachineId()));
      labelToChange.setText("Machine ID");
    }
    else {
      outsourceToggle.fire();
      labelProperty.setText(((Outsourced) partToModify).getCompanyName());
      labelToChange.setText("Company");
    }
  }

  /**
   * Cancels the modification and sends user back to the main menu
   * @param event when button is clicked, user is sent back to main menu
   * @throws IOException This is thrown when scene cannot be loaded
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
   * Changes the property of the part. From in-house to outsourced and visa versa. This changes the label of the Machine ID or Company name
   * @param event This is the radio button that is toggled between outsourced and in house
   */
  @FXML
  void changeProperty(MouseEvent event) {
    RadioButton a = (RadioButton) property.getSelectedToggle();
    String b = a.getText();

    if (b.equals("In-House")) {
      labelToChange.setText("Machine ID");
      if (partToModify instanceof inHouse) {
        labelProperty.setText(Integer.toString(((inHouse) partToModify).getMachineId()));
      }
      else {
        labelProperty.clear();
      }
    }
    else {
      labelToChange.setText("Company");
      if (partToModify instanceof  Outsourced) {
        labelProperty.setText(((Outsourced) partToModify).getCompanyName());
      }
      else {
        labelProperty.clear();
      }
    }
  }

  /**
   * Saves the part that is modified. Gets the current part and replaces it in inventory with the new part, keeping the same ID
   * @param event This button saves the part and returns the user back to main menu
   */
  @FXML
  void savePart(MouseEvent event) {
    try {
      RadioButton a = (RadioButton) property.getSelectedToggle();
      String b = a.getText();
      int index;

      if (b.equals("In-House")) {
        inHouse newPart = new inHouse(Integer.parseInt(id.getText()), partName.getText(), Double.parseDouble(partPrice.getText()), Integer.parseInt(partStock.getText()), Integer.parseInt(partMin.getText()), Integer.parseInt(partMax.getText()), Integer.parseInt(labelProperty.getText()));
        index = Inventory.getAllParts().indexOf(partToModify);
        Inventory.updatePart(index, newPart);
      } else {
        Outsourced newPart = new Outsourced(Integer.parseInt(id.getText()), partName.getText(), Double.parseDouble(partPrice.getText()), Integer.parseInt(partStock.getText()), Integer.parseInt(partMin.getText()), Integer.parseInt(partMax.getText()), labelProperty.getText());
        index = Inventory.getAllParts().indexOf(partToModify);
        Inventory.updatePart(index, newPart);
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
      alert.setContentText("Please use numbers in the correct field. Fields cannot be blank.");
      alert.showAndWait();
    } catch (Exception e) {
      System.out.println("Try Again");
    }
  }
}
