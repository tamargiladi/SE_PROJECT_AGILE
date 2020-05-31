package com.presentation;

import com.persistent.Team;
import com.persistent.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import static javax.swing.JOptionPane.showMessageDialog;

public class TeamManagerGUI extends JFrame {


    private JPanel listViewWindow = new JPanel();

    //Combo box variables
    public static JComboBox<String> comboTeamView = new JComboBox<>();
    JButton btnConfirm = new JButton("Confirm");

    //Table variables
    DefaultTableModel model = new DefaultTableModel(0, 0);
    JTable usersTable = new JTable(model);
    JScrollPane jScrollPane = new JScrollPane(usersTable);
    String str[];
    //public static User foundUser; //User that selected in user table


    //TeamManager variables&buttons
    JButton btnAddTeam = new JButton("Add Team");
    JButton btnRemoveTeam = new JButton("Remove team");//Add modfication that the team
    //  JButton btnMoveUser = new JButton("Move User");

    ImageIcon backIcon = new ImageIcon("src/com/presentation/images/background.png");
    JLabel background = new JLabel("", backIcon, JLabel.RIGHT);

    final int colSize = 2;


    //TODO: Remove the main...
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TeamManagerGUI tGUI = new TeamManagerGUI();
            }
        });
    }

    public TeamManagerGUI() {
        //=========jPanel initialization==============//
        Insets insets = listViewWindow.getInsets();
        setTitle("Team Manager");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        this.listViewWindow.setLayout(null);

       /* ImageIcon backIcon = new ImageIcon("src/com/presentation/images/background.png");
        JLabel background = new JLabel("", backIcon, JLabel.RIGHT);
        listViewWindow.add(background);
        background.setBounds(insets.left , insets.top - 35, 1000, 600);*/


        //======================================//

        generateTeamCombo();
        generateButtonConfirm();
        generateTable();

        //Generate TeamManager's buttons//
        generateButtonAddTeam();
        generateButtonRemoveTeam();
        //   generateButtonMove();


        listViewWindow.add(background);
        background.setBounds(insets.left, insets.top - 35, 1000, 600);

        add(listViewWindow);


    }

    private void generateTeamCombo() {
        Dimension size;
        Insets insets = listViewWindow.getInsets();



         comboTeamView.removeAllItems();
        generateStringArrayCombo();
        comboTeamView.setBounds(insets.left + 120, insets.top + 15, 250, 40);

        this.listViewWindow.add(comboTeamView);


    }

    private void generateStringArrayCombo() {
        int length = LoginView.teamManager.teams.size();
        ArrayList<String> listArr = new ArrayList<String>();
        str = new String[length];

        int ind = 0;
        for (Map.Entry<String, Team> me : LoginView.teamManager.teams.entrySet()) {
            if (!me.getKey().equals("default"))
                comboTeamView.addItem(me.getKey());
        }

      /*  for(int i=0;i<length;i++)//length-1 : because the 'default' group is not included.
            str[i]=listArr.get(i);*/
    }


    public void generateTable() {


        Insets insets = listViewWindow.getInsets();


        model.setRowCount(0);
        model.setColumnCount(0);
        String[] columnNames = {"Username", "Permission"};
        for (String col : columnNames)//adding columns
            model.addColumn(col);


        Iterator<User> it = getUsersListByCombo().iterator();
        Iterator<User> itPer = getUsersListByCombo().iterator();

        Random rnd = new Random();
        while (it.hasNext()) {
            model.addRow(new Object[]{it.next().getUserName(), itPer.next().getPermissionLevel().name()});
        }

        usersTable.setDefaultEditor(Object.class, null);


        usersTable.setDefaultEditor(Object.class, null);
        Dimension tableSize = usersTable.getPreferredSize();
        usersTable.getTableHeader().setBackground(Color.WHITE);
        usersTable.getTableHeader().setForeground(new Color(0, 49, 82));

        listViewWindow.add(jScrollPane).setBounds(insets.left + 20, insets.top + 100, 700, 400);


        final TableColumnModel columnModel = usersTable.getColumnModel();
        for (int column = 0; column < usersTable.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < usersTable.getRowCount(); row++) {
                TableCellRenderer renderer = usersTable.getCellRenderer(row, column);
                Component comp = usersTable.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width, width);
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }
    }


    public void generateButtonConfirm() {
        Insets insets = listViewWindow.getInsets();
        this.listViewWindow.add(btnConfirm);//adds to the interface

        //------actions-------//
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                generateTable();
            }
        };
        btnConfirm.addActionListener(actionListener);
        //------ end actions-------//


        btnConfirm.setFont(new Font(btnConfirm.getFont().getName(), Font.BOLD, 16));

        Dimension size = btnConfirm.getPreferredSize();

        size = btnConfirm.getPreferredSize();
        btnConfirm.setBounds(insets.left + 400, insets.top + 20, size.width + 5, size.height);


    }

    public List<User> getUsersListByCombo() {
        return LoginView.teamManager.teams.get(comboTeamView.getSelectedItem().toString()).getUsersList();
    }

    public void generateButtonAddTeam() {
        //Style & initialization//
        Insets insets = listViewWindow.getInsets();
        this.listViewWindow.add(btnAddTeam);

        Dimension size = btnAddTeam.getPreferredSize();
        size = btnAddTeam.getPreferredSize();


        //Actions//
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                TeamView tvAddTeam = new TeamView("Add Team");
                LoginView.teamManager.updateTeamsFile();


            }

        };
        btnAddTeam.addActionListener(actionListener);
        btnAddTeam.setBounds(insets.left + 800, insets.top + 100, size.width + 20, size.height);
        btnAddTeam.setFont(new Font(btnConfirm.getFont().getName(), Font.BOLD, 16));

    }

    public void generateButtonRemoveTeam() {
        //Style & initialization//
        Insets insets = listViewWindow.getInsets();
        this.listViewWindow.add(btnRemoveTeam);


        //Actions//
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                removeTeamUI();
                update();


            }

        };
        btnRemoveTeam.addActionListener(actionListener);
        btnRemoveTeam.setFont(new Font(btnConfirm.getFont().getName(), Font.BOLD, 16));
        Dimension size = btnRemoveTeam.getPreferredSize();

        btnRemoveTeam.setBounds(insets.left + 790, insets.top + 150, size.width + 5, size.height);

    }



   /* public void generateButtonMove()
    {
        //Style & initialization//
        Insets insets = listViewWindow.getInsets();
        this.listViewWindow.add(btnMoveUser);//adds to the interface


        //Actions//
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                getSelectedRow();

                LoginView.teamManager.removeMemberFromTeam(foundUser,getSelectedTeam());

            }

        };
        btnMoveUser.addActionListener(actionListener);
        Dimension size =btnMoveUser.getPreferredSize();

        size = btnMoveUser.getPreferredSize();
        btnMoveUser.setBounds(insets.left + 810 , insets.top + 200, size.width + 5, size.height);



    }*/

    public void removeTeamUI() {
        Object itemSelected = comboTeamView.getSelectedItem();
        if (!LoginView.teamManager.isTeamExist(comboTeamView.getSelectedItem().toString())) {
            //The team doesn't exist. Cannot be removed!


            JOptionPane.showMessageDialog(this, "The team doesn't exist. Cannot be removed!");

        } else {
            String message = "Are you sure you want to delete ";
            message.concat(comboTeamView.getSelectedItem().toString());
        }
        int input = JOptionPane.showConfirmDialog(null,
                "In order to delete '" + comboTeamView.getSelectedItem().toString() + "' click OK. Otherwise quit this window.", "Be ok!", JOptionPane.DEFAULT_OPTION);
        // 0=ok
        if (input == 0) {
            LoginView.teamManager.removeTeam(new Team(comboTeamView.getSelectedItem().toString()));
            update();
            comboTeamView.removeItem(itemSelected);
            this.dispose();

        }

    }


    private void update() {
        LoginView.teamManager.updateTeamsFile();
        LoginView.userManager.updateUsersFile();
    }
/*
    public boolean getSelectedRow() {
        int row = usersTable.getSelectedRow();
        if (row != -1) {
            String val = (String) usersTable.getValueAt(row, 0);
            foundUser = LoginView.userManager.users.get(val);
            return true;
        }

        return false;
    }*/

    static public Team getSelectedTeam() {
        String teamName = comboTeamView.getSelectedItem().toString();
        return LoginView.teamManager.teams.get(teamName);
    }



}




