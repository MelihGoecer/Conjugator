package com.translator_conjugator_dictionary.modelsSynonyms;

import com.google.gson.annotations.SerializedName;

public class Meaning_de {

    @SerializedName("schwaches Verb")
    private WordType[] schwachesVerb;
    @SerializedName("starkes Verb")
    private WordType[] starkesVerb;
    @SerializedName("unregelmäßiges Verb")
    private WordType[] unregelmaessigesVerb;
    private WordType[] Substantiv;
    private WordType[] Konjunktion;
    private WordType[] Artikel;
    private WordType[] Adjektiv;
    private WordType[] Partikel;
    private WordType[] Interjektion;
    @SerializedName(value = "Pronomen", alternate = {""})
    private WordType[] Pronomen;

    public Meaning_de(WordType[] schwachesVerb, WordType[] starkesVerb, WordType[] unregelmaessigesVerb, WordType[] substantiv, WordType[] konjunktion, WordType[] artikel, WordType[] adjektiv, WordType[] partikel, WordType[] interjektion, WordType[] pronomen) {
        this.schwachesVerb = schwachesVerb;
        this.starkesVerb = starkesVerb;
        this.unregelmaessigesVerb = unregelmaessigesVerb;
        Substantiv = substantiv;
        Konjunktion = konjunktion;
        Artikel = artikel;
        Adjektiv = adjektiv;
        Partikel = partikel;
        Interjektion = interjektion;
        Pronomen = pronomen;
    }


    public WordType[] getSchwachesVerb() {
        return schwachesVerb;
    }

    public WordType[] getStarkesVerb() {
        return starkesVerb;
    }

    public WordType[] getUnregelmaessigesVerb() {
        return unregelmaessigesVerb;
    }

    public WordType[] getSubstantiv() {
        return Substantiv;
    }

    public WordType[] getKonjunktion() {
        return Konjunktion;
    }

    public WordType[] getArtikel() {
        return Artikel;
    }

    public WordType[] getAdjektiv() {
        return Adjektiv;
    }

    public WordType[] getPartikel() {
        return Partikel;
    }

    public WordType[] getInterjektion() {
        return Interjektion;
    }

    public WordType[] getPronomen() {
        return Pronomen;
    }
}
