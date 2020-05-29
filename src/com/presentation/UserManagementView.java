package com.presentation;

import com.persistent.User;
import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

public class UserManagementView extends JFrame{

    public JPanel usersScreenViewPanel= new JPanel();
    public static User foundUser; //User that selected in user table
    private DefaultTableModel model = new DefaultTableModel(0,0);
    private JTable usersTable = new JTable(model);
    private JScrollPane jScrollPane = new JScrollPane(usersTable);

    JButton addUser,editUser,removeUser,cancelButton;

    ImageIcon backIcon = new ImageIcon("src/com/presentation/images/background.png");
    JLabel background = new JLabel("", backIcon, JLabel.RIGHT);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                UserManagementView frame = new UserManagementView("User Management Area");
            }
        });
    }

    public UserManagementView(String title) throws HeadlessException {
        setTitle(title);
        setResizable(false);
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);
        usersScreenViewPanel.setLayout(null);
        setContentPane(usersScreenViewPanel);
        setLayout();
    }

    public void setLayout() {
        Insets insets = usersScreenViewPanel.getInsets();

        //add buttons
        setLayoutButtons();

        //create user table
        usersTableCreated();

        //add background
        usersScreenViewPanel.add(background);
        background.setBounds(insets.left , insets.top - 35, 1000, 600);

       add(usersScreenViewPanel);
    }

    public void setLayoutButtons() {
        addUser = new JButton("Add New User");
        addUser.setBounds(760,200,160,40);
        addUser.setFont(new Font(addUser.getFont().getName(), Font.BOLD, 16));
        usersScreenViewPanel.add(addUser);

        editUser= new JButton("Edit User");
        editUser.setBounds(760,250,160,40);
        editUser.setFont(new Font(editUser.getFont().getName(), Font.BOLD, 16));
        usersScreenViewPanel.add(editUser);

        removeUser= new JButton("Remove User");
        removeUser.setBounds(760,300,160,40);
        removeUser.setFont(new Font(removeUser.getFont().getName(), Font.BOLD, 16));
        usersScreenViewPanel.add(removeUser);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(760,350,160,40);
        cancelButton.setFont(new Font(removeUser.getFont().getName(), Font.BOLD, 16));
        usersScreenViewPanel.add(cancelButton);

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String command = actionEvent.getActionCommand();
                if (command.equals("Add New User")) {
                    JComponent comp = (JComponent) actionEvent.getSource();
                    Window win = SwingUtilities.getWindowAncestor(comp);
                    win.dispose();
                    UserView uv = new UserView(command);
                    UserView.userViewFrame.setVisible(true);
                }

                else {
                    if (command.equals("Edit User")) {
                        if (getSelectedRow()) {
                            JComponent comp = (JComponent) actionEvent.getSource();
                            Window win = SwingUtilities.getWindowAncestor(comp);
                            win.dispose();
                            UserView uv=new UserView(command);
                            UserView.userViewFrame.setVisible(true);
                        }
                        else
                            JOptionPane.showMessageDialog(usersScreenViewPanel, "Please select user from the table");
                    }
                    else {
                        //command.equals("Remove User")
                        if (command.equals("Remove User")) {
                            if (getSelectedRow()){
                                switch (LoginView.userManager.removeUser(foundUser.getUserName())) {
                                    case 1:
                                        JOptionPane.showMessageDialog(usersScreenViewPanel, "User " + foundUser.getUserName() + " deleted");
                                        JComponent comp = (JComponent) actionEvent.getSource();
                                        Window win = SwingUtilities.getWindowAncestor(comp);
                                        win.dispose();
                                        UserManagementView uv=new UserManagementView(command);
                                        //UserManagementView.usersScreenViewPanel.setVisible(true);
                                        break;
                                    case 2:
                                        JOptionPane.showMessageDialog(usersScreenViewPanel, "Action no permitted");
                                        break;
                                    case 3:
                                        JOptionPane.showMessageDialog(usersScreenViewPanel, "Invalid to edit admin user");
                                        break;
                                    case 4:
                                        JOptionPane.showMessageDialog(usersScreenViewPanel, "User can't remove himself");
                                        break;
                                }
                            }
                            else
                                JOptionPane.showMessageDialog(usersScreenViewPanel, "Please select user from the table");
                        }

                        //command.equals("Cancel")
                        else {
                            if (command.equals("Cancel")) {
                                JComponent comp = (JComponent) actionEvent.getSource();
                                Window win = SwingUtilities.getWindowAncestor(comp);
                                win.dispose();
                                MainUserInterface mu = new MainUserInterface();
                            }
                        }

                    }
                }
            }
        };
         addUser.addActionListener(actionListener);
         editUser.addActionListener(actionListener);
         removeUser.addActionListener(actionListener);
         cancelButton.addActionListener(actionListener);
    }

    public void usersTableCreated() {
        Insets insets = usersScreenViewPanel.getInsets();

        //Prepare table data
        model.setRowCount(0);
        model.setColumnCount(0);
        String[] columnNames = {"User Name", "Permission Level", "Team"};
        for (String col : columnNames) //adding columns
            model.addColumn(col);
        for (Map.Entry<String,User> entry : LoginView.userManager.users.entrySet()) { //adding rows
            if (!(entry.getValue().getUserName().equals("admin")))
                model.addRow(new Object[]{entry.getValue().getUserName(), entry.getValue().getPermissionLevel(), entry.getValue().getTeam().getTeamsName()});
        }
        usersTable.setDefaultEditor(Object.class, null);

//      show on UI
        JLabel allUsersLabel = new JLabel("All Users");
        allUsersLabel.setVisible(true);
        Dimension size = allUsersLabel.getPreferredSize();
        allUsersLabel.setBounds(insets.left + 20 , insets.top + 40, size.width , size.height);
        usersScreenViewPanel.add(allUsersLabel);
        allUsersLabel.setFont(new Font(allUsersLabel.getFont().getName(), Font.BOLD, 12));
        allUsersLabel.setForeground(new Color(0,49,82));

        usersTable.setDefaultEditor(Object.class, null);
        Dimension tableSize = usersTable.getPreferredSize();
        usersTable.getTableHeader().setBackground(Color.WHITE);
        usersTable.getTableHeader().setForeground(new Color(0,49,82));

        usersScreenViewPanel.add(jScrollPane).setBounds(insets.left + 20,insets.top + 60,700,400);

        // Resize Columns
        final TableColumnModel columnModel = usersTable.getColumnModel();
        for (int column = 0; column < usersTable.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < usersTable.getRowCount(); row++) {
                TableCellRenderer renderer = usersTable.getCellRenderer(row, column);
                Component comp = usersTable.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width , width);
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }

    public boolean getSelectedRow(){
        int row=usersTable.getSelectedRow();
        if (row!=-1){
            String val = (String) usersTable.getValueAt(row,0);
            foundUser=LoginView.userManager.users.get(val);
            return true;
        }

        return false;
    }
}

