package com.presentation;

import com.business.WorkItemManager;
import com.persistent.WorkItem;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.List;

public class MainUserInterface extends JFrame {

    public static WorkItemManager WIManager = new WorkItemManager();
    public static JFrame mainFrame;
    public static JMenuBar mb;

    public static void main(String[] args) {
        WIManager = new WorkItemManager();
//        TeamManager teamManager = new TeamManager();
//        UserManager userManager = new UserManager();
//        ReportGenerator reportGenerator = new ReportGenerator();
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainUserInterface frame = new MainUserInterface();
            }
        });
    }

    public MainUserInterface() throws HeadlessException {
        mainFrame = new JFrame("Agile Project Management");
        mainFrame.setSize(1000,600);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setVisible(true);
        mb = new JMenuBar();
        workItemMenu(mainFrame);
        userMenu(mainFrame);
        teamMenu(mainFrame);
        reportGenMenu(mainFrame);
        boardsMenu(mainFrame);
        searchBox(mainFrame);
        mainFrame.setJMenuBar(mb);
        recentlyCreated(mainFrame);


        mainFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                    WIManager.updateWorkItemsFile();
                    System.exit(0);
                }
            });
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
    }

    public static void teamMenu(JFrame mainFrame) {
        //JMenuBar mb = new JMenuBar();
        JMenu menuTeam;
        JMenuItem mEpic;

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String command = actionEvent.getActionCommand();
                if(command.equals("Epic")) {

                }
            }
        };
        // Team menu
        menuTeam = new JMenu("Team Management");
        mb.add(menuTeam);
    }

    public static void userMenu(JFrame mainFrame) {
        //JMenuBar mb = new JMenuBar();;
        JMenu menuUser;
        JMenuItem mEpic;

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String command = actionEvent.getActionCommand();
                if(command.equals("Epic")) {

                }
            }
        };

        // User menu
        menuUser = new JMenu("User Management");
        mb.add(menuUser);
    }

    public static void reportGenMenu(JFrame mainFrame) {
        JMenu menuReports;
        JMenuItem mRep1, mRep2, mRep3, mRep4, mRep5;

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String command = actionEvent.getActionCommand();
                if(command.equals("Epic")) {

                }
            }
        };

        menuReports = new JMenu("Report Generator");
        mRep1 = new JMenuItem("Total planned hours per member");
        mRep2 = new JMenuItem("Work Item distribution by status");
        mRep3 = new JMenuItem("Bugs found in version");
        mRep4 = new JMenuItem("Bugs solved in version");
        mRep5 = new JMenuItem("Exceeding estimations");
        menuReports.add(mRep1); menuReports.add(mRep2); menuReports.add(mRep3); menuReports.add(mRep4); menuReports.add(mRep5);
        mb.add(menuReports);
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
    }

    public static void storiesBoard() {
        JFrame storiesBoard = new JFrame();
        storiesBoard.setSize(1000, 600);
        storiesBoard.setVisible(true);
        storiesBoard.setLayout(null);

        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Epics");

        HashMap<WorkItem, List<WorkItem>> epicsStories = new HashMap<WorkItem, List<WorkItem>>();
        for (Map.Entry<Integer, WorkItem> entry : WIManager.workItems.entrySet())
            if (entry.getValue().getType() == WorkItem.typeEnum.Epic && entry.getValue().getEpicID() != null) {
                epicsStories.put(WIManager.searchWorkItem(entry.getValue().getEpicID()), (List<WorkItem>) entry.getValue());
                DefaultMutableTreeNode e = new DefaultMutableTreeNode(entry.getValue());
                root.add(e);
            }
        HashMap<WorkItem, List<WorkItem>> storiesTasks = new HashMap<WorkItem, List<WorkItem>>();
        for (Map.Entry<Integer, WorkItem> entry : WIManager.workItems.entrySet())
            if ((entry.getValue().getType() == WorkItem.typeEnum.Task || entry.getValue().getType() == WorkItem.typeEnum.Bug)
                    && entry.getValue().getStoryID() != null) {
                epicsStories.put(WIManager.searchWorkItem(entry.getValue().getStoryID()), (List<WorkItem>) entry.getValue());
        }


        DefaultTreeModel treeModel = new DefaultTreeModel(root);
        JTree tree = new JTree(treeModel);
        storiesBoard.add(tree);

    }


    public static void searchBox(JFrame mainFrame) {
        JTextField searchBox = new JTextField();
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
        searchBox.setMaximumSize(new Dimension(200,30));
        searchBox.setToolTipText("Search Work Item...");
        mb.add(searchButton);
        searchButton.addActionListener(actionListener);
        mainFrame.setJMenuBar(mb);
    }

    public static void returnWorkItemFromSearch(WorkItem wi) {
        if (wi == null) {
            JOptionPane.showMessageDialog(mainFrame, "Could not found matching work item");
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
        }
    }

    public static void recentlyCreated(JFrame mainFrame) {
        JPanel panel = new JPanel(new BorderLayout());
        Insets insets = mainFrame.getInsets();

        //Prepare 2 dimensional array with work items data from hashmap
        String[] columnNames = {"ID", "Type", "Status", "Owner", "Summary"};
        Object[][] data = new Object[WIManager.workItems.size()][5];
        Integer item = 0;
        for (Map.Entry<Integer, WorkItem> entry : WIManager.workItems.entrySet()) {
            data[item][0] = entry.getValue().getId();
            data[item][1] = entry.getValue().getType();
            data[item][2] = entry.getValue().getStatus();
            data[item][3] = entry.getValue().getOwner();
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
                int selectedRow;
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    selectedRow = Collections.max(WIManager.workItems.keySet()) - table.getSelectedRow();
                    WorkItem foundWI = WIManager.searchWorkItem(selectedRow);
                    returnWorkItemFromSearch(foundWI);
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


}
