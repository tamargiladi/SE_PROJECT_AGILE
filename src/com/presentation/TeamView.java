package com.presentation;

import com.business.TeamManager;
import com.persistent.Team;
import sun.applet.Main;
import sun.jvm.hotspot.debugger.posix.elf.ELFSectionHeader;
import sun.jvm.hotspot.ui.tree.BooleanTreeNodeAdapter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import static javax.swing.JOptionPane.showMessageDialog;

public class TeamView extends JFrame {


    //The variables
    private String actionType;
    private JPanel jPanel = new JPanel();

    JTextArea teamNameBox = new JTextArea("");
    JTextArea userNameBox = new JTextArea("");
    JButton btnTeam = new JButton();


    //Views
    public TeamView(String title)
    {
        //TODO:Make all the additional windows hidden

        this.actionType=title;

        setLayout();

    }



    public void setLayout()
    {

        //Initial modifications
        Insets insets =  jPanel.getInsets();
        setTitle("Team Manager");
        setSize(1000,600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        this.jPanel.setLayout(null);
        this.setContentPane(this.jPanel);




        Dimension size;

        //========LABEL===========
        JLabel verLabel;
        verLabel = new JLabel("Enter team name:");
        this.jPanel.add(verLabel);//Adding the 'verLabel' to the interface
        size = verLabel.getPreferredSize();
        verLabel.setBounds(insets.left + 20 , insets.top + 20, size.width + 5, size.height);


        //============END OF LABEL=============


        //============INPUT BOX================
        size = teamNameBox.getPreferredSize();
        teamNameBox.setBounds(insets.left + verLabel.getWidth() + 40 , insets.top + 20, size.width + 150, size.height + 5);
        teamNameBox.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        this.jPanel.add(teamNameBox);


        /***Actions**/
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


        //==============END OF INPUT BOX===========



        //==============BUTTON==================
        this.jPanel.add(btnTeam);



        if(actionType=="Add Team")
            btnTeam.setText("Add");
        else if(actionType=="Add User To Team") {
            btnTeam.setText("Add");
            generateUsernameInput(insets.left + verLabel.getWidth() + 40);

        }
        else if(actionType=="Remove User From Team") {
            btnTeam.setText("Remove");
            generateUsernameInput(insets.left + verLabel.getWidth() + 40);
        }
        else
            btnTeam.setText("Remove");


        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {

                String command = actionEvent.getActionCommand();

                switch(actionType)
                {
                    case "Add Team":
                        generateAddTeam(teamNameBox.getText());break;
                    case "Remove Team":
                        generateRemoveTeam(teamNameBox.getText());break;
                    case "Add User To Team":
                        generateAddUserToTeam(teamNameBox.getText(), userNameBox.getText());break;
                    case "Remove User From Team":
                        generateRemoveUserFromTeam(teamNameBox.getText(), userNameBox.getText());break;
                }
            }
        };

        size = btnTeam.getPreferredSize();
        btnTeam.setBounds(insets.left + verLabel.getWidth() + teamNameBox.getY() + teamNameBox.getWidth()+ 40 , insets.top + 15, size.width + 5, size.height);
            btnTeam.addActionListener(actionListener);



        //Actions


        //==========END OF BUTTON==============


    }

    public void generateAddTeam(String teamName){


        if (MainUserInterface.teamManager.isTeamExist(teamName))
        {
            JOptionPane.showMessageDialog(this, "The team already exists!");

        }
        else {
            MainUserInterface.teamManager.addTeam(teamName);

            this.dispose();

        }





    }
    public void generateRemoveTeam(String text)
    {
       if(!MainUserInterface.teamManager.isTeamExist(text))
       {
           //The team doesn't exist. Cannot be removed!
           JOptionPane.showMessageDialog(this, "The team doesn't exist. Cannot be removed!");

       }
       else
       {
           MainUserInterface.teamManager.removeTeam(new Team(text));
           update();
           this.dispose();
       }


    }
    public void generateAddUserToTeam(String teamName, String username)
    {

        //xTODO: Make the AddUserToTeam'window visible
        //xTODO:Input box
        //xTODO:OK button

        if(MainUserInterface.userManager.users.get(username).getTeam().getTeamsName()==teamName)
        {
            //The user already belongs to this team.
            JOptionPane.showMessageDialog(this, "The user already belongs to this team.");

        }

       else
        {

            //Removes from previous team
            MainUserInterface.teamManager.removeMemberFromTeam(MainUserInterface.userManager.users.get(username),
                    MainUserInterface.userManager.users.get(username).getTeam());



            MainUserInterface.teamManager.addMemberToTeam(MainUserInterface.userManager.users.get(username),
                    MainUserInterface.teamManager.teams.get(teamName));

            MainUserInterface.userManager.updateUserTeam(MainUserInterface.userManager.users.get(username),
                    MainUserInterface.teamManager.teams.get(teamName));

            update();
            this.dispose();
        }

    }

    public void generateRemoveUserFromTeam(String teamName, String username)
    {


        if(MainUserInterface.userManager.users.get(username).getTeam().getTeamsName()==teamName) {
            MainUserInterface.teamManager.addMemberToTeam(MainUserInterface.userManager.users.get(username),
                    MainUserInterface.teamManager.teams.get("default"));

            MainUserInterface.teamManager.removeMemberFromTeam(MainUserInterface.userManager.users.get(username),
                    MainUserInterface.userManager.users.get(username).getTeam());

            MainUserInterface.userManager.updateUserTeam(MainUserInterface.userManager.users.get(username),
                    MainUserInterface.teamManager.teams.get(teamName));



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
    private void update()
    {
        MainUserInterface.teamManager.updateTeamsFile();
        MainUserInterface.userManager.updateUsersFile();
    }

    //xTODO:Add a method that would generate the **use'rs*** input box





}
