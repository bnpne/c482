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
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import model.*;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Main controller for the main screen. Includes searching for part and Product and adding, removing, modifying buttons for Parts and Products
 * @author Ben Paine
 */
public class MainController implements Initializable {
  /**
   * Inventory is set
   */
  Stage stage;
  Parent scene;
  Inventory inv;

  @FXML
  private GridPane mainPane;
  @FXML
  private Button searchPartButton;
  @FXML
  private Button searchProductButton;
  @FXML
  private Button addPartButton;
  @FXML
  private Button modifyPartButton;
  @FXML
  private Button deletePartButton;
  @FXML
  private Button addProductButton;
  @FXML
  private Button modifyProductButton;
  @FXML
  private Button deleteProductButton;
  @FXML
  private Button exitScreenButton;
  @FXML
  private TextField partSearchBox;
  @FXML
  private TextField productSearchBox;
  @FXML
  private TableView<Part> partsTable;
  @FXML
  private TableView<Product> productsTable;
  @FXML
  private TextField searchPartField;
  @FXML
  private TextField searchProductField;
  /**
   * Lists of part and product inventory
   */
  private ObservableList<Part> partInventory = FXCollections.observableArrayList();
  private ObservableList<Product> productInventory = FXCollections.observableArrayList();

  /**
   * Main constructor that sets the inventory to edit parts and products
   * @param inv This is the main inventory passed from Main
   */
  public MainController(Inventory inv) {
    this.inv = inv;
  }

  /**
   * initializes controller. Generates both product and part table
   * @param url this is the fxml url
   * @param resourceBundle this is main bundle.
   */
  @Override
  public void initialize(URL url, ResourceBundle resourceBundle) {
    generatePartsTable();
    generateProductsTable();
  }

  /**
   * Generates Part table
   */
  private void generatePartsTable() {
    partInventory.setAll(Inventory.getAllParts());

    partsTable.getColumns();

    partsTable.setItems(partInventory);
    partsTable.refresh();
  }

  /**
   * Generates products table
   */
  private void generateProductsTable() {
    productInventory.setAll(Inventory.getAllProducts());


    productsTable.getColumns();

    productsTable.setItems(productInventory);
    productsTable.refresh();
  }

  /**
   * Add new part and opens add part scene
   * @param event This is on the button click. The add part scene will open
   * @throws IOException this throws an exception on load
   */
  @FXML
  void addPart(MouseEvent event) throws IOException {
    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/addPart.fxml"));
    controllers.AddPartController controller = new controllers.AddPartController(inv);
    loader.setController(controller);
    scene = loader.load();
    stage.setScene(new Scene(scene, 800, 900));
    stage.show();
  }

  /**
   * Opens add product scene
   * @param event On click opens the add product scene
   * @throws IOException throws when scene doesnt open
   */
  @FXML
  void addProduct(MouseEvent event) throws IOException {
    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/addProduct.fxml"));
    controllers.AddProductController controller = new controllers.AddProductController(inv);
    loader.setController(controller);
    scene = loader.load();
    stage.setScene(new Scene(scene, 800, 500));
    stage.setResizable(false);
    stage.show();
  }

  /**
   * Deletes part and given confirmation. When user clicks yes on confirm. The part is deleted and table is updated
   * @param event on click, delete the part
   */
  @FXML
  void deletePart(MouseEvent event) {
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "You are about to delete a part. Is that okay?", ButtonType.YES, ButtonType.NO);
    confirm.showAndWait();

