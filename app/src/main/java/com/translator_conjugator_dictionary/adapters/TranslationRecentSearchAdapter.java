package com.translator_conjugator_dictionary.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.translator_conjugator_dictionary.R;
import com.translator_conjugator_dictionary.models.Translation;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TranslationRecentSearchAdapter extends RecyclerView.Adapter<TranslationRecentSearchAdapter.RecentTranslationHolder> {
    private List<Translation> mTranslations;
    private OnRecentTransClickListener mOnClickListener;

    public TranslationRecentSearchAdapter(List<Translation> mTranslations, OnRecentTransClickListener onClick) {
        this.mTranslations = mTranslations;
        this.mOnClickListener = onClick;
    }

    @NonNull
    @Override
    public RecentTranslationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_two_translation_item, parent, false);
        return new RecentTranslationHolder(v, mOnClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecentTranslationHolder holder, int position) {
        Translation currentItem = mTranslations.get(position);
        holder.header.setText(currentItem.getHeader());
        holder.sourceLanguage.setText(currentItem.getSource());
    }

    public void setmTranslations(List<Translation> mTranslations) {
        this.mTranslations = mTranslations;
        notifyDataSetChanged();
    }

    public List<Translation> getmTranslations() {
        return mTranslations;
    }

    @Override
    public int getItemCount() {
        return mTranslations.size();
    }

    class RecentTranslationHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView header;
        private TextView sourceLanguage;
        private OnRecentTransClickListener clickListener;

        RecentTranslationHolder(@NonNull View itemView, OnRecentTransClickListener clickListener) {
            super(itemView);
            header = itemView.findViewById(R.id.textView_first_trans);
            sourceLanguage = itemView.findViewById(R.id.textView_recent_trans_lang);
            itemView.setOnClickListener(this);
            this.clickListener = clickListener;
        }

        @Override
        public void onClick(View view) {
            clickListener.onRecentTransClick(getAdapterPosition());
        }
    }

    public interface OnRecentTransClickListener {
        void onRecentTransClick(int position);
    }
}
