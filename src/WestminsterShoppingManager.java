import javax.swing.*;
import java.io.*;
import java.util.*;

public class WestminsterShoppingManager implements ShoppingManager {
    private static Boolean continueProcess = true;
    private static int totalProductsQuantity = 0;
    private static final int fullStock = 50;
    private static Set<Product> _productSet = new TreeSet<>(); //To Store Products
    private static final Scanner input = new Scanner(System.in); //To Get User Inputs

    public static Set<Product> get_productSet() {
        return _productSet;
    }

    public static void set_productSet(Set<Product> _productSet) {
        WestminsterShoppingManager._productSet = _productSet;
    }

    public void toDisplay() throws InterruptedException {

        // Use thread to fast implementation
        Thread thread = new Thread(() -> loadFromFile()); // Load pre-saved date
        thread.start();
        thread.join();


        do {
            System.out.println("""
                    ===================================================================
                                     ONLINE SHOPPING MANAGEMENT SYSTEM
                    ===================================================================
                                    
                    01. Add Product
                    02. Delete Product
                    03. Print the List of Products
                    04. Save in a file
                    05. Customer GUI
                    """);
            System.out.print("Option:");
            if (!input.hasNext("[1-5]")) { // return since invalid input
                System.out.println("Try Again with correct input");
                input.nextLine();
                continue;
            }
            int mainMenuNumber = input.nextInt();
            switch (mainMenuNumber) {
                case 1:
                    addNewProduct();
                    break;
                case 2:
                    deleteProduct();
                    break;
                case 3:
                    printProducts();
                    break;
                case 4:
                    writeFile();
                    break;
                case 5:
                    userGui();
                    break;
                default:
                    System.out.println("Try Again!");
                    break;
            }

        } while (continueProcess);
    }
    @Override
    public void addNewProduct() {

        //To add products
        System.out.print("""
                ------------------------------
                ADD PRODUCT
                                
                Choose the product
                    01. Electronics
                    02. Clothing\s
                option:""");

        if (!input.hasNext("[1-2]")) { // return since invalid input
            System.out.println("Try Again with correct input");
            input.nextLine();
            return;
        }
        int productNumber = input.nextInt();
        input.nextLine();

        switch (productNumber) {
            case 1:
                makeElectronicProduct();
                break;
            case 2:
                makeClothingProduct();
                break;
            default:
                System.out.println("Try Again!");
                break;
        }
    }
    @Override
     public void deleteProduct() {
        //To delete products
        input.nextLine();
        System.out.print("""
                ------------------------------
                DELETE PRODUCT

                Product ID you want to delete:""");

        String productId = input.nextLine();
        countProductTotalAmount(); // Count the product quantity
        int leftQuantity = fullStock - totalProductsQuantity;
        totalProductsQuantity=0;
        // Iterating the products and delete
        Iterator<Product> productIterator = _productSet.iterator();

        boolean productFound = false;
        while (productIterator.hasNext()) {
            Product currentProduct = productIterator.next();
            if (productId.equals(currentProduct.get_productId())) {
                productFound = true;
                productIterator.remove();
                System.out.println(currentProduct + " is Deleted");
                System.out.println("Remaining Stock :" + (leftQuantity + currentProduct.get_productAddingAmount()));
                break;
            }
        }
        if (!productFound) {
            System.out.println("Not ID Found!");
            return;
        }

    }

