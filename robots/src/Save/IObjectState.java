package save;

import java.beans.PropertyVetoException;
import java.io.IOException;

public interface IObjectState {
    public void save() throws IOException;
    public void load() throws IOException, ClassNotFoundException, PropertyVetoException;

}