package com.translator_conjugator_dictionary.ViewModels;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel implements Repository.PublishTranslations {
    private static final String TAG = "MainActivityViewModel";
    private MutableLiveData<String> mFirstTranslation = new MutableLiveData<>();
    private MutableLiveData<String> mSecondTranslation = new MutableLiveData<>();


    public void setmFirstTranslation(String mFirstTranslation) {
        this.mFirstTranslation.setValue(mFirstTranslation);
    }

    public void setmSecondTranslation(String mSecondTranslation) {
        this.mSecondTranslation.setValue(mSecondTranslation);
    }

    public LiveData<String> getmFirstTranslation() {
        return mFirstTranslation;
    }

    public LiveData<String> getmSecondTranslation() {
        return mSecondTranslation;
    }

    public void sendRequest(String text, String source, String target, String model, int i, Context context) {
        Log.d(TAG, "sendRequest: " + Repository.getInstance().hashCode());
        Repository.getInstance().sendRequestForTranslation(text, source, target, model, this, i, context);
    }

    @Override
    public void getResults(String translation, int i) {
        if (i == 0) {
            setmFirstTranslation(translation);
        } else {
            setmSecondTranslation(translation);
        }

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "onCleared: ");
    }
}
