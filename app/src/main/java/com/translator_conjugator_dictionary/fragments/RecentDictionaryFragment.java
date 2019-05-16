package com.translator_conjugator_dictionary.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.translator_conjugator_dictionary.R;
import com.translator_conjugator_dictionary.UI.DictionaryActivity;
import com.translator_conjugator_dictionary.adaptersDictionary.RecentSearchesDictionaryAdapter;
import com.translator_conjugator_dictionary.modelsConj.IRecentSearch;
import com.translator_conjugator_dictionary.modelsConj.RecentSearch;
import com.translator_conjugator_dictionary.utils.DatabaseHelper;

import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecentDictionaryFragment extends Fragment implements RecentSearchesDictionaryAdapter.OnDictionaryItemClickListener {
    private DatabaseHelper mDb;

    public static RecentDictionaryFragment newInstance() {
        return new RecentDictionaryFragment();
    }

    public RecentDictionaryFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_recent_dictionary, container, false);
        mDb = new DatabaseHelper(getContext());
        RecyclerView rv = ((RecyclerView) v);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getContext()));
        rv.setAdapter(new RecentSearchesDictionaryAdapter(mDb.getAllRecentSearches("recent_dictionary_searches"), this));
        return v;
    }

    @Override
    public void onDictionaryItemClick(int position) {
        String s =
                ((RecentSearch) mDb.getAllRecentSearches("recent_dictionary_searches").get(position)).getSearchTerm();
        String l =
                ((RecentSearch) mDb.getAllRecentSearches("recent_dictionary_searches").get(position)).getSourceLanguage();
        DictionaryActivity activity = ((DictionaryActivity) getActivity());
        Objects.requireNonNull(activity).mViewPager.setCurrentItem(0);
        activity.spinnerLanguage.setSelectedIndex(activity.findPosForLang(l));
        activity.spinnerFlag.setImageResource(activity.getImageResourceFlag());
        activity.onlineDictionaryFragment.constraintLayout.setVisibility(View.GONE);
        activity.progressBar.setVisibility(View.VISIBLE);
        activity.sendRequest(s);
    }
}
