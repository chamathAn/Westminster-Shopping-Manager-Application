import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class User extends JFrame implements Serializable {
    private String userName="";
    private String name="";
    private String password="";
    private JButton signUp;
    private JButton signIn;
    private JFrame signUpFrame;
    private JButton createAccBtn;
    private JTextField userNameTextFieldSignUp;
    private JTextField nameTextFieldSignUp;
    private JTextField passwordTextFieldSignUp;

    private JTextField userNameTextFieldSignIn;
    private JTextField nameTextFieldSignIn;
    private JTextField passwordTextFieldSignIn;
    private ArrayList<Product>productList;
    private JFrame signInFrame;
    private int userLoginCount;
    private JButton logInToAccBtn;

    private static List<User> userAccounts =new ArrayList<>();

    public JTextField getPasswordTextFieldSignUp() {
        return passwordTextFieldSignUp;
    }

    public void setPasswordTextFieldSignUp(JTextField passwordTextFieldSignUp) {
        this.passwordTextFieldSignUp = passwordTextFieldSignUp;
    }


    // User constructor, getters and setters
    public User() {

        //Load saved account details
        File file = new File(System.getProperty("user.dir") + "/saveData/userAccounts.csv");
        if (file.exists()) {
            // Read user accounts from the file
            try (ObjectInputStream reader = new ObjectInputStream(new FileInputStream(file))) {
                userAccounts = (ArrayList<User>) reader.readObject();
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println(userAccounts);
        // User account main panel
        signUp = new JButton("Sign Up");
        signUp.setPreferredSize(new Dimension(300, 40));
        JPanel signUpBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signUpBtnPanel.add(signUp);
        JPanel signUpBtnMainPanel = new JPanel(new BorderLayout());
        signUpBtnMainPanel.add(signUpBtnPanel, BorderLayout.SOUTH);

        signIn = new JButton("Sign In");
        signIn.setPreferredSize(new Dimension(300, 40));
        JPanel signInBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        signInBtnPanel.add(signIn);
        JPanel signInBtnMainPanel = new JPanel(new BorderLayout());
        signInBtnMainPanel.add(signInBtnPanel, BorderLayout.NORTH);

        JPanel mainPanel = new JPanel(new GridLayout(2, 1));
        mainPanel.add(signUpBtnMainPanel);
        mainPanel.add(signInBtnMainPanel);

        JLabel title = new JLabel("<html><span style='color: blue;'>WELCOME TO WESTMINSTER SHOPPING CENTER</span></html>");
        title.setFont(title.getFont().deriveFont(30.0f));
        JPanel titlePane = new JPanel(new FlowLayout(FlowLayout.CENTER));
        titlePane.add(title);
        this.add(mainPanel, BorderLayout.CENTER);
        this.add(titlePane, BorderLayout.NORTH);

        //actionListeners for signUp frame open
        signUp.addActionListener(new openSignUpClass());
        openSignUp();

        //actionListeners for signIn frame open
        signIn.addActionListener(new openSignInClass());
        openSignIn();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    public int getUserLoginCount() {
        return userLoginCount;
    }

    public void setUserLoginCount(int userLoginCount) {
        this.userLoginCount = userLoginCount;
    }

    public ArrayList<Product> getProductList() {
        return productList;
    }

    public void setProductList(ArrayList<Product> productList) {
        this.productList = productList;
    }
    private class openSignUpClass implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == signUp) {
                signUpFrame.setVisible(true);
                userNameTextFieldSignUp.setText(""); // Make fields empty after creation
                nameTextFieldSignUp.setText("");
                passwordTextFieldSignUp.setText("");
            }
        }
    }

    private void openSignUp() {
        // Create signUp frame
        signUpFrame = new JFrame();
        signUpFrame.setSize(400, 500);

        // Fixed the frame
        Rectangle rc = signUpFrame.getBounds();
        rc.width = 400; //set max width
        rc.height = 500; // set max height
        signUpFrame.setMaximizedBounds(rc);

        // Pane and Layout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints contentCell = new GridBagConstraints();

        // Content
        JLabel title = new JLabel("<html><span style='color: Blue;'>Register</span></html>");
        title.setFont(title.getFont().deriveFont(50.0f));
        contentCell.anchor = GridBagConstraints.PAGE_START;
        contentCell.gridwidth = 2;
        contentCell.gridx = 0;
        contentCell.gridy = 0;
        contentCell.weighty = 0.2;
        mainPanel.add(title, contentCell);
        contentCell.weighty = 0;

        JLabel userNameLabel = new JLabel("User Name :");
        contentCell.insets = new Insets(0, 0, 20, 0);
        contentCell.anchor = GridBagConstraints.LINE_END;
        contentCell.gridwidth = 1;
        contentCell.gridy = 1;
        mainPanel.add(userNameLabel, contentCell);

        JLabel NameLabel = new JLabel("Full Name :");
        contentCell.gridy = 2;
        contentCell.gridx = 0;
        mainPanel.add(NameLabel, contentCell);

        JLabel passwordLabel = new JLabel("Password :");
        contentCell.gridy = 3;
        contentCell.gridx = 0;
        mainPanel.add(passwordLabel, contentCell);


        userNameTextFieldSignUp = new JTextField(20);
        contentCell.insets = new Insets(0, 20, 20, 0);
        contentCell.anchor = GridBagConstraints.LINE_START;
        contentCell.fill = GridBagConstraints.NONE;
        contentCell.gridy = 1;
        contentCell.gridx = 1;
        mainPanel.add(userNameTextFieldSignUp, contentCell);

        nameTextFieldSignUp = new JTextField(20);
        contentCell.gridx = 1;
        contentCell.gridy = 2;
        mainPanel.add(nameTextFieldSignUp, contentCell);

        passwordTextFieldSignUp = new JTextField(20);
        contentCell.gridx = 1;
        contentCell.gridy = 3;
        mainPanel.add(passwordTextFieldSignUp, contentCell);

        createAccBtn = new JButton("Create Account");
        contentCell.anchor = GridBagConstraints.FIRST_LINE_START;
        contentCell.gridwidth = 2;
        contentCell.gridx = 0;
        contentCell.gridy = 4;
        contentCell.weighty = 5;
        contentCell.insets = new Insets(0, 170, 0, 0);
        mainPanel.add(createAccBtn, contentCell);

        signUpFrame.add(mainPanel);

        createAccBtn.addActionListener(new accCreation());
    }

    public class accCreation implements ActionListener {
        // Event handler for account creation
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == createAccBtn) {
                userName = userNameTextFieldSignUp.getText();
                name = nameTextFieldSignUp.getText();
                password = passwordTextFieldSignUp.getText();
                if (Objects.equals(userName, "") || Objects.equals(name, "") || Objects.equals(password, "")) {
                    JOptionPane.showConfirmDialog(null, "Fill the all fields to create account", "Empty Field", JOptionPane.DEFAULT_OPTION);
                }else {

                    JOptionPane.showConfirmDialog(null, "Your Account Successfully Created", "Account Create", JOptionPane.DEFAULT_OPTION);
                    userLoginCount=0;
                    User user = new User();
                    user.setUserName(userName); // Set user userName
                    user.setName(name); // Set name
                    user.setPassword(password); // Set user password
                    userAccounts.add(user); // Add the created user account to ArrayList

                    // Save account details
                    try (ObjectOutputStream writer = new ObjectOutputStream(new FileOutputStream(System.getProperty("user.dir") + "/saveData/userAccounts.csv"))) {
                        writer.writeObject(userAccounts);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }


    // For the log in

    private class openSignInClass implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == signIn) {
                signInFrame.setVisible(true);
                userNameTextFieldSignUp.setText(""); // Make fields empty after creation
                passwordTextFieldSignUp.setText("");
            }
        }
    }
    private void openSignIn() {
        // Create signUp frame
        signInFrame = new JFrame();
        signInFrame.setSize(400, 500);

        // Fixed the frame
        Rectangle rc = signInFrame.getBounds();
        rc.width = 400; //set max width
        rc.height = 500; // set max height
        signInFrame.setMaximizedBounds(rc);

        // Pane and Layout
        JPanel mainPanel = new JPanel(new GridBagLayout());
        GridBagConstraints contentCell = new GridBagConstraints();

        // Content
        JLabel title = new JLabel("<html><span style='color: Blue;'>Log In</span></html>");
        title.setFont(title.getFont().deriveFont(50.0f));
        contentCell.anchor = GridBagConstraints.PAGE_START;
        contentCell.gridwidth = 2;
        contentCell.gridx = 0;
        contentCell.gridy = 0;
        contentCell.weighty = 0.2;
        mainPanel.add(title, contentCell);
        contentCell.weighty = 0;

        JLabel userNameLabel = new JLabel("User Name :");
        contentCell.insets = new Insets(0, 0, 20, 0);
        contentCell.anchor = GridBagConstraints.LINE_END;
        contentCell.gridwidth = 1;
        contentCell.gridy = 1;
        mainPanel.add(userNameLabel, contentCell);

        JLabel passwordLabel = new JLabel("Password :");
        contentCell.gridy = 2;
        contentCell.gridx = 0;
        mainPanel.add(passwordLabel, contentCell);


        userNameTextFieldSignIn = new JTextField(20);
        contentCell.insets = new Insets(0, 20, 20, 0);
        contentCell.anchor = GridBagConstraints.LINE_START;
        contentCell.fill = GridBagConstraints.NONE;
        contentCell.gridy = 1;
        contentCell.gridx = 1;
        mainPanel.add(userNameTextFieldSignIn, contentCell);

        passwordTextFieldSignIn = new JTextField(20);
        contentCell.gridx = 1;
        contentCell.gridy = 2;
        mainPanel.add(passwordTextFieldSignIn, contentCell);

        logInToAccBtn = new JButton("Log In");
        contentCell.anchor = GridBagConstraints.FIRST_LINE_START;
        contentCell.gridwidth = 2;
        contentCell.gridx = 0;
        contentCell.gridy = 4;
        contentCell.weighty = 5;
        contentCell.insets = new Insets(0, 222, 0, 0);
        mainPanel.add(logInToAccBtn, contentCell);

        signInFrame.add(mainPanel);

        logInToAccBtn.addActionListener(new accountSignIn());
    }

    public class accountSignIn implements ActionListener{
        public void actionPerformed(ActionEvent e){
            // Control all sign in
            if (e.getSource()==logInToAccBtn){
                userName= userNameTextFieldSignIn.getText();
                password= passwordTextFieldSignIn.getText();
                if (Objects.equals(userName, "") || Objects.equals(password, "")) { // Check empty fields
                    JOptionPane.showConfirmDialog(null, "Fill the all fields to log in to account", "Empty Field", JOptionPane.DEFAULT_OPTION);
                }else if(userAccounts.stream().anyMatch(user -> Objects.equals(user.getUserName(), userName))&&
                        userAccounts.stream().anyMatch(user -> Objects.equals(user.getPassword(), password))){ // Check the account availability and Open

                        //Open shopping Center
                        UserGUI gui = new UserGUI(productList);
                        gui.setSize(400,500);
                        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        gui.setVisible(true);

                }else { // Show notification if account doesn't found
                    JOptionPane.showConfirmDialog(null,"Account Not Found","Account Error", JOptionPane.DEFAULT_OPTION);
                }
            }
        }
    }
}
