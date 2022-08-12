package ru.job4j.auth.localization;

import java.util.Locale;
import java.util.ResourceBundle;

public class Messages {
    public static String getMessageForLocale(String messageKey, Locale locale) {
        return ResourceBundle.getBundle("i18n/messages", locale)
                .getString(messageKey);
    }
}
