package com.presentation;

import com.business.*;
import com.persistent.User;
import com.persistent.WorkItem;
import com.presentation.MainUserInterface;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.text.SimpleDateFormat;

public class LoginView extends JPanel{

    public static TeamManager teamManager = new TeamManager();
    public static UserManager userManager = new UserManager();

    //public static JPanel loginScreenPanel = new JPanel();
    public static JFrame loginScreenFrame;
    public static JTextField userNameTextField= new JTextField();
    public static JPasswordField passwordPasswordField= new JPasswordField();
    //public static JCheckBox showPasswordCheckBox=new JCheckBox("Show password");
    //public static JButton loginButton=new JButton("Login");
   // JLabel userNameLabel = new JLabel("<html><span style='font-size:12px'>"+"User Name:"+"</span></html>");
    //JLabel passwordLabel = new JLabel("<html><span style='font-size:12px'>"+"Password:"+"</span></html>");

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginView frame = new LoginView();
            }
        });
    }

    public LoginView() throws HeadlessException{
        loginScreenFrame = new JFrame("Login");
        loginScreenFrame.setResizable(false);
        loginScreenFrame.setSize(400,300);
        loginScreenFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginScreenFrame.setVisible(true);
        loginScreenFrame.setLayout(null);
        setLayout();
    }

    public void setLayout(){
        //user name Label
        JLabel userNameLabel = new JLabel("<html><span style='font-size:12px'>"+"User Name:"+"</span></html>");
        userNameLabel.setBounds(50,50,180,40);
        loginScreenFrame.add(userNameLabel);

        //user name TextField
        userNameTextField.setBounds(150,55,180,30);
        userNameTextField.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        loginScreenFrame.add(userNameTextField);

        //password Label
        JLabel passwordLabel = new JLabel("<html><span style='font-size:12px'>"+"Password:"+"</span></html>");
        passwordLabel.setBounds(50,100,180,40);
        loginScreenFrame.add(passwordLabel);

        //password TextField
        passwordPasswordField.setBounds(150,106,180,30);
        passwordPasswordField.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        loginScreenFrame.add(passwordPasswordField);

        //showPasswordCheckBox
        showPasswordCheckBox(loginScreenFrame);

        //login Button
        loginButton(loginScreenFrame);

        //add background
        Insets insets = loginScreenFrame.getInsets();
        ImageIcon backIcon = new ImageIcon("src/com/presentation/images/background.png");
        JLabel background = new JLabel("", backIcon, JLabel.RIGHT);
        loginScreenFrame.add(background);
        background.setBounds(insets.left , insets.top - 35, 1000, 600);

        add(loginScreenFrame);
    }

    /*public void actionPerformed(ActionEvent login) {

        Object action = login.getSource();

        if(action == loginButton) {
            String userName = userNameTextField.getText();
            String password = String.valueOf(passwordPasswordField.getPassword());
            if (userName.length() == 0)
                JOptionPane.showMessageDialog(loginScreenFrame, "Please fill User Name field");
            else {
                if (password.length() == 0)
                    JOptionPane.showMessageDialog(loginScreenFrame, "Please fill Password field");
                else{
                    if (userManager.login(userName,password)==2) {
                        JOptionPane.showMessageDialog(loginScreenFrame, "Invalid password");
                    }else if (userManager.login(userName,password)==1){
                        JComponent comp = (JComponent) login.getSource();
                        Window win = SwingUtilities.getWindowAncestor(comp);
                        win.dispose();
                        MainUserInterface frame = new MainUserInterface();
                        MainUserInterface.mainFrame.setVisible(true);
                    }else
                        JOptionPane.showMessageDialog(loginScreenFrame, "User not exist");
                }
            }
        }
    }*/

    public static void loginButton(JFrame loginScreenFrame){

        JButton loginButton=new JButton("Login");

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {

                //Object action = actionEvent.getSource();
                String command = actionEvent.getActionCommand();
                if (command.equals("Login")) {
                    String userName = userNameTextField.getText();
                    String password = String.valueOf(passwordPasswordField.getPassword());
                    if (userName.length() == 0)
                        JOptionPane.showMessageDialog(loginScreenFrame, "Please fill User Name field");
                    else {
                        if (password.length() == 0)
                            JOptionPane.showMessageDialog(loginScreenFrame, "Please fill Password field");
                        else {
                            if (userManager.login(userName, password) == 2) {
                                JOptionPane.showMessageDialog(loginScreenFrame, "Invalid password");
                            } else if (userManager.login(userName, password) == 1) {
                                JComponent comp = (JComponent) actionEvent.getSource();
                                Window win = SwingUtilities.getWindowAncestor(comp);
                                win.dispose();
                                MainUserInterface frame = new MainUserInterface();
                                MainUserInterface.mainFrame.setVisible(true);
                            } else
                                JOptionPane.showMessageDialog(loginScreenFrame, "User not exist");
                        }
                    }
                }
            }
        });
        loginButton.setBounds(190,180,90,40);
        loginButton.setFont(new Font(loginButton.getFont().getName(), Font.BOLD, 16));
        loginScreenFrame.add(loginButton);
    }

    public static void showPasswordCheckBox(JFrame loginScreenFrame){
        JCheckBox showPasswordCheckBox=new JCheckBox("Show password");

        showPasswordCheckBox.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent actionEvent) {

                if (showPasswordCheckBox.isSelected())
                    passwordPasswordField.setEchoChar((char)0);
                else
                    passwordPasswordField.setEchoChar('*');
            }
        });
        showPasswordCheckBox.setBounds(150,140,180,30);
        loginScreenFrame.add(showPasswordCheckBox);
    }
}