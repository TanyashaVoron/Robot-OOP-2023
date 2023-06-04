package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyVetoException;
import java.io.IOException;

import javax.swing.*;

import Language.Language;
import Windows.window.GameInfoWindow;
import Windows.window.GameWindow;
import Windows.window.LogWindow;
import log.Logger;

public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();
    private final LogWindow logWindow = createLogWindow();
    private final GameWindow gameWindow = createGameWindow();
    private final GameInfoWindow gameInfoWindow = createGameInfoWindow();

    public void closingWindow() throws IOException {
        int result = JOptionPane.showConfirmDialog(
                MainApplicationFrame.this,
                "Закрыть приложение?",
                "Окно подтверждения",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            dispose();

            logWindow.save();
            gameWindow.save();
            gameInfoWindow.save();
            //logWindow.del();
            //gameWindow.del();
            //gameInfoWindow.del();

            System.exit(0);
        }
    }

    public MainApplicationFrame(Language language) throws IOException, ClassNotFoundException, PropertyVetoException {
        language.setLocalLanguage();

        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    protected LogWindow createLogWindow() throws IOException, ClassNotFoundException, PropertyVetoException {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        addWindow(logWindow);

        if (logWindow.fileExists()) {
            logWindow.load();
        } else {
            logWindow.setLocation(10, 10);
            logWindow.setSize(300, 800);
            setMinimumSize(logWindow.getSize());
            logWindow.pack();
        }

        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected GameWindow createGameWindow() throws IOException, ClassNotFoundException, PropertyVetoException {
        GameWindow gameWindow = new GameWindow();
        addWindow(gameWindow);

        if (gameWindow.fileExists()) {
            gameWindow.load();
        } else {
            gameWindow.setSize(400, 400);
        }

        return gameWindow;
    }

    protected GameInfoWindow createGameInfoWindow() throws IOException, ClassNotFoundException, PropertyVetoException {
        GameInfoWindow gameInfoWindow = new GameInfoWindow(gameWindow);
        addWindow(gameInfoWindow);

        if (gameInfoWindow.fileExists()) {
            gameInfoWindow.load();
        }

        //gameInfoWindow.setLocation(gameWindow.getLocation().x,gameWindow.getLocation().y-gameInfoWindow.getLocation().y);
        return gameInfoWindow;
    }
    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuItem createMenuSubItems(String name, int keyEvent, ActionListener action) {
        JMenuItem jMenuItem = new JMenuItem(name, keyEvent);
        jMenuItem.addActionListener(action);
        return jMenuItem;
    }

    private JMenu createMenuItems(String name, int keyEvent, String description, JMenuItem... menuItems) {
        JMenu menu = new JMenu(name);
        menu.setMnemonic(keyEvent);
        menu.getAccessibleContext().setAccessibleDescription(description);
        for (JMenuItem menuItem : menuItems)
            menu.add(menuItem);
        return menu;
    }

    private JMenu generateMenuItemsDisplayMode() {
        JMenuItem menuItemSystemDiagram = createMenuSubItems("Системная схема", KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        JMenuItem menuItemUniversalDiagram = createMenuSubItems("Универсальная схема", KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        return createMenuItems("Режим отображения", KeyEvent.VK_V, "Управление режимом отображения приложения",
                menuItemSystemDiagram, menuItemUniversalDiagram);
    }

    private JMenu generateMenuItemsTest() {
        JMenuItem menuItems = createMenuSubItems("Сообщение в лог", KeyEvent.VK_S, (event) ->
                Logger.debug("Новая строка"));
        return createMenuItems("Тесты", KeyEvent.VK_T, "Тестовые команды", menuItems);
    }

    private JMenu generateMenuItemsClosedWindow() {
        JMenuItem menuItems = createMenuSubItems("Закрыть приложение", KeyEvent.VK_Q, (event) -> {
            try {
                closingWindow();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        return createMenuItems("Закрыть", KeyEvent.VK_O, "Закрыть", menuItems);
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(generateMenuItemsDisplayMode());
        menuBar.add(generateMenuItemsTest());
        menuBar.add(generateMenuItemsClosedWindow());
        return menuBar;
    }

    private void setLookAndFeel(String className) {
        try {
            UIManager.setLookAndFeel(className);
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException
                 | IllegalAccessException | UnsupportedLookAndFeelException e) {
        }
    }
}
