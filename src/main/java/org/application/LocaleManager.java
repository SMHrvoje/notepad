package org.application;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LocaleManager {

    private static List<LocaleObserver> observers = new ArrayList<>();



    public static void addObserver(LocaleObserver observer) {
        observers.add(observer);
    }

    public static void removeObserver(LocaleObserver observer) {
        observers.remove(observer);
    }

    public static void notifyObservers() {
        for (LocaleObserver observer : observers) {
            observer.updateLocale();
        }
    }
    public static void changeLocale(Locale locale) {
        ResourceBundleLanguages.changeLocale(locale);
        notifyObservers();
    }
}
