package com.maxim.easyshop.ui.shop_locator.shop_locator_list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.maxim.easyshop.R;
import com.maxim.easyshop.model.Shop;

import java.util.ArrayList;
import java.util.List;

public class AdapterListShopLocator extends RecyclerView.Adapter<AdapterListShopLocator.ShopViewHolder> {

    private List<Shop> shopList;

    public AdapterListShopLocator(List<Shop> shopList) {
        this.shopList = new ArrayList<>();
        this.shopList.addAll(shopList);
    }

    @NonNull
    @Override
    public ShopViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_shop_locator, viewGroup, false);
        return new ShopViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShopViewHolder shopViewHolder, int i) {
        Shop shop = shopList.get(i);
        String count = String.valueOf(i + 1);
        shopViewHolder.counterTxt.setText(count);
        shopViewHolder.titleTxt.setText(shop.getTitle());
        shopViewHolder.addressTxt.setText(shop.getAddress());
        shopViewHolder.cityTxt.setText(shop.getCity());
        double dist = shop.getDistanceToYou();
        String strDist = "";
        if (dist < 1000) {
            strDist = String.format("%.0f", dist) + " m";
        } else {
            double dDist = dist / 1000;
            strDist = String.format("%.2f", dDist) + " km";
        }
        shopViewHolder.distanceTxt.setText(strDist);
    }

    @Override
    public int getItemCount() {
        return shopList.size();
    }

    class ShopViewHolder extends RecyclerView.ViewHolder {

        private TextView counterTxt, titleTxt, cityTxt, addressTxt, distanceTxt;
        private CheckBox checkBox;

        public ShopViewHolder(@NonNull View itemView) {
            super(itemView);

            counterTxt = itemView.findViewById(R.id.count_shop_txt);
            titleTxt = itemView.findViewById(R.id.title_shop_txt);
            cityTxt = itemView.findViewById(R.id.city_shop_txt);
            addressTxt = itemView.findViewById(R.id.address_shop_txt);
            distanceTxt = itemView.findViewById(R.id.distance_shop_txt);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
