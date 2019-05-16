package com.translator_conjugator_dictionary.modelsConj;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import androidx.lifecycle.LiveData;

public class Conjugation extends LiveData<Conjugation> implements Parcelable {
    private List<ResultBlock> resultBlocks;

    public Conjugation(List<ResultBlock> resultBlocks) {
        this.resultBlocks = resultBlocks;
    }

    protected Conjugation(Parcel in) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
