package com.translator_conjugator_dictionary.modelsConj;

public class LanguageItem {
    private String language;
    private int flagResId;

    public LanguageItem(String language, int flagResId) {
        this.language = language;
        this.flagResId = flagResId;
    }

    public String getLanguage() {
        return language;
    }

    public int getFlagResId() {
        return flagResId;
    }
}