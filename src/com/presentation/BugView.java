package com.presentation;

import com.persistent.Team;
import com.persistent.User;
import com.persistent.WorkItem;
import com.persistent.WorkItemBuilder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.text.SimpleDateFormat;

public class BugView extends TaskView {

    public BugView(WorkItem wi) throws HeadlessException {
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
            title = new JLabel("Bug <" + formatter.format(timeStamp) + ">");
        }
        else {
            title = new JLabel("Bug " + wi.getId());
        }
        this.jPanel.add(title);
        size = title.getPreferredSize();
        title.setBounds(insets.left + 50 , insets.top + 20, size.width + 5, size.height);
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
                    String desc = descTextBox.getText();
                    String summary = summaryTextBox.getText();

                    String ep = storyIDTextBox.getText();
                    Integer storyId = null;
                    if (ep.length() != 0)
                        storyId = Integer.parseInt(ep);

                    String est = estimateTextBox.getText();
                    Integer estimate = null;
                    if (est.length() != 0)
                        estimate = Integer.parseInt(est);

                    String tSpent = timeSpentTextBox.getText();
                    Integer timeSpent = null;
                    if (tSpent.length() != 0)
                        timeSpent = Integer.parseInt(tSpent);

                    //validation
                    if (summary.length() == 0)
                        JOptionPane.showMessageDialog(jPanel, "Please fill summary field");
                    else if (descTextBox.getText().length() == 0)
                        JOptionPane.showMessageDialog(jPanel, "Please fill description field");
                    else if (storyId != null && MainUserInterface.WIManager.searchWorkItem(storyId) == null)
                        JOptionPane.showMessageDialog(jPanel, "Parent ID does not exist");
                    else if (storyId != null && MainUserInterface.WIManager.searchWorkItem(storyId).getType() != WorkItem.typeEnum.Story)
                        JOptionPane.showMessageDialog(jPanel, "Parent ID is not Story");
                    else { //validation passed
                        WorkItemBuilder.builder().
                                withSummary(summary).
                                withStatus((WorkItem.statusEnum) statusCombo.getModel().getSelectedItem()).
                                withDescription(desc).
                                withPriority((WorkItem.priorityEnum) priorityCombo.getModel().getSelectedItem()).
                                withOwner((String) ownerCombo.getModel().getSelectedItem()).
                                withTeam((String) teamCombo.getModel().getSelectedItem()).
                                withSprint((WorkItem.sprintEnum) sprintCombo.getModel().getSelectedItem()).
                                withEstimate(estimate).
                                withTimeSpent(timeSpent).
                                withTargetVersion(targetVersionTextBox.getText()).
                                withStoryID(storyId).
                                withFoundVersion(foundInVersionTextBox.getText()).
                                build(WorkItem.typeEnum.Bug, wi);
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
