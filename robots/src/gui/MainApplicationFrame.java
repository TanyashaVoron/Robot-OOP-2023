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

        LogWindow logWindow = createLogWindow();
        addWindow(logWindow);

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

    // добавление окна
    protected void addWindow(JInternalFrame frame) {
        desktopPane.add(frame);
        frame.setVisible(true);
    }

    // добавление ячейки в строку меню
    private void addCellInMenuBar(JMenuBar menuBar, String name, int keyEvent, String description, JMenuItem... menuItems) {
        JMenu menu = new JMenu(name);
        menu.setMnemonic(keyEvent);
        menu.getAccessibleContext().setAccessibleDescription(description);
        for (JMenuItem menuItem : menuItems)
            menu.add(menuItem);
        menuBar.add(menu);
    }

    // создание строки ячейки
    private JMenuItem addLineInCellInMenuBar(String name, int keyEvent, ActionListener actionListener) {
        JMenuItem jMenuItem = new JMenuItem(name, keyEvent);
        jMenuItem.addActionListener(actionListener);
        return jMenuItem;
    }

    // генерация ячейки 'Режим отображения'
    private void generateCellInMenuBarDisplayMode(JMenuBar menuBar) {
        JMenuItem systemLookAndFeel = addLineInCellInMenuBar("Системная схема", KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            this.invalidate();
        });
        JMenuItem crossplatformLookAndFeel = addLineInCellInMenuBar("Универсальная схема", KeyEvent.VK_S, (event) -> {
            setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            this.invalidate();
        });
        addCellInMenuBar(menuBar, "Режим отображения", KeyEvent.VK_V, "Управление режимом отображения приложения",
                systemLookAndFeel, crossplatformLookAndFeel);
    }

    // генерация ячейки 'Тесты'
    private void generateCellInMenuBarTest(JMenuBar menuBar) {
        JMenuItem addLogMessageItem = addLineInCellInMenuBar("Сообщение в лог", KeyEvent.VK_S, (event) ->
                Logger.debug("Новая строка"));
        addCellInMenuBar(menuBar, "Тесты", KeyEvent.VK_T, "Тестовые команды", addLogMessageItem);
    }

    // генерация ячейки 'Закрыть'
    private void generateCellInMenuBarClosedWindow(JMenuBar menuBar) {
        JMenuItem quitMenuItem = addLineInCellInMenuBar("Закрыть приложение", KeyEvent.VK_Q, (event) -> closingWindow());
        addCellInMenuBar(menuBar, "Закрыть", KeyEvent.VK_O, "Закрыть", quitMenuItem);
    }

    // генерация всей строки меню
    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        generateCellInMenuBarDisplayMode(menuBar);
        generateCellInMenuBarTest(menuBar);
        generateCellInMenuBarClosedWindow(menuBar);
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
