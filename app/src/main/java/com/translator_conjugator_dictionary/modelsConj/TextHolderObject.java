package com.translator_conjugator_dictionary.modelsConj;

public class TextHolderObject implements IRecentSearch {

    private String text;

    public TextHolderObject(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public int getType() {
        return IRecentSearch.TYPE_TEXT_HOLDER;
    }
}
