package com.blaizedtrail.kakhu.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.blaizedtrail.kakhu.R;
import com.blaizedtrail.kakhu.adapters.InventoryItemsAdapter;
import com.blaizedtrail.kakhu.utils.CompanyHandler;
import com.blaizedtrail.kakhu.utils.InventoryItem;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.gson.Gson;
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.android.schedulers.HandlerScheduler;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;

public class InventoryActivity extends AppCompatActivity implements RecyclerViewExpandableItemManager.OnGroupCollapseListener,
        RecyclerViewExpandableItemManager.OnGroupExpandListener, Action1<InventoryItem>,InventoryItemsAdapter.StockClickCallback{
    private static final String DEBUG_TAG = InventoryActivity.class.getSimpleName();
    private List<InventoryItem>inventoryItems;
    private InventoryItemsAdapter inventoryAdapter;
    private Handler handler;
    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        gson=new Gson();
        Observable<InventoryItem>stringObservable=Observable.create(new Observable.OnSubscribe<InventoryItem>() {
            @Override
            public void call(Subscriber<? super InventoryItem> subscriber) {
                List<InventoryItem> inventoryItems = CompanyHandler.get().getInventoryItems();
                for (InventoryItem inventoryItem : inventoryItems) {
                    try {
                        Thread.sleep(200);
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
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recylerView);
        inventoryAdapter=new InventoryItemsAdapter();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(inventoryAdapter);
        inventoryAdapter.setStockClickCallback(this);
        //reloadList();
    }
    private void reloadList(){
        stringObservable()
                .subscribeOn(HandlerScheduler.from(handler))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<InventoryItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<InventoryItem> s) {
                        Log.d(DEBUG_TAG, "Next: " + s.size());
                        inventoryAdapter.reloadList(inventoryItems);
                        Log.d(DEBUG_TAG,"Inventory size: "+inventoryItems.size());
                    }
                });
    }
    Observable<List<InventoryItem>>stringObservable(){
        return Observable.defer(new Func0<Observable<List<InventoryItem>>>() {
            @Override
            public Observable<List<InventoryItem>> call() {
                List<InventoryItem>inventoryItems=CompanyHandler.get().getInventoryItems();
                return Observable.just(inventoryItems);
            }
        });
    }
    public void addInventory(View view){
        YoYo.with(Techniques.SlideOutDown)
                .playOn(findViewById(R.id.fab));
        Intent intent=new Intent(this,AddInventory.class);
        startActivity(intent);
        overridePendingTransition(R.anim.abc_slide_in_bottom, R.anim.abc_slide_out_top);
    }

    @Override
    public void onGroupCollapse(int groupPosition, boolean fromUser) {

    }

    @Override
    public void onGroupExpand(int groupPosition, boolean fromUser) {
        if (fromUser){
            adjustScrollPositionOnGroupExpanded(groupPosition);
        }
    }
    private void adjustScrollPositionOnGroupExpanded(int groupPosition) {
       /* int childItemHeight = getResources().getDimensionPixelSize(100);
        int topMargin = (int) (getResources().getDisplayMetrics().density * 16); // top-spacing: 16dp

        expandableItemManager.scrollToGroup(groupPosition, childItemHeight, topMargin, topMargin);*/
    }

    @Override
    public void call(InventoryItem inventoryItem) {
        //inventoryItems.add(inventoryItem);
        inventoryAdapter.addToInventory(inventoryItem);
        Log.d(DEBUG_TAG,"Observable message: "+inventoryItem.getItemName());
    }

    @Override
    public void stockClicked(InventoryItem inventoryItem) {
        Intent intent=new Intent(this,ViewItem.class);
        intent.putExtra(ViewItem.EXTRA_INVENTORY_ID,inventoryItem.getId());
        startActivity(intent);
        overridePendingTransition(R.anim.activity_slide_in_right,R.anim.activity_slide_out_left);
    }

    @Override
    public void informationClicked(InventoryItem inventoryItem) {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(inventoryItem.getItemName());
        String content="Stock price: N "+inventoryItem.getStockPrice()+" \n" +
                "There are "+inventoryItem.getInventoryItemInfoList().size()+" left in this stock";
        builder.setMessage(content);
        builder.show();
    }

    @Override
    public void sellClicked(InventoryItem inventoryItem) {
        Intent intent=new Intent(this,SellActivity.class);
        Log.d(DEBUG_TAG,"Inventory id: "+inventoryItem.getId());
        intent.putExtra(SellActivity.EXTRAS_INVENTORY_ID, inventoryItem.getId());
        startActivity(intent);
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.activity_slide_in_left, R.anim.activity_slide_out_right);
    }
}
