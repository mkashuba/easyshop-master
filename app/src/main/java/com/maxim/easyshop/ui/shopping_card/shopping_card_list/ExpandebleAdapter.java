package com.maxim.easyshop.ui.shopping_card.shopping_card_list;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter;
import com.maxim.easyshop.R;

import java.util.ArrayList;
import java.util.List;

public class ExpandebleAdapter extends AbstractExpandableItemAdapter<GroupViewHolder, ChildViewHolder> {
    List<GroupItem> mItems;

    public ExpandebleAdapter() {
        setHasStableIds(true); // this is required for expandable feature.

        mItems = new ArrayList<>();
        for (int i = 0; i < 2; i++) {
            GroupItem group = new GroupItem("My items: ", i);
            for (int j = 0; j < 8; j++) {
                group.children.add(new ChildItem("item " + j, j));
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
