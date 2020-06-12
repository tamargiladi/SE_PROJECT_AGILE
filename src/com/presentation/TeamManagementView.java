package com.presentation;

import com.business.TeamManager;
import com.business.UserManager;
import com.business.WorkItemManager;
import com.persistent.Team;
import com.persistent.User;
import com.persistent.WorkItem;
import com.persistent.WorkItemBuilder;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;

import javax.swing.*;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

import static javax.swing.JOptionPane.showMessageDialog;


public class TeamManagementView extends JFrame {

    //========== Finals For Components =============
    final int fieldWidth = 200;
    final int fieldHeight = 30;

    final int btnWidth = 160;
    final int btnHeight = 40;

    final int smallFrameWidth = 300 ;
    final int smallFrameHeight = 200;

    //========== Frames & Panels ================
    private JPanel teamsScreenViewPanel = new JPanel();

    JFrame editFrame = new JFrame();
    JFrame addFrame = new JFrame();
    JPanel editPanel = new JPanel();
    JPanel addPanel =new JPanel();

    //======== Combo box variables ===========
    public static JComboBox<String> comboTeamView;
    String editFieldValue;


    //======= Add name =================
    JTextField fieldAdd = new JTextField("");
    JButton btnAddConfirm = new JButton("Confirm");

    //======= Editing name ==================
    JTextField fieldEdit = new JTextField("");
    JButton btnEditConfirm = new JButton("Confirm");


    //========= Table variables ===========================
    DefaultTableModel model = new DefaultTableModel(0, 0);
    JTable usersTable = new JTable(model);
    JScrollPane jScrollPane = new JScrollPane(usersTable);
    String[] str;

    //public static User foundUser; //User that selected in user table


    //======== Buttons ================
    //JButton btnConfirm = new JButton("Confirm");
    JButton btnAddTeam = new JButton("Add Team");
    JButton btnRemoveTeam = new JButton("Remove Team");//Add modfication that the team
    JButton btnEdit = new JButton("Edit Team");


    //============ Design =============
    ImageIcon backIcon = new ImageIcon("src/com/presentation/images/background.png");
    JLabel background = new JLabel("", backIcon, JLabel.RIGHT);


