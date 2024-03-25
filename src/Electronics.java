import java.lang.*;

public class Electronics extends Product{

    // Variable declaration
    private String _electronicBrand;
    private int _electronicWarrantyPeriod;

    public Electronics(String productId, String productName, int productAddingAmount, int productPrice, String electronicBrand, int electronicWarrantyPeriod) {
        super(productId, productName, productAddingAmount, productPrice);
        this._electronicBrand = electronicBrand;
        this._electronicWarrantyPeriod = electronicWarrantyPeriod;
    }

    // Getters $ Setters
    public String get_electronicBrand() {
        return _electronicBrand;
    }

    public void set_electronicBrand(String _electronicBrand) {
        this._electronicBrand = _electronicBrand;
    }

    public int get_electronicWarrantyPeriod() {
        return _electronicWarrantyPeriod;
    }

    public void set_electronicWarrantyPeriod(int _electronicWarrantyPeriod) {
        this._electronicWarrantyPeriod = _electronicWarrantyPeriod;
    }

    @Override
    public String toString() {
        return get_productName() +" Electronics product" + " that the brand was " + this._electronicBrand;
    }


}
