package com.translator_conjugator_dictionary.adaptersDictionary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.translator_conjugator_dictionary.R;
import com.translator_conjugator_dictionary.modelsConj.IRecentSearch;
import com.translator_conjugator_dictionary.modelsConj.RecentSearch;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecentSearchesDictionaryAdapter extends RecyclerView.Adapter<RecentSearchesDictionaryAdapter.RecentTranslationHolder> {
    private List<IRecentSearch> mTranslations;
    private OnDictionaryItemClickListener onClickListener;

    public RecentSearchesDictionaryAdapter(List<IRecentSearch> mTranslations,
                                           OnDictionaryItemClickListener onClickListener) {
        this.mTranslations = mTranslations;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public RecentSearchesDictionaryAdapter.RecentTranslationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_two_translation_item, parent, false);
        return new RecentSearchesDictionaryAdapter.RecentTranslationHolder(v, onClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentSearchesDictionaryAdapter.RecentTranslationHolder holder, int position) {
        IRecentSearch currentItem = mTranslations.get(position);
        holder.header.setText(((RecentSearch) currentItem).getSearchTerm());
        holder.sourceLanguage.setText(((RecentSearch) currentItem).getSourceLanguage());
    }

    @Override
    public int getItemCount() {
        return mTranslations.size();
    }

    class RecentTranslationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView header;
        private TextView sourceLanguage;
        private OnDictionaryItemClickListener mListener;

        RecentTranslationHolder(@NonNull View itemView, OnDictionaryItemClickListener listener) {
            super(itemView);
            header = itemView.findViewById(R.id.textView_first_trans);
            sourceLanguage = itemView.findViewById(R.id.textView_recent_trans_lang);
            mListener = listener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onDictionaryItemClick(getAdapterPosition());
        }
    }

    public interface OnDictionaryItemClickListener {
        void onDictionaryItemClick(int position);
    }
}
