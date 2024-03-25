public class Clothing extends Product{

    // Variable declaration
    private int _clothingSize;
    private String _clothingColor;

    public Clothing(String productId, String productName, int productAddingAmount, int productPrice, int clothingSize, String clothingColor) {
        super(productId, productName, productAddingAmount, productPrice);
        this._clothingSize = clothingSize;
        this._clothingColor = clothingColor;
    }

    //Getters & Setters
    public int get_clothingSize() {
        return _clothingSize;
    }

    public void set_clothingSize(int _clothingSize) {
        this._clothingSize = _clothingSize;
    }

    public String get_clothingColor() {
        return _clothingColor;
    }

    public void set_clothingColor(String _clothingColor) {
        this._clothingColor = _clothingColor;
    }

    // To String
    @Override
    public String toString() {
        return get_productName()+ " Clothing product " + "sized=" + this._clothingSize;
    }


}
