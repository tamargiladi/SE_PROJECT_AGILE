package com.presentation;

import com.business.TeamManager;
import com.persistent.Team;
//import sun.jvm.hotspot.debugger.posix.elf.ELFSectionHeader;
//import sun.jvm.hotspot.ui.tree.BooleanTreeNodeAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Map;

import static javax.swing.JOptionPane.showMessageDialog;

public class TeamView extends JFrame {


    //The variables
    private String actionType;
    private JPanel jPanel = new JPanel();

    JTextArea teamNameBox = new JTextArea("");
    JTextArea userNameBox = new JTextArea("");
    JButton btnTeam = new JButton();

    JComboBox<String> jComboTeam = new JComboBox<>();
    JButton btnMove = new JButton("Move");
    String[] str;


    //Views
    public TeamView(String title)
    {
        //TODO:Make all the additional windows hidden
        this.actionType=title;


        setLayout();


    }

    public void setLayout()
    {
        //=====WINDOW==========
        Insets insets =  jPanel.getInsets();
        setTitle("Team Manager");
        setSize(400,200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        this.jPanel.setLayout(null);
        this.setContentPane(this.jPanel);


        //========LABEL===========


        if(actionType=="Add Team") {
            Dimension size;
            JLabel verLabel;
            verLabel = new JLabel("Enter team name:");
            this.jPanel.add(verLabel);//Adding the 'verLabel' to the interface
            size = verLabel.getPreferredSize();
            verLabel.setBounds(insets.left + 20 , insets.top + 20, size.width + 5, size.height);

            setLayoutInputBoxTeam(insets, verLabel);
            setLayoutButtonTeam(insets, verLabel);
        }
        else
        {
            generateComboBox();
            //generateButtonMove();


        }

    }


    public void setLayoutInputBoxTeam(Insets insets, JLabel verLabel)
    {
        Dimension size;
        size = teamNameBox.getPreferredSize();
        teamNameBox.setBounds(insets.left + verLabel.getWidth() + 40 , insets.top + 20, size.width + 150, size.height + 5);
        teamNameBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        this.jPanel.add(teamNameBox);//Adding to interface

        //-------------Actions----------------//
        teamNameBox.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar() == KeyEvent.VK_ENTER){

                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        //---------------END actions-----------//

    }
    public void setLayoutButtonTeam(Insets insets, JLabel verLabel )
    {

        Dimension size;
        this.jPanel.add(btnTeam);//add to the interface


        if(actionType=="Add Team")
            btnTeam.setText("Add");
     /*   else if(actionType=="Add User To Team") {
            btnTeam.setText("Add");
            generateUsernameInput(insets.left + verLabel.getWidth() + 40);
        }
        else if(actionType=="Remove User From Team") {
            btnTeam.setText("Remove");
            generateUsernameInput(insets.left + verLabel.getWidth() + 40);
        }*/
        else
            btnTeam.setText("Remove");

        //------actions-------//
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                String command = actionEvent.getActionCommand();

                switch(actionType)
                {
                    case "Add Team":
                        generateAddTeam(teamNameBox.getText());
                        showMessageDialog(null, "In order to watch the new team, please reopen this window.");
                        break;
                    case "Remove Team":
                        generateRemoveTeam(teamNameBox.getText());break;
                    case "Add User To Team":
                        generateAddUserToTeam(teamNameBox.getText(), userNameBox.getText());break;
                    case "Remove User From Team":
                        generateRemoveUserFromTeam(teamNameBox.getText(), userNameBox.getText());break;
                }
            }
        };
        btnTeam.addActionListener(actionListener);
        //------ end actions-------//

