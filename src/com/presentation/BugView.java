package com.presentation;

import com.persistent.Team;
import com.persistent.User;
import com.persistent.WorkItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BugView extends TaskView {

    public BugView(WorkItem wi) throws HeadlessException {
        super(wi);
    }

    @Override
    public void setLayoutFoundInVersion(WorkItem wi) {
        Insets insets = this.jPanel.getInsets();
        Dimension size;
        JLabel foundVersionLabel = new JLabel("Found in version:");
        if (wi != null)
            foundInVersionTextBox.setText(wi.getFoundVersion());
        this.jPanel.add(foundVersionLabel);
        this.jPanel.add(foundInVersionTextBox);
        size = foundVersionLabel.getPreferredSize();
        foundVersionLabel.setBounds(insets.left + 450 , insets.top + 220, size.width + 5, size.height);
        size = foundInVersionTextBox.getPreferredSize();
        foundInVersionTextBox.setBounds(insets.left + 550 , insets.top + 220, size.width - 120, size.height);
    }

    @Override
    public void setLayoutButtons(WorkItem wi) {
        Insets insets = this.jPanel.getInsets();
        Dimension size;
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                String command = actionEvent.getActionCommand();
                if (command.equals("Cancel")) {
                    JComponent comp = (JComponent) actionEvent.getSource();
                    Window win = SwingUtilities.getWindowAncestor(comp);
                    win.dispose();
                    MainUserInterface mainView = new MainUserInterface();
                } else if (command.equals("Save")) {
                    WorkItem.statusEnum status = (WorkItem.statusEnum) statusCombo.getModel().getSelectedItem();
                    String desc = descTextBox.getText();
                    String summary = summaryTextBox.getText();
                    WorkItem.priorityEnum priority = (WorkItem.priorityEnum) priorityCombo.getModel().getSelectedItem();

                    //TODO: update owner as user
                    //User owner = ownerCombo.getModel().getSelectedItem();
                    User owner = null;

                    String ep = storyIDTextBox.getText();
                    Integer storyId = null;
                    if (ep.length() != 0)
                        storyId = Integer.parseInt(ep);

                    //TODO:update to team
                    //Team team = teamCombo.getModel().getSelectedItem();
                    Team team = null;

                    WorkItem.sprintEnum sprint = (WorkItem.sprintEnum) sprintCombo.getModel().getSelectedItem();

                    String est = estimateTextBox.getText();
                    Integer estimate = null;
                    if (est.length() != 0)
                        estimate = Integer.parseInt(est);

                    String tSpent = timeSpentTextBox.getText();
                    Integer timeSpent = null;
                    if (tSpent.length() != 0)
                        timeSpent = Integer.parseInt(tSpent);

                    String targetVersion = targetVersionTextBox.getText();
                    String foundVersion = foundInVersionTextBox.getText();

                    if (summary.length() == 0)
                        JOptionPane.showMessageDialog(jPanel, "Please fill summary field");
                    else if (storyId != null && MainUserInterface.WIManager.searchWorkItem(storyId) == null)
                        JOptionPane.showMessageDialog(jPanel, "Parent ID does not exist");
                    else if (storyId != null && MainUserInterface.WIManager.searchWorkItem(storyId).getType() != WorkItem.typeEnum.Bug)
                        JOptionPane.showMessageDialog(jPanel, "Parent ID is not Epic");
                    else {
                        if (wi == null) {
                            WorkItem newWI = MainUserInterface.WIManager.createNewWI(WorkItem.typeEnum.Bug);
                            MainUserInterface.WIManager.saveWorkItem(newWI, summary, status, desc, priority, owner, null, team, sprint, estimate, timeSpent, targetVersion, storyId, foundVersion);
                        } else {
                            MainUserInterface.WIManager.saveWorkItem(wi, summary, status, desc, priority, owner, null, team, sprint, estimate, timeSpent, targetVersion, storyId, foundVersion);
                        }
                        MainUserInterface.recentlyCreated(MainUserInterface.mainFrame);
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

}
