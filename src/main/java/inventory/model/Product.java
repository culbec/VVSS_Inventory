package inventory.model;

import javafx.collections.ObservableList;

public class Product {

    // Declare fields
    private final ObservableList<Part> associatedParts;
    private int productId;
    private String name;
    private double price;
    private int inStock;
    private int min;
    private int max;

    // Constructor
    public Product(String name, double price, int inStock, int min, int max, ObservableList<Part> partList) {
        this.name = name;
        this.price = price;
        this.inStock = inStock;
        this.min = min;
        this.max = max;
        this.associatedParts = partList;
    }

    /**
     * Generate an error message for invalid values in a product
     * and evaluate whether the sum of the price of associated parts
     * is less than the price of the resulting product.
     * A valid product will return an empty error message string.
     *
     * @param product The product to validate
     * @return An error message string
     */
    public static String isValidProduct(Product product) {
        String errorMessage = "";
        double sumOfParts = 0.00;

        for (int i = 0; i < product.associatedParts.size(); i++) {
            sumOfParts += product.associatedParts.get(i).getPrice();
        }
        if (product.name.isEmpty()) {
            errorMessage += "A name has not been entered. ";
        }
        if (product.min < 0) {
            errorMessage += "The inventory level must be greater than 0. ";
        }
        if (product.max < 0.01) {
            errorMessage += "The price must be greater than $0. ";
        }
        if (product.min > product.max) {
            errorMessage += "The Min value must be less than the Max value. ";
        }
        if (product.inStock < product.min) {
            errorMessage += "Inventory level is lower than minimum value. ";
        }
        if (product.inStock > product.max) {
            errorMessage += "Inventory level is higher than the maximum value. ";
        }
        if (product.associatedParts.isEmpty()) {
            errorMessage += "Product must contain at least 1 part. ";
        }
        if (sumOfParts > product.price) {
            errorMessage += "Product price must be greater than cost of parts. ";
        }
        return errorMessage;
    }

    // Getters
    public ObservableList<Part> getAssociatedParts() {
        return associatedParts;
    }

    // Setters
    public void setAssociatedParts(ObservableList<Part> newAssociatedParts) {
        this.associatedParts.clear();
        this.associatedParts.addAll(newAssociatedParts);
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
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

    // Other methods
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);
    }

    public void removeAssociatedPart(Part part) {
        associatedParts.remove(part);
    }

    public Part lookupAssociatedPart(String searchItem) {
        for (Part p : associatedParts) {
            if (p.getName().contains(searchItem) || Integer.toString(p.getPartId()).equals(searchItem)) {
                return p;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "P," + this.productId + "," + this.name + "," + this.price + "," + this.inStock + "," +
                this.min + "," + this.max;
    }
}