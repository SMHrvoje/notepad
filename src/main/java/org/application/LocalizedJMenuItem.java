package org.application;

import javax.swing.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class LocalizedJMenuItem extends JMenuItem implements LocaleObserver{

    private String key;
    private static ResourceBundle messages = ResourceBundleLanguages.getInstance();

    public LocalizedJMenuItem(String key){
        super(messages.getString(key));
        this.key=key;
        LocaleManager.addObserver(this);
    }

    @Override
    public void updateLocale() {
        messages = ResourceBundleLanguages.getInstance();
        setText(messages.getString(key));
    }
}
