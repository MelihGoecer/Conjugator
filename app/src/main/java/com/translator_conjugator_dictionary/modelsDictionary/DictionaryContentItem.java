package com.translator_conjugator_dictionary.modelsDictionary;

import java.util.List;

public class DictionaryContentItem implements DictionaryListItem, SaveItem {
    private String definition;
    private String example;
    private List<String> synonyms;

    public DictionaryContentItem(String definition, String example, List<String> synonyms) {
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

    public List<String> getSynonyms() {
        return synonyms;
    }

    @Override
    public int getType() {
        return DictionaryListItem.TYPE_CONTENT_ITEM;
    }

    @Override
    public int getSaveType() {
        return SaveItem.TYPE_DICTIONARY_CONTENT;
    }
}
