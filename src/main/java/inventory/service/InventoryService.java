package inventory.service;

import inventory.model.InhousePart;
import inventory.model.OutsourcedPart;
import inventory.model.Part;
import inventory.model.Product;
import inventory.repository.InventoryRepository;
import inventory.validator.Validator;
import inventory.validator.ValidatorException;
import javafx.collections.ObservableList;

public class InventoryService {

    private final InventoryRepository repo;

    public InventoryService(InventoryRepository repo) {
        this.repo = repo;
    }

    public void addInhousePart(String name, double price, int inStock, int min, int max, int partDynamicValue) throws ValidatorException {
        InhousePart inhousePart = new InhousePart(name, price, inStock, min, max, partDynamicValue);
        Validator.isValidPart(inhousePart);
        inhousePart.setPartId(repo.getAutoPartId());
        repo.addPart(inhousePart);
    }

    public void addOutsourcePart(String name, double price, int inStock, int min, int max, String partDynamicValue) throws ValidatorException {
        OutsourcedPart outsourcedPart = new OutsourcedPart(name, price, inStock, min, max, partDynamicValue);
        Validator.isValidPart(outsourcedPart);
        outsourcedPart.setPartId(repo.getAutoPartId());
        repo.addPart(outsourcedPart);
    }

    public void addProduct(String name, double price, int inStock, int min, int max, ObservableList<Part> addParts) throws ValidatorException {
        Product product = new Product(name, price, inStock, min, max, addParts);
        Validator.isValidProduct(product);
        product.setProductId(repo.getAutoProductId());
        repo.addProduct(product);
    }

    public ObservableList<Part> getAllParts() {
        return repo.getAllParts();
    }

    public ObservableList<Product> getAllProducts() {
        return repo.getAllProducts();
    }

    public Part lookupPart(String search) {
        return repo.lookupPart(search);
    }

    public Product lookupProduct(String search) {
        return repo.lookupProduct(search);
    }

    /**
     * Updates an inhouse part
     *
     * @param partIndex   The index of the part to update
     * @param inhousePart The new part to update
     */
    public void updateInhousePart(int partIndex, InhousePart inhousePart) {
        repo.updatePart(partIndex, inhousePart);
    }

    /**
     * Updates an outsourced part
     *
     * @param partIndex      The index of the part to update
     * @param outsourcedPart The new part to update
     */
    public void updateOutsourcedPart(int partIndex, OutsourcedPart outsourcedPart) {
        repo.updatePart(partIndex, outsourcedPart);
    }

    /**
     * Updates a product
     *
     * @param productIndex The index of the product to update
     * @param product      The new product to update
     */
    public void updateProduct(int productIndex, Product product) {
        repo.updateProduct(productIndex, product);
    }

    public void deletePart(Part part) {
        repo.deletePart(part);
    }

    public void deleteProduct(Product product) {
        repo.deleteProduct(product);
    }

}