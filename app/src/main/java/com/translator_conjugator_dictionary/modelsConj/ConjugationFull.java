package com.translator_conjugator_dictionary.modelsConj;

import java.util.List;

public class ConjugationFull {
    private String verb;
    private List<ResultBlock> resultBlocks;
    private int verbsLength;
    private String ending;
    private int countVowels;


    public ConjugationFull(String verb, List<ResultBlock> resultBlocks, int verbsLength, String ending, int countVowels) {
        this.verb = verb;
        this.resultBlocks = resultBlocks;
        this.verbsLength = verbsLength;
        this.ending = ending;
        this.countVowels = countVowels;
    }

    public ConjugationFull() {
    }

    public String getVerb() {
        return verb;
    }

    public List<ResultBlock> getResultBlocks() {
        return resultBlocks;
    }

    public int getVerbsLength() {
        return verbsLength;
    }

    public String getEnding() {
        return ending;
    }

    public int getCountVowels() {
        return countVowels;
    }
}
