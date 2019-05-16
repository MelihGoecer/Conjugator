package com.translator_conjugator_dictionary.modelsSynonyms;

import com.google.gson.annotations.SerializedName;

public class Meaning_es {

    @SerializedName("verbo intransitivo")
    private WordType[] verboIntransitivo;
    @SerializedName("nombre masculino")
    private WordType[] nombreMasculino;
    @SerializedName("nombre femenino")
    private WordType[] nombreFemenino;
    @SerializedName("")
    private WordType[] unknown;
    @SerializedName("pronombre relativo")
    private WordType[] pronombreRelativo;
    @SerializedName("determinante artículo")
    private WordType[] determinanteArtículo;
    @SerializedName("pronombre personal")
    private WordType[] pronombrePersonal;
    @SerializedName("interjección")
    private WordType[] interjección;
    @SerializedName("adjetivo")
    private WordType[] adjetivo;


    public Meaning_es(WordType[] verboIntransitivo, WordType[] nombreMasculino, WordType[] nombreFemenino, WordType[] unknown, WordType[] pronombreRelativo, WordType[] determinanteArtículo, WordType[] pronombrePersonal, WordType[] interjección, WordType[] adjetivo) {
        this.verboIntransitivo = verboIntransitivo;
        this.nombreMasculino = nombreMasculino;
        this.nombreFemenino = nombreFemenino;
        this.unknown = unknown;
        this.pronombreRelativo = pronombreRelativo;
        this.determinanteArtículo = determinanteArtículo;
        this.pronombrePersonal = pronombrePersonal;
        this.interjección = interjección;
        this.adjetivo = adjetivo;
    }

    public WordType[] getVerboIntransitivo() {
        return verboIntransitivo;
    }

    public WordType[] getNombreMasculino() {
        return nombreMasculino;
    }

    public WordType[] getNombreFemenino() {
        return nombreFemenino;
    }

    public WordType[] getUnknown() {
        return unknown;
    }

    public WordType[] getPronombreRelativo() {
        return pronombreRelativo;
    }

    public WordType[] getDeterminanteArtículo() {
        return determinanteArtículo;
    }

    public WordType[] getPronombrePersonal() {
        return pronombrePersonal;
    }

    public WordType[] getInterjección() {
        return interjección;
    }

    public WordType[] getAdjetivo() {
        return adjetivo;
    }
}
