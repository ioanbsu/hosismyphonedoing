package com.artigile.howismyphonedoing.api.model;

/**
 * User: ioanbsu
 * Date: 6/13/13
 * Time: 10:56 AM
 */
public enum GwtLocale {
    US("US"),
    ENGLISH("English"),
    CANADA("Canadian"),
    FRENCH("French"),
    GERMAN("German"),
    ITALIAN("Italian"),
//    JAPANESE("Japanese"),
//    KOREAN("Korean"),
//    CHINESE("Chinese"),
//    SIMPLIFIED_CHINESE("Simplified Chinese"),
//    TAIWAN("Thai"),
    UK("UK"),
    CANADA_FRENCH("Canadian French");
    private String languageName;

    GwtLocale(String languageName) {
        this.languageName = languageName;
    }

    public static GwtLocale parse(String lanquageStringValue) {
        for (GwtLocale gwtLocale : GwtLocale.values()) {
            if (gwtLocale.getLanguageName().equals(lanquageStringValue)) {
                return gwtLocale;
            }
        }
        return null;
    }

    public String getLanguageName() {
        return languageName;
    }
}
