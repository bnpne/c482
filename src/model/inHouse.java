package model;

/**
 * Class for In House Parts. This is inherited from the Part class.
 * @author Ben Paine
 */
public class inHouse extends Part {
  private int machineId;

  public inHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
    super(id, name, price, stock, min, max);
    this.machineId = machineId;
  }

  /**
   * gets machine ID number
   * @return machine Id
   */
  public int getMachineId() {
    return machineId;
  }

  /**
   * Sets the Machine ID
   * @param machineId
   */
  public void setMachineId(int machineId) {
    this.machineId = machineId;
  }

}
