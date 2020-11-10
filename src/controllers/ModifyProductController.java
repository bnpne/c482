package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Modifies the product. This includes adding and removing parts from the associated parts list.
 * @author Ben Paine
 */
public class ModifyProductController implements Initializable {
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
  private ObservableList<Part> partInventory = FXCollections.observableArrayList();
  private ObservableList<Part> associatedPartsInventory = FXCollections.observableArrayList();

  Product productToModify;

  /**
   * constructor for the product. Passes in the product to modify and the inventory
   * @param inv main inventory
   * @param productToModify product that is being passed to modify.
   */
  public ModifyProductController(Inventory inv, Product productToModify) {
    this.inv = inv;
    this.productToModify = productToModify;
  }

  /**
   * Initializes scene with text fields and table views. The text fields are filled with the information of the selected product.
   * the Tables are initialized with the associated parts of the product and the whole parts list for the user to add to associated parts
   * @param url This is the url of the fxml file
   * @param resourceBundle resource
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    id.setText(Integer.toString(productToModify.getId()));
    productName.setText(productToModify.getName());
    productStock.setText(Integer.toString(productToModify.getStock()));
    productPrice.setText(Double.toString(productToModify.getPrice()));
    productMax.setText(Integer.toString(productToModify.getMax()));
    productMin.setText(Integer.toString(productToModify.getMin()));

    associatedPartsInventory = productToModify.getAllAssociatedParts();
    generateAssociatedPartsTable();
    generatePartsTable();
  }

  /**
   * generates parts tables
   */
  private void generatePartsTable() {
    partInventory.setAll(Inventory.getAllParts());
    allPartsTable.getColumns();

    allPartsTable.setItems(partInventory);
    allPartsTable.refresh();
  }

  /**
   * generates associated parts tables
   */
  private void generateAssociatedPartsTable() {
    associatedPartsTable.getColumns();

    associatedPartsTable.setItems(associatedPartsInventory);
    associatedPartsTable.refresh();
  }

  /**
   * Adds associated part from the list of all parts. The part selected will show up in the associated parts table once added
   * @param event When button is clicked, part is a added to the associated parts list.
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
   * Cancels modification. Returns user to the main menu and stops modification. everything is cleared and cancelled.
   * @param event The button click that cancels the scene and loads the main menu scene
   * @throws IOException exception when error is thrown when trying to load
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
   * Removes associated part that is selected. Once user confirms, the part is deleted and it is removed from the associated parts table.
   * @param event The button initiates deleting the part.
   */
  @FXML
  void removeAssociatedPart(MouseEvent event) {
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "You are about to delete an associated Part. Is that okay?", ButtonType.YES, ButtonType.NO);
    confirm.showAndWait();

    if (confirm.getResult() == ButtonType.YES) {
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
   * Saves the modified product. The new product is created and the product is replaced into the inventory.
   * @param event the product is saved and the user is sent back to the main menu
   */
  @FXML
  void saveProduct(MouseEvent event) {
    try {
      int index = Inventory.getAllProducts().indexOf(productToModify);
      Product newProduct = new Product(Integer.parseInt(id.getText()), productName.getText(), Double.parseDouble(productPrice.getText()), Integer.parseInt(productStock.getText()), Integer.parseInt(productMin.getText()), Integer.parseInt(productMax.getText()));

      for (Part i : associatedPartsInventory) {
        newProduct.addAssociatedPart(i);
      }

      Inventory.updateProduct(index, newProduct);

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

  /**
   * This searches parts in the parts table. If the input is a number, the part is searched by ID and if string the part is searched by name. You can search by partial name
   * @param event This button will alter the table and show results.
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
   * Checks if input is numeric. If number, the part is searched by ID
   * @param str the strign to be checked
   * @return boolean
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
