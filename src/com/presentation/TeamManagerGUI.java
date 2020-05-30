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
    JComboBox<String> jCombo ;
    JButton btnConfirm = new JButton("Confirm");

    //Table variables
    DefaultTableModel model = new DefaultTableModel(0,0);
    JTable usersTable = new JTable(model);
    JScrollPane jScrollPane = new JScrollPane(usersTable);
    String str[];

    //TeamManager variables&buttons
    JButton btnAddTeam = new JButton("Add Team");

    //TODO:Add modificiation that the team is empty
    //TODO: Ask if the admin wants to delete all the users in the team
    /*TODO: If the teams is not empty and the admin doesn't want to delete, show a message that
        requires him first to transfer all the users of the team first.*/

    JButton btnRemoveTeam = new JButton("Remove team");//Add modfication that the team
    JButton btnAddUser = new JButton("Add user");
    JButton btnRemoveUser = new JButton("Remove user");


    final int colSize = 2;


//TODO: Remove the main...
    /*public static void main(String[] args) throws EOFException {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TeamViewList frame = new TeamViewList();
            }
        });
    }*/
    public TeamManagerGUI()
    {
       //=========jPanel initialization==============//
        Insets insets =  listViewWindow.getInsets();
        setTitle("Team Manager");
        setSize(1000,600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        this.listViewWindow.setLayout(null);
        this.setContentPane(this.listViewWindow);
        //======================================//

        generateTeamCombo();
        generateButtonConfirm();
        generateTable();

        //Generate TeamManager's buttons//
        generateButtonAddTeam();
        generateButtonRemoveTeam();


    }

    private void generateTeamCombo()
    {
        Dimension size;
        Insets insets = listViewWindow.getInsets();


        generateStringArrayCombo();
        jCombo = new JComboBox<>(str);
        jCombo.setBounds(insets.left + 120 , insets.top + 15, 250, 40);
        this.listViewWindow.add(jCombo);


    }

    private void generateStringArrayCombo()
    {
        int length = LoginView.teamManager.teams.size();
        ArrayList<String> listArr = new ArrayList<String>();
        str = new String[length];

        int ind = 0;
        for (Map.Entry<String, Team> me : LoginView.teamManager.teams.entrySet()) {
            if(!me.getKey().equals("default"))
                str[ind++]=me.getKey();
        }

      /*  for(int i=0;i<length;i++)//length-1 : because the 'default' group is not included.
            str[i]=listArr.get(i);*/
    }



    public void generateTable() {


        Insets insets = listViewWindow.getInsets();


        model.setRowCount(0);
        model.setColumnCount(0);
        String[] columnNames = {"Username", "Permission"};
        for(String col:columnNames)//adding columns
            model.addColumn(col);


        Iterator<User> it = getUsersListByCombo().iterator();
        Iterator<User> itPer = getUsersListByCombo().iterator();

        Random rnd = new Random();
        while(it.hasNext())
        {
            model.addRow(new Object[]{it.next().getUserName(),itPer.next().getPermissionLevel().name()});
        }

        usersTable.setDefaultEditor(Object.class, null);


       usersTable.setDefaultEditor(Object.class, null);
        Dimension tableSize = usersTable.getPreferredSize();
        usersTable.getTableHeader().setBackground(Color.WHITE);
        usersTable.getTableHeader().setForeground(new Color(0,49,82));

        listViewWindow.add(jScrollPane).setBounds(insets.left + 20,insets.top + 60,700,400);



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


    public void generateButtonConfirm()
    {
        Insets insets = listViewWindow.getInsets();
        this.listViewWindow.add(btnConfirm);//add to the interface

        //------actions-------//
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                generateTable();
            }
        };
        btnConfirm.addActionListener(actionListener);
        //------ end actions-------//

        Dimension size =btnConfirm.getPreferredSize();

        size = btnConfirm.getPreferredSize();
        btnConfirm.setBounds(insets.left + 400 , insets.top + 20, size.width + 5, size.height);



    }
    public List<User> getUsersListByCombo()
    {
                return LoginView.teamManager.teams.get(jCombo.getSelectedItem().toString()).getUsersList();
    }

    public void generateButtonAddTeam()
    {
        //Style & initialization//
        Insets insets = listViewWindow.getInsets();
        this.listViewWindow.add(btnAddTeam);

        Dimension size =btnConfirm.getPreferredSize();
        size = btnAddTeam.getPreferredSize();
        btnAddTeam.setBounds(insets.left + 810 , insets.top + 100, size.width + 5, size.height);


        //Actions//
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                TeamView tvAddTeam = new TeamView("Add Team");
                LoginView.teamManager.updateTeamsFile();



            }

        };
        btnAddTeam.addActionListener(actionListener);

    }

    public void generateButtonRemoveTeam()
    {
        //Style & initialization//
        Insets insets = listViewWindow.getInsets();
        this.listViewWindow.add(btnRemoveTeam);

        Dimension size =btnConfirm.getPreferredSize();
        size = btnRemoveTeam.getPreferredSize();
        btnRemoveTeam.setBounds(insets.left + 810 , insets.top + 150, size.width + 5, size.height);


        //Actions//
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                Object itemSelected = jCombo.getSelectedItem();
                    removeTeamUI();
                    update();
                    jCombo.removeItem(itemSelected);


            }

        };
        btnRemoveTeam.addActionListener(actionListener);

    }

    public void removeTeamUI(){
        if(!LoginView.teamManager.isTeamExist(jCombo.getSelectedItem().toString()))
        {
            //The team doesn't exist. Cannot be removed!

            JOptionPane.showMessageDialog(this, "A basic JOptionPane message dialog");






            JOptionPane.showMessageDialog(this, "The team doesn't exist. Cannot be removed!");

        }
        else
        {
            LoginView.teamManager.removeTeam(new Team(jCombo.getSelectedItem().toString()));
            update();
            this.dispose();
        }

    }

    private void update()
    {
        LoginView.teamManager.updateTeamsFile();
        LoginView.userManager.updateUsersFile();
    }


    private void updateCombo()
    {

    }
}




