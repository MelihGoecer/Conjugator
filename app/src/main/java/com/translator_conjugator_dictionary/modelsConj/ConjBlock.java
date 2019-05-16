package com.translator_conjugator_dictionary.modelsConj;

import com.google.gson.annotations.Expose;

public class ConjBlock {
    @Expose
    private String person;
    @Expose
    private String result;

    public ConjBlock(String person, String result) {
        this.person = person;
        this.result = result;
    }

    public ConjBlock() {
    }

    public String getPerson() {
        return person;
    }

    public String getResult() {
        return result;
    }
}
