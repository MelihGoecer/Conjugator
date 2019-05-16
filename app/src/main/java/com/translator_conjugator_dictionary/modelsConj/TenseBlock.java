package com.translator_conjugator_dictionary.modelsConj;

import com.google.gson.annotations.Expose;
import com.translator_conjugator_dictionary.modelsDictionary.SaveItem;

import java.util.List;

public class TenseBlock implements SaveItem {
    @Expose
    private String header;
    @Expose
    private List<ConjBlock> conjBlocks;

    public TenseBlock(String header, List<ConjBlock> conjBlocks) {
        this.header = header;
        this.conjBlocks = conjBlocks;
    }

    public TenseBlock() {
    }

    public String getHeader() {
        return header;
    }

    public List<ConjBlock> getConjBlocks() {
        return conjBlocks;
    }

    @Override
    public int getSaveType() {
        return SaveItem.TYPE_CONJUGATION_CONTENT;
    }
}
