package inventory.repository;

import inventory.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.StringTokenizer;

public class InventoryRepository {
    private static final String ITEMS_FILE = "data/items.txt";

    private final Logger logger = LogManager.getLogger(InventoryRepository.class);

    private ObservableList<Product> allProducts;
    private ObservableList<Part> allParts;
    private int autoPartId;
    private int autoProductId;

    public InventoryRepository(Boolean isTesting) {
        this.allProducts = FXCollections.observableArrayList();
        this.allParts = FXCollections.observableArrayList();
        this.autoProductId = 0;
        this.autoPartId = 0;
        if(!isTesting){
        readParts();
        readProducts();
    }}

    public void readParts() {
        File file = new File(ITEMS_FILE);
        ObservableList<Part> listP = FXCollections.observableArrayList();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Part part = getPartFromString(line);
                if (part != null)
                    listP.add(part);
            }
        } catch (IOException e) {
            logger.error("Reading parts failed, reason: {}", e.getMessage());
        }

        allParts = listP;
    }

    private Part getPartFromString(String line) {
        Part item;

        if (line == null || line.isEmpty()) return null;

        StringTokenizer st = new StringTokenizer(line, ",");
        String type = st.nextToken();

        int id = Integer.parseInt(st.nextToken());
        autoPartId = id;
        String name = st.nextToken();
        double price = Double.parseDouble(st.nextToken());
        int inStock = Integer.parseInt(st.nextToken());
        int minStock = Integer.parseInt(st.nextToken());
        int maxStock = Integer.parseInt(st.nextToken());

        switch (type) {
            case "I": {
                int idMachine = Integer.parseInt(st.nextToken());
                item = new InhousePart(name, price, inStock, minStock, maxStock, idMachine);
                item.setPartId(id);
                break;
            }
            case "O": {
                String company = st.nextToken();
                item = new OutsourcedPart(name, price, inStock, minStock, maxStock, company);
                item.setPartId(id);
                break;
            }
            default: {
                logger.debug("Type '{}' is not for a part", type);
                return null;
            }
        }
        return item;
    }

    public void readProducts() {
        File file = new File(ITEMS_FILE);

        ObservableList<Product> listP = FXCollections.observableArrayList();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Product product = getProductFromString(line);
                if (product != null)
                    listP.add(product);
            }
        } catch (IOException e) {
            logger.error("Reading products failed, reason: {}", e.getMessage());
        }

        allProducts = listP;
    }

    private Product getProductFromString(String line) {
        Product product;

        if (line == null || line.isEmpty()) return null;

        StringTokenizer st = new StringTokenizer(line, ",");
        String type = st.nextToken();

        if (type.equals("P")) {
            int id = Integer.parseInt(st.nextToken());
            autoProductId = id;
            String name = st.nextToken();
            double price = Double.parseDouble(st.nextToken());
            int inStock = Integer.parseInt(st.nextToken());
            int minStock = Integer.parseInt(st.nextToken());
            int maxStock = Integer.parseInt(st.nextToken());
            String partIDs = st.nextToken();

            StringTokenizer ids = new StringTokenizer(partIDs, ":");
            ObservableList<Part> list = FXCollections.observableArrayList();
            while (ids.hasMoreTokens()) {
                String idP = ids.nextToken();
                Part part = this.lookupPart(idP);
                if (part != null)
                    list.add(part);
            }
            product = new Product(name, price, inStock, minStock, maxStock, list);
            product.setProductId(id);
            product.setAssociatedParts(list);
        } else {
            logger.debug("Type '{}' is not for a product", type);
            return null;
        }

        return product;
    }

    public void writeAll() {
        File file = new File(ITEMS_FILE);

        ObservableList<Part> parts = getAllParts();
        ObservableList<Product> products = getAllProducts();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Part p : parts) {
                logger.info("Writing part: {}", p);
                bw.write(p.toString());
                bw.newLine();
            }

            StringBuilder sb = new StringBuilder();
            for (Product pr : products) {
                sb.append(pr.toString()).append(",");
                ObservableList<Part> list = pr.getAssociatedParts();
                int index = 0;

                while (index < list.size()) {
                    sb.append(list.get(index).getPartId()).append(":");
                    index++;
                }
                sb.append("\n");
                bw.write(sb.toString());
            }
        } catch (IOException e) {
            logger.error("Writing failed, reason: {}", e.getMessage());
        }
    }

    public void addPart(Part part) {
        allParts.add(part);
        writeAll();
    }

    public void addProduct(Product product) {
        allProducts.add(product);
        writeAll();
    }

    public int getAutoPartId() {
        return autoPartId;
    }

    public int getAutoProductId() {
        return autoProductId;
    }

    public ObservableList<Part> getAllParts() {
        return this.allParts;
    }

    public ObservableList<Product> getAllProducts() {
        return this.allProducts;
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

        logger.warn("No part with '{}' found", searchItem);
        return null;
    }

    /**
     * Accepts search parameter and if an ID or name matches input, that product is returned
     *
     * @param searchItem The search parameter
     * @return The product that matched the search parameter or null if no match
     */
    public Product lookupProduct(String searchItem) {
        for (Product p : allProducts) {
            if (p.getName().contains(searchItem) || (p.getProductId() + "").equals(searchItem)) {
                return p;
            }
        }

        logger.warn("No product with name '{}' found", searchItem);
        return null;
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
     * Update product at given index
     *
     * @param index   The index of the item to be replaced
     * @param product The product to replace the item on that index
     */
    public void updateProduct(int index, Product product) {
        allProducts.set(index, product);
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
     * Remove product from observable list products
     *
     * @param product The product to be removed
     */
    public void deleteProduct(Product product) {
        allProducts.remove(product);
    }
}
