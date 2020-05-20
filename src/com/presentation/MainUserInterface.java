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
import java.awt.Graphics;
import java.awt.Color;
import javax.swing.border.CompoundBorder;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class MainUserInterface extends JPanel {

    public static WorkItemManager WIManager = new WorkItemManager();
    public static UserManager userManager = new UserManager();
    public static TeamManager teamManager = new TeamManager();
    public static ReportGenerator reportGenerator = new ReportGenerator();
    public static JFrame mainFrame;
    public static JMenuBar mb;


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
        teamManager.addTeam("Algo");
        teamManager.addTeam("Software");
        teamManager.addTeam("QA");
        teamManager.addTeam("Hardware");
        userManager.addUser("Voldemort", "123", User.PermissionLevel.admin, algo);
        userManager.addUser("Harry Potter", "123", User.PermissionLevel.member, algo);
        userManager.addUser("Albus Dumbledore", "123", User.PermissionLevel.manager, algo);
        userManager.addUser("Yuval Levi", "123", User.PermissionLevel.manager, algo);
        userManager.login("Yuval Levi", "123");

        mainFrame = new JFrame("Agile Project Management");
        mainFrame.setResizable(false);
        mainFrame.setSize(1000,600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
        mb = new JMenuBar();
        mb.setBackground(new Color(87,160,211));;
        workItemMenu(mainFrame);
        userMenu(mainFrame);
        teamMenu(mainFrame);
        reportGenMenu(mainFrame);
        boardsMenu(mainFrame);
        searchBox(mainFrame);
        if (userManager.loggedInUser != null)
            userConnected(mainFrame);
        mainFrame.setJMenuBar(mb);
        title(mainFrame);
        recentlyCreated(mainFrame);

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
        JLabel title = new JLabel("Agile Project Management Tool");

        Font labelFont = title.getFont();
        title.setFont(new Font(labelFont.getName(), Font.PLAIN, 20));
        title.setForeground(new Color(0,49,82));
        mainFrame.add(title);
        Dimension size = title.getPreferredSize();
        title.setBounds(insets.left + 10 , insets.top - 20, size.width + 5, size.height);
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
        JMenu menuTeam;
        menuTeam = new JMenu("Team Management");
        mb.add(menuTeam);
        menuTeam.setBorder(BorderFactory.createLineBorder(new Color(70,130,180), 1));

        if (userManager.loggedInUser.getPermissionLevel() != User.PermissionLevel.admin) {
            menuTeam.setEnabled(false);
            menuTeam.setToolTipText("You have no permissions to this area");
        }
    }

    public static void userMenu(JFrame mainFrame) {
        JMenu menuUser;
        menuUser = new JMenu("User Management");
        menuUser.setBorder(BorderFactory.createLineBorder(new Color(70,130,180), 1));
        mb.add(menuUser);

        if (userManager.loggedInUser.getPermissionLevel() == User.PermissionLevel.member) {
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
        //menuReports.addSeparator();
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

    public static void searchBox(JFrame mainFrame) {
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

    public static void recentlyCreated(JFrame mainFrame) {
        JPanel panel = new JPanel(new BorderLayout());
        Insets insets = mainFrame.getInsets();
        String username;

        //Prepare 2 dimensional array with work items data from hashmap
        String[] columnNames = {"ID", "Type", "Status", "Owner", "Summary"};
        Object[][] data = new Object[WIManager.workItems.size()][5];
        Integer item = 0;
        for (Map.Entry<Integer, WorkItem> entry : WIManager.workItems.entrySet()) {
            data[item][0] = entry.getValue().getId();
            data[item][1] = entry.getValue().getType();
            data[item][2] = entry.getValue().getStatus();
            data[item][3] = username = entry.getValue().getOwner();
            data[item][4] = entry.getValue().getSummary();
            item++;
        }
        JTable jTable = new JTable(data, columnNames);
        jTable.setDefaultEditor(Object.class, null);
        jTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    Integer val = (Integer) jTable.getValueAt(row,0);
                    WorkItem foundWI = WIManager.searchWorkItem(val);
                    returnWorkItemFromSearch(foundWI);
                    WIManager.updateWorkItemsFile();
                    mainFrame.dispose();
                }
            }
        });

        // Sort work items by id - descending (most recently created)
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(jTable.getModel());
        jTable.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();

        // show on main screen
        panel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(60,10,205,410),
                BorderFactory.createTitledBorder("Recently created Work Items")));
        jTable.getColumnModel().getColumn(4).setMinWidth(250);
        jTable.setShowGrid(false);
        panel.add(jTable, BorderLayout.WEST);
        panel.add(jTable.getTableHeader(), BorderLayout.NORTH);
        mainFrame.add(panel);
    }

    public static void userConnected(JFrame mainFrame) {
        JLabel usernameLabel = new JLabel(userManager.loggedInUser.getUserName() + "   ");
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
