package com.translator_conjugator_dictionary.adaptersConj;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.translator_conjugator_dictionary.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SpinnerAdapterConj extends ArrayAdapter<String> {

    public SpinnerAdapterConj(Context context, List<String> spinnerItemList) {
        super(context, 0, spinnerItemList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_dropdown_item_conjugation, parent, false
            );
        }

        TextView textViewLanguage = convertView.findViewById(R.id.textView_conjugation_spinner);
        String currentItem = getItem(position);

        if (currentItem != null) {
            textViewLanguage.setText(currentItem);
        }

        return convertView;
    }
}
