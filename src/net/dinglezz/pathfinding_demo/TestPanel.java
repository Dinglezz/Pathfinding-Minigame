package net.dinglezz.pathfinding_demo;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public class TestPanel extends JPanel {
    // Screen Setting
    final int maxCols = 15;
    final int maxRows = 10;
    final int nodeSize = 70;
    final int screenWidth = nodeSize * maxCols;
    final int screenHeight = nodeSize * maxRows;

    // Nodes
    HashMap<Point, Node> nodeMap = new HashMap<>();
    Node startNode, goalNode, currentNode;
    ArrayList<Node> openList = new ArrayList<>();
    ArrayList<Node> checkedList = new ArrayList<>();

    // Others
    boolean goalReached = false;
    boolean autoSearch = false;
    boolean labels = true;
    int step = 0;

    public TestPanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setLayout(new GridLayout(maxRows, maxCols));
        this.addKeyListener(new InputHandler(this));
        this.setFocusable(true);

        placeNodes();
    }
    public void placeNodes() {
        // Clear existing nodes
        this.removeAll();
        nodeMap.clear();

        // Place Nodes
        int col = 0;
        int row = 0;

        while (col < maxCols && row < maxRows) {
            Node newNode = new Node(col, row, this);
            nodeMap.put(new Point(col, row), newNode);
            this.add(newNode);

            col++;
            if (col == maxCols) {
                col = 0;
                row++;
            }
        }
        setCostOnNodes();
    }
    public void resetAll() {
        for (Node node : nodeMap.values()) {
            node.start = false;
            node.goal = false;
            node.solid = false;
            node.open = false;
            node.checked = false;
            node.setBackground(Color.white);
            node.setForeground(Color.black);
            node.setText(labels ? "" : null); // Update text based on labels state
        }
        nodeMap.clear();
        startNode = null;
        goalNode = null;
        currentNode = null;
        openList.clear();
        checkedList.clear();
        goalReached = false;
        step = 0;
    }
    void setCostOnNodes() {
        int col = 0;
        int row = 0;

        while (col < maxCols && row < maxRows) {
            getCost(nodeMap.get(new Point(col, row)));

            col++;
            if (col == maxCols) {
                col = 0;
                row++;
            }
        }
    }
    private void getCost(Node node) {
        if (startNode != null && goalNode != null) {
            // Get G cost (distance from the start node)
            int xDistance = Math.abs(node.col - startNode.col);
            int yDistance = Math.abs(node.row - startNode.row);
            node.gCost = xDistance + yDistance;

            // Get H cost (distance from the goal node)
            xDistance = Math.abs(node.col - goalNode.col);
            yDistance = Math.abs(node.row - goalNode.row);
            node.hCost = xDistance + yDistance;

            // Get F cost (distance from the total node)
            node.fCost = node.gCost + node.hCost;
        }
        // Display the cost node
        if (node != startNode && node != goalNode && labels) {
            node.setText("<html>F:" + node.fCost + "<br>G:" + node.gCost + "<html>");
        } else if (!labels) {
            node.setText("");
        } else if (node == startNode) {
            node.setText("Start");
        } else if (node == goalNode) {
            node.setText("Goal");
        }
    }
    public void search() {
        if (!goalReached) {
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            // Open the node above
            if (row - 1 >= 0) {
                openNode(nodeMap.get(new Point(col, row - 1)));
            }
            // Open the node on the left
            if (col - 1 >= 0) {
                openNode(nodeMap.get(new Point(col - 1, row)));
            }
            // Open the node below
            if (row + 1 < maxRows) {
                openNode(nodeMap.get(new Point(col, row + 1)));
            }
            // Open the node on the right
            if (col + 1 < maxCols) {
                openNode(nodeMap.get(new Point(col + 1, row )));
            }

            // Find the best node
            int bestNodeIndex = 0;
            int bestNodeFCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                // Check if this node's F cost is better
                if (openList.get(i).fCost < bestNodeFCost) {
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).fCost;
                } else if (openList.get(i).fCost == bestNodeFCost) { // If F cost is equal, check G cost
                    if (openList.get(i).gCost < openList.get(i).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            // After loop, get the best node which is the next step
            currentNode = openList.get(bestNodeIndex);

            if (currentNode == goalNode) {
                goalReached = true;
            }
            step++;
        }
    }
    public void autoSearch() {
        while (!goalReached && step < 300) {
            int col = currentNode.col;
            int row = currentNode.row;

            currentNode.setAsChecked();
            checkedList.add(currentNode);
            openList.remove(currentNode);

            // Open the node above
            if (row - 1 >= 0) {
                openNode(nodeMap.get(new Point(col, row - 1)));
            }
            // Open the node on the left
            if (col - 1 >= 0) {
                openNode(nodeMap.get(new Point(col - 1, row)));
            }
            // Open the node below
            if (row + 1 < maxRows) {
                openNode(nodeMap.get(new Point(col, row + 1)));
            }
            // Open the node on the right
            if (col + 1 < maxCols) {
                openNode(nodeMap.get(new Point(col + 1, row )));
            }

            // Find the best node
            int bestNodeIndex = 0;
            int bestNodeFCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                // Check if this node's F cost is better
                if (openList.get(i).fCost < bestNodeFCost) {
                    bestNodeIndex = i;
                    bestNodeFCost = openList.get(i).fCost;
                } else if (openList.get(i).fCost == bestNodeFCost) { // If F cost is equal, check G cost
                    if (openList.get(i).gCost < openList.get(i).gCost) {
                        bestNodeIndex = i;
                    }
                }
            }

            // After loop, get the best node which is the next step
            currentNode = openList.get(bestNodeIndex);

            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }
        }
        step++;
    }
    private void openNode(Node node) {
        if (!node.open && !node.checked && !node.solid) {
            // If it is not opened, add it to the list
            node.setAsOpen();
            node.parent = currentNode;
            openList.add(node);
        }
    }
    private void trackThePath() {
        // Backtrack and draw the best path
        Node current = goalNode;

        while (current != startNode) {
            current = current.parent;
            if (current != startNode) {
                current.setAsPath();
            }
        }
    }
}
