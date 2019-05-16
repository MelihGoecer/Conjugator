package com.translator_conjugator_dictionary.modelsSynonyms;

public class ListObject<T, M> {
    private String word;
    private M phonetic;
    private String pronunciation;
    private T meaning;

    public ListObject(String word, M phonetic, String pronunciation, T meaning) {
        this.word = word;
        this.phonetic = phonetic;
        this.pronunciation = pronunciation;
        this.meaning = meaning;
    }

    public String getWord() {
        return word;
    }

    public M getPhonetic() {
        return phonetic;
    }

    public String getPronunciation() {
        return pronunciation;
    }

    public T getMeaning() {
        return meaning;
    }
}
