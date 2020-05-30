package com.presentation;

import com.persistent.User;
import com.persistent.WorkItem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.text.SimpleDateFormat;

public class StoryView extends EpicView{

    public StoryView(WorkItem wi) throws HeadlessException {
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
            title = new JLabel("Story <" + formatter.format(timeStamp) + ">");
        }
        else {
            title = new JLabel("Story " + wi.getId());
        }
        this.jPanel.add(title);
        size = title.getPreferredSize();
        title.setBounds(insets.left + 50 , insets.top + 20, size.width + 5, size.height);
    }

    @Override
    public void setLayoutPriority(WorkItem wi) {
        Insets insets = this.jPanel.getInsets();
        Dimension size;
        JLabel priorityLabel = new JLabel("Priority:");
        if (wi != null)
            priorityCombo.setSelectedItem(wi.getPriority());
        jPanel.add(priorityLabel);
        jPanel.add(priorityCombo);
        size = priorityLabel.getPreferredSize();
        priorityLabel.setBounds(insets.left + 75 , insets.top + 100, size.width + 5, size.height);
        size = priorityCombo.getPreferredSize();
        priorityCombo.setBounds(insets.left + 140 , insets.top + 95, size.width + 150, size.height);
    }

    @Override
    public void setLayoutOwner(WorkItem wi) {
        Insets insets = this.jPanel.getInsets();
        Dimension size;
        JLabel ownerLabel = new JLabel("Owner:");
        if (wi != null)
            ownerCombo.setSelectedItem(wi.getOwner());
        jPanel.add(ownerLabel);
        jPanel.add(ownerCombo);
        size = ownerLabel.getPreferredSize();
        ownerLabel.setBounds(insets.left + 75 , insets.top + 140, size.width + 5, size.height);
        size = ownerCombo.getPreferredSize();
        ownerCombo.setBounds(insets.left + 140 , insets.top + 135, size.width + 115, size.height);
    }

    @Override
    public void setLayoutEpicID(WorkItem wi) {
        Insets insets = this.jPanel.getInsets();
        Dimension size;
        JLabel epicIdLabel = new JLabel("[Set Parent]   Epic ID:");
        if (wi != null && wi.getEpicID() != null)
            epicIDTextBox.setText(Integer.toString(wi.getEpicID()));
        this.jPanel.add(epicIdLabel);
        this.jPanel.add(epicIDTextBox);
        size = epicIdLabel.getPreferredSize();
        epicIdLabel.setBounds(insets.left + 75 , insets.top + 260, size.width + 5, size.height);
        size = epicIDTextBox.getPreferredSize();
        epicIDTextBox.setBounds(insets.left + 200 , insets.top + 260, size.width - 150, size.height);
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

                    String owner = (String) ownerCombo.getModel().getSelectedItem();

                    String ep = epicIDTextBox.getText();
                    Integer epicId = null;
                    if (ep.length() != 0)
                        epicId = Integer.parseInt(epicIDTextBox.getText());

                    if (summary.length() == 0)
                        JOptionPane.showMessageDialog(jPanel, "Please fill summary field");
                    else if (descTextBox.getText().length() == 0)
                        JOptionPane.showMessageDialog(jPanel, "Please fill description field");
                    else if (epicId != null && MainUserInterface.WIManager.searchWorkItem(epicId) == null)
                        JOptionPane.showMessageDialog(jPanel, "Parent ID does not exist");
                    else if (epicId != null && MainUserInterface.WIManager.searchWorkItem(epicId).getType() != WorkItem.typeEnum.Epic)
                        JOptionPane.showMessageDialog(jPanel, "Parent ID is not Epic");
                    else {
                        if (wi == null) {
                            WorkItem newWI = MainUserInterface.WIManager.createNewWI(WorkItem.typeEnum.Story);
                            MainUserInterface.WIManager.saveWorkItem(newWI, summary, status, desc, priority, owner, epicId, null, null, null, null, null, null, null, true);
                        } else {
                            MainUserInterface.WIManager.saveWorkItem(wi, summary, status, desc, priority, owner, epicId, null, null, null, null, null, null, null, false);
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

}
