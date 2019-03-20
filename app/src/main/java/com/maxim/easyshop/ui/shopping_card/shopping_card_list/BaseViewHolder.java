package com.maxim.easyshop.ui.shopping_card.shopping_card_list;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;


public abstract class BaseViewHolder extends AbstractExpandableItemViewHolder {
    TextView textView;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(android.R.id.text1);

    }
}
