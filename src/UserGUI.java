import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class UserGUI extends JFrame {
    private final JComboBox categoryComboBox;
    private JTable midTable;
    private List<Product> productList;
    private JTextArea productDetailTextArea;
    private JButton viewCartButton;
    static ArrayList<Product> selectedProductsToCart = new ArrayList<>();
    private Product selectedProduct;
    private JButton addToCartBtn;
    private shoppingCart  shoppingCart;

    public UserGUI(ArrayList<Product> productList) {

        // Top Panel
        String[] category = {"All", "Electronics", "Clothing"};
        JLabel selectCategoryLabel = new JLabel("Select Product Category");
        this.categoryComboBox = new JComboBox<>(category);
         viewCartButton = new JButton("Shopping Cart");
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 10)); // Set the layout for top panel
        topPanel.add(selectCategoryLabel);
        topPanel.add(categoryComboBox);
        topPanel.add(viewCartButton);

        viewCartButton.addActionListener(new viewShoppingCartActionListener());
        this.categoryComboBox.addActionListener(new tableActionListener());

        // Table panel
        this.productList = productList;
        Object[][] productDataArray = productsArrayListToObjectArray("All");
        String[] columnNames = {"Product Id", "Name", "Category", "Price(Rs)", "Info"};
        TableModel midTableModel = new DefaultTableModel(productDataArray, columnNames);
        this.midTable = new JTable(midTableModel) {
            public Component prepareRenderer( // To change the color; learnt from web and customized as I want, reference will be in report
                                              TableCellRenderer renderer, int row, int column) {
                Component c = super.prepareRenderer(renderer, row, column);
                String productId = (String) midTable.getValueAt(row, 0); // Get the ID of selected row
                for (Product product : productList) {
                    if (product.get_productId().equals(productId)) {
                        c.setBackground(product.get_productAddingAmount() <= 3 ? Color.RED : getBackground());
                    }
                }
                return c;
            }
        };
        midTable.setDefaultEditor(Object.class, null); // Disable the editing cells. Extracted this line from stackoverflow, will refer in the report
        JScrollPane midPane = new JScrollPane(midTable);


        // Product Detail Panel
        addToCartBtn = new JButton("Add to Shopping Cart");
        JLabel ignoreLabel = new JLabel("Selected Product Details"); // Ignore
        ignoreLabel.setFont(new Font("Times New Roman", Font.BOLD, 18));
        this.productDetailTextArea = new JTextArea(10, 1);
        JScrollPane productDetailPane = new JScrollPane();
        productDetailPane.setColumnHeaderView(ignoreLabel);
        productDetailPane.setViewportView(productDetailTextArea);

        addToCartBtn.addActionListener(new addToCart());

        JPanel grdForProductDetail = new JPanel(new BorderLayout());
        JPanel addCartBtnFlowLayout = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addCartBtnFlowLayout.add(addToCartBtn); // Add addToCart btn to a flowlayout
        grdForProductDetail.add(productDetailPane, BorderLayout.CENTER); // Add Both scroll panel and btn to grid layout
        grdForProductDetail.add(addCartBtnFlowLayout, BorderLayout.SOUTH);
        this.midTable.addMouseListener(new mouseClickedOnProduct()); // Adding MouseListener

        JPanel mainPanel = new JPanel(new BorderLayout()); // Set the layout for main panel
        mainPanel.add(topPanel, BorderLayout.NORTH); // Add top panel to main panel
        mainPanel.add(midPane, BorderLayout.CENTER); // Add mid panel to main panel
        mainPanel.add(grdForProductDetail, BorderLayout.SOUTH); // Add both scroll panel and btn grid layout to main panel
        this.add(mainPanel);

        // Initiating an instance of cart window
        shoppingCart = new shoppingCart(selectedProductsToCart);
    }

    private Object[][] productsArrayListToObjectArray(String category) {
        // Filtering the data into table according to chosen category

        Object[][] productData = new Object[this.productList.size()][5];
        switch (category) {
            case "All" -> {
                for (int i = 0; i < this.productList.size(); i++) {
                    productData[i][0] = productList.get(i).get_productId();
                    productData[i][1] = productList.get(i).get_productName();
                    productData[i][2] = (productList.get(i) instanceof Electronics) ? "Electronics" : "Clothing";
                    productData[i][3] = productList.get(i).get_productPrice();
                    if (productList.get(i) instanceof Electronics) {
                        productData[i][4] = ((Electronics) productList.get(i)).get_electronicBrand() + ", " + ((Electronics) productList.get(i)).get_electronicWarrantyPeriod() + " months warranty";
                    } else if (productList.get(i) instanceof Clothing) {
                        productData[i][4] = ((Clothing) productList.get(i)).get_clothingSize() + ", " + ((Clothing) productList.get(i)).get_clothingColor();
                    }
                }
            }
            case "Electronics" -> {
                for (int i = 0; i < this.productList.size(); i++) {
                    if (productList.get(i) instanceof Electronics) {
                        productData[i][0] = productList.get(i).get_productId();
                        productData[i][1] = productList.get(i).get_productName();
                        productData[i][2] = "Electronics";
                        productData[i][3] = productList.get(i).get_productPrice();
                        productData[i][4] = ((Electronics) productList.get(i)).get_electronicBrand() + ", " + ((Electronics) productList.get(i)).get_electronicWarrantyPeriod() + " months warranty";

                    }
                }
            }
            case "Clothing" -> {
                for (int i = 0; i < this.productList.size(); i++) {
                    if (productList.get(i) instanceof Clothing) {
                        productData[i][0] = productList.get(i).get_productId();
                        productData[i][1] = productList.get(i).get_productName();
                        productData[i][2] = "Clothing";
                        productData[i][3] = productList.get(i).get_productPrice();
                        productData[i][4] = ((Clothing) productList.get(i)).get_clothingSize() + ", " + ((Clothing) productList.get(i)).get_clothingColor();
                    }
                }
            }
        }
        return productData;
    }

    public class tableActionListener implements ActionListener {
        // Action handler for table in actions

        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == categoryComboBox) {
                String[] columnNames = {"Product Id", "Name", "Category", "Price(Rs)", "Info"};
                if (categoryComboBox.getSelectedItem() == "All") {
                    Object[][] productDataArray = productsArrayListToObjectArray("All"); // Call out the method to keep filter the values
                    midTable.setModel(new DefaultTableModel(productDataArray, columnNames)); // Set the table according to chosen category
                } else if (categoryComboBox.getSelectedItem() == "Electronics") {
                    Object[][] productDataArray = productsArrayListToObjectArray("Electronics");
                    midTable.setModel(new DefaultTableModel(productDataArray, columnNames));
                } else {
                    Object[][] productDataArray = productsArrayListToObjectArray("Clothing");
                    midTable.setModel(new DefaultTableModel(productDataArray, columnNames));
                }
            }
        }
    }

    public class mouseClickedOnProduct extends MouseAdapter {
        // To Display Product Details after select
        @Override
        public void mouseClicked(MouseEvent e) {
            String detail = "";
            int row = midTable.getSelectedRow(); // Get the selected row number
            String productId = (String) midTable.getValueAt(row, 0); // Get the ID of selected row
            for (Product currentProduct : productList) { // Choose the similar product
                if (Objects.equals(currentProduct.get_productId(), productId) && currentProduct instanceof Electronics) {
                    selectedProduct=currentProduct; // Save selected product to if it wants to add to cart
                    detail = "Product Id :" + currentProduct.get_productId() + "\n" +
                            "Category :" + " Electronic\n" + "Name :" + currentProduct.get_productName() +
                            "\nBrand :" + ((Electronics) currentProduct).get_electronicBrand() +
                            "\nWarranty :" + ((Electronics) currentProduct).get_electronicWarrantyPeriod() + " Months" +
                            "\nItems Available :" + currentProduct.get_productAddingAmount();
                    break;
                } else if (Objects.equals(currentProduct.get_productId(), productId) && currentProduct instanceof Clothing) {
                    selectedProduct=currentProduct; // Save selected product to if it wants to add to cart
                    detail = "Product Id :" + currentProduct.get_productId() + "\n" +
                            "Category :" + " Electronic\n" + "Name :" + currentProduct.get_productName() +
                            "\nSize :" + ((Clothing) currentProduct).get_clothingSize() +
                            "\nColor :" + ((Clothing) currentProduct).get_clothingSize() + "\"" +
                            "\nItems Available :" + currentProduct.get_productAddingAmount();
                    break;
                }
            }
            productDetailTextArea.setText(detail); // Display All Details
            productDetailTextArea.setEditable(false);
        }
    }

    //shoppingCart window manipulation

    public class viewShoppingCartActionListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(e.getSource()==viewCartButton){
                //Make the shoppingCart instance visible
                shoppingCart.setSize(400,500);
                shoppingCart.setVisible(true);
            }
        }
    }

    public class addToCart implements ActionListener{
        public void actionPerformed(ActionEvent e){
            if(e.getSource()==addToCartBtn){

                // update the table model
                selectedProductsToCart.add(selectedProduct);
                shoppingCart.setProductsOnCart(selectedProductsToCart);
            }
        }
    }
}
