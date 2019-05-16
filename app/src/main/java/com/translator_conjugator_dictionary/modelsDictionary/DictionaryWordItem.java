package com.translator_conjugator_dictionary.modelsDictionary;

public class DictionaryWordItem implements DictionaryListItem {
    private String searchTerm;

    public DictionaryWordItem(String searchTerm) {
        this.searchTerm = searchTerm;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    @Override
    public int getType() {
        return DictionaryListItem.TYPE_WORD_ITEM;
    }
}
