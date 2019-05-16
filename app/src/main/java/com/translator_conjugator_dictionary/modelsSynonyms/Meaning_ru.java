package com.translator_conjugator_dictionary.modelsSynonyms;

import com.google.gson.annotations.SerializedName;

public class Meaning_ru {
    @SerializedName("Женский род")
    private WordType[] Женскийрод;
    @SerializedName("")
    private WordType[] unknown;
    @SerializedName("Средний род")
    private WordType[] Среднийрод;


    public Meaning_ru(WordType[] женскийрод, WordType[] unknown, WordType[] среднийрод) {
        Женскийрод = женскийрод;
        this.unknown = unknown;
        Среднийрод = среднийрод;
    }

    public WordType[] getЖенскийрод() {
        return Женскийрод;
    }

    public WordType[] getUnknown() {
        return unknown;
    }

    public WordType[] getСреднийрод() {
        return Среднийрод;
    }
}
