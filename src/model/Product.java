package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Product class. The class initializes and gets the product information
 * @author Ben Paine
 */

public class Product {
  List<Part> partList = new ArrayList<>();
  private ObservableList<Part> associatedParts = FXCollections.observableList(partList);
  private int id;
  private String name;
  private double price;
  private int stock;
  private int min;
  private int max;

  public Product(int id, String name, double price, int stock, int min, int max) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.stock = stock;
    this.min = min;
    this.max = max;
  }

  /**
   * The ID of the product.
   * @return id of the product
   */
  public int getId() {
    return id;
  }

  /**
   * @param id the id to set
   */
  public void setId(int id) {
    this.id = id;
  }

  /**
   * @return name of the product
   */
  public String getName() {
    return name;
  }

  /**
   * @param name the name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * @return price of the product
   */
  public double getPrice() {
    return price;
  }

  /**
   * @param price the price to set
   */
  public void setPrice(double price) {
    this.price = price;
  }

  /**
   * @return stock number of the product
   */
  public int getStock() {
    return stock;
  }

  /**
   * @param stock the stock to set
   */
  public void setStock(int stock) {
    this.stock = stock;
  }

  /**
   * @return min of the product
   */
  public int getMin() {
    return min;
  }

  /**
   * @param min the min to set
   */
  public void setMin(int min) {
    this.min = min;
  }

  /**
   * @return max of the product
   */
  public int getMax() {
    return max;
  }

  /**
   * @param max the max to set
   */
  public void setMax(int max) {
    this.max = max;
  }

  /**
   * @param part the part associated to the product
   */
  public void addAssociatedPart(Part part) {
    associatedParts.add(part);
  }

  /**
   * @param part the part associated and chosen to delete
   * @return true if deleted false if not
   */
  public boolean deleteAssociatedPart(Part part) {
    for (Part i : associatedParts) {
      if (i == part) {
        associatedParts.remove(i);
        return true;
      }
    }
    return false;
  }

  /**
   * @return associated part
   */
  public ObservableList<Part> getAllAssociatedParts() {
    return associatedParts;
  }
}
