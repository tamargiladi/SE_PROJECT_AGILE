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

public class LoginView extends JFrame implements ActionListener {

    public static TeamManager teamManager = new TeamManager();
    public static UserManager userManager = new UserManager();

    JPanel loginScreenPanel = new JPanel();
    JFrame loginScreenFrame= new JFrame();
    JLabel usersNameLabel = new JLabel("<html><span style='font-size:12px'>"+"User Name:"+"</span></html>");
    JTextField userNameTextField= new JTextField();
    JLabel passwordLabel = new JLabel("<html><span style='font-size:12px'>"+"Password:"+"</span></html>");
    JTextField passwordTextField= new JTextField();
    JButton loginButton=new JButton("Login");



    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginView frame = new LoginView();
            }
        });
    }

    public LoginView() throws HeadlessException{
        super("Login");
        setLayout();
    }

    public void setLayout(){
        Insets insets = loginScreenFrame.getInsets();
        setSize(400,250);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        usersNameLabel.setBounds(50,50,180,40);
        loginScreenPanel.add(usersNameLabel);
        userNameTextField.setBounds(150,55,180,30);
        userNameTextField.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        loginScreenPanel.add(userNameTextField);
        passwordLabel.setBounds(50,100,180,40);
        loginScreenPanel.add(passwordLabel);
        passwordTextField.setBounds(150,106,180,30);
        passwordTextField.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        loginScreenPanel.add(passwordTextField);
        loginButton.setBounds(190,150,90,40);
        loginButton.setFont(new Font(loginButton.getFont().getName(), Font.BOLD, 16));
        loginScreenPanel.add(loginButton);
        loginButton.addActionListener(this);

        loginScreenPanel.setLayout(null);
        add(loginScreenPanel);

        //add background
        ImageIcon backIcon = new ImageIcon("src/com/presentation/images/background.png");
        JLabel background = new JLabel("", backIcon, JLabel.RIGHT);
        loginScreenPanel.add(background);
        background.setBounds(insets.left , insets.top - 35, 1000, 600);
    }

    public void actionPerformed(ActionEvent login) {

        Object action = login.getSource();

        if(action == loginButton) {
            String userName = userNameTextField.getText();
            String password = passwordTextField.getText();
            if (userName.length() == 0)
                JOptionPane.showMessageDialog(loginScreenFrame, "Please fill User Name field");
            else {
                if (password.length() == 0)
                    JOptionPane.showMessageDialog(loginScreenFrame, "Please fill Password field");
                else{
                    if (userManager.login(userName,password)){
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
    }


}