package com.translator_conjugator_dictionary.adaptersDictionary;

import android.graphics.Color;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.translator_conjugator_dictionary.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SearchFilteringAdapter extends RecyclerView.Adapter<SearchFilteringAdapter.FilteringHolder> {
    private List<String> mDataset;
    private OnFilterButtonClickListener onFilterButtonClickListener;
    private SparseBooleanArray sparseBooleanArray = new SparseBooleanArray();

    public SearchFilteringAdapter(OnFilterButtonClickListener onFilterButtonClickListener) {
        this.onFilterButtonClickListener = onFilterButtonClickListener;
    }

    @NonNull
    @Override
    public FilteringHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.filtering_item, parent, false);
        return new FilteringHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final FilteringHolder holder, final int position) {
        String currentString = mDataset.get(position);
        holder.materialButtonWordType.setText(currentString);
        if (sparseBooleanArray.get(position)) {
            holder.materialButtonWordType.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.materialButtonWordType.setTextColor(Color.parseColor("#000000"));
        }
        holder.materialButtonWordType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialButton materialButton = ((MaterialButton) view);
                if (sparseBooleanArray.get(holder.getAdapterPosition())) {
                    materialButton.setTextColor(Color.parseColor("#000000"));
                    sparseBooleanArray.put(holder.getAdapterPosition(), false);
                    onFilterButtonClickListener.onFilterButtonClick(sparseBooleanArray);
                } else {
                    materialButton.setTextColor(Color.parseColor("#ffffff"));
                    sparseBooleanArray.put(holder.getAdapterPosition(), true);
                    onFilterButtonClickListener.onFilterButtonClick(sparseBooleanArray);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mDataset != null) {
            return mDataset.size();
        } else {
            return 0;
        }
    }

    public void setmDataset(List<String> mDataset) {
        if (mDataset != null) {
            this.mDataset = new ArrayList<>(mDataset);
            for (int i = 0; i < mDataset.size(); i++) {
                sparseBooleanArray.append(i, false);
            }
        } else {
            this.mDataset = new ArrayList<>();
        }
        notifyDataSetChanged();
    }

    public void setSparseBooleanArray(SparseBooleanArray sparseBooleanArray) {
        this.sparseBooleanArray = sparseBooleanArray;
    }

    class FilteringHolder extends RecyclerView.ViewHolder {
        private MaterialButton materialButtonWordType;

        FilteringHolder(@NonNull View itemView) {
            super(itemView);
            materialButtonWordType = itemView.findViewById(R.id.materialButton_filtering);
        }
    }

    public interface OnFilterButtonClickListener {
        void onFilterButtonClick(SparseBooleanArray sparseBooleanArray);
    }
}
