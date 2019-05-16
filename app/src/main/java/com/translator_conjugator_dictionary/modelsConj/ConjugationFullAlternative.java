package com.translator_conjugator_dictionary.modelsConj;

import java.util.List;

public class ConjugationFullAlternative {

    private String verb;
    private List<ResultBlockAlternative> resultBlocks;
    private int verbsLength;
    private String ending;
    private int countVowels;


    public ConjugationFullAlternative(String verb, List<ResultBlockAlternative> resultBlocks, int verbsLength, String ending, int countVowels) {
        this.verb = verb;
        this.resultBlocks = resultBlocks;
        this.verbsLength = verbsLength;
        this.ending = ending;
        this.countVowels = countVowels;
    }

    public ConjugationFullAlternative() {
    }

    public String getVerb() {
        return verb;
    }

    public List<ResultBlockAlternative> getResultBlocks() {
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
