package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Adds product to main product list. Covers error checking and validation. Adds associated parts to the product as well
 * @author Ben Paine
 */
public class AddProductController implements Initializable {
  @FXML
  private TextField id;

  @FXML
  private TextField productName;

  @FXML
  private TextField productStock;

  @FXML
  private TextField productPrice;

  @FXML
  private TextField productMax;

  @FXML
  private TextField productMin;

  @FXML
  private TableView allPartsTable;

  @FXML
  private TextField searchPartField;

  @FXML
  private Button addAssociatedPart;

  @FXML
  private TableView associatedPartsTable;

  @FXML
  private Button removeAssociatedPartButton;

  @FXML
  private Button cancelProductButton;

  @FXML
  private Button saveProductButton;

  @FXML
  private Button searchPartButton;
  Stage stage;
  Parent scene;
  Inventory inv;
  /**
   * Parts and associated parts list to show in the add product list
   */
  private ObservableList<Part> partInventory = FXCollections.observableArrayList();
  private ObservableList<Part> associatedPartsInventory = FXCollections.observableArrayList();

  /**
   * Constructor for Add product. This initializes the same inventory that has been passed from the Main component.
   * @param inv this is the main inventory that products will be added to
   */
  public AddProductController(Inventory inv) {
    this.inv = inv;
  }

  /**
   * Initializes both tables and creates the table views whenever the scene is loaded
   * @param url The url of the fxml file
   * @param resourceBundle scene
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    generateAssociatedPartsTable();
    generatePartsTable();
  }

  /**
   * generates parts tables when initialized
   */
  private void generatePartsTable() {
    partInventory.setAll(Inventory.getAllParts());
    allPartsTable.getColumns();

    allPartsTable.setItems(partInventory);
    allPartsTable.refresh();
  }

  /**
   * generates associated parts tables when initialized
   */
  private void generateAssociatedPartsTable() {
    associatedPartsTable.getColumns();

    associatedPartsTable.setItems(associatedPartsInventory);
    associatedPartsTable.refresh();
  }

  /**
   * Add associated Part to the associated parts list. This is then added to the product when product is saved.
   * @param event this is the event of the button pressed.
   */
  @FXML
  void addAssociatedPart(MouseEvent event) {
    if (allPartsTable.getSelectionModel().getSelectedItem() != null) {
      Part partToAdd = (Part) allPartsTable.getSelectionModel().getSelectedItem();

      associatedPartsInventory.add(partToAdd);
      generateAssociatedPartsTable();
    }
  }

  /**
   * cancels product. No product is created when the button is pressed and you return to the main menu
   * @param event this is the button clicked that triggers the cancel product function
   * @throws IOException this is thrown when the scene is not loaded.
   */
  @FXML
  void cancelProduct(MouseEvent event) throws IOException {
    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/main.fxml"));
    controllers.MainController controller = new controllers.MainController(inv);
    loader.setController(controller);
    scene = loader.load();
    stage.setScene(new Scene(scene, 1000, 500));
    stage.show();
  }

  /**
   * Removes associated part from the product.
   * @param event When this button is clicked the part is removed.
   */
  @FXML
  void removeAssociatedPart(MouseEvent event) {
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "You are about to delete an associated Part. Is that okay?", ButtonType.YES, ButtonType.NO);
    confirm.showAndWait();

    if(confirm.getResult() == ButtonType.YES) {
      Part partToDelete = (Part) associatedPartsTable.getSelectionModel().getSelectedItem();

      if(associatedPartsInventory.remove(partToDelete)) {
        generatePartsTable();
      }
      else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Error deleting part. Try again.");
        alert.showAndWait();
      }
    }
  }

  /**
   * Saves the product and adds it to the inventory product list.
   * @param event This button saves the product and sends the user back to the main menu.
   */
  @FXML
  void saveProduct(MouseEvent event) {
    try {
      Product newProduct = new Product(Inventory.getProductGeneratedId(), productName.getText(), Double.parseDouble(productPrice.getText()), Integer.parseInt(productStock.getText()), Integer.parseInt(productMin.getText()), Integer.parseInt(productMax.getText()));

      for (Part i : associatedPartsInventory) {
        newProduct.addAssociatedPart(i);
      }

      Inventory.addProduct(newProduct);

      stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/main.fxml"));
      controllers.MainController controller = new controllers.MainController(inv);
      loader.setController(controller);
      scene = loader.load();
      stage.setScene(new Scene(scene, 1000, 500));
      stage.show();
    } catch (NumberFormatException e) {
      // checks if correct format is used or if the fields are blank
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("Please use numbers in the correct field. Fields cannot be blank.");
      alert.showAndWait();
    } catch (Exception e) {
      System.out.println("Try Again");
    }
  }

  /**
   * Searches a part so the user can add it to the associated parts list.
   * @param event When pressed, this button will filter the table and show results that contain the searched word.
   */
  @FXML
  void searchPart(MouseEvent event) {
    if (searchPartField.getText().equals("")) {
      allPartsTable.setItems(partInventory);
      allPartsTable.refresh();
    }
    else {
      if (isNumeric(searchPartField.getText())) {
        Part tempPart = Inventory.lookupPart(Integer.parseInt(searchPartField.getText()));
        ObservableList<Part> newInventory = FXCollections.observableArrayList();
        newInventory.add(tempPart);
        if (newInventory.isEmpty()) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error");
          alert.setContentText("No Product Found");
          alert.showAndWait();
        }
        else {
          allPartsTable.setItems(newInventory);
          allPartsTable.refresh();
        }
      }
      else {
        ObservableList<Part> temp = Inventory.lookupPart(searchPartField.getText());
        allPartsTable.setItems(temp);
        allPartsTable.refresh();
      }
    }
  }

  /**
   * Checks if the string is a number or not.
   * @param str The string that is going to be checked.
   * @return a boolean if the string is a number or not.
   */
  public static boolean isNumeric(final String str) {

    // null or empty
    if (str == null || str.length() == 0) {
      return false;
    }

    for (char c : str.toCharArray()) {
      if (!Character.isDigit(c)) {
        return false;
      }
    }

    return true;

  }

}
