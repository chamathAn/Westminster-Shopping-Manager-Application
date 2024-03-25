import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

public class shoppingCart extends JFrame {
    private ArrayList<Product> productsOnCart;
    private JTable cartTable;
    private final JButton deleteProductBtn = new JButton("Delete From Cart");
    private String[] productId;
    private JTextArea productTotalPriceTextArea;
    private JButton calculateFinalPriceBtn, purchaseBtn;
    private DefaultTableModel tableModel;
    private ArrayList <Integer>quantityInTable;
    private boolean priceCalculated = false;
    public void setProductsOnCart(ArrayList<Product> productsOnCart) {
        this.productsOnCart = productsOnCart;
        updateCartTable();
    }

    public ArrayList<Product> getProductsOnCart() {
        return productsOnCart;
    }

    public shoppingCart(ArrayList<Product> productsOnCart) {
        this.productsOnCart = productsOnCart;

        //Table and its pane
        String[] columnNames = {"Product", "Quantity", "Price(Rs)"};
        Object[][] productData = productsOnCartArrayListToObjectArray(); // Call product allocating method to populate the cart table
        // Give access to edit qty column in cart table
        tableModel = new DefaultTableModel(productData, columnNames) { // Give access to edit one column table
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 1;
            }
        };
        cartTable = new JTable(tableModel);
        JPanel deleteBtnPane = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        deleteBtnPane.add(deleteProductBtn);
        enableTheDeleteBtn();
        this.cartTable.addMouseListener(new deleteRowFromCartTable()); // Adding MouseListener to Delete Product form Cart

        JScrollPane cartTablePane = new JScrollPane(cartTable);
        JPanel gridPane = new JPanel(new GridLayout(2, 1));

        JPanel dltBtnAndTable = new JPanel(new BorderLayout());
        dltBtnAndTable.add(cartTablePane,BorderLayout.CENTER);
        dltBtnAndTable.add(deleteBtnPane,BorderLayout.SOUTH);
        gridPane.add(dltBtnAndTable);

        //Total price details
        this.productTotalPriceTextArea = new JTextArea(10, 1);
        JLabel ignoreLabel = new JLabel("Final products prices description");
        JScrollPane finalPricePane = new JScrollPane();
        finalPricePane.setColumnHeaderView(ignoreLabel);
        finalPricePane.setViewportView(productTotalPriceTextArea);
        gridPane.add(finalPricePane);

        calculateFinalPriceBtn = new JButton("Calculate Price");
        purchaseBtn = new JButton("Purchase");
        JPanel footerBtnSection = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        footerBtnSection.add(calculateFinalPriceBtn);
        footerBtnSection.add(purchaseBtn);

        JPanel mainPane = new JPanel(new BorderLayout());
        mainPane.add(gridPane);
        mainPane.add(footerBtnSection,BorderLayout.SOUTH);
        this.add(mainPane);

