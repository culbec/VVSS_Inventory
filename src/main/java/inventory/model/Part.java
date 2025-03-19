package inventory.model;


public abstract class Part {

    // Declare fields
    private int partId;
    private String name;
    private double price;
    private int inStock;
    private int min;
    private int max;

    // Constructor
    protected Part(String name, double price, int inStock, int min, int max) {
        this.name = name;
        this.price = price;
        this.inStock = inStock;
        this.min = min;
        this.max = max;
    }

    /**
     * Generate an error message for invalid values in a part
     * Valid part will return an empty string
     *
     * @param name    The name of the product
     * @param price   The price of the product
     * @param inStock The inventory level of the product
     * @param min     The minimum inventory level of the product
     * @param max     The maximum inventory level of the product
     * @return An error message if the part is invalid, otherwise an empty string
     */
    public static String isValidPart(String name, double price, int inStock, int min, int max) {
        String errorMessage = "";

        if (name.isEmpty()) {
            errorMessage += "A name has not been entered. ";
        }
        if (price < 0.01) {
            errorMessage += "The price must be greater than 0. ";
        }
        if (inStock < 1) {
            errorMessage += "Inventory level must be greater than 0. ";
        }
        if (min > max) {
            errorMessage += "The Min value must be less than the Max value. ";
        }
        if (inStock < min) {
            errorMessage += "Inventory level is lower than minimum value. ";
        }
        if (inStock > max) {
            errorMessage += "Inventory level is higher than the maximum value. ";
        }
        return errorMessage;
    }

    // Getters
    public int getPartId() {
        return partId;
    }

    // Setters
    public void setPartId(int partId) {
        this.partId = partId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    @Override
    public String toString() {
        return this.partId + "," + this.name + "," + this.price + "," + this.inStock + "," +
                this.min + "," + this.max;
    }
}
