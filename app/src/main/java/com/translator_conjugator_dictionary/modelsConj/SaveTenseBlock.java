package com.translator_conjugator_dictionary.modelsConj;

import com.google.gson.annotations.Expose;
import com.translator_conjugator_dictionary.modelsDictionary.SaveItem;

import java.util.List;

public class SaveTenseBlock implements SaveItem {
    private String verb;
    @Expose
    private String header;
    @Expose
    private List<ConjBlock> conjBlocks;
    private String language;

    public SaveTenseBlock(String verb, String header, List<ConjBlock> conjBlocks, String language) {
        this.verb = verb;
        this.header = header;
        this.conjBlocks = conjBlocks;
        this.language = language;
    }

    public String getVerb() {
        return verb;
    }

    public String getHeader() {
        return header;
    }

    public List<ConjBlock> getConjBlocks() {
        return conjBlocks;
    }

    public String getLanguage() {
        return language;
    }

    @Override
    public int getSaveType() {
        return SaveItem.TYPE_CONJUGATION_CONTENT;
    }
}
