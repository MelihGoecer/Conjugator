package com.translator_conjugator_dictionary.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.translator_conjugator_dictionary.R;
import com.translator_conjugator_dictionary.models.SpinnerItem;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import de.hdodenhof.circleimageview.CircleImageView;

public class SpinnerAdapter extends ArrayAdapter<SpinnerItem> {

    public SpinnerAdapter(Context context, ArrayList<SpinnerItem> spinnerItemList) {
        super(context, 0, spinnerItemList);
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);

    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView,
                                @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.spinner_item, parent, false
            );
        }

        CircleImageView circularImageView = convertView.findViewById(R.id.circular_imageView);
        TextView textViewLanguage = convertView.findViewById(R.id.textView_language);

        SpinnerItem currentItem = getItem(position);

        if (currentItem != null) {
            circularImageView.setImageResource(currentItem.getImage());
            textViewLanguage.setText(currentItem.getLanguage());
        }

        return convertView;
    }
}
