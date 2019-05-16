package com.translator_conjugator_dictionary.models;

public class SpinnerItem {

    private String language;
    private int image;

    public SpinnerItem(String language, int image) {
        this.language = language;
        this.image = image;
    }

    public String getLanguage() {
        return language;
    }

    public int getImage() {
        return image;
    }

}
