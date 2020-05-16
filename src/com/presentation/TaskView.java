package com.presentation;

import com.persistent.Team;
import com.persistent.User;
import com.persistent.WorkItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.text.SimpleDateFormat;

public class TaskView extends StoryView {

    public TaskView(WorkItem wi) throws HeadlessException {
        super(wi);
    }

    @Override
    public void setLayoutTitle(WorkItem wi) {
        Insets insets = jPanel.getInsets();
        Dimension size;
        JLabel title;
        if (wi == null) {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
            Time timeStamp = new Time(System.currentTimeMillis());
            title = new JLabel("Task <" + formatter.format(timeStamp) + ">");
        }
        else {
            title = new JLabel("Task " + wi.getId());
        }
        this.jPanel.add(title);
        size = title.getPreferredSize();
        title.setBounds(insets.left + 50 , insets.top + 20, size.width + 5, size.height);
    }

    @Override
    public void setLayoutEpicID(WorkItem wi) {
    }

    @Override
    public void setLayoutStoryID(WorkItem wi) {
        Insets insets = this.jPanel.getInsets();
        Dimension size;
        JLabel storyIDLabel = new JLabel("[Set Parent]   Story ID:");
        if (wi != null && wi.getStoryID() != null)
            storyIDTextBox.setText(Integer.toString(wi.getStoryID()));
        this.jPanel.add(storyIDLabel);
        this.jPanel.add(storyIDTextBox);
        size = storyIDLabel.getPreferredSize();
        storyIDLabel.setBounds(insets.left + 75 , insets.top + 260, size.width + 5, size.height);
        size = storyIDTextBox.getPreferredSize();
        storyIDTextBox.setBounds(insets.left + 200 , insets.top + 260, size.width - 150, size.height);
    }

    @Override
    public void setLayoutTeam(WorkItem wi) {
        Insets insets = this.jPanel.getInsets();
        Dimension size;
        JLabel teamLabel = new JLabel("Team:");
        if (wi != null)
            teamCombo.setSelectedItem(wi.getTeam());
        jPanel.add(teamLabel);
        jPanel.add(teamCombo);
        size = teamLabel.getPreferredSize();
        teamLabel.setBounds(insets.left + 75 , insets.top + 180, size.width + 5, size.height);
        size = teamCombo.getPreferredSize();
        teamCombo.setBounds(insets.left + 140 , insets.top + 175, size.width + 150, size.height);
    }

    @Override
    public void setLayoutSprint(WorkItem wi) {
        Insets insets = this.jPanel.getInsets();
        Dimension size;
        JLabel sprintLabel = new JLabel("Sprint:");
        if (wi != null)
            sprintCombo.setSelectedItem(wi.getSprint());
        jPanel.add(sprintLabel);
        jPanel.add(sprintCombo);
        size = sprintLabel.getPreferredSize();
        sprintLabel.setBounds(insets.left + 75 , insets.top + 220, size.width + 5, size.height);
        size = sprintCombo.getPreferredSize();
        sprintCombo.setBounds(insets.left + 140 , insets.top + 215, size.width + 150, size.height);
    }

    @Override
    public void setLayoutEstimate(WorkItem wi) {
        Insets insets = this.jPanel.getInsets();
        Dimension size;
        JLabel estimateLabel = new JLabel("Estimate [h]:");
        if (wi != null)
            estimateTextBox.setText(Integer.toString(wi.getEstimate()));
        this.jPanel.add(estimateLabel);
        this.jPanel.add(estimateTextBox);
        size = estimateLabel.getPreferredSize();
        estimateLabel.setBounds(insets.left + 450 , insets.top + 100, size.width + 5, size.height);
        size = estimateTextBox.getPreferredSize();
        estimateTextBox.setBounds(insets.left + 550 , insets.top + 100, size.width - 120, size.height);
    }

    @Override
    public void setLayoutTimeSpent(WorkItem wi) {
        Insets insets = this.jPanel.getInsets();
        Dimension size;
        JLabel timeSpentLabel = new JLabel("Time Spent [h]:");
        if (wi != null)
            timeSpentTextBox.setText(Integer.toString(wi.getTimeSpent()));
        this.jPanel.add(timeSpentLabel);
        this.jPanel.add(timeSpentTextBox);
        size = timeSpentLabel.getPreferredSize();
        timeSpentLabel.setBounds(insets.left + 450 , insets.top + 140, size.width + 5, size.height);
        size = timeSpentTextBox.getPreferredSize();
        timeSpentTextBox.setBounds(insets.left + 550 , insets.top + 140, size.width - 120, size.height);
    }

    @Override
    public void setLayoutTargetVersion(WorkItem wi) {
        Insets insets = this.jPanel.getInsets();
        Dimension size;
        JLabel targetVersionLabel = new JLabel("Target version:");
        if (wi != null)
            targetVersionTextBox.setText(wi.getTargetVersion());
        this.jPanel.add(targetVersionLabel);
        this.jPanel.add(targetVersionTextBox);
        size = targetVersionLabel.getPreferredSize();
        targetVersionLabel.setBounds(insets.left + 450 , insets.top + 180, size.width + 5, size.height);
        size = targetVersionTextBox.getPreferredSize();
        targetVersionTextBox.setBounds(insets.left + 550 , insets.top + 180, size.width - 120, size.height);
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

                    if (summary.length() == 0)
                        JOptionPane.showMessageDialog(jPanel, "Please fill summary field");
                    else if (storyId != null && MainUserInterface.WIManager.searchWorkItem(storyId) == null)
                        JOptionPane.showMessageDialog(jPanel, "Parent ID does not exist");
                    else if (storyId != null && MainUserInterface.WIManager.searchWorkItem(storyId).getType() != WorkItem.typeEnum.Story)
                        JOptionPane.showMessageDialog(jPanel, "Parent ID is not Epic");
                    else {
                        if (wi == null) {
                            WorkItem newWI = MainUserInterface.WIManager.createNewWI(WorkItem.typeEnum.Task);
                            MainUserInterface.WIManager.saveWorkItem(newWI, summary, status, desc, priority, owner, null, team, sprint, estimate, timeSpent, targetVersion, storyId, null);
                        } else {
                            MainUserInterface.WIManager.saveWorkItem(wi, summary, status, desc, priority, owner, null, team, sprint, estimate, timeSpent, targetVersion, storyId, null);
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
