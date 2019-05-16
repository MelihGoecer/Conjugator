package com.translator_conjugator_dictionary.adaptersConj;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import com.translator_conjugator_dictionary.R;
import com.translator_conjugator_dictionary.UI.ConjugationActivity;
import com.translator_conjugator_dictionary.modelsConj.ConjBlock;
import com.translator_conjugator_dictionary.modelsConj.Conjugation;
import com.translator_conjugator_dictionary.utils.ConjugatorHelper;

import java.util.List;

public class ConjugationAdapter extends RecyclerView.Adapter<ConjugationAdapter.ConjugationHolder> implements View.OnClickListener,
        SendDataToConjugation {
    private List<ConjBlock> conjBlocks;
    private OnConjItemClickListener onConjItemClickListener;
    private int mCount = 0;
    private Context mContext;
    private boolean shouldMarkEndings;

    public void setShouldMarkEndings(boolean shouldMarkEndings) {
        this.shouldMarkEndings = shouldMarkEndings;
        mCount = 0;
        notifyDataSetChanged();
    }

    public ConjugationAdapter(List<ConjBlock> pConjBlocks, Context context,
                              boolean shouldMark) {
        this.conjBlocks = pConjBlocks;
   /*     this.onConjItemClickListener = ((ConjugationActivity) context);*/
        this.mContext = context;
        this.shouldMarkEndings = shouldMark;
    }

    public ConjugationAdapter(List<ConjBlock> pConjBlocks, Context context) {
        this.conjBlocks = pConjBlocks;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ConjugationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (mCount < 5) {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.conjugation_item, parent, false);
            mCount++;
        } else {
            v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.conjugation_item, parent, false);
            v.setBackground(null);
            mCount = 0;
        }
        return new ConjugationHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ConjugationHolder holder, int position) {
        ConjBlock currentItem = conjBlocks.get(position);
        holder.pronoun.setText(currentItem.getPerson());
        if (shouldMarkEndings){
        holder.verb.setText(colorWordsEnding(currentItem.getResult(),
                ConjugatorHelper.getLangShort(data[1],
                        mContext.getResources().getStringArray(R.array.allLanguagesLong),
                        mContext.getResources().getStringArray(R.array.allLanguagesShort)),
                currentItem.getPerson(), data[0],
                data[2]));
        } else {
            holder.verb.setText(currentItem.getResult());
        }
        if (!ConjugationActivity.inAuditionMode) {
            holder.itemView.setOnClickListener(null);
        } else {
            holder.itemView.setOnClickListener(this);
        }

    }

    @Override
    public int getItemCount() {
        return conjBlocks.size();
    }

    private SpannableString colorWordsEnding(String s, String lang, String pronoun, String header
            , String verb) {
        String text = s;
        SpannableString ss = new SpannableString(text);
        switch (lang) {
            case "de":
                if (text.contains(" ")) {
                    text = text.substring(0, text.indexOf(" "));

                }
                if (text.substring(text.length() - 2).equals("en")) {
                    ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#3897F0"));
                    ss.setSpan(fcs, text.length() - 2, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (text.substring(text.length() - 2).equals("st")) {
                    if (!text.equals("ist") && !text.equals("bist")) {
                        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#3897F0"));
                        ss.setSpan(fcs, text.length() - 2, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                    if (header.equals("Konjunktiv I Präsens") || header.equals("Konjunktiv I " +
                            "Perfekt") || header.equals("Konjunktiv I Futur I") || header.equals(
                            "Konjunktiv I Futur II") || header.equals("Konjunktiv II " +
                            "Präteritum") || header.equals("Konjunktiv II Plusquamperfekt") ||
                            header.equals("Konjunktiv II Futur I") ||
                            header.equals("Konjunktiv II Futur II")) {
                        UnderlineSpan underlineSpan = new UnderlineSpan();
                        ss.setSpan(underlineSpan, text.length() - 3, text.length() - 2,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } else if (text.substring(text.length() - 2).equals("te")) {
                    ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#3897F0"));
                    ss.setSpan(fcs, text.length() - 2, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (text.substring(text.length() - 1).equals("e")) {
                    ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#3897F0"));
                    ss.setSpan(fcs, text.length() - 1, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else if (text.substring(text.length() - 1).equals("t")) {
                    ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#3897F0"));
                    ss.setSpan(fcs, text.length() - 1, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    if (header.equals("Konjunktiv I Präsens") || header.equals("Konjunktiv I " +
                            "Perfekt") || header.equals("Konjunktiv I Futur I") || header.equals(
                            "Konjunktiv I Futur II") || header.equals("Konjunktiv II " +
                            "Präteritum") || header.equals("Konjunktiv II Plusquamperfekt") ||
                            header.equals("Konjunktiv II Futur I") ||
                            header.equals("Konjunktiv II Futur II")) {
                        UnderlineSpan underlineSpan = new UnderlineSpan();
                        ss.setSpan(underlineSpan, text.length() - 2, text.length() - 1,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                return ss;
            case "en":
                if (text.charAt(text.length() - 1) == 's') {
                    ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#3897F0"));
                    ss.setSpan(fcs, text.length() - 1, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    if (!text.substring(0, text.length() - 1).equals(verb)) {
                        UnderlineSpan underlineSpan = new UnderlineSpan();
                        ss.setSpan(underlineSpan, text.length() - 2, text.length()-1,
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                } else if (text.length() > 3) {
                    if (text.substring(text.length() - 3, text.length()).equals("ing")) {
                        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.parseColor("#3897F0"));
                        ss.setSpan(fcs, text.length() - 3, text.length(),
                                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
                return ss;
            case "fr":


                return ss;
            default:
                return SpannableString.valueOf(text);
        }
    }

    class ConjugationHolder extends RecyclerView.ViewHolder {
        private TextView pronoun;
        private TextView verb;
        private View itemView;

        ConjugationHolder(@NonNull View itemView) {
            super(itemView);
            pronoun = itemView.findViewById(R.id.textView_pronoun);
            verb = itemView.findViewById(R.id.textView_verb);
            this.itemView = itemView;
        }


    }

    @Override
    public void onClick(View view) {
        onConjItemClickListener.onClick2(view);
    }

    public interface OnConjItemClickListener {
        void onClick2(View v);
    }
}
