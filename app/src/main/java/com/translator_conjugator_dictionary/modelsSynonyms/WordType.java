package com.translator_conjugator_dictionary.modelsSynonyms;

public class WordType {

    private String definition;
    private String example;
    private String []synonyms;

    public WordType(String definition, String example, String[] synonyms) {
        this.definition = definition;
        this.example = example;
        this.synonyms = synonyms;
    }

    public String getDefinition() {
        return definition;
    }

    public String getExample() {
        return example;
    }

    public String[] getSynonyms() {
        return synonyms;
    }
}
