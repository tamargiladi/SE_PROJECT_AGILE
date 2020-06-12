package com.presentation;

import com.business.ReportGenerator;
import com.business.WorkItemManager;
import com.persistent.Team;
import com.persistent.User;
import com.persistent.WorkItem;

import javax.swing.*;
import java.awt.Color;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;


public class MainUserInterface extends JPanel {

    public static WorkItemManager WIManager = WorkItemManager.getInstance();
    //public static UserManager userManager = new UserManager();
    //public static TeamManager teamManager = new TeamManager();
    public static ReportGenerator reportGenerator = new ReportGenerator();
    public static JFrame mainFrame;
    public static JMenuBar mb;
    private DefaultTableModel model = new DefaultTableModel(0, 0);
    private JTable recentlyCreatedTable = new JTable(model);
    private JScrollPane jScrollPane = new JScrollPane(recentlyCreatedTable);
    private DefaultTableModel modelMyTasks = new DefaultTableModel(0, 0);
    private JTable myTasksTable = new JTable(modelMyTasks);
    private JScrollPane myTasksPane = new JScrollPane(myTasksTable);



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
        /*Team algo = new Team("Algo");
        LoginView.teamManager.addTeam("Algo");
        LoginView.teamManager.addTeam("Software");
        LoginView.teamManager.addTeam("QA");
        LoginView.userManager.login("admin", "admin");
        LoginView.teamManager.addTeam("Hardware");
        LoginView.userManager.addUser("Voldemort", "123", User.PermissionLevel.admin, "Algo");
        LoginView.userManager.addUser("Harry Potter", "123", User.PermissionLevel.member, "Software");
        LoginView.userManager.addUser("Albus Dumbledore", "123", User.PermissionLevel.manager, "QA");
        LoginView.userManager.addUser("Yuval Levi", "123", User.PermissionLevel.admin, "Hardware");
        LoginView.userManager.login("Yuval Levi", "123");*/


