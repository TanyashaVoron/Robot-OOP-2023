package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

import Language.Language;
import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    public void closingWindow() {
        int result = JOptionPane.showConfirmDialog(
                MainApplicationFrame.this,
                "Закрыть приложение?",
                "Окно подтверждения",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);

        if (result == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    public MainApplicationFrame(Language language) {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        language.setLocalLanguage();

        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);
        addWindow(createLogWindow());

        addWindow(createGameWindow());

        setJMenuBar(generateMenuBar());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    protected LogWindow createLogWindow() {
        LogWindow logWindow = new LogWindow(Logger.getDefaultLogSource());
        logWindow.setLocation(10, 10);
        logWindow.setSize(300, 800);
        setMinimumSize(logWindow.getSize());
        logWindow.pack();
        Logger.debug("Протокол работает");
        return logWindow;
    }

    protected GameWindow createGameWindow() {
        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        return gameWindow;
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
        JMenuItem menuItems = createMenuSubItems("Закрыть приложение", KeyEvent.VK_Q, (event) -> closingWindow());
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
            // just ignore
        }
    }
}
