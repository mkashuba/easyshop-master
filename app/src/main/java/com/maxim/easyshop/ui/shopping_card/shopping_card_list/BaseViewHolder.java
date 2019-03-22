package com.maxim.easyshop.ui.shopping_card.shopping_card_list;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder;
import com.maxim.easyshop.R;


public abstract class BaseViewHolder extends AbstractExpandableItemViewHolder {
    TextView textView;

    public BaseViewHolder(@NonNull View itemView) {
        super(itemView);
        textView = itemView.findViewById(R.id.text1);
    }
}
