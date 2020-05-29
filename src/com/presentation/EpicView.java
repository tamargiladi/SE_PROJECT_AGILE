package com.presentation;

import com.business.UserManager;
import com.business.WorkItemManager;
import com.persistent.WorkItem;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.text.SimpleDateFormat;

public class EpicView extends JFrame {

    protected JPanel jPanel = new JPanel();
    JTextArea descTextBox = new JTextArea("");
    JTextField summaryTextBox = new JTextField("",30);
    JComboBox statusCombo = new JComboBox(WorkItem.statusEnum.values());
    JComboBox priorityCombo = new JComboBox(WorkItem.priorityEnum.values());
    JComboBox ownerCombo = new JComboBox(LoginView.userManager.users.keySet().toArray());
    JTextField epicIDTextBox = new JTextField("",30);
    JTextField storyIDTextBox = new JTextField("",30);
    JComboBox teamCombo = new JComboBox(LoginView.teamManager.teams.keySet().toArray());
    JComboBox sprintCombo = new JComboBox(WorkItem.sprintEnum.values());
    JTextField estimateTextBox = new JTextField("",30);
    JTextField timeSpentTextBox = new JTextField("",30);
    JTextField targetVersionTextBox = new JTextField("",30);
    JTextField foundInVersionTextBox = new JTextField("",30);

    public EpicView(WorkItem wi) throws HeadlessException {
        ownerCombo.addItem("Unassigned");
        ownerCombo.setSelectedItem("Unassigned");
        teamCombo.addItem("Unassigned");
        teamCombo.setSelectedItem("Unassigned");
        setLayout(wi);
    }

    public void setLayout(WorkItem wi) throws  HeadlessException {
        Insets insets = jPanel.getInsets();
        setTitle("Work Item");
        setSize(1000,600);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setVisible(true);

        this.setContentPane(this.jPanel);
        this.jPanel.setLayout(null);
        setLayoutTitle(wi);
        setLayoutStatus(wi);
        setLayoutDescription(wi);
        setLayoutSummary(wi);
        setLayoutButtons(wi);
        setLayoutPriority(wi);
        setLayoutOwner(wi);
        setLayoutEpicID(wi);
        setLayoutStoryID(wi);
        setLayoutTeam(wi);
        setLayoutSprint(wi);
        setLayoutEstimate(wi);
        setLayoutTimeSpent(wi);
        setLayoutTargetVersion(wi);
        setLayoutFoundInVersion(wi);

        ImageIcon backIcon = new ImageIcon("src/com/presentation/images/background.png");
        JLabel background = new JLabel("", backIcon, JLabel.RIGHT);
        jPanel.add(background);
        background.setBounds(insets.left , insets.top - 35, 1000, 600);
    }

