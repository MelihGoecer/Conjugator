package com.translator_conjugator_dictionary.modelsDictionary;

import java.util.List;

public class SaveDictionaryContentItem implements SaveItem {
    private String word;
    private String definition;
    private String example;
    private List<String> synonyms;

    public SaveDictionaryContentItem(String word, String definition, String example, List<String> synonyms) {
        this.word = word;
        this.definition = definition;
        this.example = example;
        this.synonyms = synonyms;
    }

    public String getWord() {
        return word;
    }

    public String getDefinition() {
        return definition;
    }

    public String getExample() {
        return example;
    }

    public List<String> getSynonyms() {
        return synonyms;
    }

    @Override
    public int getSaveType() {
        return SaveItem.TYPE_DICTIONARY_CONTENT;
    }
}
