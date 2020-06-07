package com.presentation;

import com.persistent.User;
import com.persistent.WorkItem;
import javafx.application.Application;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

public class ReportView extends JFrame {

    private JPanel jPanel = new JPanel();
    private String reportName;
    JTextArea versionName = new JTextArea("");
    JComboBox sprintCombo = new JComboBox(WorkItem.sprintEnum.values());
    JButton generate = new JButton("Generate Report");
    DefaultTableModel model = new DefaultTableModel(0,0);
    JTable resultsTable = new JTable(model);
    JScrollPane jScrollPane = new JScrollPane(resultsTable);
    JLabel error = new JLabel("No matching results");
    JLabel title = new JLabel();

    ImageIcon backIcon = new ImageIcon("src/com/presentation/images/background.png");
    JLabel background = new JLabel("", backIcon, JLabel.RIGHT);

    public ReportView(String reportName) throws HeadlessException {
        this.reportName = reportName;
        setLayout();
    }


    public void setLayout() {
        Insets insets = jPanel.getInsets();
        setTitle("Report Generator");
        setSize(1000,600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setVisible(true);
        setResizable(false);
        jPanel.setLayout(null);
        jPanel.add(title);
        setContentPane(jPanel);
        resultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);


        if (reportName == "Work Item distribution by status" || reportName == "Total planned hours per member") {
            setLayoutSprint();
            setLayoutButtonGen();
        }
        else if (reportName == "Bugs found in version" || reportName == "Bugs solved in version") {
            setLayoutVersion();
            setLayoutButtonGen();
        }
        else { //exceeding estimations
            setLayoutButtonGen();
        }


        error.setVisible(false);
        this.jPanel.add(error);
        error.setBounds(insets.left + 25 , insets.top + 70, error.getPreferredSize().width + 5, error.getPreferredSize().height);

        jPanel.add(background);
        background.setBounds(insets.left , insets.top - 35, 1000, 600);
    }

