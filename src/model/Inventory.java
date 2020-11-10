package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

/**
 * Inventory Class. Contains all the code for the lists for the table
 * @author Ben Paine
 */

public class Inventory {
  static private ObservableList<Part> allParts = FXCollections.observableArrayList();
  static private ObservableList<Product> allProducts = FXCollections.observableArrayList();
  static int partGeneratedId;
  static int productGeneratedId;

  /**
   * Initializes IDs for parts and products
   */
  public Inventory() {
    partGeneratedId = 3;
    productGeneratedId = 103;
  }

  /**
   * Used for getting part ID when creating a part
   * @return part ID incremented
   */
  static public int getPartGeneratedId() {
    partGeneratedId+=1;
    return partGeneratedId;
  }

  /**
   * increments Product ID
   * @return returns product ID incremented
   */
  static public int getProductGeneratedId() {
    productGeneratedId+=1;
    return productGeneratedId;
  }

  /**
   *  Adds new part into the inventory list. Checks if min is lless than max and if the stock is a valid number
   *  Corrected Runtime error in the code below
   * @param newPart with all the specific parameters.
   * @throws Exception This exception is thrown when the user inputs either no inputs, min is greater than max, or stock is not between min and max.
   */
  static public void addPart(Part newPart) throws Exception {
    // This checks if the min is greater than the max. if the Max is less than the min, the exception is thrown adn the new part is not made. A dialogue box is displayed.
    if (newPart.getMin() > newPart.getMax()) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("Min value cannot be greater than max value. Try again.");
      alert.showAndWait();


      throw new Exception();
    }
    // This checks if the stock is betweem the min and the max
    else if (newPart.getStock() > newPart.getMax() || newPart.getStock() < newPart.getMin()) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("Stock value not between min and max. Try again.");
      alert.showAndWait();

      // Exception is thrown and part not added
      throw new Exception();
    }
    allParts.add(newPart);
  }

  /**
   * Makes a new product. Checks if Min is less than Max and if stock is a valid number.
   * @param newProduct with specific parameters
   * @throws Exception exception is thrown if the product doesnt match specified conditions
   */
  static public void addProduct(Product newProduct) throws Exception {
    // the min and max need to be in order. min cannot be greater than the max.
    if (newProduct.getMin() > newProduct.getMax()) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("Min value cannot be greater than max value. Try again.");
      alert.showAndWait();

      throw new Exception();
    }
    // The stock value has to lie between the min and max
    else if (newProduct.getStock() > newProduct.getMax() || newProduct.getStock() < newProduct.getMin()) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("Stock value not between min and max. Try again.");
      alert.showAndWait();

      throw new Exception();
    }
    allProducts.add(newProduct);
  }

  /**
   *  looks up part by ID and returns the part by ID
   * @param partId This is the id of the number searched. Find the part by ID and returns it
   * @return part if found, null if not found
   */
  static public Part lookupPart(int partId) {
    for (Part i : allParts) {
      if (i.getId() == partId) {
        return i;
      }
    }

    return null;
  }

  /**
   * Looks for product by ID.
   * @param productId the ID of the product searched. Find the product by ID and returns the product
   * @return product if found, null if not found
   */
  static public Product lookupProduct(int productId) {
    for (Product i : allProducts) {
      if (i.getId() == productId) {
        return i;
      }
    }
    //TODO: see if you need to return a string
    return null;
  }

  /**
   * Returns the list of parts that have the same name or partial name provided
   * @param partName the name of the part or part of the name. It will return a list of parts withe the same name or including part of the name
   * @return return list of parts with that name
   */
  static public ObservableList<Part> lookupPart(String partName) {
    ObservableList<Part> temp = FXCollections.observableArrayList();

    for (Part i : allParts) {
      if (i.getName().contains(partName)) {
        temp.add(i);
      }
    }

    return temp;
  }

  /**
   * Returns the list of products that have the same name or partial name provided
   * @param productName the name of the product or part of the name. It will return a list of products withe the same name or including part of the name
   * @return return list of products with that name
   */
  static public ObservableList<Product> lookupProduct(String productName) {
    ObservableList<Product> temp = FXCollections.observableArrayList();

    for (Product i : allProducts) {
      if (i.getName().contains(productName)) {
        temp.add(i);
      }
    }

    return temp;
  }

  /**
   * updates the part when modified. It finds the part by index and replaces it with the new part, keeping the index of the old part
   * the min and max is checked and if the stock is a valid number
   * @param index the index or placement of the part inside the list of parts
   * @param selectedPart the part to replace the old part
   */
  static public void updatePart (int index, Part selectedPart) throws Exception {
    if (selectedPart.getMin() > selectedPart.getMax()) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("Min value cannot be greater than max value. Try again.");
      alert.showAndWait();

      throw new Exception();
    }
    else if (selectedPart.getStock() > selectedPart.getMax() || selectedPart.getStock() < selectedPart.getMin()) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("Stock value not between min and max. Try Again.");
      alert.showAndWait();

      throw new Exception();
    }
    try {
      allParts.set(index, selectedPart);
    } catch (Exception e) {
      System.out.println("Part does not exist.");
    }
  }

  /**
   * Updates the product when modified. It finds the product by index and replaces it with the new product, keeping the index of the old product
   * The min and max is checked and if the stock is a valid number
   * @param index the index or location of the product in the list
   * @param newProduct the new product to replace the old one
   */
  static public void updateProduct (int index, Product newProduct) throws Exception {
    if (newProduct.getMin() > newProduct.getMax()) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("Min value cannot be greater than max value. Try again.");
      alert.showAndWait();

      throw new Exception();
    }
    else if (newProduct.getStock() > newProduct.getMax() || newProduct.getStock() < newProduct.getMin()) {
      Alert alert = new Alert(Alert.AlertType.ERROR);
      alert.setTitle("Error");
      alert.setContentText("Stock value not between min and max. Try again.");
      alert.showAndWait();

      throw new Exception();
    }
    try {
      allProducts.set(index, newProduct);
    } catch (Exception e) {
      System.out.println("Part does not exist.");
    }
  }

  /**
   * removes the selected part from the list
   * @param selectedPart the part that is going to be removed
   * @return true if deleted false if now
   */
  static public boolean deletePart(Part selectedPart) {
    for (Part i : allParts) {
      if (i == selectedPart) {
        allParts.remove(i);
        return true;
      }
    }

    return false;
  }

  /**
   * removes the selected Product from the list
   * @param selectedProduct the selected product to be removed
   * @return true if deleted false if now
   */
  static public boolean deleteProduct(Product selectedProduct) {
    for (Product i : allProducts) {
      if (i == selectedProduct) {
        allProducts.remove(i);
        return true;
      }
    }

    return false;
  }

  /**
   *  returns the list of parts to populate the table
   * @return list of all parts
   */
  static public ObservableList<Part> getAllParts() {
    return allParts;
  }

  /**
   * returns the list of products to populate the table
   * @return list of all products
   */
  static public ObservableList<Product> getAllProducts() {
    return allProducts;
  }
}
