package ru.job4j.auth.exception;

import ru.job4j.auth.localization.Messages;
import java.util.Locale;

public class LocalizedException extends Exception {
    private static final long serialVersionUID = 1L;
    private final String messageKey;
    private final Locale locale;

    public LocalizedException(String messageKey) {
        this(messageKey, Locale.getDefault());
    }

    public LocalizedException(String messageKey, Locale locale) {
        this.messageKey = messageKey;
        this.locale = locale;
    }

    public String getMessage() {
        return getLocalizedMessage();
    }

    public String getLocalizedMessage() {
        return Messages.getMessageForLocale(messageKey, locale);
    }
}
