package net.dinglezz.pathfinding_demo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Node extends JButton implements ActionListener {
    TestPanel testPanel;

    Node parent;
    int col;
    int row;
    int gCost;
    int hCost;
    int fCost;
    boolean start;
    boolean goal;
    boolean solid;
    boolean open;
    boolean checked;

    public Node(int col, int row, TestPanel testPanel) {
        this.col = col;
        this.row = row;
        this.testPanel = testPanel;

        setBackground(Color.white);
        setForeground(Color.black);
        addActionListener(this);
    }

    public void setAsStart() {
        setBackground(Color.blue);
        setForeground(Color.white);
        if (testPanel.labels) {
            setText("Start");
        }
        start = true;
        testPanel.startNode = this;
        testPanel.currentNode = this;
    }
    public void setAsGoal() {
        setBackground(Color.yellow);
        setForeground(Color.black);
        if (testPanel.labels) {
            setText("Goal");
        }
        goal = true;
        testPanel.goalNode = this;
    }
    public void setAsSolid() {
        setBackground(Color.black);
        setForeground(Color.black);
        if (testPanel.labels) {
            setText("Solid");
        }
        solid = true;
    }
    public void setAsOpen() {
        open = true;
    }
    public void setAsChecked() {
        if (!start && !goal) {
            setBackground(Color.orange);
            setForeground(Color.black);
            checked = true;
        }
    }
    public void setAsPath() {
        setBackground(Color.green);
        setForeground(Color.black);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!solid && !start && !goal && !(testPanel.step > 1)) {
            if (testPanel.startNode == null) {
                setAsStart();
            } else if (testPanel.goalNode == null) {
                setAsGoal();
                testPanel.setCostOnNodes();
            } else {
                setAsSolid();
                testPanel.setCostOnNodes();
            }
        } else if (solid) {
            setBackground(Color.white);
            setForeground(Color.black);
            solid = false;
            testPanel.setCostOnNodes();
        }
        testPanel.requestFocusInWindow();
    }
}
