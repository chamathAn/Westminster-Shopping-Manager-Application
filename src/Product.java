import java.lang.*;
import java.util.Objects;

public abstract class Product implements Comparable<Product> {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return get_productAddingAmount() == product.get_productAddingAmount() && get_productPrice() == product.get_productPrice() && Objects.equals(get_productId(), product.get_productId()) && Objects.equals(get_productName(), product.get_productName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(get_productId(), get_productName(), get_productAddingAmount(), get_productPrice());
    }

    //variables declaration
    private String _productId;
    private String _productName;
    private int _productAddingAmount;
    private int _productPrice;

    public Product (String productId, String productName, int productAddingAmount, int productPrice) {
        this._productId = productId;
        this._productName = productName;
        this._productAddingAmount = productAddingAmount;
        this._productPrice = productPrice;
    }

    //Getters and Setters
    public String get_productId() {
        return _productId;
    }

    public void set_productId(String _productId) {
        this._productId = _productId;
    }

    public String get_productName() {
        return _productName;
    }

    public void set_productName(String _productName) {
        this._productName = _productName;
    }

    public int get_productAddingAmount() {
        return _productAddingAmount;
    }

    public void set_productAddingAmount(int _productAddingAmount) {
        this._productAddingAmount = _productAddingAmount;
    }

    public int get_productPrice() {
        return _productPrice;
    }

    public void set_productPrice(int _productPrice) {
        this._productPrice = _productPrice;
    }

    @Override
    public int compareTo(Product otherProduct) {
        return this.get_productId().compareTo(otherProduct.get_productId());
    }
}
