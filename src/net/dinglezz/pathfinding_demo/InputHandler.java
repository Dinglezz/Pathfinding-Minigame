package net.dinglezz.pathfinding_demo;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class InputHandler implements KeyListener {
    TestPanel testPanel;

    public  InputHandler(TestPanel testPanel) {
        this.testPanel = testPanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_Q) {
            testPanel.resetAll();
            testPanel.placeNodes();
        }

        if (code == KeyEvent.VK_F) {
            testPanel.labels = !testPanel.labels;
            testPanel.setCostOnNodes();
        }

        if (code == KeyEvent.VK_E) {
            testPanel.autoSearch = !testPanel.autoSearch;
        }

        if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER) {
            if (testPanel.autoSearch) {
                testPanel.autoSearch();
            } else {
                testPanel.search();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
