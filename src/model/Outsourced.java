package model;

/**
 * Outsourced Part class inherited from part
 */
public class Outsourced extends Part {
  private String companyName;

  public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
    super(id, name, price, stock, min, max);
    this.companyName = companyName;
  }

  /**
   * Returns the company name from the part
   * @return company name
   */
  public String getCompanyName() {
    return companyName;
  }

  /**
   * Sets the company name
   * @param companyName the name of the company given that the part is outsourced
   */
  public void setCompanyName(String companyName) {
    this.companyName = companyName;
  }
}
