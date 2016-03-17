package com.blaizedtrail.kakhu.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.blaizedtrail.kakhu.R;
import com.blaizedtrail.kakhu.adapters.ViewItemAdapter;
import com.blaizedtrail.kakhu.utils.Company;
import com.blaizedtrail.kakhu.utils.CompanyHandler;
import com.blaizedtrail.kakhu.utils.InventoryItem;
import com.blaizedtrail.kakhu.utils.InventoryItemInfo;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ViewItem extends AppCompatActivity implements Action1<InventoryItemInfo>{
    public static final String EXTRA_INVENTORY_ID ="com.blaizedtrail.kakhu.activities.ViewItem.EXTRA_INVENTORY_ID";
    private static final String DEBUG_TAG = ViewItem.class.getSimpleName();
    private InventoryItem inventoryItem;
    private int inventoryId;
    private List<InventoryItemInfo> inventoryItemInfoListOne;
    private TextView itemPriceText,itemQuantityText;
    private ViewItemAdapter viewItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
            }
        });
        instantiateVariables();
    }
    private void instantiateVariables(){
        Intent intent=getIntent();
        inventoryId=intent.getIntExtra(EXTRA_INVENTORY_ID, 0);
        inventoryItem= CompanyHandler.get().getInventoryInfoById(inventoryId);
        inventoryItemInfoListOne=new ArrayList<>();
        getSupportActionBar().setTitle(inventoryItem.getItemName());
        itemPriceText=(TextView)findViewById(R.id.itemPrice);
        itemQuantityText=(TextView)findViewById(R.id.itemQuantity);
        viewItemAdapter=new ViewItemAdapter();
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        StaggeredGridLayoutManager staggeredGridLayoutManager=new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(staggeredGridLayoutManager);
        recyclerView.setAdapter(viewItemAdapter);
        Observable<InventoryItemInfo>infoObservable=Observable.create(new Observable.OnSubscribe<InventoryItemInfo>() {
            @Override
            public void call(Subscriber<? super InventoryItemInfo> subscriber) {
                List<InventoryItemInfo>inventoryItemInfoList = inventoryItem.getInventoryItemInfoList();
                for (InventoryItemInfo inventoryItemInfo:inventoryItemInfoList){
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    subscriber.onNext(inventoryItemInfo);
                }
            }
        });
        infoObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(this);


    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
    }

    @Override
    public void call(InventoryItemInfo inventoryItemInfo) {
        inventoryItemInfoListOne.add(inventoryItemInfo);
        int size=inventoryItemInfoListOne.size();
        switch (size){
            case 0:
                itemQuantityText.setText("empty");
                break;
            case 1:
                itemQuantityText.setText(size+" unit");
                break;
            default:
                itemQuantityText.setText(size+" units");
                break;
        }
        viewItemAdapter.addToList(inventoryItemInfo);
        itemPriceText.setText("N " + inventoryItem.getStockPrice()*inventoryItemInfoListOne.size());
        Log.d(DEBUG_TAG, "inventory info: " + inventoryItemInfo.getItemName());
    }
}
