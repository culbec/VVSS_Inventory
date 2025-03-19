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
    private Inventory inventory;

    public InventoryRepository() {
        this.inventory = new Inventory();
        readParts();
        readProducts();
    }

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
        inventory.setAllParts(listP);
    }

    private Part getPartFromString(String line) {
        Part item;

        if (line == null || line.isEmpty()) return null;

        StringTokenizer st = new StringTokenizer(line, ",");
        String type = st.nextToken();

        int id = Integer.parseInt(st.nextToken());
        inventory.setAutoPartId(id);
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
        inventory.setProducts(listP);
    }

    private Product getProductFromString(String line) {
        Product product;

        if (line == null || line.isEmpty()) return null;

        StringTokenizer st = new StringTokenizer(line, ",");
        String type = st.nextToken();

        if (type.equals("P")) {
            int id = Integer.parseInt(st.nextToken());
            inventory.setAutoProductId(id);
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
                Part part = inventory.lookupPart(idP);
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

        ObservableList<Part> parts = inventory.getAllParts();
        ObservableList<Product> products = inventory.getProducts();

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
        inventory.addPart(part);
        writeAll();
    }

    public void addProduct(Product product) {
        inventory.addProduct(product);
        writeAll();
    }

    public int getAutoPartId() {
        return inventory.getAutoPartId();
    }

    public int getAutoProductId() {
        return inventory.getAutoProductId();
    }

    public ObservableList<Part> getAllParts() {
        return inventory.getAllParts();
    }

    public ObservableList<Product> getAllProducts() {
        return inventory.getProducts();
    }

    public Part lookupPart(String search) {
        return inventory.lookupPart(search);
    }

    public Product lookupProduct(String search) {
        return inventory.lookupProduct(search);
    }

    public void updatePart(int partIndex, Part part) {
        inventory.updatePart(partIndex, part);
        writeAll();
    }

    public void updateProduct(int productIndex, Product product) {
        inventory.updateProduct(productIndex, product);
        writeAll();
    }

    public void deletePart(Part part) {
        inventory.deletePart(part);
        writeAll();
    }

    public void deleteProduct(Product product) {
        inventory.removeProduct(product);
        writeAll();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }
}
