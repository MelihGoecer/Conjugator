package com.translator_conjugator_dictionary.modelsDictionary;

public interface DictionaryListItem {
    int TYPE_WORD_ITEM = 100;
    int TYPE_CONTENT_ITEM = 101;
    int TYPE_TYPE_OF_WORD = 102;

    int getType();
}
