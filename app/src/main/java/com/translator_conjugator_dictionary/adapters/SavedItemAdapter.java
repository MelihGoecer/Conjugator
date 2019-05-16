package com.translator_conjugator_dictionary.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.translator_conjugator_dictionary.R;
import com.translator_conjugator_dictionary.adaptersConj.ConjugationAdapter;
import com.translator_conjugator_dictionary.adaptersConj.SendDataToConjugation;
import com.translator_conjugator_dictionary.modelsConj.SaveTenseBlock;
import com.translator_conjugator_dictionary.modelsDictionary.SaveDictionaryContentItem;
import com.translator_conjugator_dictionary.modelsDictionary.SaveItem;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class SavedItemAdapter extends RecyclerView.Adapter implements SendDataToConjugation {
    private List<SaveItem> mSaveItemsList;
    private Context mContext;

    public SavedItemAdapter(List<SaveItem> mSaveItemsList, Context context) {
        this.mSaveItemsList = mSaveItemsList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (viewType == SaveItem.TYPE_CONJUGATION_CONTENT) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.save_tense_block_item, parent, false);
            return new TenseBlockHolder(v);
        } else if (viewType == SaveItem.TYPE_DICTIONARY_CONTENT) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.save_dictionary_content_item, parent, false);
            return new DictionaryContentHolder(v);
        }
        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.save_tense_block_item, parent, false);
        return new TenseBlockHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SaveItem currentItem = mSaveItemsList.get(position);
        switch (currentItem.getSaveType()) {
            case SaveItem.TYPE_CONJUGATION_CONTENT:
                ((TenseBlockHolder) holder).textViewVerb.setText(((SaveTenseBlock) currentItem).getVerb());
                ((TenseBlockHolder) holder).header.setText(((SaveTenseBlock) currentItem).getHeader());
                data[0] = ((SaveTenseBlock) currentItem).getHeader();
                data[1] =  ((SaveTenseBlock) currentItem).getLanguage();
                data[2] = ((SaveTenseBlock) currentItem).getVerb();
                ConjugationAdapter conjugationAdapter =
                        new ConjugationAdapter(((SaveTenseBlock) currentItem).getConjBlocks(),
                                mContext);
                ((TenseBlockHolder) holder).recyclerViewConj.setAdapter(conjugationAdapter);
                ((TenseBlockHolder) holder).recyclerViewConj.setHasFixedSize(true);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
                ((TenseBlockHolder) holder).recyclerViewConj.setLayoutManager(linearLayoutManager);
                break;
            case SaveItem.TYPE_DICTIONARY_CONTENT:
                final DictionaryContentHolder dictionaryContentHolder = ((DictionaryContentHolder) holder);
                final SaveDictionaryContentItem dictionaryContentItem = ((SaveDictionaryContentItem) currentItem);
                dictionaryContentHolder.textViewWord.setText(dictionaryContentItem.getWord());
                dictionaryContentHolder.textViewDefinition.setText(dictionaryContentItem.getDefinition());
                dictionaryContentHolder.textViewExample.setText(dictionaryContentItem.getExample());
                if (dictionaryContentItem.getSynonyms().size() > 1) {
                    dictionaryContentHolder.imageViewDropDown.setVisibility(View.VISIBLE);
                    for (String s : dictionaryContentItem.getSynonyms()) {
                        addTextView(dictionaryContentHolder.linearLayoutExpandableLayout, s);
                    }
                    dictionaryContentHolder.imageViewDropDown.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dictionaryContentHolder.expandableLayout.toggle();
                            if (!dictionaryContentHolder.expandableLayout.isExpanded()) {
                                changeRotate(dictionaryContentHolder.imageViewDropDown, 180f, 0f).start();
                                dictionaryContentHolder.textViewSynonymTitle.setTextColor(mContext.getResources().getColor(R.color.black));

                            } else if (dictionaryContentHolder.expandableLayout.isExpanded()) {
                                changeRotate(dictionaryContentHolder.imageViewDropDown, 0f, 180f).start();
                                dictionaryContentHolder.textViewSynonymTitle.setTextColor(mContext.getResources().getColor(R.color.darkGrey));
                            }
                        }
                    });
                } else {
                    dictionaryContentHolder.imageViewDropDown.setVisibility(View.GONE);
                }
                break;
        }
    }

    private void addTextView(LinearLayout pLinearLayout, String text) {
        TextView textView = new TextView(mContext);
        textView.setText(text);
        textView.setTextSize(16);
        textView.setTextColor(mContext.getResources().getColor(R.color.black));
        textView.setPadding(40, 10, 0, 10);
        textView.setGravity(Gravity.START);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pLinearLayout.addView(textView, layoutParams);
    }

    private ObjectAnimator changeRotate(ImageView v, float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }

    @Override
    public int getItemCount() {
        return mSaveItemsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mSaveItemsList.get(position).getSaveType();
    }

    class TenseBlockHolder extends RecyclerView.ViewHolder {
        private TextView textViewVerb;
        private TextView header;
        private RecyclerView recyclerViewConj;

        TenseBlockHolder(@NonNull View itemView) {
            super(itemView);
            textViewVerb = itemView.findViewById(R.id.textView_save_verb);
            header = itemView.findViewById(R.id.textView_tense_block_header);
            recyclerViewConj = itemView.findViewById(R.id.recyclerView_conjugations);
            recyclerViewConj.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        }
    }

    class DictionaryContentHolder extends RecyclerView.ViewHolder {
        private TextView textViewWord;
        private TextView textViewDefinition;
        private TextView textViewExample;
        private TextView textViewSynonymTitle;
        private ImageView imageViewDropDown;
        private ExpandableLayout expandableLayout;
        private LinearLayout linearLayoutExpandableLayout;

        DictionaryContentHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewWord = itemView.findViewById(R.id.textView_save_d);
            this.textViewDefinition = itemView.findViewById(R.id.textView_definition);
            this.textViewExample = itemView.findViewById(R.id.textView_example);
            this.textViewSynonymTitle = itemView.findViewById(R.id.textView_synonyms_title);
            this.imageViewDropDown = itemView.findViewById(R.id.imageView_dropdown_example);
            this.expandableLayout = itemView.findViewById(R.id.expandable_layout);
            this.linearLayoutExpandableLayout = itemView.findViewById(R.id.linearLayout_expandableLayout);
        }

    }

}
