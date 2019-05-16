package com.translator_conjugator_dictionary.modelsSynonyms;

import com.google.gson.annotations.SerializedName;

public class Meaning {
    private WordType[] noun;
    private WordType[] verb;
    private WordType[] adjective;
    private WordType[] adverb;
    private WordType[] preposition;
    private WordType[] pronoun;
    private WordType[] conjunction;
    private WordType[] determiner;
    private WordType[] exclamation;
    @SerializedName("modal verb")
    private WordType []modalVerb;



    public Meaning(WordType[] noun, WordType[] verb, WordType[] adjective, WordType[] adverb, WordType[] preposition, WordType[] pronoun, WordType[] conjunction, WordType[] determiner, WordType[] exclamation, WordType[] modalVerb, WordType[] schwachesVerb, WordType[] starkesVerb, WordType[] unregelmaessigesVerb, WordType[] substantiv, WordType[] konjunktion, WordType[] artikel, WordType[] adjektiv, WordType[] partikel, WordType[] interjektion, WordType[] pronomen) {
        this.noun = noun;
        this.verb = verb;
        this.adjective = adjective;
        this.adverb = adverb;
        this.preposition = preposition;
        this.pronoun = pronoun;
        this.conjunction = conjunction;
        this.determiner = determiner;
        this.exclamation = exclamation;
        this.modalVerb = modalVerb;
    }

    public WordType[] getNoun() {
        return noun;
    }

    public WordType[] getVerb() {
        return verb;
    }

    public WordType[] getAdjective() {
        return adjective;
    }

    public WordType[] getAdverb() {
        return adverb;
    }

    public WordType[] getPreposition() {
        return preposition;
    }

    public WordType[] getPronoun() {
        return pronoun;
    }

    public WordType[] getConjunction() {
        return conjunction;
    }

    public WordType[] getDeterminer() {
        return determiner;
    }

    public WordType[] getExclamation() {
        return exclamation;
    }

    public WordType[] getModalVerb() {
        return modalVerb;
    }

}