    //TODO: Remove the main...
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                TeamManagementView tGUI = new TeamManagementView();
            }
        });
    }

    //=============== Frames =====================
    public TeamManagementView() {

        //========= teamsScreenViewPanel Initialization ==============//
        Insets insets = teamsScreenViewPanel.getInsets();
        setTitle("Team Manager");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        this.teamsScreenViewPanel.setLayout(null);


        //========= Generates =========
        generateTeamCombo();
        generateTable();

        //--------- Buttons -----------
        generateButtonAddTeam();
        generateButtonRemoveTeam();
        //generateButtonConfirm();
        generateButtonEdit();
       // generateButtonExit();


        //======= Style ==========

        background.setBounds(insets.left, insets.top - 35, 1000, 600);
        setContentPane(teamsScreenViewPanel);

        teamsScreenViewPanel.add(background);//Addition to the frame

    }

    public void generateRemoveWindow() {

        String message = "Are you sure you want to delete ";
        message.concat(comboTeamView.getSelectedItem().toString());

        int input = JOptionPane.showConfirmDialog(null,
                "In order to delete '" + comboTeamView.getSelectedItem().toString() + "' click OK. Otherwise quit this window.", "Be ok!", JOptionPane.DEFAULT_OPTION);
        // 0=ok
        if (input == 0) {

            int usersSize = getSelectedTeam().getUsers().size();
            int teamsSize =  LoginView.teamManager.teams.size();
            if(usersSize==0)
            {
                LoginView.teamManager.removeTeam(comboTeamView.getSelectedItem().toString());
                comboTeamView.removeItem(comboTeamView.getSelectedItem());
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Team with users cannot be deleted!");


            }


        }

        LoginView.teamManager.printTeamManager();
    }

   public void generateEditWindow() {

       editFrame.setVisible(true);
       editPanel.setVisible(true);

       setLayoutEdit();

       //======== Components ===========
       generateFieldEdit();
       generateBtnEditConfirm();


       //======= Style ==========

       Insets insets = editPanel.getInsets();
       background.setBounds(insets.left, insets.top - 35, smallFrameWidth, smallFrameHeight);
       editFrame.setContentPane(editPanel);

       editPanel.add(background);//Addition to the frame

       //  generateSmallWindow("ADD");

    }

    public void generateAddWindow() {

        addFrame.setVisible(true);
        addPanel.setVisible(true);

        setLayoutAdd();

        //======== Components ===========
        generateFieldAdd();
        generateBtnAddConfirm();


        //======= Style ==========

        Insets insets = addPanel.getInsets();
        background.setBounds(insets.left, insets.top - 35, smallFrameWidth, smallFrameHeight);
        addFrame.setContentPane(addPanel);

        addPanel.add(background);//Addition to the frame

     //  generateSmallWindow("ADD");
    }

    //============== Buttons =====================

    //------------ Main Screen ----------------
  /*  public void generateButtonConfirm() {
        Insets insets = teamsScreenViewPanel.getInsets();
        this.teamsScreenViewPanel.add(btnConfirm);//adds to the interface

        //------actions-------//
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
    generateTable();
            }
        };
        btnConfirm.addActionListener(actionListener);
        //------ end actions-------//


        btnConfirm.setFont(new Font("Arial", Font.BOLD, 16));

        Dimension size = btnConfirm.getPreferredSize();

        size = btnConfirm.getPreferredSize();
        btnConfirm.setBounds(400, 20, 160, 40);

    }*/

   public void generateButtonEdit() {
        Insets insets = teamsScreenViewPanel.getInsets();
        this.teamsScreenViewPanel.add(btnEdit);//adds to the interface

        //------actions-------//
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                generateEditWindow();

            }
        };
        btnEdit.addActionListener(actionListener);
        //------ end actions-------//


        btnEdit.setFont(new Font("Arial", Font.BOLD, 16));

        Dimension size = btnEdit.getPreferredSize();

        size = btnEdit.getPreferredSize();
        btnEdit.setBounds( 790, insets.top + 200, 160, 40);


    }

    public void generateButtonAddTeam() {
        //Style & initialization//
        Insets insets = teamsScreenViewPanel.getInsets();
        this.teamsScreenViewPanel.add(btnAddTeam);

        Dimension size = btnAddTeam.getPreferredSize();
        size = btnAddTeam.getPreferredSize();


        //Actions//
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

               /* TeamView tvAddTeam = new TeamView("Add Team");
                LoginView.teamManager.updateTeamsFile();*/

               generateAddWindow();



            }

        };
        btnAddTeam.addActionListener(actionListener);
        btnAddTeam.setBounds(790, insets.top + 100, 160, 40);
        btnAddTeam.setFont(new Font("Arial", Font.BOLD, 16));

    }

    public void generateButtonRemoveTeam() {
        //Style & initialization//
        Insets insets = teamsScreenViewPanel.getInsets();
        this.teamsScreenViewPanel.add(btnRemoveTeam);


        //Actions//
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(getSelectedTeam().getUsers().size()==0) {
                    generateRemoveWindow();
                    closeTeamsScreenViewPanel(actionEvent);
                }
                else
                    JOptionPane.showMessageDialog(null,  "The team must be empty in order to delete it.\nPlease assign new team to all team members.");
            }

        };
        btnRemoveTeam.addActionListener(actionListener);
        btnRemoveTeam.setFont(new Font("Arial", Font.BOLD, 16));
        Dimension size = btnRemoveTeam.getPreferredSize();

        btnRemoveTeam.setBounds( 790, insets.top + 150, 160, 40);

    }


    //---------- Add Screen ----------------

    public void setLayoutAdd() {
      //  Insets insets = addPanel.getInsets();

        addFrame.setContentPane(addPanel);
        //addPanel.setBackground();

        Insets insets = addPanel.getInsets();
        addFrame.setTitle("Team Manager");
        addFrame.setSize(smallFrameWidth, smallFrameHeight);
        addFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        addFrame.setVisible(true);
        addFrame.setResizable(false);
        addFrame.setLayout(null);

        ImageIcon backIcon = new ImageIcon("src/com/presentation/images/background.png");
        JLabel background = new JLabel("", backIcon, JLabel.RIGHT);


        //======= Style ==========
        /*
        background.setBounds(insets.left, insets.top - 35, smallFrameWidth, smallFrameHeight);
        addFrame.setContentPane(addPanel);

        addPanel.add(background);//Addition to the frame*/


    }

    public void generateBtnAddConfirm(){
        Insets insets = addFrame.getInsets();

        Dimension size = addFrame.getSize();

        background.setSize(size.width,size.height);

        addPanel.add(btnAddConfirm);//adds to the interface

        //------actions-------//
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                if(!fieldAdd.getText().isEmpty()) {
                    if(LoginView.teamManager.isTeamExist(fieldAdd.getText()))
                        JOptionPane.showMessageDialog(null,"Team already exist. ");
                    else {
                        LoginView.teamManager.addTeam(fieldAdd.getText());
                        comboTeamView.addItem(fieldAdd.getText());
                        addFrame.dispose();
                        closeTeamsScreenViewPanel(actionEvent);
                    }
                }
                else
                {
                    JOptionPane.showMessageDialog(null,"New team cannot be empty.");
                }
                //changeTeamsName(getSelectedTeam().getTeamsName(),fieldEdit.getText());
                // update();
            }
        };
        btnAddConfirm.addActionListener(actionListener);


        btnAddConfirm.setFont(new Font("Arial", Font.BOLD, 16));

        Dimension btnSize = btnAddConfirm.getPreferredSize();
        btnAddConfirm.setBounds(fieldAdd.getX(), fieldAdd.getY() +50  , btnSize.width , btnHeight);
    }

    public void generateFieldAdd() {
        fieldAdd.setBounds(20,20,fieldWidth,fieldHeight);
        fieldAdd.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        addPanel.add(fieldAdd);
    }


    //-------- Edit Screen --------------

    public void setLayoutEdit() {

        editFrame.setContentPane(editPanel);
        //addPanel.setBackground();

        Insets insets = editPanel.getInsets();
        editFrame.setTitle("Team Manager");
        editFrame.setSize(smallFrameWidth, smallFrameHeight);
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editFrame.setVisible(true);
        editFrame.setResizable(false);
        editFrame.setLayout(null);

        ImageIcon backIcon = new ImageIcon("src/com/presentation/images/background.png");
        JLabel background = new JLabel("", backIcon, JLabel.RIGHT);
    }

    public void generateBtnEditConfirm() {
        Insets insets = editFrame.getInsets();

        Dimension size = editFrame.getSize();

        background.setSize(size.width,size.height);

        editPanel.add(btnEditConfirm);//adds to the interface

        //------actions-------//
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
            //TODO:Add action for editing

                String newTeamName = fieldEdit.getText(),
                        oldName = getSelectedTeam().getTeamsName();
                if(!newTeamName.equals(oldName)&&!newTeamName.isEmpty()) {
                    changeTeamsName(oldName,newTeamName);
                    editFrame.dispose();
                    closeTeamsScreenViewPanel(actionEvent);

                }
                else if(newTeamName.isEmpty())
                    JOptionPane.showMessageDialog(null,"The new name cannot be empty!.");
                else
                    JOptionPane.showMessageDialog(null,"The new name is the same as the old one.");


            }
        };
        btnEditConfirm.addActionListener(actionListener);


        btnEditConfirm.setFont(new Font("Arial", Font.BOLD, 16));

        Dimension btnSize = btnEditConfirm.getPreferredSize();
        btnEditConfirm.setBounds(fieldEdit.getX(), fieldEdit.getY() +50  , btnSize.width , btnHeight);

    }

    public void generateFieldEdit() {

        fieldEdit.setBounds(20,20,fieldWidth,fieldHeight);
        fieldEdit.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        fieldEdit.setForeground(new Color(116, 116, 116));
        fieldEdit.setText(getSelectedTeam().getTeamsName());

        fieldEdit.addKeyListener(new KeyListener() {
          @Override
          public void keyTyped(KeyEvent e) {
              fieldEdit.setForeground(new Color(0, 0, 0));

          }

          @Override
          public void keyPressed(KeyEvent e) {

          }

          @Override
          public void keyReleased(KeyEvent e) {



          }
      });



        editPanel.add(fieldEdit);


    }

    public void generateTable() {
        Insets insets = teamsScreenViewPanel.getInsets();

        remove(background);
        //==== Initialization ===
         model.setRowCount(0);
        model.setColumnCount(0);
        String[] columnNames = {"Username","Permission"};

        //====== Rows & Columns addition ========
        for (String col : columnNames)//Adding columns
            model.addColumn(col);

        for (String username : getUsersListByCombo()) {//Adding rows
            model.addRow(new Object[]{username,
                    LoginView.userManager.users.get(username).getPermissionLevel().name()});
        }



        usersTable.setDefaultEditor(Object.class, null);
        usersTable.setDefaultEditor(Object.class, null);



       // Dimension tableSize = usersTable.getPreferredSize();
        //========== Style ===========
        usersTable.getTableHeader().setBackground(Color.WHITE);
        usersTable.getTableHeader().setForeground(new Color(0, 49, 82));


        teamsScreenViewPanel.add(jScrollPane).setBounds(insets.left + 20,
                insets.top + 100, 700, 400);//Addition & style


        //============ Technical Addition to the table ================
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

        add(background);
    }

    private void generateTeamCombo() {
        Dimension size;
        Insets insets = teamsScreenViewPanel.getInsets();

        comboTeamView = new JComboBox(TeamManager.getInstance().teams.keySet().toArray());

        comboTeamView.setBounds(insets.left + 120, 20, 250, 40);

        comboTeamView.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                generateTable();
            }
        });

        this.teamsScreenViewPanel.add(comboTeamView);

    }

    public List<String> getUsersListByCombo() {

        Iterator<Map.Entry<String,User>> itUser = UserManager.getInstance().users.entrySet().iterator();
        List<String> usersList = new LinkedList<>();//explicit ..

        Team team = getSelectedTeam();
        if (team != null) {
            String teamName = team.getTeamsName();
            while (itUser.hasNext()) {
                String username = itUser.next().getKey(),
                        userTeam = UserManager.getInstance().users.get(username).getTeamName();
                if (userTeam.equals(teamName))
                    usersList.add(username);

            }
        }

        return usersList;
    }

    private void update() {
        LoginView.teamManager.updateTeamsFile();
       // LoginView.userManager.updateUsersFile();
       // LoginView.userManager.updateUsersFile();
    }

    public Team getSelectedTeam() {

        String teamName = Objects.requireNonNull(comboTeamView.getSelectedItem()).toString();
        return TeamManager.getInstance().getTeam(teamName);
    }

    private void closeTeamsScreenViewPanel(ActionEvent actionEvent)
    {

        this.dispose();
        MainUserInterface.reopenTeamWindow();
    }


    public void changeTeamsName(String oldTeam, String newTeam)
    {
        TeamManager.getInstance().updateTeamsName(oldTeam,newTeam);

        // Change team name for all users belongs to that team
        for (Map.Entry<String, User> stringUserEntry : LoginView.userManager.users.entrySet()) {
            String username = stringUserEntry.getKey();
            String userTeam = stringUserEntry.getValue().getTeamName();
            if(userTeam.equals(oldTeam))
                 LoginView.userManager.updateUserTeam(username,newTeam);
        }

        //Change team name for all work items associated with that team
        for (Map.Entry<Integer, WorkItem> workItemEntry : WorkItemManager.getInstance().workItems.entrySet()) {
            Integer id = workItemEntry.getKey();
            String teamName = workItemEntry.getValue().getTeam();
            if (teamName != null && teamName.equals(oldTeam))
                workItemEntry.getValue().setTeam(newTeam);
        }

    }

    //Comments to remove at THE END

   /* public void moveUsersFromTeam(String oldTeam, String newTeam)
    {

        for (String username : LoginView.teamManager.teams.get(oldTeam).getUsers()) {
           // LoginView.userManager.users.get(user.getUserName()).setTeam(LoginView.teamManager.teams.get(newTeam));
           LoginView.

        }
    }*/

   /* public User getUserByTeamAndInd(String teamName, int ind)
    {
        return LoginView.teamManager.teams.get(teamName).getUsers().get(ind);
    }*/

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

   /* private void resetFile()
    {

        for (Map.Entry<String, Team> stringTeamEntry : LoginView.teamManager.teams.entrySet()) {
            String teamName = stringTeamEntry.getKey();
            for (User user : LoginView.teamManager.teams.get(teamName).getUsers()) {
                String username = user.getUserName();
                if (!LoginView.userManager.isUserExist(username))
                    LoginView.teamManager.teams.get(teamName).removeUser(LoginView.userManager.users.get(username));

            }

        }

        update();
    }*/

   //Exit Button
   /*
    public void generateButtonExit() {
        Insets insets = teamsScreenViewPanel.getInsets();
        this.teamsScreenViewPanel.add(btnExit);//adds to the interface

        //------ Actions -------//
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                closeTeamsScreenViewPanel(actionEvent,false);

                update();
            }
        };

        btnExit.addActionListener(actionListener);
        //------ end actions-------//


        btnExit.setFont(new Font(btnConfirm.getFont().getName(), Font.BOLD, 16));

        Dimension size = btnExit.getPreferredSize();

        size = btnExit.getPreferredSize();
        btnExit.setBounds(790
                , insets.top + 400, 160, 40);

    }
*/
    //JButton btnExit = new JButton("Exit");


}










