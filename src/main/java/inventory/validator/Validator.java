package inventory.validator;

import inventory.model.InhousePart;
import inventory.model.OutsourcedPart;
import inventory.model.Part;
import inventory.model.Product;

import java.util.Objects;

public class Validator {
    private Validator() {
    }

    public static void isValidProduct(Product product) throws ValidatorException {
        StringBuilder errorBuilder = new StringBuilder();

        // Null product
        if (Objects.isNull(product)) {
            errorBuilder.append("Product is null");
            throw new ValidatorException(errorBuilder.toString());
        }

        // Initialized product, checking fields
        if (product.getName().isEmpty() || product.getName().isBlank()) {
            errorBuilder.append("Product name is empty");
        }

        if (product.getPrice() < 0) {
            errorBuilder.append("Product price is negative");
        }

        if (product.getInStock() < 0) {
            errorBuilder.append("Product inStock is negative");
        }

        if (product.getMin() < 0) {
            errorBuilder.append("Product min is negative");
        }

        if (product.getMax() < 0) {
            errorBuilder.append("Product max is negative");
        }

        if (product.getMin() >= 0 && product.getMax() >= 0 && product.getMin() > product.getMax()) {
            errorBuilder.append("Product min > product max");
        }

        if (product.getMin() > product.getMax() || product.getMax() < product.getInStock()) {
            errorBuilder.append("Product inStock not between Product min and Product max");
        }

        if (product.getAssociatedParts().isEmpty()) {
            errorBuilder.append("Product associated parts is empty");
        }

        if (!errorBuilder.isEmpty()) {
            throw new ValidatorException(errorBuilder.toString());
        }
    }

    public static void isValidPart(Part part) throws ValidatorException {
        StringBuilder errorBuilder = new StringBuilder();

        // Null part
        if (Objects.isNull(part)) {
            errorBuilder.append("Part is null");
            throw new ValidatorException(errorBuilder.toString());
        }

        // Initialized part, checking fields
        if (part.getName().isEmpty() || part.getName().isBlank()) {
            errorBuilder.append("Part name is empty");
        }

        if (part.getPrice() < 0) {
            errorBuilder.append("Part price is negative");
        }

        if (part.getInStock() < 0) {
            errorBuilder.append("Part inStock is negative");
        }

        if (part.getMin() < 0) {
            errorBuilder.append("Part min is negative");
        }

        if (part.getMax() < 0) {
            errorBuilder.append("Part max is negative");
        }

        if (part.getMin() >= 0 && part.getMax() >= 0 && part.getMin() > part.getMax()) {
            errorBuilder.append("Part min > part max");
        }

        if (part.getMin() > part.getInStock() || part.getMax() < part.getInStock()) {
            errorBuilder.append("Part inStock not between Part min and Part max");
        }

        if (part instanceof InhousePart inhousePart && inhousePart.getMachineId() < 0) {
            errorBuilder.append("Part machineId is negative");
        } else if (part instanceof OutsourcedPart outsourcedPart && (outsourcedPart.getCompanyName().isEmpty() || outsourcedPart.getCompanyName().isBlank())) {
            errorBuilder.append("Part companyName is empty");
        }


        if (!errorBuilder.isEmpty()) {
            throw new ValidatorException(errorBuilder.toString());
        }
    }
}
