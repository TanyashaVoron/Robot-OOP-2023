package gui;

import Language.Russian;

import java.awt.Frame;
import java.beans.PropertyVetoException;
import java.io.IOException;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class RobotsProgram {
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> {
            MainApplicationFrame frame;
            try {
                frame = new MainApplicationFrame(new Russian());
            } catch (IOException | ClassNotFoundException | PropertyVetoException e) {
                throw new RuntimeException(e);
            }
            frame.pack();
            frame.setVisible(true);
            frame.setExtendedState(Frame.MAXIMIZED_BOTH);
        });
    }
}
