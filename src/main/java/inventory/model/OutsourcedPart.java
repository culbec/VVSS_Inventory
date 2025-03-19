package inventory.model;


public class OutsourcedPart extends Part {

    // Declare fields
    private String companyName;

    // Constructor
    public OutsourcedPart(String name, double price, int inStock, int min, int max, String companyName) {
        super(name, price, inStock, min, max);
        this.companyName = companyName;
    }

    // Getter
    public String getCompanyName() {
        return companyName;
    }

    // Setter
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "O," + super.toString() + "," + getCompanyName();
    }

}
