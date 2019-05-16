package com.translator_conjugator_dictionary.modelsConj;

public class RecentSearch implements DictionaryItem, IRecentSearch {

    private String searchTerm;
    private String sourceLanguage;

    public RecentSearch(String searchTerm, String sourceLanguage) {
        this.searchTerm = searchTerm;
        this.sourceLanguage = sourceLanguage;
    }

    public String getSearchTerm() {
        return searchTerm;
    }

    public String getSourceLanguage() {
        return sourceLanguage;
    }

    @Override
    public int getType() {
        return IRecentSearch.TYPE_RECENT_SEARCH;
    }
}
