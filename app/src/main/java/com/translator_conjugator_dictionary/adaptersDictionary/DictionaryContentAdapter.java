package com.translator_conjugator_dictionary.adaptersDictionary;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.translator_conjugator_dictionary.R;
import com.translator_conjugator_dictionary.fragments.OnlineDictionaryFragment;
import com.translator_conjugator_dictionary.modelsDictionary.DictionaryContentItem;
import com.translator_conjugator_dictionary.modelsDictionary.DictionaryListItem;
import com.translator_conjugator_dictionary.modelsDictionary.DictionaryWordItem;
import com.translator_conjugator_dictionary.modelsDictionary.WordTypeManager;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DictionaryContentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<DictionaryListItem> mDictionaryListItems;
    private SparseBooleanArray expandState = new SparseBooleanArray();
    private Context mContext;
    private OnDictionaryLongClickListener onDictionaryLongClickListener;
    private OnCheckBoxClickListener mOnCheckBoxClick;

    public DictionaryContentAdapter(Context context
            , OnDictionaryLongClickListener onDictionaryLongClickListener, OnCheckBoxClickListener onCheckBoxClickListener) {
        this.mContext = context;
        this.onDictionaryLongClickListener = onDictionaryLongClickListener;
        this.mOnCheckBoxClick = onCheckBoxClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        switch (viewType) {
            case DictionaryListItem.TYPE_WORD_ITEM:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dictionary_word_item, parent, false);
                return new SearchTermHolder(v);
            case DictionaryListItem.TYPE_CONTENT_ITEM:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dictionary_definition_example_synonym, parent, false);
                return new DictionaryContentHolder(v, onDictionaryLongClickListener);
            case DictionaryListItem.TYPE_TYPE_OF_WORD:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.word_type_item, parent, false);
                return new WordTypeHolder(v);
            default:
                v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dictionary_word_item, parent, false);
                return new SearchTermHolder(v);
        }
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        DictionaryListItem currentItem = mDictionaryListItems.get(position);
        switch (currentItem.getType()) {
            case DictionaryListItem.TYPE_WORD_ITEM:
                SearchTermHolder searchTermHolder = ((SearchTermHolder) holder);
                DictionaryWordItem dictionaryWordItem = ((DictionaryWordItem) currentItem);
                searchTermHolder.textViewSearchTerm.setText(dictionaryWordItem.getSearchTerm());
                break;
            case DictionaryListItem.TYPE_TYPE_OF_WORD:
                WordTypeHolder wordTypeHolder = ((WordTypeHolder) holder);
                wordTypeHolder.textView.setText(((WordTypeManager) currentItem).getTypeOfWord());
                break;
            case DictionaryListItem.TYPE_CONTENT_ITEM:
                final DictionaryContentHolder dictionaryContentHolder = ((DictionaryContentHolder) holder);
                final DictionaryContentItem dictionaryContentItem = ((DictionaryContentItem) currentItem);
                dictionaryContentHolder.textViewDefinition.setText(dictionaryContentItem.getDefinition());
                dictionaryContentHolder.textViewExample.setText(dictionaryContentItem.getExample());
                if (dictionaryContentItem.getSynonyms() != null) {
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

                dictionaryContentHolder.checkBox.setTag(position);
                if (!OnlineDictionaryFragment.inActionMode) {
                    dictionaryContentHolder.checkBox.setVisibility(View.GONE);
                } else {
                    dictionaryContentHolder.checkBox.setChecked(false);
                    dictionaryContentHolder.checkBox.setVisibility(View.VISIBLE);
                }
                dictionaryContentHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mOnCheckBoxClick.onCheckBoxClick(((Integer) view.getTag()), ((CheckBox) view).isChecked());
                    }
                });

                if (isInList(position)) {
                    dictionaryContentHolder.checkBox.setChecked(true);
                }
                break;

        }

    }

    private boolean isInList(int position) {
        for (Integer i : OnlineDictionaryFragment.positionCheckBoxes) {
            if (i.equals(position))
                return true;
        }
        return false;
    }

    private ObjectAnimator changeRotate(ImageView v, float from, float to) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(v, "rotation", from, to);
        animator.setDuration(300);
        animator.setInterpolator(new LinearInterpolator());
        return animator;
    }


    @Override
    public int getItemCount() {
        if (mDictionaryListItems != null) {
            return mDictionaryListItems.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        return mDictionaryListItems.get(position).getType();
    }

    public void setDictionaryItemsList(List<? extends DictionaryListItem> dictionaryItemsList) {
        if (mDictionaryListItems == null) {
            mDictionaryListItems = new ArrayList<>();
        }
        for (int i = 0; i < mDictionaryListItems.size(); i++) {
            mDictionaryListItems.remove(i);
        }
        for (int i = 0; i < mDictionaryListItems.size(); i++) {
            expandState.append(i, false);
        }
        mDictionaryListItems.clear();
        mDictionaryListItems = new ArrayList<>(dictionaryItemsList);

        notifyDataSetChanged();
    }

    public List<DictionaryListItem> getmDictionaryListItems() {
        return mDictionaryListItems;
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

    class SearchTermHolder extends RecyclerView.ViewHolder {
        private TextView textViewSearchTerm;


        SearchTermHolder(@NonNull View itemView) {
            super(itemView);
            this.textViewSearchTerm = itemView.findViewById(R.id.textView_word);
        }
    }

    class WordTypeHolder extends RecyclerView.ViewHolder {
        private TextView textView;

        WordTypeHolder(@NonNull View itemView) {
            super(itemView);
            this.textView = itemView.findViewById(R.id.textView_type_of_word);
        }
    }

    class DictionaryContentHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
        private TextView textViewDefinition;
        private TextView textViewExample;
        private TextView textViewSynonymTitle;
        private CheckBox checkBox;
        private ImageView imageViewDropDown;
        private ExpandableLayout expandableLayout;
        private LinearLayout linearLayoutExpandableLayout;
        private OnDictionaryLongClickListener onDictionaryLongClickListener;

        DictionaryContentHolder(@NonNull View itemView, OnDictionaryLongClickListener onDictionaryLongClickListener) {
            super(itemView);
            this.textViewDefinition = itemView.findViewById(R.id.textView_definition);
            this.textViewExample = itemView.findViewById(R.id.textView_example);
            this.textViewSynonymTitle = itemView.findViewById(R.id.textView_synonyms_title);
            this.imageViewDropDown = itemView.findViewById(R.id.imageView_dropdown_example);
            this.expandableLayout = itemView.findViewById(R.id.expandable_layout);
            this.linearLayoutExpandableLayout = itemView.findViewById(R.id.linearLayout_expandableLayout);
            this.checkBox = itemView.findViewById(R.id.checkBox_dictionary);
            this.onDictionaryLongClickListener = onDictionaryLongClickListener;
            textViewDefinition.setOnLongClickListener(this);
            textViewSynonymTitle.setOnLongClickListener(this);
            textViewExample.setOnLongClickListener(this);

        }

        @Override
        public boolean onLongClick(View view) {
            this.onDictionaryLongClickListener.onLongClick(getAdapterPosition());
            return true;
        }
    }

    public interface OnDictionaryLongClickListener {
        void onLongClick(int position);
    }

    public interface OnCheckBoxClickListener {
        void onCheckBoxClick(int position, Boolean i);
    }
}
