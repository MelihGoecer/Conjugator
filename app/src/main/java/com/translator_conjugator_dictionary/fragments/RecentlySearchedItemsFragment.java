package com.translator_conjugator_dictionary.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.translator_conjugator_dictionary.R;
import com.translator_conjugator_dictionary.UI.ConjugationActivity;
import com.translator_conjugator_dictionary.modelsConj.Conjugation;
import com.translator_conjugator_dictionary.modelsConj.IRecentSearch;
import com.translator_conjugator_dictionary.modelsConj.RecentSearch;
import com.translator_conjugator_dictionary.modelsConj.TextHolderObject;
import com.translator_conjugator_dictionary.utils.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class RecentlySearchedItemsFragment extends Fragment {
    private static final String TAG = "RecentlySearchedItemsFr";
    public RecyclerView recyclerView;
    private ConjugationActivity activityRef;

    public static RecentlySearchedItemsFragment newInstance() {
        return new RecentlySearchedItemsFragment();
    }

    public RecentlySearchedItemsFragment() {
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        activityRef = ((ConjugationActivity) getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recently_searched_fragment, container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView_recently_searched);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.d(TAG, "onScrolled: " + dy);
                if (dy > 0 && activityRef.appBarLayout.getElevation() == 0) {
                    activityRef.appBarLayout.setElevation(getContext().getResources().getDimension(R.dimen.elevation_popupwindow));
                }
            }
        });

        DatabaseHelper mDb = new DatabaseHelper(getContext());
        List<IRecentSearch> recentSearches = mDb.getAllRecentSearches("recent_conjugations");
        List<IRecentSearch> recentSearchItems = new ArrayList<IRecentSearch>(recentSearches);
        RecentlySearchedItemsAdapter tenseBlockAdapter = new RecentlySearchedItemsAdapter(recentSearchItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(tenseBlockAdapter);
    }


    private class RecentlySearchedItemsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
        private List<IRecentSearch> mDataset;
        private int counter = 0;

        public RecentlySearchedItemsAdapter(List<IRecentSearch> mDataset) {
            this.mDataset = mDataset;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v;
            switch (viewType) {
                case IRecentSearch.TYPE_RECENT_SEARCH:
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_search_item,
                            parent, false);
                    return new RecentSearchHolder(v, activityRef);
                case IRecentSearch.TYPE_TEXT_HOLDER:
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.text_holder,
                            parent, false);
                    return new TextHolder(v);
                default:
                    v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_search_item,
                            parent, false);
                    return new RecentSearchHolder(v, activityRef);
            }

        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            if (mDataset.get(position).getType() == IRecentSearch.TYPE_RECENT_SEARCH) {
                ((RecentSearchHolder) holder).textViewVerb.setText(((RecentSearch) mDataset.get(position)).getSearchTerm());
                ((RecentSearchHolder) holder).textViewLanguage.setText(((RecentSearch) mDataset.get(position)).getSourceLanguage());
            }

        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        @Override
        public int getItemViewType(int position) {
            return mDataset.get(position).getType();
        }

        class RecentSearchHolder extends RecyclerView.ViewHolder {
            private TextView textViewVerb;
            private TextView textViewLanguage;
            private OnRecentSearchItemClickListener onRecentSearchItemClickListener;

            public RecentSearchHolder(@NonNull View itemView, final OnRecentSearchItemClickListener onRecentSearchItemClickListener) {
                super(itemView);
                this.onRecentSearchItemClickListener = onRecentSearchItemClickListener;
                itemView.findViewById(R.id.relativeLayout_recent_conj).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "onClick: " + textViewVerb.getText() + " ; " + textViewLanguage.getText());
                        onRecentSearchItemClickListener.onRecentSearchClick(textViewVerb.getText(),
                                textViewLanguage.getText());
                    }
                });
                textViewVerb = itemView.findViewById(R.id.textView_searched_verb);
                textViewLanguage = itemView.findViewById(R.id.textView_language_verb);

            }


        }

        class TextHolder extends RecyclerView.ViewHolder {

            public TextHolder(@NonNull View itemView) {
                super(itemView);
            }
        }


    }

    public interface OnRecentSearchItemClickListener{
        void onRecentSearchClick(CharSequence... strings);
    }

}