    private  void makeElectronicProduct() {
        //Make Electronic obj and add to treeSet
        System.out.print("Product ID:");
        String _productId = input.nextLine();
        for (Product currentProduct : _productSet) {
            while (_productId.equals(currentProduct.get_productId())) {
                System.out.println("Product ID is already exists, Try Again!");
                return;
            }
        }
        System.out.print("Product Name:");
        if (!input.hasNext("[a-z A-Z]+")) {
            System.out.println("Use Only Letters For The Name!");
            input.nextLine();
            return;
        }
        String _productName = input.nextLine();
        System.out.print("Product Adding Amount:");
        if (!input.hasNext("[0-9]+")) {
            System.out.println("Use Only Numbers For The Amount!");
            input.nextLine();
            return;
        }
        int _productAddingAmount = input.nextInt();
        while (_productAddingAmount > 50) { // Check the amount if it is exceeding
            System.out.print("""
                    Amount is beyond the stock limit, Please enter a amount less than 50
                    Amount:""");
            _productAddingAmount = input.nextInt();
        }
        System.out.print("Product Price:");
        if (!input.hasNext("[0-9]+")) {
            System.out.println("Use Only Numbers For The price!");
            input.nextLine();
            return;
        }
        int _productPrice = input.nextInt();
        System.out.print("Electronic Brand:");
        if (!input.hasNext("[a-z A-Z]+")) {
            System.out.println("Use Only Letters For The Brand!");
            input.nextLine();
            return;
        }
        input.nextLine();
        String _electronicBrand = input.nextLine();

        System.out.print("Warranty Period (Months):");
        if (!input.hasNext("[0-9]+")) {
            System.out.println("Use Only Numbers For The price!");
            input.nextLine();
            return;
        }
        int _electronicWarranty = input.nextInt();
        // Check for the quantity in stock
        countProductTotalAmount();
        if (totalProductsQuantity > 50) {
            totalProductsQuantity=0;
            System.out.println("Stock reaches to its limit 50, Can't add the product!");
            return;
        }
        totalProductsQuantity=0;
        Product electronicObj = new Electronics(_productId, _productName, _productAddingAmount, _productPrice, _electronicBrand, _electronicWarranty); // Creating elect obj
        _productSet.add(electronicObj); // Add product to treeSet
        System.out.println(electronicObj.get_productName() + " Product Added Successfully!");
    }

    private  void makeClothingProduct() {
        //Make Clothing obj and add to treeSet
        System.out.print("Product ID:");
        String _productId = input.nextLine();
        System.out.print("Product Name:");
        if (!input.hasNext("[a-z A-Z]+")) {
            System.out.println("Use Only Letters For The Name!");
            input.nextLine();
            return;
        }
        String _productName = input.nextLine();
        System.out.print("Product Adding Amount:");
        if (!input.hasNext("[0-9]+")) {
            System.out.println("Use Only Numbers For The Amount!");
            input.nextLine();
            return;
        }
        int _productAddingAmount = input.nextInt();
        while (_productAddingAmount > 50) { // Check the amount if it is exceeding
            System.out.print("""
                    Amount is beyond the stock limit, Please enter a amount less than 50
                    Amount:""");
            _productAddingAmount = input.nextInt();
        }
        System.out.print("Product Price:");
        if (!input.hasNext("[0-9]+")) {
            System.out.println("Use Only Numbers For The price!");
            input.nextLine();
            return;
        }
        int _productPrice = input.nextInt();
        input.nextLine();
        System.out.print("Clothes Color:");
        if (!input.hasNext("[a-zA-Z]+")) {
            System.out.println("Use Only Letters For The Color!");
            return;
        }
        String _clothingColor = input.nextLine();
        System.out.print("Clothes Size:");
        if (!input.hasNext("[0-9]+")) {
            System.out.println("Use Only Numbers For The Size!");
            input.nextLine();
            return;
        }
        int _clothingSize = input.nextInt();
        // Check for the quantity in stock
        countProductTotalAmount();
        if (totalProductsQuantity > 50) {
            totalProductsQuantity=0; // Set 0 because when needs the total quantity, countProductTotalAmount() would be there
            System.out.println("Stock reaches to its limit 50, Can't add the product!");
            return;
        }
        totalProductsQuantity=0;
        Product clothingObj = new Clothing(_productId, _productName, _productAddingAmount, _productPrice, _clothingSize, _clothingColor); // Make clothing
        _productSet.add(clothingObj); // Add product to treeSet
        System.out.println(clothingObj.get_productName() + " Product Added Successfully!");
    }

