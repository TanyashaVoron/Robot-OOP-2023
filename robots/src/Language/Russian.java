package Language;

import javax.swing.UIManager;


public class Russian implements Language {
    public final void setLocalLanguage() {
        UIManager.put("OptionPane.noButtonText", "Нет");
        UIManager.put("OptionPane.yesButtonText", "Да");
    }
}
