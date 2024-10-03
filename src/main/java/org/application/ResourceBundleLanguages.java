package org.application;

import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleLanguages {

    private static ResourceBundle resourceBundle;


    private ResourceBundleLanguages(){
    }

    public static ResourceBundle getInstance(){
        if(resourceBundle==null){
            resourceBundle = ResourceBundle.getBundle("languages.messages",Locale.ENGLISH);
        }
        return resourceBundle;
    }

    public static void changeLocale(Locale locale){
        resourceBundle = ResourceBundle.getBundle("languages.messages",locale);
    }
}
