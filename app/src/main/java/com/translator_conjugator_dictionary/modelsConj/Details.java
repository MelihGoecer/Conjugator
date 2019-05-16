package com.translator_conjugator_dictionary.modelsConj;

public class Details {
    private double confidence;
    private boolean isReliable;
    private String language;

    public Details(double confidence, boolean isReliable, String language) {
        this.confidence = confidence;
        this.isReliable = isReliable;
        this.language = language;
    }

    public double getConfidence() {
        return confidence;
    }

    public boolean isReliable() {
        return isReliable;
    }

    public String getLanguage() {
        return language;
    }
}