        mainFrame = new JFrame("Agile Project Management");
        mainFrame.setResizable(false);
        mainFrame.setSize(1000, 600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mainFrame.setResizable(false);
        mainFrame.setLayout(null);
        mainFrame.setContentPane(jScrollPane);

        mb = new JMenuBar();
        mb.setBackground(new Color(87, 160, 211));
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
        myTasks(mainFrame);
        title(mainFrame);

        mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                WIManager.updateWorkItemsFile();
                LoginView.userManager.updateUsersFile();
                LoginView.teamManager.updateTeamsFile();
                System.exit(0);
            }
        });

    }


    public static void title(JFrame mainFrame) {
        Insets insets = mainFrame.getInsets();
        ImageIcon backIcon = new ImageIcon("src/com/presentation/images/background.png");
        ImageIcon vIcon = new ImageIcon("src/com/presentation/images/v-icon.png");
        Image newVIcon = vIcon.getImage().getScaledInstance(30, 40, Image.SCALE_AREA_AVERAGING);
        vIcon = new ImageIcon(newVIcon);
        JLabel title = new JLabel("Agile Project Management Tool", vIcon, JLabel.LEFT);
        JLabel background = new JLabel("", backIcon, JLabel.RIGHT);

        title.setFont(new Font("Comic Sans MS", Font.BOLD, 20));
        title.setForeground(new Color(0, 49, 82));
        mainFrame.add(title);
        mainFrame.add(background);
        title.setVisible(true);
        background.setVisible(true);
        Dimension size = title.getPreferredSize();
        title.setBounds(insets.left + 10, insets.top - 15, size.width + 5, size.height);
        background.setBounds(insets.left - 45, insets.top - 35, size.width + 700, size.height + 550);
    }

    public static void workItemMenu(JFrame mainFrame) {
        JMenu menuWorkItem;
        JMenuItem mEpic, mStory, mTask, mBug;

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String command = actionEvent.getActionCommand();
                if (command.equals("Epic")) {
                    EpicView ep = new EpicView(null);
                } else if (command.equals("Story")) {
                    StoryView ep = new StoryView(null);
                } else if (command.equals("Task")) {
                    TaskView ep = new TaskView(null);
                } else if (command.equals("Bug")) {
                    BugView ep = new BugView(null);
                }
                WIManager.updateWorkItemsFile();
                mainFrame.dispose();
            }
        };

        // Work Item menu
        menuWorkItem = new JMenu("Create New Work Item");
        mEpic = new JMenuItem("Epic");
        mEpic.addActionListener(actionListener);
        mStory = new JMenuItem("Story");
        mStory.addActionListener(actionListener);
        mTask = new JMenuItem("Task");
        mTask.addActionListener(actionListener);
        mBug = new JMenuItem("Bug");
        mBug.addActionListener(actionListener);
        menuWorkItem.add(mEpic);
        menuWorkItem.add(mStory);
        menuWorkItem.add(mTask);
        menuWorkItem.add(mBug);
        mb.add(menuWorkItem);
        menuWorkItem.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1));
    }

    public static void teamMenu(JFrame mainFrame) {

        //TODO:================================================================
        //TODO:                          TAMAR
        //TODO:================================================================


        JMenu menuTeam;
        menuTeam = new JMenu("Team Management");
        mb.add(menuTeam);
        menuTeam.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1));

        JMenuItem mClick = new JMenuItem("Team Management Area");

        // JMenuItem mAddTeam = new JMenuItem("View all teams");
      /*  JMenuItem mRemoveTeam = new JMenuItem("Remove Team");
        JMenuItem mAddUserToTeam = new JMenuItem("Add User To Team");
        JMenuItem mRemoveUserFromTeam = new JMenuItem("Remove User From Team");*/

        if (LoginView.userManager.loggedInUser.getPermissionLevel() != User.PermissionLevel.admin) {
            menuTeam.setEnabled(false);
            menuTeam.setToolTipText("You have no permissions to this area");
        } else {

            ActionListener actionListener = new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent actionEvent) {

                    TeamManagementView tvl = new TeamManagementView();

                  /* String command = actionEvent.getActionCommand();
                         TeamView tv = new TeamView(command);*/
                }
            };

            menuTeam.add(mClick);
            mClick.addActionListener(actionListener);
           /* menuTeam.add(mRemoveTeam);mRemoveTeam.addActionListener(actionListener);
            menuTeam.add(mAddUserToTeam);mAddUserToTeam.addActionListener(actionListener);
            menuTeam.add(mRemoveUserFromTeam);mRemoveUserFromTeam.addActionListener(actionListener);*/


        }

        //================================================================
        //TODO:                          TAMAR
        //================================================================
    }

    public static void userMenu(JFrame mainFrame) {
        JMenu menuUsers;
        menuUsers = new JMenu("User Management");
        menuUsers.setBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 1));
        mb.add(menuUsers);

        JMenuItem mViewUsers = new JMenuItem("User Management Area");
        menuUsers.add(mViewUsers);

        if (LoginView.userManager.loggedInUser.getPermissionLevel() == User.PermissionLevel.member) {
            menuUsers.setEnabled(false);
            menuUsers.setToolTipText("You have no permissions to this area");
        }

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String command = actionEvent.getActionCommand();
                if (command.equals(("User Management Area"))) {
                    MainUserInterface.mainFrame.dispose();
                    UserManagementView uv = new UserManagementView(command);
                }
            }
        };
        mViewUsers.addActionListener(actionListener);
    }

    public static void reopenTeamWindow() {
        TeamManagementView tv = new TeamManagementView();
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
                    dailyBoard();
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
        // Set JFrame
        ImageIcon backIcon = new ImageIcon("src/com/presentation/images/background.png");
        JLabel background = new JLabel("", backIcon, JLabel.RIGHT);

        JFrame storiesBoard = new JFrame();
        storiesBoard.setTitle("Hierarchy Board: Epic > Story > Task/Bug");
        Insets insets = storiesBoard.getInsets();
        storiesBoard.setSize(1000, 600);
        storiesBoard.setLayout(null);
        storiesBoard.setVisible(true);
        storiesBoard.setLayout(null);
        storiesBoard.setResizable(false);

        //Initialize tree
        DefaultMutableTreeNode epicT = new DefaultMutableTreeNode("<html><body style='width:600'><PRE><b>  >>  Epic\tUnassigned</PRE></html>");
        DefaultMutableTreeNode storyT = new DefaultMutableTreeNode("<html><body style='width:600'><PRE>><b>  Story\tUnassigned</PRE></html>");
        DefaultMutableTreeNode rootT = new DefaultMutableTreeNode("<html><body style='width:600'><PRE><b><font size=\"4\">\tType\t   ID\t\t   Status\t\t   Summary</PRE></html>");
        DefaultMutableTreeNode tempNodeEpic, tempNodeStory, tempNodeWI;
        JTree tree = new JTree(rootT); rootT.add(epicT); epicT.add(storyT);
        tree.setCellRenderer(new WorkItemTreeCellRenderer());
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();

        JScrollPane treeView = new JScrollPane(tree);
        storiesBoard.setContentPane(treeView);

        treeView.add(background);
        storiesBoard.add(background);
        background.setBounds(0,0, background.getPreferredSize().width, background.getPreferredSize().height);

        //Adding nodes to tree
        for (Map.Entry<Integer, WorkItem> entryEp : WIManager.workItems.entrySet()) {
            // Set EPICS
            if (entryEp.getValue().getType() == WorkItem.typeEnum.Epic) {
                tempNodeEpic = new DefaultMutableTreeNode(convertWorkItemToString(entryEp.getValue(), "story"));
                model.insertNodeInto(tempNodeEpic, root, root.getChildCount());
                // Set STORIES
                for (Map.Entry<Integer, WorkItem> entrySt : WIManager.workItems.entrySet())
                    if (entrySt.getValue().getType() == WorkItem.typeEnum.Story && entrySt.getValue().getEpicID() == entryEp.getKey()) {
                        tempNodeStory = new DefaultMutableTreeNode(convertWorkItemToString(entrySt.getValue(), "story"));
                        model.insertNodeInto(tempNodeStory, tempNodeEpic, tempNodeEpic.getChildCount());
                        // Set BUGS / TASKS
                        for (Map.Entry<Integer, WorkItem> entryTB : WIManager.workItems.entrySet())
                            if ((entryTB.getValue().getType() == WorkItem.typeEnum.Task || entryTB.getValue().getType() == WorkItem.typeEnum.Bug) && entryTB.getValue().getStoryID() == entrySt.getKey()) {
                                tempNodeWI = new DefaultMutableTreeNode(convertWorkItemToString(entryTB.getValue(), "story"));
                                model.insertNodeInto(tempNodeWI, tempNodeStory, tempNodeStory.getChildCount());
                            }
                    }

                    else if (entrySt.getValue().getType() == WorkItem.typeEnum.Story && entrySt.getValue().getEpicID() == null) {
                        tempNodeStory = new DefaultMutableTreeNode("Story " + entrySt.getKey() + ": " + entrySt.getValue().getSummary());
                        model.insertNodeInto(tempNodeStory, epicT, epicT.getChildCount());
                        for (Map.Entry<Integer, WorkItem> entryTB : WIManager.workItems.entrySet())
                            if ((entryTB.getValue().getType() == WorkItem.typeEnum.Task || entryTB.getValue().getType() == WorkItem.typeEnum.Bug) && entryTB.getValue().getStoryID() == entrySt.getKey()) {
                                tempNodeWI = new DefaultMutableTreeNode(convertWorkItemToString(entryTB.getValue(), "story"));
                                model.insertNodeInto(tempNodeWI, storyT, storyT.getChildCount());
                            }
                    }
            }
            else if (entryEp.getValue().getType() == WorkItem.typeEnum.Task && entryEp.getValue().getStoryID() == null) {
                tempNodeWI = new DefaultMutableTreeNode(convertWorkItemToString(entryEp.getValue(), "story"));
                model.insertNodeInto(tempNodeWI, storyT, storyT.getChildCount());
            }
        }
        model.reload(root);
    }

    public static void dailyBoard() {
        // Set JFrame
        JFrame dailyBoard = new JFrame();
        dailyBoard.setTitle("Daily Board");
        Insets insets = dailyBoard.getInsets();
        dailyBoard.setSize(1000, 600);
        dailyBoard.setVisible(true);
        dailyBoard.setLayout(null);
        dailyBoard.setResizable(false);

        //Initialize tree
        DefaultMutableTreeNode rootT = new DefaultMutableTreeNode("Daily Board");
        DefaultMutableTreeNode titleT = new DefaultMutableTreeNode("<html><body style='width:600'><PRE><b>Type\tID\tStatus\t\tSummary</PRE></html>");
        DefaultMutableTreeNode teamT = new DefaultMutableTreeNode("Team - Unassigned");
        DefaultMutableTreeNode ownerT = new DefaultMutableTreeNode("Owner - Unassigned");
        DefaultMutableTreeNode tempNodeTeam, tempNodeUser, tempNodeWI;
        JTree tree = new JTree(rootT); rootT.add(teamT); teamT.add(ownerT);
        tree.setCellRenderer(new WorkItemTreeCellRenderer());
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        model.insertNodeInto(new DefaultMutableTreeNode(titleT), ownerT, ownerT.getChildCount());

        //Adding nodes to tree
        for (Map.Entry<String, Team> entryTeam : LoginView.teamManager.teams.entrySet()) {
            tempNodeTeam = new DefaultMutableTreeNode(entryTeam.getKey());
            model.insertNodeInto(tempNodeTeam, root, root.getChildCount());
            for (Map.Entry<String, User> entryOwner : LoginView.userManager.users.entrySet())
                if (entryOwner.getValue().getTeamName().equals(entryTeam.getKey())) {
                    tempNodeUser = new DefaultMutableTreeNode(entryOwner.getKey());
                    model.insertNodeInto(tempNodeUser, tempNodeTeam, tempNodeTeam.getChildCount());
                    model.insertNodeInto(new DefaultMutableTreeNode(titleT), tempNodeUser, tempNodeUser.getChildCount());
                    for (Map.Entry<Integer, WorkItem> entryWorkItem : WIManager.workItems.entrySet()) {
                        if (entryWorkItem.getValue().getOwner() != null && entryWorkItem.getValue().getOwner().equals(entryOwner.getKey())) {
                            tempNodeWI = new DefaultMutableTreeNode(convertWorkItemToString(entryWorkItem.getValue(), "daily"));
                            model.insertNodeInto(tempNodeWI, tempNodeUser, tempNodeUser.getChildCount());
                        }
                    }
                }
        }
        for (Map.Entry<Integer, WorkItem> entryWorkItem : WIManager.workItems.entrySet())
            if (entryWorkItem.getValue().getOwner() != null && entryWorkItem.getValue().getOwner().equals("Unassigned"))
                model.insertNodeInto(new DefaultMutableTreeNode(convertWorkItemToString(entryWorkItem.getValue(), "daily")), ownerT, ownerT.getChildCount());

        model.reload(root);
        JScrollPane treeView = new JScrollPane(tree);
        dailyBoard.setContentPane(treeView);
    }

    public static String convertWorkItemToString(WorkItem wi, String boardType) {
        if (boardType == "daily") {
            if (wi.getStatus() == WorkItem.statusEnum.InProgress)
                return ("<html><body style='width:850'><PRE>" + wi.getType().name() + "\t" + wi.getId() + "\t" + wi.getStatus() + "\t" + wi.getSummary() + "\t</PRE></html>");
            else
                return ("<html><body style='width:850'><PRE>" + wi.getType().name() + "\t" + wi.getId() + "\t" + wi.getStatus() + "\t\t" + wi.getSummary() + "\t</PRE></html>");
        }
        else {
            if (wi.getType() == WorkItem.typeEnum.Epic)
                return ("<html><body style='width:850'><PRE><b><font size=\"4\">  >> " + wi.getType().name() + "\t" + wi.getId() + "\t\t" + wi.getStatus() + "\t\t" + wi.getSummary()  + "\t</PRE></html>");
            else if (wi.getType() == WorkItem.typeEnum.Story)
                return ("<html><body style='width:850'><PRE>> <b>" + wi.getType().name() + "\t     " + wi.getId() + "\t     " + wi.getStatus() + "\t     " + wi.getSummary()  + "\t</PRE></html>");
            else if (wi.getStatus() == WorkItem.statusEnum.InProgress)
                return ("<html><body style='width:850'><PRE>" + wi.getType().name() + "\t  " + wi.getId() + "\t\t  " + wi.getStatus() + "\t  " + wi.getSummary()  + "</PRE></html>");
            else
                return ("<html><body style='width:850'><PRE>" + wi.getType().name() + "\t  " + wi.getId() + "\t\t  " + wi.getStatus() + "\t\t  " + wi.getSummary()  + "</PRE></html>");
        }
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
        String[] columnNames = {"ID", "Type", "Status", "Owner", "Team", "Priority", "Summary"};
        for (String col : columnNames) //adding columns
            model.addColumn(col);
        for (Map.Entry<Integer, WorkItem> entry : WIManager.workItems.entrySet()) //adding rows
            model.addRow(new Object[]{entry.getValue().getId(), entry.getValue().getType(), entry.getValue().getStatus(), entry.getValue().getOwner(), entry.getValue().getTeam(), entry.getValue().getPriority(), entry.getValue().getSummary()});
        recentlyCreatedTable.setDefaultEditor(Object.class, null);

        // Sort work items by id - descending (most recently created)
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(recentlyCreatedTable.getModel());
        recentlyCreatedTable.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();

        //show on UI
        JLabel recentLabel = new JLabel("Recently created Work Items");
        jScrollPane.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(75,10,205,150),
                BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Recently Created Work Items", TitledBorder.CENTER, TitledBorder.TOP)));
        recentLabel.setVisible(true);
        recentlyCreatedTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        Dimension size = recentLabel.getPreferredSize();
        recentLabel.setBounds(insets.left + 12 , insets.top + 40, size.width * 2, size.height);
        mainFrame.add(recentLabel);
        recentLabel.setFont(new Font(recentLabel.getFont().getName(), Font.BOLD, 12));
        recentLabel.setForeground(new Color(0,49,82));
        recentlyCreatedTable.setDefaultEditor(Object.class, null);
        Dimension tableSize = recentlyCreatedTable.getPreferredSize();
        recentlyCreatedTable.getTableHeader().setBackground(Color.WHITE);
        recentlyCreatedTable.getTableHeader().setForeground(new Color(0,49,82));

        // Resize Columns
        final TableColumnModel columnModel = recentlyCreatedTable.getColumnModel();
        for (int column = 0; column < recentlyCreatedTable.getColumnCount(); column++) {
            int width = 15; // Min width
            for (int row = 0; row < recentlyCreatedTable.getRowCount(); row++) {
                TableCellRenderer renderer = recentlyCreatedTable.getCellRenderer(row, column);
                Component comp = recentlyCreatedTable.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width , width);
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }

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
                    JComponent comp = (JComponent) actionEvent.getSource();
                    Window win = SwingUtilities.getWindowAncestor(comp);
                    win.dispose();
                    LoginView.userNameTextField.setText("");
                    LoginView.passwordPasswordField.setText("");
                    LoginView frame = new LoginView();
                    LoginView.loginScreenFrame.setVisible(true);
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

    public void myTasks(JFrame mainFrame) {

        Insets insets = mainFrame.getInsets();
        //Prepare table data
        modelMyTasks.setRowCount(0);
        modelMyTasks.setColumnCount(0);
        String[] columnNames = {"ID", "Type", "Status", "Sprint", "Priority", "Summary"};
        for (String col : columnNames) //adding columns
            modelMyTasks.addColumn(col);
        for (Map.Entry<Integer, WorkItem> entry : WIManager.workItems.entrySet()) //adding rows
            if (entry.getValue().getOwner() != null && entry.getValue().getOwner().equals(LoginView.userManager.loggedInUser.getUserName()))
                modelMyTasks.addRow(new Object[]{entry.getValue().getId(), entry.getValue().getType(), entry.getValue().getStatus(), entry.getValue().getSprint(), entry.getValue().getPriority(), entry.getValue().getSummary()});
        myTasksTable.setDefaultEditor(Object.class, null);


        //show on UI
        JLabel myWorkItemsLabel = new JLabel("My Work Items");
        myTasksTable.setModel(modelMyTasks);
        myTasksTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mainFrame.add(myTasksPane).setBounds(insets.left + 12 , insets.top + 345, 825, 150);
        myWorkItemsLabel.setVisible(true);
        Dimension size = myWorkItemsLabel.getPreferredSize();
        myWorkItemsLabel.setBounds(insets.left + 12 , insets.top + 325, size.width * 2, size.height);
        mainFrame.add(myWorkItemsLabel);
        myWorkItemsLabel.setFont(new Font(myWorkItemsLabel.getFont().getName(), Font.BOLD, 12));
        myWorkItemsLabel.setForeground(new Color(0,49,82));
        myTasksTable.setDefaultEditor(Object.class, null);
        myTasksTable.getTableHeader().setBackground(Color.WHITE);
        myTasksTable.getTableHeader().setForeground(new Color(0,49,82));

        // Resize Columns
        final TableColumnModel columnModel = myTasksTable.getColumnModel();
        for (int column = 0; column < myTasksTable.getColumnCount(); column++) {
            int width = 10; // Min width
            for (int row = 0; row < myTasksTable.getRowCount(); row++) {
                TableCellRenderer renderer = myTasksTable.getCellRenderer(row, column);
                Component comp = myTasksTable.prepareRenderer(renderer, row, column);
                width = Math.max(comp.getPreferredSize().width , width);
            }
            columnModel.getColumn(column).setPreferredWidth(width);
        }

        // Sort work items by id - descending (most recently created)
        TableRowSorter<TableModel> sorter = new TableRowSorter<>(myTasksTable.getModel());
        myTasksTable.setRowSorter(sorter);
        List<RowSorter.SortKey> sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(0, SortOrder.DESCENDING));
        sorter.setSortKeys(sortKeys);
        sorter.sort();


        myTasksTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    Integer val = (Integer) myTasksTable.getValueAt(row,0);
                    WorkItem foundWI = MainUserInterface.WIManager.searchWorkItem(val);
                    MainUserInterface.returnWorkItemFromSearch(foundWI);
                    MainUserInterface.WIManager.updateWorkItemsFile();
                    LoginView.teamManager.updateTeamsFile();//Updates the team's file.
                    MainUserInterface.mainFrame.dispose();
                }
            }
        });
    }

    static class WorkItemTreeCellRenderer implements TreeCellRenderer {
        private JLabel label;
        ImageIcon bugIcon = new ImageIcon("src/com/presentation/images/bug-icon.png");
        Image resizedBugIcon = bugIcon.getImage().getScaledInstance(20,20, Image.SCALE_AREA_AVERAGING);

        ImageIcon storyIcon = new ImageIcon("src/com/presentation/images/story-icon.png");
        Image resizedStoryIcon = storyIcon.getImage().getScaledInstance(20,20, Image.SCALE_AREA_AVERAGING);

        ImageIcon epicIcon = new ImageIcon("src/com/presentation/images/epic-icon.png");
        Image resizedEpicIcon = epicIcon.getImage().getScaledInstance(20,20, Image.SCALE_AREA_AVERAGING);

        ImageIcon taskIcon = new ImageIcon("src/com/presentation/images/task-icon.png");
        Image resizedTaskIcon = taskIcon.getImage().getScaledInstance(20,20, Image.SCALE_AREA_AVERAGING);

        ImageIcon infoIcon = new ImageIcon("src/com/presentation/images/info-icon.png");
        Image resizedInfoIcon = infoIcon.getImage().getScaledInstance(20,20, Image.SCALE_AREA_AVERAGING);

        ImageIcon teamIcon = new ImageIcon("src/com/presentation/images/team-icon.png");
        Image resizedTeamIcon = teamIcon.getImage().getScaledInstance(20, 20, Image.SCALE_AREA_AVERAGING);

        ImageIcon userIcon = new ImageIcon("src/com/presentation/images/user-icon.png");
        Image resizedUserIcon = userIcon.getImage().getScaledInstance(20, 20, Image.SCALE_AREA_AVERAGING);


        WorkItemTreeCellRenderer() {
            label = new JLabel();
        }

        public Component getTreeCellRendererComponent(JTree tree, Object value, boolean selected, boolean expanded,
                                                      boolean leaf, int row, boolean hasFocus) {

            Object obj = ((DefaultMutableTreeNode) value).getUserObject();
            String text = obj.toString();
            Integer level = ((DefaultMutableTreeNode) value).getLevel();


            if (text.contains("Bug"))
                label.setIcon(new ImageIcon(resizedBugIcon));
            else if (text.contains("Story"))
                label.setIcon(new ImageIcon(resizedStoryIcon));
            else if (text.contains("Epic"))
                label.setIcon(new ImageIcon(resizedEpicIcon));
            else if (text.contains("Task"))
                label.setIcon(new ImageIcon(resizedTaskIcon));
            else if (level == 1 && !text.contains("Epic"))
                label.setIcon(new ImageIcon(resizedTeamIcon));
            else if (level == 2 && !text.contains("Story"))
                label.setIcon(new ImageIcon(resizedUserIcon));
            else
                label.setIcon(new ImageIcon(resizedInfoIcon));
            label.setText(text);
            return label;
        }
    }


}




