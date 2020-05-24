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
        this.jPanel.setLayout(null);
        this.setContentPane(this.jPanel);

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
        sprintLabel = new JLabel("Enter version:");
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
        this.jPanel.add(generate);
        size = generate.getPreferredSize();
        generate.setBounds(insets.left + 280 , insets.top + 15, size.width + 5, size.height);

        generate.addActionListener(actionListener);
    }

    public void generateWorkItemDistribution() {
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
        resultsTable.setDefaultEditor(Object.class, null);
        resultsTable.revalidate();
        Dimension tableSize = resultsTable.getPreferredSize();
        this.jPanel.add(jScrollPane).setBounds(insets.left + 10 , insets.top + 10, tableSize.width * 2 , tableSize.height + 25);
    }

    public void generateTotalPlannedHoursPerMember() {
        HashMap<String, Integer> sumHoursPerUser = new HashMap<>();
        MainUserInterface.reportGenerator.setChosenSprint((WorkItem.sprintEnum) sprintCombo.getModel().getSelectedItem());
        sumHoursPerUser = MainUserInterface.reportGenerator.totalPlannedHoursPerMember();

        for (Map.Entry<String, Integer> ent : sumHoursPerUser.entrySet())
            if (ent.getValue() > 0) {
                resultsTable.setVisible(true);
                //Setting table data and structure
                model.setRowCount(0);
                model.setColumnCount(0);
                String[] columnNames = {"Owner", "Total Estimated Work [h]"};
                for (String col : columnNames) //adding columns
                    model.addColumn(col);
                for (Map.Entry<String, Integer> entry : sumHoursPerUser.entrySet()) //adding rows
                    model.addRow(new Object[]{entry.getKey(), entry.getValue()});

                //set GUI
                jPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(60,60,60,60),
                        BorderFactory.createTitledBorder("Total work estimation per owner for sprint " + sprintCombo.getModel().getSelectedItem())));
                Insets insets = this.jPanel.getInsets();
                resultsTable.setDefaultEditor(Object.class, null);
                resultsTable.revalidate();
                Dimension tableSize = resultsTable.getPreferredSize();
                this.jPanel.add(jScrollPane).setBounds(insets.left + 10 , insets.top + 10, tableSize.width * 2 , tableSize.height + 25);
                return;
            }
        resultsTable.setVisible(false);
        resultsTable.revalidate();
    }


    public void generateBugsFoundInVersion() {
        List<WorkItem> bugsFound = new ArrayList<>();
        MainUserInterface.reportGenerator.setChosenVersion(versionName.getText().split("\n")[0]);
        bugsFound = MainUserInterface.reportGenerator.bugsFoundInVersion();

        //Setting table data and structure
        model.setRowCount(0);
        model.setColumnCount(0);
        String[] columnNames = {"Bug ID", "Summary", "Status", "Priority", "Owner", "Team", "Sprint", "Estimate[h]", "Time Spent[h]"};
        for (String col : columnNames) //adding columns
            model.addColumn(col);
        for (Object obj : bugsFound) //adding rows
        {
            WorkItem wi = (WorkItem) obj;
            model.addRow(new Object[]{((WorkItem) obj).getId(), ((WorkItem) obj).getSummary(), ((WorkItem) obj).getStatus(), ((WorkItem) obj).getPriority(), ((WorkItem) obj).getOwner(), ((WorkItem) obj).getTeam(), ((WorkItem) obj).getSprint(), ((WorkItem) obj).getEstimate(), ((WorkItem) obj).getTimeSpent()});
        }

        //set GUI
        jPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(60,30,60,30),
                BorderFactory.createTitledBorder("Bugs found in version " + versionName.getText())));
        Insets insets = this.jPanel.getInsets();
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

    }

    public void generateBugsSolvedInVersion() {
        List<WorkItem> bugsSolved = new ArrayList<>();
        MainUserInterface.reportGenerator.setChosenVersion(versionName.getText().split("\n")[0]);
        bugsSolved = MainUserInterface.reportGenerator.bugsSolvedInVersion();

        //Setting table data and structure
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
        jPanel.setBorder(new CompoundBorder(BorderFactory.createEmptyBorder(60,30,60,30),
                BorderFactory.createTitledBorder("Bugs found in version " + versionName.getText())));
        Insets insets = this.jPanel.getInsets();
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

    }

    public void generateExceedingEstimations() {
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

    }

}