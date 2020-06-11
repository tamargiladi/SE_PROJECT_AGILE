package com.presentation;

import com.persistent.Team;
import com.persistent.User;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;

import javax.swing.*;
import javax.swing.event.PopupMenuListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    public static JComboBox<String> comboTeamView = new JComboBox<>();
    String selectedTeam;


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
    JButton btnConfirm = new JButton("Confirm");
    JButton btnAddTeam = new JButton("Add Team");
    JButton btnRemoveTeam = new JButton("Remove team");//Add modfication that the team
    JButton btnEdit = new JButton("Edit");


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
        generateButtonConfirm();
        //generateButtonEdit();
       // generateButtonExit();


        //======= Style ==========

        background.setBounds(insets.left, insets.top - 35, 1000, 600);
        setContentPane(teamsScreenViewPanel);

        teamsScreenViewPanel.add(background);//Addition to the frame

    }

    public void generateRemoveWindow() {
        Object itemSelected = comboTeamView.getSelectedItem();
        /*if (!LoginView.teamManager.isTeamExist(comboTeamView.getSelectedItem().toString())) {
            //The team doesn't exist. Cannot be removed!

            JOptionPane.showMessageDialog(this, "The team doesn't exist. Cannot be removed!");

        } */
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
            }
            else
            {
                JOptionPane.showMessageDialog(this, "Team with users cannot be deleted!");


            }


        }

        LoginView.teamManager.printTeamManager();
        update();
    }

   /* public void generateEditWindow() {

        addFrame.setVisible(true);
        addPanel.setVisible(true);

        setLayoutEdit();

        //======== Components ===========
        generateFieldAdd();
        generateBtnAddConfirm();


        //======= Style ==========

        Insets insets = addPanel.getInsets();
        background.setBounds(insets.left, insets.top - 35, smallFrameWidth, smallFrameHeight);
        addFrame.setContentPane(addPanel);

        addPanel.add(background);//Addition to the frame
      // generateSmallWindow("EDIT");

    }*/

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
    public void generateButtonConfirm() {
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

    }

   /* public void generateButtonEdit() {
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


        btnEdit.setFont(new Font(btnConfirm.getFont().getName(), Font.BOLD, 16));

        Dimension size = btnEdit.getPreferredSize();

        size = btnEdit.getPreferredSize();
        btnEdit.setBounds( 790, insets.top + 200, 160, 40);


    }*/

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
        btnAddTeam.setFont(new Font(btnConfirm.getFont().getName(), Font.BOLD, 16));

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

                    update();
                    closeTeamsScreenViewPanel(actionEvent, true);



                }
                else
                    JOptionPane.showConfirmDialog(null,  "The team must be empty in order to delete it.");



            }

        };
        btnRemoveTeam.addActionListener(actionListener);
        btnRemoveTeam.setFont(new Font(btnConfirm.getFont().getName(), Font.BOLD, 16));
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

                if(!fieldAdd.getText().equals("")) {
                    LoginView.teamManager.addTeam(fieldAdd.getText());
                    closeTeamsScreenViewPanel(actionEvent, true);
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


        btnAddConfirm.setFont(new Font(btnConfirm.getFont().getName(), Font.BOLD, 16));

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

        Insets insets = editPanel.getInsets();
        setTitle("Team Manager");
        editFrame.setSize(smallFrameWidth, smallFrameHeight);
        editFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);



    }

    public void generateBtnEditConfirm() {
        Insets insets = editPanel.getInsets();

        editPanel.add(background);
        background.setBounds(790, insets.top - 35, 1000, 600);


        this.editPanel.add(btnEditConfirm);//adds to the interface

        //------ Actions -------//
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                //changeTeamsName(getSelectedTeam().getTeamsName(),fieldEdit.getText());
                update();
            }
        };
        btnEditConfirm.addActionListener(actionListener);



        btnEditConfirm.setFont(new Font(btnConfirm.getFont().getName(), Font.BOLD, 16));
        btnEditConfirm.setBounds(insets.left + 850, insets.top + 200, btnWidth, btnHeight);

    }

    public void generateFieldEdit() {

        fieldEdit.setBounds(20,20,fieldWidth,fieldHeight);
        fieldEdit.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        editPanel.add(fieldEdit);


        //------ end actions-------//


    }

    private void generateStringArrayCombo() {
        int length = LoginView.teamManager.teams.size(),
                existComboCount = comboTeamView.getItemCount();
        ArrayList<String> listArr = new ArrayList<String>();
        str = new String[length];

        if(existComboCount>0)
            {
                for(int i=existComboCount-1;i>=1;i--)
                {
                    comboTeamView.removeItemAt(i);
                }
                comboTeamView.removeItemAt(0);//The '0' item is being added twice.

            }

        for (Map.Entry<String, Team> me : LoginView.teamManager.teams.entrySet()) {
            if (!me.getKey().equals("default"))
                comboTeamView.addItem(me.getKey());
        }


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


        //------ end actions-------//

        generateStringArrayCombo();
        comboTeamView.setBounds(insets.left + 120, 20, 250, 40);

        this.teamsScreenViewPanel.add(comboTeamView);


    }

    public List<String> getUsersListByCombo() {

        Iterator<Map.Entry<String,User>> itUser = LoginView.userManager.users.entrySet().iterator();
        List<String> usersList = new LinkedList<>();//explicit ..

        String teamName = getSelectedTeam().getTeamsName();
        while(itUser.hasNext())
        {
            String username = itUser.next().getKey(),
                    userTeam = LoginView.userManager.users.get(username).getTeamName();
            if(userTeam.equals(teamName))
                usersList.add(username);

        }

        return usersList;
    }

    private void update() {
        LoginView.teamManager.updateTeamsFile();
       // LoginView.userManager.updateUsersFile();
    }


    static public Team getSelectedTeam() {

        String teamName = Objects.requireNonNull(comboTeamView.getSelectedItem()).toString();
        return LoginView.teamManager.getTeam(teamName);
    }


    private void closeTeamsScreenViewPanel(ActionEvent actionEvent, boolean openAgain)
    {
        JComponent comp = (JComponent) actionEvent.getSource();
        Window win = SwingUtilities.getWindowAncestor(comp);
        win.dispose();

        if(openAgain)
            MainUserInterface.reopenTeamWindow();

    }



    //Comments to remove at THE END
/*    public void changeTeamsName(String oldTeam, String newTeam)
    {
        //d:TeamManager operations:
            //TODO:Iterate through all the users in the team and change their teams name with the function 'updateUserTeam'

                LoginView.teamManager.updateTeamsName(oldTeam,newTeam);

        for (Map.Entry<String, User> stringUserEntry : LoginView.userManager.users.entrySet()) {
            LoginView.userManager.updateUserTeam(stringUserEntry.getKey(), newTeam);
        }

            update();
    }*/


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










