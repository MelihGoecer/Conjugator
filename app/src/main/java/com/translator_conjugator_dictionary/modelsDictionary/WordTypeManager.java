package com.translator_conjugator_dictionary.modelsDictionary;

import com.translator_conjugator_dictionary.modelsSynonyms.WordType;

public class WordTypeManager implements DictionaryListItem {
    private WordType []wordTypes;
    private String typeOfWord;

    public WordTypeManager(String typeOfWord, WordType[] wordTypes) {
        this.wordTypes = wordTypes;
        this.typeOfWord = typeOfWord;
    }

    public WordType[] getWordTypes() {
        return wordTypes;
    }

    public String getTypeOfWord() {
        return typeOfWord;
    }

    @Override
    public int getType() {
        return DictionaryListItem.TYPE_TYPE_OF_WORD;
    }
}
