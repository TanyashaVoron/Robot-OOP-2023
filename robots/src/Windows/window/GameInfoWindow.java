package Windows.window;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import java.util.TimerTask;
import java.util.Timer;
import javax.swing.*;

import Windows.WindowFileState;
import Windows.window.GameWindow;
import gui.GameLogic;

public class GameInfoWindow extends WindowFileState implements Observer {
    /*
    public GameInfoWindow(GameWindow gameWindow) {
        super("Информация о роботе", true, true, true, true, "GameInfoWindow");
        super.setSize(100, 100);
        JTextArea area = new JTextArea();
        add(area);
        Timer timer = new Timer("robotCoordinates", true);
        timer.schedule(new TimerTask() {
            public void run() {
                int x = (int) gameWindow.getM_visualizer().getGameLogic().getM_robotPositionX();
                int y = (int) gameWindow.getM_visualizer().getGameLogic().getM_robotPositionY();
                area.setText("X = " + x
                        + " \nY = " + y);
            }
        }, 0, 5);
    }*/

    private  final GameLogic gameLogic;
    private final JTextArea m_text;

    public GameInfoWindow(GameWindow gameWindow) {
        super("Информация о роботе", true, true, true, true, "GameInfoWindow");
        super.setSize(100, 100);

        m_text = new JTextArea("");
        gameLogic = gameWindow.getM_visualizer();
        gameLogic.addObserver(this);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_text, BorderLayout.CENTER);

        panel.add(new JPanel(new BorderLayout()), BorderLayout.SOUTH);
        getContentPane().add(panel);
        pack();
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (gameLogic.equals(observable)) {
            m_text.setText("X = " + (int) gameLogic.getM_robotPositionX()
                    + " \nY = " + (int) gameLogic.getM_robotPositionY());
        }
    }
}