package Windows.window;

import Windows.WindowFileState;
import gui.GameLogic;
import gui.GameVisualizer;

import java.awt.*;

import javax.swing.JPanel;

public class GameWindow extends WindowFileState {
    private final GameVisualizer m_visualizer;
    public GameWindow() {
        super("Игровое поле", true, true, true, true, "GameWindow");
        m_visualizer = new GameVisualizer();
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_visualizer, BorderLayout.CENTER);
        getContentPane().add(panel);
        pack();
    }
    public GameLogic getM_visualizer() {
        return m_visualizer.getGameLogic();
    }
}
