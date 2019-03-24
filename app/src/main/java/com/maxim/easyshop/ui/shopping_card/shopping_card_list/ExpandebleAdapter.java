package com.maxim.easyshop.ui.shopping_card.shopping_card_list;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.maxim.easyshop.R;
import com.maxim.easyshop.model.Calculator;
import com.maxim.easyshop.model.EconomyObj;
import com.maxim.easyshop.model.OptimalObj;
import com.maxim.easyshop.model.ShoppingListSingletone;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ExpandebleAdapter extends AbstractExpandableItemAdapter<GroupViewHolder, ChildViewHolder> {
    List<GroupItem> mItems;

    public ExpandebleAdapter(boolean flag) {
        setHasStableIds(true); // this is required for expandable feature.
        mItems = new ArrayList<>();

        if (!flag) {
            String shopName = "";
            List<OptimalObj> listObj = Calculator.calculateOptimalMode(ShoppingListSingletone.getInstance().getShoppingList());

            for (int i = 0; i < listObj.size(); i++) {
                shopName = listObj.get(i).getNameShop();
                String s = String.format("%.2f", listObj.get(i).getTotalCoast());
                GroupItem group = new GroupItem(shopName, i, s);
                for (int j = 0; j < listObj.get(i).getItemListInThisShop().size(); j++) {
                    String item = listObj.get(i).getItemListInThisShop().get(j).getTitle();
                    group.children.add(new ChildItem(item, j));
                }
                mItems.add(group);
            }
        } else {
            String shopName = "";
            List<EconomyObj> listObj = Calculator.calculateEconomyMode(ShoppingListSingletone.getInstance().getShoppingList());

            for (int i = 0; i < listObj.size(); i++) {
                shopName = listObj.get(i).getNameShop();
                String s = String.valueOf(listObj.get(i).getItemListInThisShop().size());
                GroupItem group = new GroupItem(shopName, i, s);
                for (int j = 0; j < listObj.get(i).getItemListInThisShop().size(); j++) {
                    String item = listObj.get(i).getItemListInThisShop().get(j).getTitle();
                    group.children.add(new ChildItem(item, j));
                }
                mItems.add(group);
            }

        }

    }

    @Override
    public int getGroupCount() {
        return mItems.size();
    }

    @Override
    public int getChildCount(int groupPosition) {
        return mItems.get(groupPosition).children.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        // This method need to return unique value within all group items.
        return mItems.get(groupPosition).id;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // This method need to return unique value within the group.
        return mItems.get(groupPosition).children.get(childPosition).id;
    }

    @Override
    @NonNull
    public GroupViewHolder onCreateGroupViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_group_item_for_expandable, parent, false);
        return new GroupViewHolder(v);
    }

    @Override
    @NonNull
    public ChildViewHolder onCreateChildViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_child_item_for_expandable, parent, false);
        return new ChildViewHolder(v);
    }

    @Override
    public void onBindGroupViewHolder(@NonNull GroupViewHolder holder, int groupPosition, int viewType) {
        GroupItem group = mItems.get(groupPosition);
        holder.textView.setText(group.text);
        holder.totalCoast.setText(group.totalCoast);

        String url = "";
        if (group.text.equals("shufersal")) {
            url = "https://firebasestorage.googleapis.com/v0/b/easyshop-dd97e.appspot.com/o/store_1%402x.png?alt=media&token=ff43c473-2f1d-4274-af45-9492c7b2ec09";
            Picasso.get()
                    .load(url)
                    .fit()
                    .centerCrop()
                    .into(holder.imgShop);
        } else if (group.text.equals("tivtaam")) {
            url = "https://firebasestorage.googleapis.com/v0/b/easyshop-dd97e.appspot.com/o/store_2%402x.png?alt=media&token=4f3ff7f5-1561-4fee-81de-262100d05bc6";
            Picasso.get()
                    .load(url)
                    .fit()
                    .centerCrop()
                    .into(holder.imgShop);
        } else if (group.text.equals("rami_levy")) {
            url = "https://firebasestorage.googleapis.com/v0/b/easyshop-dd97e.appspot.com/o/store_3%402x.png?alt=media&token=f883cd4a-7089-42d9-bc92-ec57db566c12";
            Picasso.get()
                    .load(url)
                    .fit()
                    .centerCrop()
                    .into(holder.imgShop);
        }

    }

    @Override
    public void onBindChildViewHolder(@NonNull ChildViewHolder holder, int groupPosition, int childPosition, int viewType) {
        ChildItem child = mItems.get(groupPosition).children.get(childPosition);
        holder.textView.setText(child.text);

    }

    @Override
    public boolean onCheckCanExpandOrCollapseGroup(@NonNull GroupViewHolder holder, int groupPosition, int x, int y, boolean expand) {
        return true;
    }
}