        calculateFinalPriceBtn.addActionListener(new finalPrice());
        purchaseBtn.addActionListener(new purchaseItemClass());
    }


    private Object[][] productsOnCartArrayListToObjectArray() {
        // To assign the product details into cart table
        Object[][] productData = new Object[this.productsOnCart.size()][3];
        for (int i = 0; i < this.productsOnCart.size(); i++) {
            if (productsOnCart.get(i) instanceof Electronics) {
                productData[i][0] = productsOnCart.get(i).get_productId() + "," + productsOnCart.get(i).get_productName() + "\n" +
                        ((Electronics) productsOnCart.get(i)).get_electronicBrand() +
                        ", " + ((Electronics) productsOnCart.get(i)).get_electronicWarrantyPeriod() + " months warranty";
                productData[i][1] = "Type Qty Here";
                productData[i][2] = productsOnCart.get(i).get_productPrice();
            } else if (productsOnCart.get(i) instanceof Clothing) {
                productData[i][0] = productsOnCart.get(i).get_productId() + "," + productsOnCart.get(i).get_productName() + "\n" +
                        ((Clothing) productsOnCart.get(i)).get_clothingSize() + ", " + ((Clothing) productsOnCart.get(i)).get_clothingColor();
                productData[i][1] = "Type Qty Here";
                productData[i][2] = productsOnCart.get(i).get_productPrice();
            }
        }
        return productData;
    }

    public void updateCartTable() {
        // Update the table when a new product is adding
        String[] columnNames = {"Product", "Quantity", "Price(Rs)"};
        Object[][] productData = productsOnCartArrayListToObjectArray();
        cartTable.setModel(new DefaultTableModel(productData, columnNames));
    }

    public void enableTheDeleteBtn() {
        // Disable the btn until a product is selected
        deleteProductBtn.setEnabled(false);

        // Enable product delete btn after select a row
        cartTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                deleteProductBtn.setEnabled(true);
                deleteProductBtn.addActionListener(new deleteRowFromCartTable()); // For delete the row
            }
        });
    }

    // Deletion part On Shopping Cart
    public class deleteRowFromCartTable extends MouseAdapter implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == deleteProductBtn) {
                // remove selected row from the model
                DefaultTableModel model = (DefaultTableModel) cartTable.getModel();
                int[] rows = cartTable.getSelectedRows();
                for (int i = 0; i < rows.length; i++) {
                    model.removeRow(rows[i] - i);
                }

                deleteProductBtn.setEnabled(false); // Disable again delete button after a deletion

                // Remove product from arraylist
                int indexOfDeletingProductFromCart = -1;
                for (Product currentProduct : UserGUI.selectedProductsToCart) {
                    if (currentProduct != null && Objects.equals(currentProduct.get_productId(), productId[0])) {
                        indexOfDeletingProductFromCart = UserGUI.selectedProductsToCart.indexOf(currentProduct);
                        break;
                    }
                }
                if (indexOfDeletingProductFromCart != -1) {
                    UserGUI.selectedProductsToCart.remove(indexOfDeletingProductFromCart);
                    System.out.println("Successfully deleted");
                }
            }
        }

        public void mouseClicked(MouseEvent e) {

            // get the value of selected row
            String temp = (String) cartTable.getValueAt(cartTable.getSelectedRow(), 0);
            productId = temp.split(",");// Splitting to get the product ID

        }
    }

    public class finalPrice implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(e.getSource()==calculateFinalPriceBtn){


                String totalString = String.format("%66s", "")+"Total";
                String firstProductDiscountString = String.format("%24s", "")+"First Purchase Discount (10%)";
                String threeProductsDiscountString = "Three items in same category discount (20%)";
                String totaPriceProductsString = String.format("%57s", "")+"Final Total   Rs.";
                int total = 0;
                int firstProductDiscount=0;
                int threeProductsDiscount=0;
                int moreThanThreeProductCount=0;
                quantityInTable = new ArrayList<>();
                ArrayList <Integer>priceOfProductsInTable = new ArrayList<>();
                ArrayList <String>productIdsInTable = new ArrayList<>();
                ArrayList moreThanThreeProducts=new ArrayList<>();

                for (int i = 0;i<cartTable.getModel().getRowCount();i++) {
                    try {
                        int quantityValue = Integer.parseInt((String)cartTable.getValueAt(i, 1));
                        quantityInTable.add(quantityValue);
                        priceOfProductsInTable.add((int) cartTable.getValueAt(i, 2));
                        productIdsInTable.add((String)cartTable.getValueAt(i, 0));
                        if (Integer.parseInt((String)cartTable.getValueAt(i, 1))>2)moreThanThreeProductCount++; // if there is more than 3 qty in same product
                    } catch (NumberFormatException f) {
                        JOptionPane.showConfirmDialog(null, "Please fill the quantity using a numeric value", "Quantity Error", JOptionPane.PLAIN_MESSAGE);
                    }
                }

                //Get frequencies of each element
                Map<String, Long> frequencies = productIdsInTable.stream()
                        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
                //then filter only the inputs which have frequency greater than 2
                frequencies.entrySet().stream()
                        .filter(entry -> entry.getValue() > 2)
                        .forEach(entry -> moreThanThreeProducts.add( entry.getValue()));

                // Calculate Total Price
                for (int i =0; i< quantityInTable.size();i++){
                    total += quantityInTable.get(i) * priceOfProductsInTable.get(i);
                }
                //For three product discount
                if (!moreThanThreeProducts.isEmpty()||moreThanThreeProductCount!=0){
                    threeProductsDiscount= (int) (total*0.2);
                }

                productTotalPriceTextArea.setText(totalString+"   Rs."+total+"\n"+
                        threeProductsDiscountString+"   Rs."+threeProductsDiscount+"\n"+
                        firstProductDiscountString+"   Rs."+firstProductDiscount+"\n"+ totaPriceProductsString+
                                (total-threeProductsDiscount-firstProductDiscount)
                        );
                priceCalculated=true;
            }
        }
    }
    public class purchaseItemClass implements ActionListener {
        // Purchase process
        public void actionPerformed(ActionEvent e) {
            if (priceCalculated) {
                for (int i = 0; i < productsOnCart.size(); i++) {
                    // Deducting the purchased items
                    int leftProductAmount = productsOnCart.get(i).get_productAddingAmount() - quantityInTable.get(i);
                    productsOnCart.get(i).set_productAddingAmount(leftProductAmount);
                }
                JOptionPane.showConfirmDialog(null, "Purchase is Successful, Thank You!", "Purchase Successful", JOptionPane.PLAIN_MESSAGE);
            } else {
                JOptionPane.showConfirmDialog(null, "Please calculate the final price before attempting to purchase.", "Price Calculation Required", JOptionPane.WARNING_MESSAGE);
            }
        }
    }


}