    public void setLayoutVersion() {
        Insets insets = this.jPanel.getInsets();
        Dimension size;
        JLabel verLabel;
        verLabel = new JLabel("Enter version:");
        this.jPanel.add(verLabel);
        this.jPanel.add(versionName);
        size = verLabel.getPreferredSize();
        verLabel.setBounds(insets.left + 20 , insets.top + 20, size.width + 5, size.height);
        size = versionName.getPreferredSize();
        versionName.setBounds(insets.left + 120 , insets.top + 20, size.width + 150, size.height + 5);
        versionName.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        versionName.addKeyListener(new KeyListener() {
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

    public void setLayoutSprint() {
        Insets insets = this.jPanel.getInsets();
        Dimension size;
        JLabel sprintLabel;
        sprintLabel = new JLabel("Choose Sprint:");
        this.jPanel.add(sprintLabel);
        this.jPanel.add(sprintCombo);
        size = sprintLabel.getPreferredSize();
        sprintLabel.setBounds(insets.left + 20 , insets.top + 20, size.width + 5, size.height);
        size = sprintCombo.getPreferredSize();
        sprintCombo.setBounds(insets.left + 120 , insets.top + 15, size.width + 25, size.height);
    }

    public void setLayoutButtonGen() {
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String command = actionEvent.getActionCommand();
                if (reportName == "Work Item distribution by status")
                    generateWorkItemDistribution();
                else if (reportName == "Total planned hours per member")
                    generateTotalPlannedHoursPerMember();
                else if (reportName == "Bugs found in version")
                    generateBugsFoundInVersion();
                else if (reportName == "Bugs solved in version")
                    generateBugsSolvedInVersion();
                else //exceeding estimations
                    generateExceedingEstimations();
            }
        };

        Insets insets = this.jPanel.getInsets();
        Dimension size;
        jPanel.add(generate);
        size = generate.getPreferredSize();
        generate.setBounds(insets.left + 280 , insets.top + 15, size.width + 5, size.height);

        generate.addActionListener(actionListener);
    }

    // Generate reports functions
    public void generateWorkItemDistribution() {
        jPanel.remove(background);
        jPanel.remove(title);
        HashMap<WorkItem.statusEnum, Integer> distribution = new HashMap<>();
        MainUserInterface.reportGenerator.setChosenSprint((WorkItem.sprintEnum) sprintCombo.getModel().getSelectedItem());
        distribution = MainUserInterface.reportGenerator.workItemStatusDistribution();

       //Setting table data and structure
        model.setRowCount(0);
        model.setColumnCount(0);
        String[] columnNames = {"Work Item Type", "Amount"};
        for (String col : columnNames) //adding columns
            model.addColumn(col);
        for (Map.Entry<WorkItem.statusEnum, Integer> entry : distribution.entrySet()) //adding rows
            model.addRow(new Object[]{entry.getKey(), entry.getValue()});

        //set GUI
        jPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(60,60,60,60),
                BorderFactory.createTitledBorder("Work Item Distribution by Status for Sprint " + sprintCombo.getModel().getSelectedItem())));
        Insets insets = this.jPanel.getInsets();
        title = new JLabel("Work Item Distribution by Status for Sprint " + sprintCombo.getModel().getSelectedItem());
        title.setBounds(insets.left + 5, insets.top - 15, title.getPreferredSize().width, title.getPreferredSize().height);
        jPanel.add(title);
        resultsTable.setDefaultEditor(Object.class, null);
        resultsTable.revalidate();
        Dimension tableSize = resultsTable.getPreferredSize();
        this.jPanel.add(jScrollPane).setBounds(insets.left + 10 , insets.top + 10, tableSize.width * 2 , tableSize.height + 25);
        jPanel.add(background);
        resultsTable.getTableHeader().setBackground(Color.WHITE);
        resultsTable.getTableHeader().setForeground(new Color(0,49,82));

    }

    public void generateTotalPlannedHoursPerMember() {
        jPanel.remove(background);
        jPanel.remove(title);
        HashMap<String, Integer> sumHoursPerUser = new HashMap<>();
        MainUserInterface.reportGenerator.setChosenSprint((WorkItem.sprintEnum) sprintCombo.getModel().getSelectedItem());
        sumHoursPerUser = MainUserInterface.reportGenerator.totalPlannedHoursPerMember();

        if (sumHoursPerUser.size() > 0) {
            error.setVisible(false);
            jScrollPane.setVisible(true);
            for (Map.Entry<String, Integer> ent : sumHoursPerUser.entrySet()) {
                resultsTable.setVisible(true);
                //Setting table data and structure
                model.setRowCount(0);
                model.setColumnCount(0);
                String[] columnNames = {"Owner", "Total Estimated Work [h]", "Team"};
                for (String col : columnNames) //adding columns
                    model.addColumn(col);
                for (Map.Entry<String, Integer> entry : sumHoursPerUser.entrySet()) //adding rows
                    if (entry.getKey() == null || LoginView.userManager.users.get(entry.getKey()) == null)
                        model.addRow(new Object[]{"Unassigned", entry.getValue(), "Unassigned"});
                    else if (LoginView.userManager.users.get(entry.getKey()).getTeamName() != null && LoginView.teamManager.teams.get(LoginView.userManager.users.get(entry.getKey()).getTeamName())!= null)
                        model.addRow(new Object[]{entry.getKey(), entry.getValue(), LoginView.userManager.users.get(entry.getKey()).getTeamName()});
                    else
                        model.addRow(new Object[]{entry.getKey(), entry.getValue(), "Unassigned"});

                //set GUI
                jPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(60, 60, 60, 60),
                        BorderFactory.createTitledBorder("Total work estimation per owner for sprint " + sprintCombo.getModel().getSelectedItem())));
                Insets insets = this.jPanel.getInsets();
                title = new JLabel("Total work estimation per owner for sprint " + sprintCombo.getModel().getSelectedItem());
                title.setBounds(insets.left + 5, insets.top - 15, title.getPreferredSize().width, title.getPreferredSize().height);
                jPanel.add(title);
                resultsTable.setDefaultEditor(Object.class, null);
                resultsTable.revalidate();
                Dimension tableSize = resultsTable.getPreferredSize();
                this.jPanel.add(jScrollPane).setBounds(insets.left + 10, insets.top + 10, tableSize.width * 2, tableSize.height + 40);
                jPanel.add(background);
                return;
            }
        }
        else {
                jScrollPane.setVisible(false);
                error.setVisible(true);
            }
        jPanel.add(background);
    }

    public void generateBugsFoundInVersion() {
        jPanel.remove(background);
        jPanel.remove(title);
        List<WorkItem> bugsFound = new ArrayList<>();
        MainUserInterface.reportGenerator.setChosenVersion(versionName.getText().split("\n")[0]);
        bugsFound = MainUserInterface.reportGenerator.bugsFoundInVersion();

        //Setting table data and structure
        model.setRowCount(0);
        model.setColumnCount(0);
        String[] columnNames = {"Bug ID", "Summary", "Status", "Priority", "Owner", "Team", "Sprint", "Estimate[h]", "Time Spent[h]"};

        if (bugsFound.size() > 0) {
            error.setVisible(false);
            jScrollPane.setVisible(true);
            for (String col : columnNames) //adding columns
                model.addColumn(col);
            for (Object obj : bugsFound) //adding rows
            {
                WorkItem wi = (WorkItem) obj;
                model.addRow(new Object[]{((WorkItem) obj).getId(), ((WorkItem) obj).getSummary(), ((WorkItem) obj).getStatus(), ((WorkItem) obj).getPriority(), ((WorkItem) obj).getOwner(), ((WorkItem) obj).getTeam(), ((WorkItem) obj).getSprint(), ((WorkItem) obj).getEstimate(), ((WorkItem) obj).getTimeSpent()});
            }
            //set GUI
            jPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(60, 30, 60, 30),
                    BorderFactory.createTitledBorder("Bugs found in version " + versionName.getText())));
            Insets insets = this.jPanel.getInsets();
            title = new JLabel("Bugs found in version " + versionName.getText());
            title.setBounds(insets.left + 5, insets.top - 15, title.getPreferredSize().width, title.getPreferredSize().height);
            jPanel.add(title);
            resultsTable.setDefaultEditor(Object.class, null);
            resultsTable.revalidate();
            Dimension tableSize = resultsTable.getPreferredSize();
            this.jPanel.add(jScrollPane).setBounds(insets.left + 10, insets.top + 10, jPanel.getWidth() - 2 * insets.left - 20, tableSize.height + 40);
        }
        else {
            jScrollPane.setVisible(false);
            error.setVisible(true);
        }

        resultsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                int selectedRow;
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    Integer val = (Integer) resultsTable.getValueAt(row,0);
                    WorkItem foundWI = MainUserInterface.WIManager.searchWorkItem(val);
                    MainUserInterface.returnWorkItemFromSearch(foundWI);
                    MainUserInterface.WIManager.updateWorkItemsFile();
                    MainUserInterface.mainFrame.dispose();
                }
            }
        });
        jPanel.add(background);
    }

    public void generateBugsSolvedInVersion() {
        jPanel.remove(background);
        jPanel.remove(title);
        List<WorkItem> bugsSolved = new ArrayList<>();
        MainUserInterface.reportGenerator.setChosenVersion(versionName.getText().split("\n")[0]);
        bugsSolved = MainUserInterface.reportGenerator.bugsSolvedInVersion();

        //Setting table data and structure
        if (bugsSolved.size() > 0) {
            jScrollPane.setVisible(true);
            error.setVisible(false);
            model.setRowCount(0);
            model.setColumnCount(0);
            String[] columnNames = {"Bug ID", "Summary", "Status", "Priority", "Owner", "Team", "Sprint", "Estimate[h]", "Time Spent[h]"};
            for (String col : columnNames) //adding columns
                model.addColumn(col);
            for (Object obj : bugsSolved) //adding rows
            {
                WorkItem wi = (WorkItem) obj;
                model.addRow(new Object[]{((WorkItem) obj).getId(), ((WorkItem) obj).getSummary(), ((WorkItem) obj).getStatus(), ((WorkItem) obj).getPriority(), ((WorkItem) obj).getOwner(), ((WorkItem) obj).getTeam(), ((WorkItem) obj).getSprint(), ((WorkItem) obj).getEstimate(), ((WorkItem) obj).getTimeSpent()});
            }

            //set GUI
            jPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(60, 30, 60, 30),
                    BorderFactory.createTitledBorder("Bugs solved in version " + versionName.getText())));
            Insets insets = this.jPanel.getInsets();
            title = new JLabel("Bugs solved in version " + versionName.getText());
            title.setBounds(insets.left + 5, insets.top - 15, title.getPreferredSize().width, title.getPreferredSize().height);
            jPanel.add(title);
            resultsTable.setDefaultEditor(Object.class, null);
            resultsTable.revalidate();
            Dimension tableSize = resultsTable.getPreferredSize();
            this.jPanel.add(jScrollPane).setBounds(insets.left + 10, insets.top + 10, jPanel.getWidth() - 2 * insets.left - 20, tableSize.height + 40);
        }
        else {
            jScrollPane.setVisible(false);
            error.setVisible(true);
        }

        resultsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                int selectedRow;
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    Integer val = (Integer) resultsTable.getValueAt(row,0);
                    WorkItem foundWI = MainUserInterface.WIManager.searchWorkItem(val);
                    MainUserInterface.returnWorkItemFromSearch(foundWI);
                    MainUserInterface.WIManager.updateWorkItemsFile();
                    MainUserInterface.mainFrame.dispose();
                }
            }
        });
        jPanel.add(background);

    }

    public void generateExceedingEstimations() {
        jPanel.remove(background);
        jPanel.remove(title);
        List<WorkItem> exceedingEstimations = new ArrayList<>();
        exceedingEstimations = MainUserInterface.reportGenerator.exceedingEstimations();

        //Setting table data and structure
        model.setRowCount(0);
        model.setColumnCount(0);
        String[] columnNames = {"Work Item ID", "Type", "Summary", "Status", "Priority", "Owner", "Team", "Sprint", "Estimate[h]", "Time Spent[h]"};
        for (String col : columnNames) //adding columns
            model.addColumn(col);
        for (Object obj : exceedingEstimations) //adding rows
        {
            WorkItem wi = (WorkItem) obj;
            model.addRow(new Object[]{((WorkItem) obj).getId(), ((WorkItem) obj).getType(), ((WorkItem) obj).getSummary(), ((WorkItem) obj).getStatus(), ((WorkItem) obj).getPriority(), ((WorkItem) obj).getOwner(), ((WorkItem) obj).getTeam(), ((WorkItem) obj).getSprint(), ((WorkItem) obj).getEstimate(), ((WorkItem) obj).getTimeSpent()});
        }

        //set GUI
        jPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(45,30,45,30),
                BorderFactory.createTitledBorder("Exceeding Estimations")));
        Insets insets = this.jPanel.getInsets();
        title = new JLabel("Exceeding Estimations");
        title.setBounds(insets.left + 5, insets.top - 25, title.getPreferredSize().width, title.getPreferredSize().height);
        jPanel.add(title);
        resultsTable.setDefaultEditor(Object.class, null);
        resultsTable.revalidate();
        Dimension tableSize = resultsTable.getPreferredSize();
        this.jPanel.add(jScrollPane).setBounds(insets.left + 10 , insets.top + 10, jPanel.getWidth() - 2 * insets.left - 20, tableSize.height + 40);

        resultsTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                JTable table =(JTable) mouseEvent.getSource();
                Point point = mouseEvent.getPoint();
                int row = table.rowAtPoint(point);
                if (mouseEvent.getClickCount() == 2 && table.getSelectedRow() != -1) {
                    Integer val = (Integer) resultsTable.getValueAt(row,0);
                    WorkItem foundWI = MainUserInterface.WIManager.searchWorkItem(val);
                    MainUserInterface.returnWorkItemFromSearch(foundWI);
                    MainUserInterface.WIManager.updateWorkItemsFile();
                    MainUserInterface.mainFrame.dispose();
                }
            }
        });
        jPanel.add(background);
    }

}