    public void setLayoutTitle(WorkItem wi) {
        Insets insets = this.jPanel.getInsets();
        Dimension size;
        JLabel title;
        if (wi == null) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            Time timeStamp = new Time(System.currentTimeMillis());
            title = new JLabel("Epic <" + formatter.format(timeStamp) + ">");
        }
        else {
            title = new JLabel("Epic " + wi.getId());
        }
        this.jPanel.add(title);
        size = title.getPreferredSize();
        title.setBounds(insets.left + 50 , insets.top + 20, size.width + 5, size.height);
    }

    public void setLayoutSummary(WorkItem wi) {
        Insets insets = this.jPanel.getInsets();
        Dimension size;
        JLabel summaryLabel = new JLabel("Summary");

        if (wi != null)
            summaryTextBox.setText(wi.getSummary());
        this.jPanel.add(summaryLabel);
        this.jPanel.add(summaryTextBox);
        size = summaryLabel.getPreferredSize();
        summaryLabel.setBounds(insets.left + 50 , insets.top + 50, size.width + 5, size.height);
        size = summaryTextBox.getPreferredSize();
        summaryTextBox.setBounds(insets.left + 120 , insets.top + 50, size.width + 320, size.height);
    }

    public void setLayoutStatus(WorkItem wi) {
        Insets insets = this.jPanel.getInsets();
        Dimension size;
        JLabel statusLabel = new JLabel("Status");
        if (wi != null)
            statusCombo.setSelectedItem(wi.getStatus());
        this.jPanel.add(statusLabel);
        this.jPanel.add(statusCombo);
        size = statusLabel.getPreferredSize();
        statusLabel.setBounds(insets.left + 800 , insets.top + 55, size.width + 5, size.height);
        size = statusCombo.getPreferredSize();
        statusCombo.setBounds(insets.left + 850 , insets.top + 50, size.width + 5, size.height);
    }

    public void setLayoutDescription(WorkItem wi) {
        Insets insets = this.jPanel.getInsets();
        Dimension size;
        JLabel descLabel = new JLabel("Description:");
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        descTextBox.setBorder(border);
        // Description
        if (wi != null)
            descTextBox.setText(wi.getDescription());
        this.jPanel.add(descLabel);
        this.jPanel.add(descTextBox);
        size = descLabel.getPreferredSize();
        descLabel.setBounds(insets.left + 50 , insets.top + 300, size.width + 5, size.height);
        size = descTextBox.getPreferredSize();
        descTextBox.setBounds(insets.left + 50 , insets.top + 325, size.width + 870, size.height + 200);
    }

    public void setLayoutButtons(WorkItem wi) {
        Insets insets = this.jPanel.getInsets();
        Dimension size;
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String command = actionEvent.getActionCommand();
                if(command.equals("Cancel")) {
                    JComponent comp = (JComponent) actionEvent.getSource();
                    Window win = SwingUtilities.getWindowAncestor(comp);
                    win.dispose();
                    MainUserInterface mainView = new MainUserInterface();
                }
                else if (command.equals("Save")) {
                    WorkItem.statusEnum status = (WorkItem.statusEnum) statusCombo.getModel().getSelectedItem();
                    String desc = descTextBox.getText();
                    String summary = summaryTextBox.getText();
                    if (summary.length() == 0)
                        JOptionPane.showMessageDialog(jPanel, "Please fill summary field");
                    else if (descTextBox.getText().length() == 0)
                        JOptionPane.showMessageDialog(jPanel, "Please fill description field");
                    else {
                        if (wi == null && WorkItemManager.getAvailableId() != 100) {
                            WorkItem newWI = MainUserInterface.WIManager.createNewWI(WorkItem.typeEnum.Epic);
                            MainUserInterface.WIManager.saveWorkItem(newWI, summary,status,desc,null, null, null, null, null,null,null,null,null,null, true);
                        }
                        else if (wi == null && WorkItemManager.getAvailableId() == 100) {
                            WorkItem newWI = MainUserInterface.WIManager.createNewWI(WorkItem.typeEnum.Epic);
                            MainUserInterface.WIManager.saveWorkItem(newWI, summary,status,desc,null, null, null, null, null,null,null,null,null,null, true);
                        }
                        else {
                            MainUserInterface.WIManager.saveWorkItem(wi, summary,status,desc,null, null, null, null, null,null,null,null,null,null, false);
                        }
//                        MainUserInterface.recentlyCreated(MainUserInterface.mainFrame);
                        MainUserInterface mainView = new MainUserInterface();
                        JComponent comp = (JComponent) actionEvent.getSource();
                        Window win = SwingUtilities.getWindowAncestor(comp);
                        win.dispose();
                    }
                }
            }
        };

        // save button
        this.jPanel.add(saveButton);
        size = saveButton.getPreferredSize();
        saveButton.setBounds(insets.right + 880, insets.top + 20, size.width + 10, size.height);
        saveButton.setBackground(Color.CYAN);
        saveButton.addActionListener(actionListener);

        // cancel button
        this.jPanel.add(cancelButton);
        size = cancelButton.getPreferredSize();
        cancelButton.setBounds(insets.right + 790, insets.top + 20, size.width + 10, size.height);
        cancelButton.setBackground(Color.WHITE);
        cancelButton.addActionListener(actionListener);
    }

    public void setLayoutPriority(WorkItem wi) {
    }

    public void setLayoutOwner(WorkItem wi) {
    }

    public void setLayoutEpicID(WorkItem wi) {
    }

    public void setLayoutStoryID(WorkItem wi) {
    }

    public void setLayoutTeam(WorkItem wi) {
    }

    public void setLayoutSprint(WorkItem wi) {
    }

    public void setLayoutEstimate(WorkItem wi) {
    }

    public void setLayoutTimeSpent(WorkItem wi) {
    }

    public void setLayoutTargetVersion(WorkItem wi) {
    }

    public void setLayoutFoundInVersion(WorkItem wi) {
    }

    public static void main(String[] args) {
    }
}
