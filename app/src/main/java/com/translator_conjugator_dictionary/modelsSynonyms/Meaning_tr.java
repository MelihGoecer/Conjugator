package com.translator_conjugator_dictionary.modelsSynonyms;

import com.google.gson.annotations.SerializedName;

public class Meaning_tr {

    private WordType[] ünlem;
    @SerializedName("")
    private WordType[] unknown;
    private WordType[] ad;
    private WordType[] belirteç;

    public Meaning_tr(WordType[] ünlem, WordType[] unknown, WordType[] ad, WordType[] belirteç) {
        this.ünlem = ünlem;
        this.unknown = unknown;
        this.ad = ad;
        this.belirteç = belirteç;
    }

    public WordType[] getÜnlem() {
        return ünlem;
    }

    public WordType[] getUnknown() {
        return unknown;
    }

    public WordType[] getAd() {
        return ad;
    }

    public WordType[] getBelirteç() {
        return belirteç;
    }
}
