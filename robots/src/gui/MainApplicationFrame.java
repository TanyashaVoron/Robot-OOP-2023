package gui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.*;

import log.Logger;

/**
 * Что требуется сделать:
 * 1. Метод создания меню перегружен функционалом и трудно читается.
 * Следует разделить его на серию более простых методов (или вообще выделить отдельный класс).
 */
public class MainApplicationFrame extends JFrame {
    private final JDesktopPane desktopPane = new JDesktopPane();

    // закрытие окна с подтверждением
    public void closingWindow() { // метод закрытия
        UIManager.put("OptionPane.yesButtonText", "Да"); // кнопочти на русском
        UIManager.put("OptionPane.noButtonText", "Нет");

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

    public MainApplicationFrame() {
        //Make the big window be indented 50 pixels from each edge
        //of the screen.
        int inset = 50;
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(inset, inset,
                screenSize.width - inset * 2,
                screenSize.height - inset * 2);

        setContentPane(desktopPane);
        addWindow(createLogWindow());

        GameWindow gameWindow = new GameWindow();
        gameWindow.setSize(400, 400);
        addWindow(gameWindow);

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

    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    private JMenuItem menuSubItems(String name, int keyEvent, ActionListener action) {
        JMenuItem jMenuItem = new JMenuItem(name, keyEvent);
        jMenuItem.addActionListener(action);
        return jMenuItem;
    }

    private JMenu menuItems(String name, int keyEvent, String description, JMenuItem... menuItems) {
        JMenu menu = new JMenu(name);
        menu.setMnemonic(keyEvent);
        menu.getAccessibleContext().setAccessibleDescription(description);
        for (JMenuItem menuItem : menuItems)
            menu.add(menuItem);
        return menu;
    }

    private JMenu generateMenuItemsDisplayMode() {
        JMenuItem menuSubItems1 = menuSubItems("Системная схема", KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        JMenuItem menuSubItems2 = menuSubItems("Универсальная схема", KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        return menuItems("Режим отображения", KeyEvent.VK_V, "Управление режимом отображения приложения",
                menuSubItems1, menuSubItems2);
    }

    private JMenu generateMenuItemsTest() {
        JMenuItem menuSubItems = menuSubItems("Сообщение в лог", KeyEvent.VK_S, (event) ->
                Logger.debug("Новая строка"));
        return menuItems("Тесты", KeyEvent.VK_T, "Тестовые команды", menuSubItems);
    }

    private JMenu generateMenuItemsClosedWindow() {
        JMenuItem menuSubItems = menuSubItems("Закрыть приложение", KeyEvent.VK_Q, (event) -> closingWindow());
        return menuItems("Закрыть", KeyEvent.VK_O, "Закрыть", menuSubItems);
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