        size = btnTeam.getPreferredSize();
        btnTeam.setBounds(insets.left + verLabel.getWidth() + teamNameBox.getY() + teamNameBox.getWidth()+ 40 , insets.top + 15, size.width + 5, size.height);

    }

    public void generateAddTeam(String teamName){


        if (LoginView.teamManager.isTeamExist(teamName))
        {
            JOptionPane.showMessageDialog(this, "The team already exists!");

        }
        else {
            LoginView.teamManager.addTeam(teamName);

            this.dispose();

        }





    }

    public void generateComboBox()
    {
        Dimension size;
        Insets insets = jPanel.getInsets();


        generateStringArrayCombo();
        jComboTeam = new JComboBox<>(str);
        jComboTeam.setBounds(insets.left + 120 , insets.top + 15, 250, 40);
        this.jPanel.add(jComboTeam);
    }
    public void generateRemoveTeam(String text)
    {
       if(!LoginView.teamManager.isTeamExist(text))
       {
           //The team doesn't exist. Cannot be removed!
           JOptionPane.showMessageDialog(this, "The team doesn't exist. Cannot be removed!");

       }
       else
       {
           LoginView.teamManager.removeTeam(new Team(text));
           update();
           this.dispose();
       }


    }
    public void generateAddUserToTeam(String teamName, String username)
    {

        //xTODO: Make the AddUserToTeam'window visible
        //xTODO:Input box
        //xTODO:OK button

        if(LoginView.userManager.users.get(username).getTeam().getTeamsName()==teamName)
        {
            //The user already belongs to this team.
            JOptionPane.showMessageDialog(this, "The user already belongs to this team.");

        }

       else
        {

            //Removes from previous team
            LoginView.teamManager.removeMemberFromTeam(LoginView.userManager.users.get(username),
                    LoginView.userManager.users.get(username).getTeam());



            LoginView.teamManager.addMemberToTeam(LoginView.userManager.users.get(username),
                    LoginView.teamManager.teams.get(teamName));

            LoginView.userManager.updateUserTeam(LoginView.userManager.users.get(username).getUserName(),
                    LoginView.teamManager.teams.get(teamName).getTeamsName());

            update();
            this.dispose();
        }

    }

    public void generateRemoveUserFromTeam(String teamName, String username)
    {


        if(LoginView.userManager.users.get(username).getTeam().getTeamsName()==teamName) {
            LoginView.teamManager.addMemberToTeam(LoginView.userManager.users.get(username),
                    LoginView.teamManager.teams.get("default"));

            LoginView.teamManager.removeMemberFromTeam(LoginView.userManager.users.get(username),
                    LoginView.userManager.users.get(username).getTeam());

            LoginView.userManager.updateUserTeam(LoginView.userManager.users.get(username).getUserName(),
                    LoginView.teamManager.teams.get(teamName).getTeamsName());



            update();
            this.dispose();
        }
        else
        {
            //User doesn't belong to this team!!
            JOptionPane.showMessageDialog(this, "The user doesn't belong to this team.Cannot ber removed from it.");

        }
    }


    private void generateUsernameInput(int location)
    {
        Dimension size;



        //=====BOX======
        size = userNameBox.getPreferredSize();
        userNameBox.setBounds(location,60,size.width + 150, size.height + 5);
        userNameBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        //===============



        //=====LABEL====
        JLabel verLabel;
        verLabel = new JLabel("Enter username:");
        this.jPanel.add(verLabel);//Adding the 'verLabel' to the interface
        size = verLabel.getPreferredSize();
        verLabel.setBounds(location-userNameBox.getWidth()+15 , 60, size.width + 5, size.height);
        //==============


        this.jPanel.add(userNameBox);
        this.jPanel.add(verLabel);


        /***Actions**/
        userNameBox.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyChar() == KeyEvent.VK_ENTER){

                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }

  /*  public void generateButtonMove()

    {

        //Style & initialization//
        Insets insets = jPanel.getInsets();
        this.jPanel.add(btnMove);//adds to the interface


        //Actions//
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                LoginView.teamManager.removeMemberFromTeam(TeamManagerGUI.foundUser, TeamManagerGUI.getSelectedTeam());
                LoginView.teamManager.addMemberToTeam(TeamManagerGUI.foundUser, getSelectedTeam());

            }


        };
        btnMove.addActionListener(actionListener);
        Dimension size =btnMove.getPreferredSize();

        size = btnMove.getPreferredSize();
        btnMove.setBounds(insets.left + 810 , insets.top + 200, size.width + 5, size.height);

    }*/
    private void update()
    {
        LoginView.teamManager.updateTeamsFile();
        LoginView.userManager.updateUsersFile();
    }

    //xTODO:Add a method that would generate the **use'rs*** input box

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

    public Team getSelectedTeam() {
        String teamName = jComboTeam.getSelectedItem().toString();
        return LoginView.teamManager.teams.get(teamName);
    }






}
