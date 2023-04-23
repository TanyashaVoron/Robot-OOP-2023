package gui;

import java.awt.*;
import java.io.File;
import java.util.Observer;
import java.util.TimerTask;
import java.util.Timer;
import javax.swing.*;

import log.LogWindowSource;
import save.WindowState;

public class GameInfoWindow extends WindowState{
    public GameInfoWindow(GameWindow gameWindow) {
        super("Информация о роботе", true, true, true, true, "GameInfoWindow");
        super.setSize(100, 100);
        JTextArea area = new JTextArea();
        add(area);
        Timer timer = new Timer("robotCoordinates", true);
        timer.schedule(new TimerTask() {
            public void run() {
                area.setText("X = " + (int) gameWindow.getM_visualizer().getM_robotPositionX()
                        + " \nY = " + (int) gameWindow.getM_visualizer().getM_robotPositionY());

            }
        }, 0, 5);
    }
}