    private  void countProductTotalAmount() {
        // Count the total number of products
        for (Product product : _productSet) {
            totalProductsQuantity += product.get_productAddingAmount();
        }
    }
    @Override
    public void printProducts() {
        // Print Products
        System.out.println("""
                ------------------------------
                PRINT PRODUCTS
                """);
        System.out.printf("%-17s%25s%25s\n", "Product ID       |", "Product Category   |", String.format("%" + (-14) + "s", "      Information      |" + "\n" +
                "-------------------------------------------------------------------"));
        for (Product currentProduct : _productSet) {
            if (currentProduct instanceof Electronics electronicProduct) {
                String productInfo = "Name - " + currentProduct.get_productName() + "\n" +
                        String.format("%42s", "") + "Amount - " + currentProduct.get_productAddingAmount() + "\n" +
                        String.format("%42s", "") + "Price - " + currentProduct.get_productPrice() + "\n" +
                        String.format("%42s", "") + "Brand - " + electronicProduct.get_electronicBrand() + "\n" +
                        String.format("%42s", "") + "Warranty - " + electronicProduct.get_electronicWarrantyPeriod() + " months" + "\n" +
                        "-------------------------------------------------------------------";
                System.out.printf("%-17s%-25s%25s\n", currentProduct.get_productId(), "Electronics", productInfo);
            } else if (currentProduct instanceof Clothing clothingProduct) {
                String productInfo = "Name - " + currentProduct.get_productName() + "\n" +
                        String.format("%42s", "") + "Amount - " + currentProduct.get_productAddingAmount() + "\n" +
                        String.format("%42s", "") + "Price - " + currentProduct.get_productPrice() + "\n" +
                        String.format("%42s", "") + "Color - " + clothingProduct.get_clothingColor() + "\n" +
                        String.format("%42s", "") + "Size - " + clothingProduct.get_clothingSize() + "\n" +
                        "-------------------------------------------------------------------";
                System.out.printf("%-17s%-25s%25s\n", currentProduct.get_productId(), "Clothing", productInfo);
            }
        }
    }
@Override
    public void writeFile() {
        //write into a file

        File file = new File(System.getProperty("user.dir") + "/saveData/productData.txt");
        try {
            FileWriter fileWriter = new FileWriter(file, true);
            for (Product product : _productSet) {
                if (product instanceof Electronics electronicProduct) {
                    fileWriter.write("Electronic\n");
                    fileWriter.write(product.get_productId() + "/");
                    fileWriter.write(product.get_productName() + "/");
                    fileWriter.write(product.get_productAddingAmount() + "/");
                    fileWriter.write(product.get_productPrice() + "/");
                    fileWriter.write(electronicProduct.get_electronicBrand() + "/");
                    fileWriter.write(electronicProduct.get_electronicWarrantyPeriod() + "\n");
                } else if (product instanceof Clothing clothingProduct) {
                    fileWriter.write("Clothing\n");
                    fileWriter.write(product.get_productId() + "/");
                    fileWriter.write(product.get_productName() + "/");
                    fileWriter.write(product.get_productAddingAmount() + "/");
                    fileWriter.write(product.get_productPrice() + "/");
                    fileWriter.write(clothingProduct.get_clothingSize() + "/");
                    fileWriter.write(clothingProduct.get_clothingColor() + "\n");
                }
            }
            fileWriter.flush();
            fileWriter.close();
        } catch (Exception ignored) {
        }

        System.out.println("Successfully Saved Data!");
    }

    private void loadFromFile() {
        // Load From File

        File file = new File(System.getProperty("user.dir") + "/saveData/productData.txt");
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String readLine;
            ArrayList<String> readLineList = new ArrayList<>();
            while ((readLine = reader.readLine()) != null) // Read & Add to ArrayList
            {
                readLineList.add(readLine);
            }
            for (int i = 0; i < readLineList.size(); i++) { // Go through the ArrayList
                if (readLineList.get(i).equals("Electronic")) {
                    String[] arr = readLineList.get(i + 1).split("/");
                    // Make the objects & saving in treeSet
                    _productSet.add(new Electronics(arr[0], arr[1], Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), arr[4], Integer.parseInt(arr[5])));
                } else if (readLineList.get(i).equals("Clothing")) {
                    String[] arr = readLineList.get(i + 1).split("/");
                    // Make the objects & saving in treeSet
                    _productSet.add(new Clothing(arr[0], arr[1], Integer.parseInt(arr[2]), Integer.parseInt(arr[3]), Integer.parseInt(arr[4]), arr[5]));
                }
            }
            System.out.println("Saved records loaded successfully...");
            reader.close();
        } catch (Exception ignored) {
        }
    }

    // GUI is starting from here
    private void userGui(){

        // Copying products to arraylist
        ArrayList<Product> productsList = new ArrayList<>(_productSet);

        // Main Configuration

        User user = new User();
        user.setSize(400,500);
        user.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        user.setVisible(true);
        user.setProductList(productsList);


//        UserGUI gui = new UserGUI(productsList);
//        gui.setSize(400,500);
//        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        gui.setVisible(true);
    }
}

