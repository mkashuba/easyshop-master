package com.maxim.easyshop.ui.shopping_card.shopping_card_list;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.maxim.easyshop.R;
import com.maxim.easyshop.model.Calculator;
import com.maxim.easyshop.model.OptimalObj;
import com.maxim.easyshop.model.ShoppingListSingletone;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ExpandebleAdapter extends AbstractExpandableItemAdapter<GroupViewHolder, ChildViewHolder> {
    List<GroupItem> mItems;

    public ExpandebleAdapter() {
        setHasStableIds(true); // this is required for expandable feature.

        mItems = new ArrayList<>();

        String shopName = "";
        int size = ShoppingListSingletone.getInstance().getShoppingList().size();

        List<OptimalObj> listObj = Calculator.calculateOptimalMode(ShoppingListSingletone.getInstance().getShoppingList());

        Log.d("MY_TAG", "ExpandebleAdapter: SIZE LIST OBJ = " + listObj.size());
        for(OptimalObj o : listObj){
            Log.d("MY_TAG", "ExpandebleAdapter: " + o.getNameShop());
            Log.d("MY_TAG", "ExpandebleAdapter: " + o.getItemListInThisShop().size());
        }


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
