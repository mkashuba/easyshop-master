package com.maxim.easyshop.ui.catalogue;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.maxim.easyshop.R;
import com.maxim.easyshop.model.Calculator;
import com.maxim.easyshop.model.Item;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteItemAdapter extends ArrayAdapter<Item> {
    private List<Item> itemListFull;

    public AutoCompleteItemAdapter(@NonNull Context context, List<Item> itemList) {
        super(context, 0, itemList);
        itemListFull = new ArrayList<>(itemList);
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.row_autocomplete_item, parent, false
            );
        }

        ImageView imgSearchItem = convertView.findViewById(R.id.search_item_img);
        TextView textViewTitle = convertView.findViewById(R.id.title_txt);
        TextView textViewPrice = convertView.findViewById(R.id.price_txt);

        final Item item = getItem(position);

        if (item != null) {
            Picasso.get()
                    .load(item.getImg_url())
                    .fit()
                    .centerInside()
//                    .centerCrop()
                    .into(imgSearchItem);
            textViewTitle.setText(item.getTitle());
            textViewPrice.setText("from " + String.valueOf(Calculator.getLowestPriceInItem(item)));
        }

        return convertView;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return itemFilter;
    }

    private Filter itemFilter = new Filter() {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            List<Item> suggestions = new ArrayList<>();
            suggestions.clear();
            results.values = null;
            results.count = 0;

            if (constraint == null || constraint.length() == 0) {
                suggestions.addAll(itemListFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Item item : itemListFull) {
                    if (item.getTitle().toLowerCase().contains(filterPattern)) {
                        suggestions.add(item);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }


        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((List) results.values);
            notifyDataSetChanged();
        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Item) resultValue).getTitle();
        }
    };

}
