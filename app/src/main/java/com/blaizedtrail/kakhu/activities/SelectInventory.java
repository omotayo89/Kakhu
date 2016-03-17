package com.blaizedtrail.kakhu.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.blaizedtrail.kakhu.R;
import com.blaizedtrail.kakhu.adapters.InventoryItemsAdapter;
import com.blaizedtrail.kakhu.utils.CompanyHandler;
import com.blaizedtrail.kakhu.utils.InventoryItem;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SelectInventory extends AppCompatActivity implements Action1<InventoryItem>,
        InventoryItemsAdapter.StockClickCallback{
    private List<InventoryItem>inventoryItems;
    private InventoryItemsAdapter inventoryAdapter;
    private Handler handler;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_inventory);
        instantiateVariables();
    }
    private void instantiateVariables(){
        gson=new Gson();
        Observable<InventoryItem> stringObservable=Observable.create(new Observable.OnSubscribe<InventoryItem>() {
            @Override
            public void call(Subscriber<? super InventoryItem> subscriber) {
                List<InventoryItem> inventoryItems = CompanyHandler.get().getInventoryItems();
                for (InventoryItem inventoryItem : inventoryItems) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext(inventoryItem);
                }
            }
        });
        stringObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);
        //Subscription subscription=stringObservable.subscribe(this);
        handler=new Handler(Looper.getMainLooper());
        inventoryItems=new ArrayList<>();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        inventoryAdapter=new InventoryItemsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(inventoryAdapter);
        inventoryAdapter.setStockClickCallback(this);
        //reloadList();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void finish(View view){
        finish();
        overridePendingTransition(R.anim.abc_slide_in_top,R.anim.abc_slide_out_bottom);
    }

    @Override
    public void call(InventoryItem inventoryItem) {
        inventoryAdapter.addToInventory(inventoryItem);
    }

    @Override
    public void stockClicked(InventoryItem inventoryItem) {

    }

    @Override
    public void informationClicked(InventoryItem inventoryItem) {

    }

    @Override
    public void sellClicked(InventoryItem inventoryItem) {

    }
}
