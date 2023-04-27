package Windows;

import save.IObjectState;

import java.io.*;
import java.awt.Dimension;
import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import javax.swing.JInternalFrame;


public class WindowFileState extends JInternalFrame implements IObjectState {
    private final String fileName;

    public WindowFileState(String title, boolean resizable, boolean closable, boolean maximizable, boolean iconable, String name) {
        super(title, resizable, closable, maximizable, iconable);
        fileName = System.getProperty("user.home") + File.separator + name + ".txt";
    }

    public boolean fileExists() {
        File file = new File(fileName);
        return file.exists();
    }

    public void save() throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
        oos.writeObject(getLocation().x);
        oos.writeObject(getLocation().y);
        oos.writeObject(getSize());
        oos.writeObject(isIcon());
        oos.flush();
    }

    public void load() throws IOException, ClassNotFoundException, PropertyVetoException {
        ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(fileName)));
        setLocation((int) ois.readObject(), (int) ois.readObject());
        setSize((Dimension) ois.readObject());
        setIcon((boolean) ois.readObject());
    }
}