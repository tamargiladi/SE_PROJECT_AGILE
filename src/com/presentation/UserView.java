package com.presentation;

import com.persistent.User;

import java.lang.String;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserView extends JPanel {

    public static JFrame userViewFrame;
    public String action;
    public static JTextField userNameField, passwordField;
    public static JComboBox permissionLevelCombo, teamCombo;
    JLabel userNameLabel, passwordLabel, permissionLevelLabel, teamLabel;
    JButton saveButton, cancelButton;

    ImageIcon backIcon = new ImageIcon("src/com/presentation/images/background.png");
    JLabel background = new JLabel("", backIcon, JLabel.RIGHT);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UserView frame = new UserView("Add New User");
            }
        });
    }

    public UserView(String title) throws HeadlessException {
        action = title;
        userViewFrame = new JFrame(action);
        userViewFrame.setResizable(false);
        userViewFrame.setSize(450, 400);
        userViewFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        userViewFrame.setVisible(true);
        userViewFrame.setLayout(null);
        setLayout();
    }

    public void setLayout() {
        Insets insets = userViewFrame.getInsets();
        //Dimension size;

        //user name Label
        userNameLabel = new JLabel("<html><span style='font-size:12px'>" + "User Name:" + "</span></html>");
        //size = userNameLabel.getPreferredSize();
        //userNameLabel.setBounds(insets.left + 20 , insets.top + 20, size.width + 5, size.height);
        userNameLabel.setBounds(30, 50, 180, 40);
        userViewFrame.add(userNameLabel);

        //user name TextField
        userNameField = new JTextField();
        userNameField.setBounds(200, 55, 180, 30);
        userNameField.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        userViewFrame.add(userNameField);

        //password Label
        passwordLabel = new JLabel("<html><span style='font-size:12px'>" + "Password:" + "</span></html>");
        //size = passwordLabel.getPreferredSize();
        //passwordLabel.setBounds(insets.left + 20 , insets.top + 20, size.width + 5, size.height);
        passwordLabel.setBounds(30, 100, 180, 40);
        userViewFrame.add(passwordLabel);

        //password TextField
        passwordField = new JTextField();
        passwordField.setBounds(200, 106, 180, 30);
        passwordField.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        userViewFrame.add(passwordField);

        //permission level Label
        permissionLevelLabel = new JLabel("<html><span style='font-size:12px'>" + "Permission Level:" + "</span></html>");
        //size = permissionLevelLabel.getPreferredSize();
        //permissionLevelLabel.setBounds(insets.left + 20 , insets.top + 20, size.width + 5, size.height);
        permissionLevelLabel.setBounds(30, 157, 180, 40);
        userViewFrame.add(permissionLevelLabel);

        //permission level Combo
        permissionLevelCombo = new JComboBox(User.PermissionLevel.values());
        permissionLevelCombo.setBounds(200, 157, 180, 40);
        userViewFrame.add(permissionLevelCombo);

        //team Label
        teamLabel = new JLabel("<html><span style='font-size:12px'>" + "Team:" + "</span></html>");
        //size = teamLabel.getPreferredSize();
        //teamLabel.setBounds(insets.left + 20 , insets.top + 20, size.width + 5, size.height);
        teamLabel.setBounds(30, 208, 180, 40);
        userViewFrame.add(teamLabel);

        //team Combo
        teamCombo = new JComboBox(LoginView.teamManager.teams.keySet().toArray());
        teamCombo.setBounds(200, 208, 180, 40);
        userViewFrame.add(teamCombo);

        //Buttons
        setLayoutButtons();

        //set all values
        setValues();

        //add background
        userViewFrame.add(background);
        background.setBounds(insets.left, insets.top - 35, 1000, 600);

        add(userViewFrame);
    }

    public void setLayoutButtons() {
        saveButton = new JButton("Save");
        cancelButton = new JButton("Cancel");

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String command = actionEvent.getActionCommand();
                if (command.equals("Save")) {
                    String userName = userNameField.getText();
                    String password = passwordField.getText();
                    String teamName = (String) teamCombo.getModel().getSelectedItem();
                    User.PermissionLevel permissionLevel = (User.PermissionLevel) permissionLevelCombo.getModel().getSelectedItem();

                    if (action.equals("Add New User")) {

                        if (userName.length() == 0)
                            JOptionPane.showMessageDialog(userViewFrame, "Please fill User Name field");
                        else if (password.length() == 0)
                            JOptionPane.showMessageDialog(userViewFrame, "Please fill Password field");
                        else {
                            switch (LoginView.userManager.addUser(userName, password, permissionLevel, teamName)) {
                                case 1:
                                    JOptionPane.showMessageDialog(userViewFrame, "User successfully added");
                                    JComponent comp = (JComponent) actionEvent.getSource();
                                    Window win = SwingUtilities.getWindowAncestor(comp);
                                    win.dispose();
                                    UserManagementView uv = new UserManagementView("User Management Area");
                                    //uv.usersScreenViewPanel.setVisible(true);
                                    break;
                                case 2:
                                    JOptionPane.showMessageDialog(userViewFrame, "Action no permitted");
                                    break;
                                case 3:
                                    JOptionPane.showMessageDialog(userViewFrame, "Username already exist");
                                    break;
                            }
                        }
                    }
                    //action.equals("Edit User")
                    else {
                        switch (LoginView.userManager.updateUserPermission(userName, permissionLevel)) {
                            case 1:
                                LoginView.userManager.updateUserTeam(userName, teamName);
                                if (LoginView.userManager.isActionPermitted()){
                                    JOptionPane.showMessageDialog(userViewFrame, "User saved successfully");
                                    JComponent comp = (JComponent) actionEvent.getSource();
                                    Window win = SwingUtilities.getWindowAncestor(comp);
                                    win.dispose();
                                    UserManagementView uv = new UserManagementView("User Management Area");
                                }
                                //if no permission to loggedInUser
                                else{
                                    JOptionPane.showMessageDialog(userViewFrame, "User saved successfully");
                                    JComponent comp = (JComponent) actionEvent.getSource();
                                    Window win = SwingUtilities.getWindowAncestor(comp);
                                    win.dispose();
                                    MainUserInterface uv = new MainUserInterface();
                                }
                                break;
                            case 2:
                                JOptionPane.showMessageDialog(userViewFrame, "Action no permitted");
                                break;
                            case 3:
                                JOptionPane.showMessageDialog(userViewFrame, "Invalid to edit admin user");
                                break;
                        }
                    }
                }

                //command.equals("Cancel")=true
                else {
                    JComponent comp = (JComponent) actionEvent.getSource();
                    Window win = SwingUtilities.getWindowAncestor(comp);
                    win.dispose();
                    UserManagementView uv = new UserManagementView("User Management Area");
                    //uv.usersScreenViewPanel.setVisible(true);
                }
            }

        };

        //save button
        userViewFrame.add(saveButton);
        saveButton.setBounds(150, 270, 90, 40);
        saveButton.setFont(new Font(saveButton.getFont().getName(), Font.BOLD, 16));
        saveButton.addActionListener(actionListener);

        // cancel button
        userViewFrame.add(cancelButton);
        cancelButton.setBounds(250, 270, 90, 40);
        cancelButton.setFont(new Font(saveButton.getFont().getName(), Font.BOLD, 16));
        cancelButton.addActionListener(actionListener);
    }

    public void setValues() {
        //the action is "Add New User"
        if (action.equals("Add New User")){
            UserView.userNameField.setText("");
            UserView.passwordField.setText("");
            UserView.teamCombo.setSelectedItem(LoginView.teamManager.teams.get("default").getTeamsName());
            UserView.permissionLevelCombo.setSelectedItem(User.PermissionLevel.member);
        }

        //the action is "Edit User"
        else {
            UserView.userNameField.setText(UserManagementView.foundUser.getUserName());
            UserView.userNameField.setEnabled(false);
            UserView.passwordField.setText("*****");
            UserView.passwordField.setEnabled(false);
            UserView.teamCombo.setSelectedItem(UserManagementView.foundUser.getTeam().getTeamsName());
            UserView.permissionLevelCombo.setSelectedItem(UserManagementView.foundUser.getPermissionLevel());
        }

    }

}
