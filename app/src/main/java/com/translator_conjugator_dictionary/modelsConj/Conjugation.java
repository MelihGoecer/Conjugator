package com.translator_conjugator_dictionary.modelsConj;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class Conjugation implements Parcelable {
    private List<ResultBlock> resultBlocks;
    private String id;

    public Conjugation(List<ResultBlock> resultBlocks, String id) {
        this.resultBlocks = resultBlocks;
        this.id = id;
    }

    protected Conjugation(Parcel in) {
        id = in.readString();
    }

    public static final Creator<Conjugation> CREATOR = new Creator<Conjugation>() {
        @Override
        public Conjugation createFromParcel(Parcel in) {
            return new Conjugation(in);
        }

        @Override
        public Conjugation[] newArray(int size) {
            return new Conjugation[size];
        }
    };

    public List<ResultBlock> getResultBlocks() {
        return resultBlocks;
    }

    public String getId() {
        return id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
    }
}
