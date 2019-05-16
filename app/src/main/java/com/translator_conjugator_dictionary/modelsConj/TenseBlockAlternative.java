package com.translator_conjugator_dictionary.modelsConj;

import java.util.List;

public class TenseBlockAlternative {
    private String header;
    private List<ConjBlock> conjBlocks;

    public TenseBlockAlternative(String header, List<ConjBlock> conjBlocks) {
        this.header = header;
        this.conjBlocks = conjBlocks;
    }

    public TenseBlockAlternative() {
    }

    public String getHeader() {
        return header;
    }

    public List<ConjBlock> getConjBlocks() {
        return conjBlocks;
    }
}
