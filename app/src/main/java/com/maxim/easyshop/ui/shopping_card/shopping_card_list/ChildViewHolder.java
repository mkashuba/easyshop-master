package com.maxim.easyshop.ui.shopping_card.shopping_card_list;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.maxim.easyshop.R;


public class ChildViewHolder extends BaseViewHolder {

    TextView priceItem;
    ConstraintLayout constraintLayout;

    public ChildViewHolder(@NonNull View itemView) {
        super(itemView);
        priceItem = itemView.findViewById(R.id.price_in_row);
        constraintLayout = itemView.findViewById(R.id.child_row_item);
    }
}
