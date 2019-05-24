package com.translator_conjugator_dictionary.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.translator_conjugator_dictionary.R;
import com.translator_conjugator_dictionary.UI.ConjugationActivity;

public class ConjugationFragment extends Fragment {
    private static final String TAG = "ConjugationFragment";
    private ConjugationActivity activityRef;

    public static ConjugationFragment newInstance(Bundle args) {
        ConjugationFragment conjugationFragment = new ConjugationFragment();
        conjugationFragment.setArguments(args);
        return conjugationFragment;
    }

    private ConjugationFragment() {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activityRef = ((ConjugationActivity) getActivity());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_conjugation, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated: " + getArguments().getStringArrayList("tenses").size());
    }


}
