package org.application;

import javax.swing.*;

public class LocalizedJMenu extends JMenu implements LocaleObserver {
    private String key;

    public LocalizedJMenu(String key) {
        super(ResourceBundleLanguages.getInstance().getString(key));
        this.key = key;
        LocaleManager.addObserver(this);
    }

    @Override
    public void updateLocale() {
        System.out.println("updating");
        this.setText(ResourceBundleLanguages.getInstance().getString(key));
    }
}
