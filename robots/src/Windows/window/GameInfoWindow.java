package Windows.window;

import java.awt.*;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;

import Windows.WindowFileState;
import Game.Game;

public class GameInfoWindow extends WindowFileState implements Observer {
    private final Game game;
    private final JTextArea m_text;

    public GameInfoWindow(GameWindow gameWindow) {
        super("Информация о роботе", true, true, true, true, "GameInfoWindow");
        super.setSize(100, 100);

        m_text = new JTextArea("");
        game = gameWindow.getM_visualizer();
        game.getRobot().addObserver(this);

        JPanel panel = new JPanel(new BorderLayout());
        panel.add(m_text, BorderLayout.CENTER);

        panel.add(new JPanel(new BorderLayout()), BorderLayout.SOUTH);
        getContentPane().add(panel);
        pack();
    }

    @Override
    public void update(Observable observable, Object arg) {
        if (game.getRobot().equals(observable)) {
            m_text.setText("X = " + game.getRobot().getPosition().x
                    + " \nY = " + game.getRobot().getPosition().y);
        }
    }
}