    if (confirm.getResult() == ButtonType.YES) {
      Part partToDelete = partsTable.getSelectionModel().getSelectedItem();

      if(!Inventory.deletePart(partToDelete)) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("Error deleting product. Try again.");
        alert.showAndWait();
      }
      else {
        Inventory.deletePart(partToDelete);
        generatePartsTable();
      }
    }
  }

  /**
   * Deletes product and gives user the confirmation. On confirm, the table is updated
   * @param event Button pressed
   */
  @FXML
  void deleteProduct(MouseEvent event) {
    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "You are about to delete a product. Is that okay?", ButtonType.YES, ButtonType.NO);
    confirm.showAndWait();

    if (confirm.getResult() == ButtonType.YES) {
      Product productToDelete = productsTable.getSelectionModel().getSelectedItem();

      if (!productToDelete.getAllAssociatedParts().isEmpty()) {
        Alert warn = new Alert(Alert.AlertType.WARNING, "You cannot delete a product that has associated Parts. Delete associated parts first.", ButtonType.OK);
        warn.showAndWait();
      }
      else {
        if(!Inventory.deleteProduct(productToDelete)) {
          Alert alert = new Alert(Alert.AlertType.ERROR);
          alert.setTitle("Error");
          alert.setContentText("Error deleting product. Try again.");
          alert.showAndWait();
        }
        else {
          Inventory.deleteProduct(productToDelete);
          generateProductsTable();
        }
      }
    }
  }

  /**
   * Exits the program
   * @param event on click exit the program.
   */
  @FXML
  void exitScreen(MouseEvent event) {
    stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    stage.close();
  }

  /**
   * Opens the modify part scene.
   * @param event button to open the scene
   * @throws Exception this is thrown when the part cannot be saved or the fields entered are not valid.
   */
  @FXML
  void modifyPart(MouseEvent event) throws IOException {

    //TODO: if no part pressed, fix!!
    if (partsTable.getSelectionModel().getSelectedItem() == null) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Warning");
      alert.setContentText("Please select a Part");
      alert.showAndWait();
    }
    else {
      Part partToModify = partsTable.getSelectionModel().getSelectedItem();

      stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/modifyPart.fxml"));
      controllers.ModifyPartController controller = new controllers.ModifyPartController(inv, partToModify);

      loader.setController(controller);
      scene = loader.load();
      stage.setScene(new Scene(scene, 800, 900));
      stage.show();
    }
  }

  /**
   * Open modify product scene
   * @param event opens scene
   * @throws Exception when the product cannot be modified.
   */
  @FXML
  void modifyProduct(MouseEvent event) throws IOException {
    if (productsTable.getSelectionModel().getSelectedItem() == null) {
      Alert alert = new Alert(Alert.AlertType.WARNING);
      alert.setTitle("Warning");
      alert.setContentText("Please select a Product");
      alert.showAndWait();
    }
    else {
      Product productToModify = productsTable.getSelectionModel().getSelectedItem();

      stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../res/modifyProduct.fxml"));
      controllers.ModifyProductController controller = new controllers.ModifyProductController(inv, productToModify);

      loader.setController(controller);
      scene = loader.load();
      stage.setScene(new Scene(scene, 800, 500));
      stage.show();
    }
  }

  /**
   * Searches for part when button is pressed. This function finds part by ID when the user enters a number and searches by name when string is entered.
   * Searches by partial names.
   * @param event searches when the button is pressed and updates table.
   */
  @FXML
  void searchPart(MouseEvent event) {
    if (searchPartField.getText().equals("")) {
      partsTable.setItems(partInventory);
      partsTable.refresh();
    }
    else {
      if (isNumeric(searchPartField.getText())) {
        Part tempPart = Inventory.lookupPart(Integer.parseInt(searchPartField.getText()));
        ObservableList<Part> newInventory = FXCollections.observableArrayList();
        newInventory.add(tempPart);
        partsTable.setItems(newInventory);
        partsTable.refresh();
      }
      else {
        ObservableList<Part> temp = Inventory.lookupPart(searchPartField.getText());
        partsTable.setItems(temp);
        partsTable.refresh();
      }
    }
  }

  /**
   * Searches for Product when button is pressed. This function finds product by ID when the user enters a number and searches by name when string is entered.
   * @param event searches when the button is pressed and updates table.
   */
  @FXML
  void searchProduct(MouseEvent event) {
    if (searchProductField.getText().equals("")) {
      productsTable.setItems(productInventory);
      productsTable.refresh();
    }
    else {
      if (isNumeric(searchProductField.getText())) {
        Product tempProduct = Inventory.lookupProduct(Integer.parseInt(searchProductField.getText()));
        ObservableList<Product> newInventory = FXCollections.observableArrayList();
        newInventory.add(tempProduct);
        productsTable.setItems(newInventory);
        productsTable.refresh();
      }
      else {
        ObservableList<Product> temp = Inventory.lookupProduct(searchProductField.getText());
        productsTable.setItems(temp);
        productsTable.refresh();
      }
    }
  }

  /**
   * Checks if the string entered in the search bar is a number. if its a number the function searches for part ID.
   * This function could extend functionality when you update the program. This is needed to complete the program.
   * This function is needed to determine if the user inputs a number or a string to search. without this function, you wouldnt be able to search for the right thing
   * @param str string to check if its a number
   * @return boolean.
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
