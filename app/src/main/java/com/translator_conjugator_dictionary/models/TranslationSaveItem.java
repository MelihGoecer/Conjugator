package com.translator_conjugator_dictionary.models;

import java.util.List;

public class TranslationSaveItem {
    /*includes timestamp and language direction*/
    private String header;
    private List<Translation> translations;
    private String timestamp;


    public TranslationSaveItem(String header, List<Translation> translations, String timestamp) {
        this.header = header;
        this.translations = translations;
        this.timestamp = timestamp;
    }

    public String getHeader() {
        return header;
    }

    public List<Translation> getTranslations() {
        return translations;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
