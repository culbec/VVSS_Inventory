package inventory.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Inventory {
    private final Logger logger = LogManager.getLogger(Inventory.class);
    // Declare fields
    private ObservableList<Product> products;
    private ObservableList<Part> allParts;
    private int autoPartId;
    private int autoProductId;

    public Inventory() {
        this.products = FXCollections.observableArrayList();
        this.allParts = FXCollections.observableArrayList();
        this.autoProductId = 0;
        this.autoPartId = 0;
    }

    // Declare methods

    /**
     * Getter for Product Observable List
     *
     * @return The Product Observable List
     */
    public ObservableList<Product> getProducts() {
        return products;
    }

    public void setProducts(ObservableList<Product> list) {
        products = list;
    }

    /**
     * Add new product to observable list products
     *
     * @param product The product to be added
     */
    public void addProduct(Product product) {
        products.add(product);
    }

    /**
     * Remove product from observable list products
     *
     * @param product The product to be removed
     */
    public void removeProduct(Product product) {
        products.remove(product);
    }


    /**
     * Update product at given index
     *
     * @param index   The index of the item to be replaced
     * @param product The product to replace the item on that index
     */
    public void updateProduct(int index, Product product) {
        products.set(index, product);
    }

    /**
     * Accepts search parameter and if an ID or name matches input, that product is returned
     *
     * @param searchItem The search parameter
     * @return The product that matched the search parameter or null if no match
     */
    public Product lookupProduct(String searchItem) {
        for (Product p : products) {
            if (p.getName().contains(searchItem) || (p.getProductId() + "").equals(searchItem)) {
                return p;
            }
        }

        logger.warn("No product with name " + searchItem + " found");
        return null;
    }

    /**
     * Add new part to observable list allParts
     *
     * @param part The part to be added
     */
    public void addPart(Part part) {
        allParts.add(part);
    }

    /**
     * Removes part passed as parameter from allParts
     *
     * @param part The part to be removed
     */
    public void deletePart(Part part) {
        allParts.remove(part);
    }

    /**
     * Update part at given index
     *
     * @param index The index of the part to be updated
     * @param part  The part that will replace the part at the given index
     */
    public void updatePart(int index, Part part) {
        allParts.set(index, part);
    }

    /**
     * Getter for allParts Observable List
     *
     * @return The allParts Observable List
     */
    public ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Setter for allParts Observable List
     *
     * @param list The list to be set as allParts
     */
    public void setAllParts(ObservableList<Part> list) {
        allParts = list;
    }

    /**
     * Accepts search parameter and if an ID or name matches input, that part is returned
     *
     * @param searchItem The search parameter
     * @return The part that matched the search parameter or null if no match
     */
    public Part lookupPart(String searchItem) {
        for (Part p : allParts) {
            if (p.getName().contains(searchItem) || (p.getPartId() + "").equals(searchItem)) return p;
        }

        logger.warn("No part with " + searchItem + " found");
        return null;
    }

    /**
     * Method for incrementing part ID to be used to automatically
     * assign ID numbers to parts
     *
     * @return An integer representing the next available part ID
     */
    public int getAutoPartId() {
        autoPartId++;
        return autoPartId;
    }

    /**
     * Setter for autoPartId
     *
     * @param id The ID to be set
     */
    public void setAutoPartId(int id) {
        autoPartId = id;
    }

    /**
     * Method for incrementing product ID to be used to automatically
     * assign ID numbers to products
     *
     * @return An integer representing the next available product ID
     */
    public int getAutoProductId() {
        autoProductId++;
        return autoProductId;
    }

    /**
     * Setter for autoProductId
     *
     * @param id The ID to be set
     */
    public void setAutoProductId(int id) {
        autoProductId = id;
    }

}
