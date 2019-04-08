package com.maxim.easyshop.ui.catalogue;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.maxim.easyshop.R;
import com.maxim.easyshop.model.Item;

import java.util.ArrayList;
import java.util.List;

public class AdapterItems extends RecyclerView.Adapter<AdapterItems.ItemViewHolder> {

    private List<Item> listItems;
    private AdapterItemsClickCallback adapterItemsClickCallback;

    public void setAdapterItemsClickCallback(AdapterItemsClickCallback adapterItemsClickCallback) {
        this.adapterItemsClickCallback = adapterItemsClickCallback;
    }

    public AdapterItems(List<Item> listItems) {
        this.listItems = new ArrayList<>();
        this.listItems.addAll(listItems);
    }

    public void remove(int position) {
        listItems.remove(position);
        notifyItemRemoved(position);
    }

    public void add(Item item){
        listItems.add(0, item);
        notifyItemInserted(0);
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.row_shopping_list, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, int i) {
        Item item = listItems.get(i);
        itemViewHolder.rowTitleTxt.setText(item.getTitle());
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        private TextView rowTitleTxt;
        private ImageButton rowDeleteBtn;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);

            rowTitleTxt = itemView.findViewById(R.id.row_title_item);
            rowDeleteBtn = itemView.findViewById(R.id.row_delete_btn);

            rowDeleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(getAdapterPosition() != RecyclerView.NO_POSITION){
                        adapterItemsClickCallback.onDeleteItemClicked(getAdapterPosition());
                    }
                }
            });

        }
    }
}
