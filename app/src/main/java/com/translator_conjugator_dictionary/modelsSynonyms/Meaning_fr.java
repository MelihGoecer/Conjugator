package com.translator_conjugator_dictionary.modelsSynonyms;

import com.google.gson.annotations.SerializedName;

public class Meaning_fr {
    @SerializedName("verbe intransitif")
    private WordType[] verbeIntransitif;
    @SerializedName("verbe transitif")
    private WordType[] verbeTransitif;
    @SerializedName("nom masculin")
    private WordType[] nomMasculin;
    @SerializedName("nom féminin")
    private WordType[] nomFéminin;
    @SerializedName("adjectif, nom masculin, adverbe et interjection")
    private WordType[] AdjectifNomMasculinAdverbeEtInterjection;
    private WordType[] adjectif;
    private WordType[] adverbe;
    private WordType[] interjection;
    private WordType[] préposition;
    private WordType[] conjonction;

    public Meaning_fr(WordType[] verbeIntransitif, WordType[] verbeTransitif, WordType[] nomMasculin, WordType[] nomFéminin, WordType[] adjectifNomMasculinAdverbeEtInterjection, WordType[] adjectif, WordType[] adverbe, WordType[] interjection, WordType[] préposition, WordType[] conjonction) {
        this.verbeIntransitif = verbeIntransitif;
        this.verbeTransitif = verbeTransitif;
        this.nomMasculin = nomMasculin;
        this.nomFéminin = nomFéminin;
        AdjectifNomMasculinAdverbeEtInterjection = adjectifNomMasculinAdverbeEtInterjection;
        this.adjectif = adjectif;
        this.adverbe = adverbe;
        this.interjection = interjection;
        this.préposition = préposition;
        this.conjonction = conjonction;
    }

    public WordType[] getVerbeIntransitif() {
        return verbeIntransitif;
    }

    public WordType[] getVerbeTransitif() {
        return verbeTransitif;
    }

    public WordType[] getNomMasculin() {
        return nomMasculin;
    }

    public WordType[] getNomFéminin() {
        return nomFéminin;
    }

    public WordType[] getAdjectifNomMasculinAdverbeEtInterjection() {
        return AdjectifNomMasculinAdverbeEtInterjection;
    }

    public WordType[] getAdjectif() {
        return adjectif;
    }

    public WordType[] getAdverbe() {
        return adverbe;
    }

    public WordType[] getInterjection() {
        return interjection;
    }

    public WordType[] getPréposition() {
        return préposition;
    }

    public WordType[] getConjonction() {
        return conjonction;
    }
}
