package com.maxim.easyshop.ui.shopping_card.shopping_card_list;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxim.easyshop.R;


public class GroupViewHolder extends BaseViewHolder {
    TextView totalCoast;
    ImageView imgShop;
    public GroupViewHolder(@NonNull View itemView) {
        super(itemView);
        totalCoast = itemView.findViewById(R.id.total_coast);
        imgShop = itemView.findViewById(R.id.img_shop);
    }
}
