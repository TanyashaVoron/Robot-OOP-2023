package Windows.window;

import Windows.WindowFileState;
import Game.Game;
import Game.GameVisualizer;

import java.util.Timer;
import java.util.TimerTask;
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
        Timer m_timer = initTimer();
        m_timer.schedule(new TimerTask() {
            @Override
            public void run() {
                m_visualizer.getGame().setSizeFieldRobot(new Point(panel.getWidth() * 2, panel.getHeight() * 2));
            }
        }, 0, 1);
    }

    public Game getM_visualizer() {
        return m_visualizer.getGame();
    }

    private static Timer initTimer() {
        return new Timer("SizeFieldRobot", true);
    }
}
