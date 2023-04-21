package gui;

import save.WindowState;

import java.awt.*;

import javax.swing.JPanel;

public class GameWindow extends WindowState {
    public GameWindow() {
        super("Игровое поле", true, true, true, true, "GameWindow");
        GameVisualizer m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
}
