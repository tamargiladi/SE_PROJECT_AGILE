package com.presentation;

import com.business.ReportGenerator;
import com.business.TeamManager;
import com.business.UserManager;
import com.business.WorkItemManager;
import com.persistent.Team;
import com.persistent.User;
import com.persistent.WorkItem;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.Color;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class MainUserInterface extends JPanel {

    public static WorkItemManager WIManager = new WorkItemManager();
    //public static UserManager userManager = new UserManager();
    //public static TeamManager teamManager = new TeamManager();
    public static ReportGenerator reportGenerator = new ReportGenerator();
    public static JFrame mainFrame;
    public static JMenuBar mb;
    private DefaultTableModel model = new DefaultTableModel(0,0);
    private JTable recentlyCreatedTable = new JTable(model);
    private JScrollPane jScrollPane = new JScrollPane(recentlyCreatedTable);

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainUserInterface frame = new MainUserInterface();
            }
        });
    }

    public MainUserInterface() throws HeadlessException {

        //TODO: yuval test - delete later
        Team algo = new Team("Algo");
        LoginView.teamManager.addTeam("Algo");
        LoginView.teamManager.addTeam("Software");
        LoginView.teamManager.addTeam("QA");
        LoginView.teamManager.addTeam("Hardware");
        LoginView.userManager.addUser("Voldemort", "123", User.PermissionLevel.admin, "Algo");
        LoginView.userManager.addUser("Harry Potter", "123", User.PermissionLevel.member, "Algo");
        LoginView.userManager.addUser("Albus Dumbledore", "123", User.PermissionLevel.manager, "Algo");
        LoginView.userManager.addUser("Yuval Levi", "123", User.PermissionLevel.admin, "Algo");
        //LoginView.userManager.login("Yuval Levi", "123");


        mainFrame = new JFrame("Agile Project Management");
        mainFrame.setResizable(false);
        mainFrame.setSize(1000,600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
        mainFrame.setLayout(null);
        mainFrame.setContentPane(jScrollPane);

        mb = new JMenuBar();
        mb.setBackground(new Color(87,160,211));;
        workItemMenu(mainFrame);
        userMenu(mainFrame);
        teamMenu(mainFrame);
        reportGenMenu(mainFrame);
        boardsMenu(mainFrame);
        searchBox(mainFrame);
        if (LoginView.userManager.loggedInUser != null)
            userConnected(mainFrame);
        mainFrame.setJMenuBar(mb);
        recentlyCreated(mainFrame);
        title(mainFrame);

        mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    WIManager.updateWorkItemsFile();
                    System.exit(0);
                }
            });

    }


    public static void title(JFrame mainFrame) {
        Insets insets = mainFrame.getInsets();
        ImageIcon backIcon = new ImageIcon("src/com/presentation/images/background.png");
        ImageIcon vIcon = new ImageIcon("src/com/presentation/images/v-icon.png");
        Image newVIcon = vIcon.getImage().getScaledInstance(30,40, Image.SCALE_AREA_AVERAGING);
        vIcon = new ImageIcon(newVIcon);
        JLabel title = new JLabel("Agile Project Management Tool", vIcon, JLabel.LEFT);
        JLabel background = new JLabel("", backIcon, JLabel.RIGHT);


        title.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        title.setForeground(new Color(0,49,82));
        mainFrame.add(title);
        mainFrame.add(background);
        title.setVisible(true);
        background.setVisible(true);
        Dimension size = title.getPreferredSize();
        title.setBounds(insets.left + 10 , insets.top - 15, size.width + 5, size.height);
        background.setBounds(insets.left , insets.top - 35, size.width + 700, size.height + 550);
    }

    public static void workItemMenu(JFrame mainFrame) {
        JMenu menuWorkItem;
        JMenuItem mEpic, mStory, mTask, mBug;

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String command = actionEvent.getActionCommand();
                if(command.equals("Epic")) {
                    EpicView ep = new EpicView(null);
                }
                else if(command.equals("Story")) {
                    StoryView ep = new StoryView(null);
                }
                else if(command.equals("Task")) {
                    TaskView ep = new TaskView(null);
                }
                else if(command.equals("Bug")) {
                    BugView ep = new BugView(null);
                }
                WIManager.updateWorkItemsFile();
                mainFrame.dispose();
            }
        };

        // Work Item menu
        menuWorkItem = new JMenu("Create New Work Item");
        mEpic = new JMenuItem("Epic"); mEpic.addActionListener(actionListener);
        mStory = new JMenuItem("Story"); mStory.addActionListener(actionListener);
        mTask = new JMenuItem("Task"); mTask.addActionListener(actionListener);
        mBug = new JMenuItem("Bug"); mBug.addActionListener(actionListener);
        menuWorkItem.add(mEpic); menuWorkItem.add(mStory); menuWorkItem.add(mTask); menuWorkItem.add(mBug);
        mb.add(menuWorkItem);
        menuWorkItem.setBorder(BorderFactory.createLineBorder(new Color(70,130,180), 1));
    }

    public static void teamMenu(JFrame mainFrame) {

        //TODO:================================================================
        //TODO:                          TAMAR
        //TODO:================================================================


        JMenu menuTeam;
        menuTeam = new JMenu("Team Management");
        mb.add(menuTeam);
        menuTeam.setBorder(BorderFactory.createLineBorder(new Color(70,130,180), 1));

        JMenuItem mAddTeam = new JMenuItem("Add Team");
        JMenuItem mRemoveTeam = new JMenuItem("Remove Team");
        JMenuItem mAddUserToTeam = new JMenuItem("Add User To Team");
        JMenuItem mRemoveUserFromTeam = new JMenuItem("Remove User From Team");


        if (LoginView.userManager.loggedInUser.getPermissionLevel() != User.PermissionLevel.admin) {
            menuTeam.setEnabled(false);
            menuTeam.setToolTipText("You have no permissions to this area");
        }
        else {

            ActionListener actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {
                    String command = actionEvent.getActionCommand();
                    TeamView tv = new TeamView(command);
                }
            };

            menuTeam.add(mAddTeam);mAddTeam.addActionListener(actionListener);
            menuTeam.add(mRemoveTeam);mRemoveTeam.addActionListener(actionListener);
            menuTeam.add(mAddUserToTeam);mAddUserToTeam.addActionListener(actionListener);
            menuTeam.add(mRemoveUserFromTeam);mRemoveUserFromTeam.addActionListener(actionListener);





        }

        //================================================================
        //TODO:                          TAMAR
        //================================================================
    }

    public static void userMenu(JFrame mainFrame) {
        JMenu menuUser;
        menuUser = new JMenu("User Management");
        menuUser.setBorder(BorderFactory.createLineBorder(new Color(70,130,180), 1));
        mb.add(menuUser);

        if (LoginView.userManager.loggedInUser.getPermissionLevel() == User.PermissionLevel.member) {
            menuUser.setEnabled(false);
            menuUser.setToolTipText("You have no permissions to this area");
        }



    }

    public static void reportGenMenu(JFrame mainFrame) {
        JMenu menuReports;
        JMenuItem mRep1, mRep2, mRep3, mRep4, mRep5;

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String command = actionEvent.getActionCommand();
                ReportView rv = new ReportView(command);
            }
        };

        menuReports = new JMenu("Report Generator");
        mRep1 = new JMenuItem("Total planned hours per member"); mRep1.addActionListener(actionListener);
        mRep2 = new JMenuItem("Work Item distribution by status"); mRep2.addActionListener(actionListener);
        mRep3 = new JMenuItem("Bugs found in version"); mRep3.addActionListener(actionListener);
        mRep4 = new JMenuItem("Bugs solved in version"); mRep4.addActionListener(actionListener);
        mRep5 = new JMenuItem("Exceeding estimations"); mRep5.addActionListener(actionListener);
        menuReports.add(mRep1); menuReports.add(mRep2); menuReports.add(mRep3); menuReports.add(mRep4); menuReports.add(mRep5);
        menuReports.setBorder(BorderFactory.createLineBorder(new Color(70,130,180), 1));
        mb.add(menuReports);
        if (LoginView.userManager.loggedInUser.getPermissionLevel() == User.PermissionLevel.member) {
            menuReports.setEnabled(false);
            menuReports.setToolTipText("You have no permissions to this area");
        }
    }

    public static void boardsMenu(JFrame mainFrame) {
        JMenu menuBoards;
        JMenuItem daily, story;

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String command = actionEvent.getActionCommand();
                if(command.equals("Daily Board")) {

                } else if (command.equals("Stories Board")) {
                    storiesBoard();
                }
            }
        };

        menuBoards = new JMenu("Boards");
        daily = new JMenuItem("Daily Board"); daily.addActionListener(actionListener);
        story = new JMenuItem("Stories Board"); story.addActionListener(actionListener);
        menuBoards.add(daily); menuBoards.add(story);
        mb.add(menuBoards);
        menuBoards.setBorder(BorderFactory.createLineBorder(new Color(70,130,180), 1));
    }

    public static void storiesBoard() {

    }

    public void searchBox(JFrame mainFrame) {
        JTextField searchBox = new JTextField("",10);
        JButton searchButton = new JButton("Search");

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String command = actionEvent.getActionCommand();
                if(command.equals("Search")) {
                    WorkItem wi =  WIManager.searchWorkItem(Integer.parseInt(searchBox.getText()));
                    returnWorkItemFromSearch(wi);
                }
            }
        };

        searchButton.setOpaque(false);
        searchButton.setBorder(new EmptyBorder(5,15,5,15));

        mb.add(searchBox);
        searchBox.setMaximumSize(new Dimension(400,20));
        searchButton.setMaximumSize(new Dimension(80,20));
        searchBox.setToolTipText("Search Work Item...");
        mb.add(searchButton);
        searchButton.addActionListener(actionListener);
        mainFrame.setJMenuBar(mb);
    }

    public static void returnWorkItemFromSearch(WorkItem wi) {
        if (wi == null) {
            JOptionPane.showMessageDialog(mainFrame, "Could not found matching work item");
            return;
        }
        else {
            if (wi.getType() == WorkItem.typeEnum.Epic) {
                EpicView ev = new EpicView(wi);
            } else if (wi.getType() == WorkItem.typeEnum.Story) {
                StoryView sv = new StoryView(wi);
            }
            else if (wi.getType() == WorkItem.typeEnum.Task) {
                TaskView tv = new TaskView(wi);
            } else { //Bug
                BugView bv = new BugView(wi);
            }
            WIManager.updateWorkItemsFile();
            mainFrame.dispose();
        }
    }

    public void recentlyCreated(JFrame mainFrame) {
        Insets insets = mainFrame.getInsets();
        //Prepare table data
        model.setRowCount(0);
        model.setColumnCount(0);
        String[] columnNames = {"ID", "Type", "Status", "Owner", "Summary"};
        for (String col : columnNames) //adding columns
            model.addColumn(col);
        for (Map.Entry<Integer, WorkItem> entry : WIManager.workItems.entrySet()) //adding rows
            model.addRow(new Object[]{entry.getValue().getId(), entry.getValue().getType(), entry.getValue().getStatus(), entry.getValue().getOwner(), entry.getValue().getSummary()});
        recentlyCreatedTable.setDefaultEditor(Object.class, null);

//        // Sort work items by id - descending (most recently created)
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(recentlyCreatedTable.getModel());
        recentlyCreatedTable.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();

//        //show on UI
        JLabel recentLabel = new JLabel("Recently created Work Items");
        jScrollPane.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(75,10,205,410),
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Recently Created Work Items", TitledBorder.CENTER, TitledBorder.TOP)));
        recentLabel.setVisible(true);
        Dimension size = recentLabel.getPreferredSize();
        recentLabel.setBounds(insets.left + 12 , insets.top + 40, size.width * 2, size.height);
        mainFrame.add(recentLabel);
        recentLabel.setFont(new Font(recentLabel.getFont().getName(), Font.BOLD, 12));
        recentLabel.setForeground(new Color(0,49,82));
        recentlyCreatedTable.setDefaultEditor(Object.class, null);
        Dimension tableSize = recentlyCreatedTable.getPreferredSize();
        recentlyCreatedTable.getTableHeader().setBackground(Color.WHITE);
        recentlyCreatedTable.getTableHeader().setForeground(new Color(0,49,82));


        recentlyCreatedTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    Integer val = (Integer) recentlyCreatedTable.getValueAt(row,0);
                    WorkItem foundWI = MainUserInterface.WIManager.searchWorkItem(val);
                    MainUserInterface.returnWorkItemFromSearch(foundWI);
                    MainUserInterface.WIManager.updateWorkItemsFile();
                    LoginView.teamManager.updateTeamsFile();//Updates the team's file.
                    MainUserInterface.mainFrame.dispose();
                }
            }
        });
    }

    public static void userConnected(JFrame mainFrame) {
        JLabel usernameLabel = new JLabel(LoginView.userManager.loggedInUser.getUserName() + "  (" + LoginView.userManager.loggedInUser.getPermissionLevel().name() + ")   ");
        JButton logoutButton = new JButton("Logout");

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String command = actionEvent.getActionCommand();
                if(command.equals("Logout")) {

                }
            }
        };

        usernameLabel.setMaximumSize(new Dimension(200,30));
        mb.add(Box.createHorizontalGlue());
        mb.add(usernameLabel);

        logoutButton.addActionListener(actionListener);
        logoutButton.setMaximumSize(new Dimension(80,20));
        mb.add(logoutButton);
        mainFrame.setJMenuBar(mb);

    }

}
