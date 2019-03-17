package com.maxim.easyshop.ui.catalogue;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;

import com.arellomobile.mvp.MvpAppCompatFragment;
import com.arellomobile.mvp.presenter.InjectPresenter;
import com.maxim.easyshop.R;
import com.maxim.easyshop.model.Item;
import com.maxim.easyshop.model.ShoppingListSingletone;
import com.maxim.easyshop.presentation.presenter.CataloguePresenter;
import com.maxim.easyshop.presentation.view.CatalogueView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import me.relex.circleindicator.CircleIndicator;

public class CatalogueFragment extends MvpAppCompatFragment implements CatalogueView, AdapterItemsClickCallback {

    @InjectPresenter
    CataloguePresenter cataloguePresenter;

    @BindView(R.id.fab)
    FloatingActionButton addBtn;
    @BindView(R.id.circle)
    CircleIndicator circleIndicator;
    @BindView(R.id.input_add_item)
    AutoCompleteTextView inputItem;
    @BindView(R.id.items_recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.view_pager)
    ViewPager viewPager;
    private Unbinder unbinder;

    private AutoCompleteItemAdapter ACIAdapter;
    private AdapterItems adapterItems;
    private ViewPagerAdapter adapter;

    private String[] imageUrls = new String[]{
            "https://s.hi-news.ru/wp-content/uploads/2018/01/alcohol.jpg",
            "http://okeanmarket.ru/media/images/catalog/bakaleya.jpg",
            "https://images.kz.prom.st/72113771_w640_h640_bakaleya-i-krupy.jpg",
            "http://italy4.me/wp-content/uploads/2016/10/cheese_cr_cr.jpg",
            "https://roscontrol.com/files/images/articles/79/55/7955a8a026903eafb837_content_big_87fde87d.jpg"
    };

    private Item itemToAdd;

    public CatalogueFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_catalogue, container, false);
        unbinder = ButterKnife.bind(this, view);
        //set title on toolbar
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("CATALOGUE");

        cataloguePresenter.initViewPager();
        cataloguePresenter.initRecyclerView();
        cataloguePresenter.initAutoCompleteAdapter();

        //clickListener on item choose
        inputItem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String title = ((Item) parent.getItemAtPosition(position)).getTitle();
                inputItem.setText(title);
                itemToAdd = ((Item) parent.getItemAtPosition(position));
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick(R.id.fab)
    public void addItemInList() {
        if (itemToAdd != null) {
            cataloguePresenter.addItemInList(itemToAdd);
        }
    }

    @Override
    public void initViewPagerView() {
        //init ViewPager
        adapter = new ViewPagerAdapter(getContext(), imageUrls);
        viewPager.setAdapter(adapter);
        //init circle on ViewPager
        circleIndicator.setViewPager(viewPager);
    }

    @Override
    public void initRecyclerView(List<Item> list) {
        //init RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        adapterItems = new AdapterItems(list);
        recyclerView.setAdapter(adapterItems);
        recyclerView.setLayoutManager(layoutManager);
        adapterItems.setAdapterItemsClickCallback(this);

    }

    @Override
    public void initAutoCompleteAdapter(List<Item> list) {
        Log.d("MY_TAG", "initAutoCompleteAdapter: list each we set on adapter. Size = " + list.size());
        ACIAdapter = new AutoCompleteItemAdapter(getContext(), list);
        inputItem.setAdapter(ACIAdapter);
    }

    @Override
    public void addItemInList(Item item) {
        if (item != null) {
            hideKeyboard();
            adapterItems.add(item);
            inputItem.getText().clear();
            itemToAdd = null;
        }
    }

    //shutdown after change orientation on NullPointerException getWindowToken();
    @Override
    public void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    @Override
    public void onDeleteItemClicked(int position) {
        adapterItems.remove(position);
        ShoppingListSingletone.getInstance().deleteItem(position);
    }
}
