package com.blaizedtrail.kakhu.fragments;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blaizedtrail.kakhu.R;
import com.blaizedtrail.kakhu.utils.ActivityItem;
import com.blaizedtrail.kakhu.utils.Agent;
import com.blaizedtrail.kakhu.utils.BariolTextView;
import com.blaizedtrail.kakhu.utils.Company;
import com.blaizedtrail.kakhu.utils.CompanyHandler;
import com.blaizedtrail.kakhu.utils.InventoryItem;
import com.blaizedtrail.kakhu.utils.InventoryItemInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class OverviewFragment extends Fragment{
    private static final String DEBUG_TAG = OverviewFragment.class.getSimpleName();
    private View rootView;
    private Agent agent;
    private Handler handler;
    public OverviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView=inflater.inflate(R.layout.fragment_overview, container, false);
        instantiateVariables();
        return rootView;
    }
    private void instantiateVariables(){
        handler=new Handler(Looper.getMainLooper());
        agent=CompanyHandler.get().getCompany().getAgent();
        Picasso.with(getContext())
                .load(Uri.parse(agent.getImage_uri()))
                .skipMemoryCache()
                .into((CircleImageView)rootView.findViewById(R.id.image_view));
        ((BariolTextView)rootView.findViewById(R.id.agentName)).setText(agent.getAgent_firstName()+" "+agent.getAgent_lastName());
        getTotalItemsInStock();
    }
    private void getTotalItemsInStock(){
        final BariolTextView itemCount=(BariolTextView)rootView.findViewById(R.id.itemCount);
        Observable<Integer>integerObservable=Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                int count=0;
                List<InventoryItem>inventoryItems=CompanyHandler.get().getInventoryItems();
                for (InventoryItem inventoryItem:inventoryItems){
                    Log.d(DEBUG_TAG,"Inventory item list size: "+inventoryItem.getInventoryItemInfoList().size());
                    List<InventoryItemInfo>inventoryItemInfos=inventoryItem.getInventoryItemInfoList();
                    for (InventoryItemInfo inventoryItemInfo:inventoryItemInfos){
                        Log.d(DEBUG_TAG,"Inventory info name: "+inventoryItemInfo.getItemName());
                        count++;
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        subscriber.onNext(count);
                    }
                }
            }
        });
        integerObservable.subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        itemCount.setText(String.valueOf(integer));
                    }
                });
    }

}
